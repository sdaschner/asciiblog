package com.sebastian_daschner.asciiblog.business.source.control;

import com.sebastian_daschner.asciiblog.business.environment.control.Environment;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class GitExtractorTest {

    private GitExtractor cut;
    private File gitCloneDirectory;
    private File file1;
    private File file2;

    @Before
    public void setUp() throws IOException, GitAPIException {
        cut = new GitExtractor();
        cut.env = Environment.INTEGRATION;
        cut.gitDirectory = Files.createTempDirectory("git-blog-").toFile();
        gitCloneDirectory = Files.createTempDirectory("git-clone-").toFile();
        file1 = Paths.get(gitCloneDirectory.getAbsolutePath(), "file1").toFile();
        file2 = Paths.get(gitCloneDirectory.getAbsolutePath(), "file2").toFile();
        initGitAndClone();
        addTestCommits();
    }

    @Test
    public void testGetChangedFiles() throws GitAPIException, IOException {
        cut.openGit();

        final Map<String, String> expectedFiles = new HashMap<>();
        expectedFiles.put("file1", "hello world\nhello world");
        expectedFiles.put("file2", "hi world");

        assertEquals(expectedFiles, cut.getChangedFiles());
        createNextCommit();

        expectedFiles.clear();
        expectedFiles.put("file1", "hello world\nhello worldhi world!\nhello hi");
        expectedFiles.put("file2", "hi worldworld");
        assertEquals(expectedFiles, cut.getChangedFiles());
        assertEquals(Collections.emptyMap(), cut.getChangedFiles());

        cut.closeGit();
    }

    @Test
    public void testGetChangedFilesTagsOnly() throws GitAPIException, IOException {
        cut.openGit();
        cut.env = Environment.PRODUCTION;

        final Map<String, String> expectedFiles = new HashMap<>();
        expectedFiles.put("file1", "hello world\nhello world");

        assertEquals(expectedFiles, cut.getChangedFiles());
        assertEquals(Collections.emptyMap(), cut.getChangedFiles());

        createNextCommit();

        expectedFiles.clear();
        expectedFiles.put("file1", "hello world\nhello worldhi world!\nhello hi");
        expectedFiles.put("file2", "hi world");
        assertEquals(expectedFiles, cut.getChangedFiles());

        cut.closeGit();
    }

    @After
    public void tearDown() throws IOException {
        delete(cut.gitDirectory);
        delete(gitCloneDirectory);
    }

    private static void delete(final File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles())
                delete(c);
        }
        if (!f.delete())
            throw new FileNotFoundException("Failed to delete file: " + f);
    }

    private void initGitAndClone() throws GitAPIException {
        Git.init().setDirectory(cut.gitDirectory).setBare(true).call().close();
        Git.cloneRepository().setURI(cut.gitDirectory.getAbsolutePath()).setDirectory(gitCloneDirectory).call().close();
    }

    private void addTestCommits() throws IOException, GitAPIException {
        final Git git = Git.open(gitCloneDirectory);

        try (FileWriter writer = new FileWriter(file1, true)) {
            writer.write("hello world");
        }
        git.add().addFilepattern(".").call();
        git.commit().setMessage("updated").call();

        try (FileWriter writer = new FileWriter(file1, true)) {
            writer.append("\nhello world");
        }
        git.add().addFilepattern(".").call();
        git.commit().setMessage("updated").call();
        git.tag().setName("v0.1").call();

        try (FileWriter writer = new FileWriter(file2, true)) {
            writer.write("hi world");
        }
        git.add().addFilepattern(".").call();
        git.commit().setMessage("updated").call();
        git.push().setRemote("origin").add("master").setPushTags().call();

        git.close();
    }

    private void createNextCommit() throws IOException, GitAPIException {
        final Git git = Git.open(gitCloneDirectory);
        git.pull().setRemote("origin").setRemoteBranchName("master").call();

        try (FileWriter writer = new FileWriter(file1, true)) {
            writer.write("hi world!\nhello hi");
        }
        git.add().addFilepattern(".").call();
        git.commit().setMessage("updated file1").call();
        git.tag().setName("v0.2").call();

        try (FileWriter writer = new FileWriter(file2, true)) {
            writer.write("world");
        }
        git.add().addFilepattern(".").call();
        git.commit().setMessage("updated file2").call();

        git.push().setRemote("origin").add("master").setPushTags().call();

        git.close();
    }

}