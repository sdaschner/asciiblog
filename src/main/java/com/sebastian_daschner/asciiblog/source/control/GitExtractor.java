package com.sebastian_daschner.asciiblog.source.control;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.sebastian_daschner.asciiblog.source.entity.ChangeSet;
import io.quarkus.runtime.Startup;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.transport.*;
import org.eclipse.jgit.util.FS;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

import static org.eclipse.jgit.lib.Constants.MASTER;

@ApplicationScoped
@Startup
public class GitExtractor {

    private SshTransportConfigCallback transportConfigCallback;
    private ChangeCalculator changeCalculator;
    private Git git;
    private ObjectId lastCommit;

    @ConfigProperty(name = "asciiblog.git.uri")
    String gitUri;

    @ConfigProperty(name = "asciiblog.git.ssh")
    String sshPath;

    @PostConstruct
    void openGit() {
        transportConfigCallback = new SshTransportConfigCallback(sshPath);
        try {
            final File workingDir = Files.createTempDirectory("blog-git").toFile();

            git = Git.cloneRepository()
                    .setDirectory(workingDir)
                    .setTransportConfigCallback(transportConfigCallback)
                    .setURI(gitUri)
                    .call();

            changeCalculator = new ChangeCalculator(git);
        } catch (IOException | GitAPIException e) {
            throw new RuntimeException("Could not open Git repository.", e);
        }
    }

    /**
     * Returns the AsciiDoc files which have been changed since the last call.
     * <p>
     * <b>Note:</b> Only relevant AsciiDoc files are included.
     */
    public ChangeSet getChanges() {
        ObjectId currentCommit = null;
        try {
            updateGit();
            currentCommit = getLatestCommit();

            if (currentCommit == null)
                return new ChangeSet();

            if (lastCommit == null) {
                return changeCalculator.getChanges(currentCommit);
            }

            return changeCalculator.getChanges(lastCommit, currentCommit);

        } catch (IOException | GitAPIException e) {
            throw new RuntimeException(e);
        } finally {
            lastCommit = currentCommit;
        }
    }

    private void updateGit() throws GitAPIException {
        git.pull().setTransportConfigCallback(transportConfigCallback).call();
    }

    private ObjectId getLatestCommit() throws IOException {
        return git.getRepository().resolve(MASTER);
    }

    @PreDestroy
    public void closeGit() {
        final File directory = git.getRepository().getWorkTree();
        git.close();
        delete(directory);
    }

    private void delete(final File file) {
        final File[] files;
        if (file.isDirectory() && (files = file.listFiles()) != null)
            Stream.of(files).forEach(this::delete);
        if (!file.delete())
            throw new IllegalStateException("Could not delete file: " + file);
    }

    private static class SshTransportConfigCallback implements TransportConfigCallback {

        private final SshSessionFactory sshSessionFactory;

        public SshTransportConfigCallback(String sshPath) {
            sshSessionFactory = new JschConfigSessionFactory() {

                @Override
                protected JSch getJSch(OpenSshConfig.Host hc, FS fs) throws JSchException {
                    JSch jsch = super.getJSch(hc, fs);
                    jsch.removeAllIdentity();
                    jsch.addIdentity(sshPath);
                    return jsch;
                }

                @Override
                protected void configure(final OpenSshConfig.Host hc, final Session session) {
                    session.setConfig("StrictHostKeyChecking", "no");
                }
            };
        }

        @Override
        public void configure(final Transport transport) {
            SshTransport sshTransport = (SshTransport) transport;
            sshTransport.setSshSessionFactory(sshSessionFactory);
        }

    }

}
