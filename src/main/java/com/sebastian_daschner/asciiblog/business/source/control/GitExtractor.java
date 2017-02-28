/*
 * Copyright (C) 2015 Sebastian Daschner, sebastian-daschner.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sebastian_daschner.asciiblog.business.source.control;

import com.jcraft.jsch.Session;
import com.sebastian_daschner.asciiblog.business.source.entity.ChangeSet;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.transport.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

import static org.eclipse.jgit.lib.Constants.MASTER;

@Startup
@Singleton
public class GitExtractor {

    private ChangeCalculator changeCalculator;
    private Git git;
    private ObjectId lastCommit;

    @Inject
    String gitUri;

    @PostConstruct
    public void openGit() {
        try {
            final File workingDir = Files.createTempDirectory("blog-git").toFile();

            git = Git.cloneRepository()
                    .setDirectory(workingDir)
                    .setTransportConfigCallback(new SshTransportConfigCallback())
                    .setURI(gitUri)
                    .call();

            changeCalculator = new ChangeCalculator(git);
        } catch (IOException | GitAPIException e) {
            throw new IllegalStateException("Could not open Git repository.", e);
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
            return new ChangeSet();
        } finally {
            lastCommit = currentCommit;
        }
    }

    private void updateGit() throws GitAPIException {
        git.pull().call();
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

        private final SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
            @Override
            protected void configure(final OpenSshConfig.Host hc, final Session session) {
                session.setConfig("StrictHostKeyChecking", "no");
            }
        };

        @Override
        public void configure(final Transport transport) {
            SshTransport sshTransport = (SshTransport) transport;
            sshTransport.setSshSessionFactory(sshSessionFactory);
        }

    }

}
