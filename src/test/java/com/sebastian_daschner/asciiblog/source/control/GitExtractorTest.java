package com.sebastian_daschner.asciiblog.source.control;

import com.sebastian_daschner.asciiblog.source.entity.ChangeSet;
import com.sebastian_daschner.asciiblog.source.entity.ChangeSetBuilder;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GitExtractorTest {

    private GitExtractor cut;
    private File gitCloneDirectory;
    private File file1;
    private File file2;
    private File gitDirectory;

    @BeforeEach
    public void setUp() throws Exception {
        gitDirectory = Files.createTempDirectory("git-blog-").toFile();

        cut = new GitExtractor();
        // assuming that normalizer works correct
        gitCloneDirectory = Files.createTempDirectory("git-clone-").toFile();
        file1 = Paths.get(gitCloneDirectory.getAbsolutePath(), "file1.adoc").toFile();
        file2 = Paths.get(gitCloneDirectory.getAbsolutePath(), "file2.adoc").toFile();

        deactivateTransportCallback();
        initGitAndClone();
        addTestCommits();
        openGit();
    }

    @Test
    public void testGetChanges() throws Exception {
        ChangeSet expectedChanges = ChangeSetBuilder.withRemovedFiles()
                .andChangedFile("file1", "hello world\nhello world")
                .andChangedFile("file2", "hi world").build();

        Assertions.assertEquals(expectedChanges, cut.getChanges());
        Assertions.assertEquals(new ChangeSet(), cut.getChanges());
        createNextCommit();

        expectedChanges = ChangeSetBuilder.withRemovedFiles()
                .andChangedFile("file1", "hello world\nhello worldhi world!\nhello hi")
                .andChangedFile("file2", "hi worldworld").build();
        Assertions.assertEquals(expectedChanges, cut.getChanges());
        Assertions.assertEquals(new ChangeSet(), cut.getChanges());

        cut.closeGit();
    }

    @Test
    public void testGetChangesRenamedEntry() throws Exception {
        createNextCommit();

        ChangeSet expectedChanges = ChangeSetBuilder.withRemovedFiles()
                .andChangedFile("file1", "hello world\nhello worldhi world!\nhello hi")
                .andChangedFile("file2", "hi worldworld").build();

        Assertions.assertEquals(expectedChanges, cut.getChanges());
        Assertions.assertEquals(new ChangeSet(), cut.getChanges());

        renameFile();

        expectedChanges = ChangeSetBuilder.withRemovedFiles("file2")
                .andChangedFile("file3", "hi worldworld").build();
        Assertions.assertEquals(expectedChanges, cut.getChanges());
        Assertions.assertEquals(new ChangeSet(), cut.getChanges());

        cut.closeGit();
    }

    @Test
    public void testGetChangesDeletedEntry() throws Exception {
        createNextCommit();

        ChangeSet expectedChanges = ChangeSetBuilder.withRemovedFiles()
                .andChangedFile("file1", "hello world\nhello worldhi world!\nhello hi")
                .andChangedFile("file2", "hi worldworld").build();

        Assertions.assertEquals(expectedChanges, cut.getChanges());
        Assertions.assertEquals(new ChangeSet(), cut.getChanges());

        deleteFile();

        expectedChanges = ChangeSetBuilder.withRemovedFiles("file2").build();
        Assertions.assertEquals(expectedChanges, cut.getChanges());
        Assertions.assertEquals(new ChangeSet(), cut.getChanges());

        cut.closeGit();
    }

    @Test
    public void testGetChangesChangedAndRenamedEntry() throws Exception {
        createNextCommit();

        ChangeSet expectedChanges = ChangeSetBuilder.withRemovedFiles()
                .andChangedFile("file1", "hello world\nhello worldhi world!\nhello hi")
                .andChangedFile("file2", "hi worldworld").build();

        Assertions.assertEquals(expectedChanges, cut.getChanges());
        Assertions.assertEquals(new ChangeSet(), cut.getChanges());

        changeAndRenameFile();

        expectedChanges = ChangeSetBuilder.withRemovedFiles("file2")
                .andChangedFile("file3", "hi worldworld\nchanged").build();
        Assertions.assertEquals(expectedChanges, cut.getChanges());
        Assertions.assertEquals(new ChangeSet(), cut.getChanges());

        cut.closeGit();
    }

    @Test
    public void testGetChangesIgnoreNonAsciiDocFiles() throws Exception {
        createNextCommit();
        addNotRelevantFile("foobar");

        ChangeSet expectedChanges = ChangeSetBuilder.withRemovedFiles()
                .andChangedFile("file1", "hello world\nhello worldhi world!\nhello hi")
                .andChangedFile("file2", "hi worldworld").build();

        Assertions.assertEquals(expectedChanges, cut.getChanges());
        Assertions.assertEquals(new ChangeSet(), cut.getChanges());

        addNotRelevantFile("foobar2");
        Assertions.assertEquals(new ChangeSet(), cut.getChanges());

        addNotRelevantFile("foobar3");
        renameFile();

        expectedChanges = ChangeSetBuilder.withRemovedFiles("file2")
                .andChangedFile("file3", "hi worldworld").build();
        Assertions.assertEquals(expectedChanges, cut.getChanges());
        Assertions.assertEquals(new ChangeSet(), cut.getChanges());

        cut.closeGit();
    }

    private void changeAndRenameFile() throws IOException, GitAPIException {
        final Git git = Git.open(gitCloneDirectory);
        git.pull().setRemote("origin").setRemoteBranchName("master").call();

        try (FileWriter writer = new FileWriter(file2, true)) {
            writer.append("\nchanged");
        }

        if (!file2.renameTo(file2.toPath().resolveSibling("file3.adoc").toFile()))
            throw new IOException("Could not rename file2 to file3");

        git.add().setUpdate(true).addFilepattern(".").call();
        git.add().addFilepattern(".").call();
        git.commit().setMessage("changed file2 and renamed to file3").call();
        git.push().setRemote("origin").add("master").call();
        git.close();
    }

    @AfterEach
    public void tearDown() throws IOException {
        delete(gitDirectory);
        delete(gitCloneDirectory);
    }

    private void openGit() throws Exception {
        final File workingDir = Files.createTempDirectory("blog-git").toFile();

        final Git git = Git.cloneRepository()
                .setDirectory(workingDir)
                .setURI(gitDirectory.getAbsolutePath())
                .call();

        final ChangeCalculator changeCalculator = new ChangeCalculator(git);

        inject("git", git);
        inject("changeCalculator", changeCalculator);
    }

    private void inject(final String fieldName, final Object object) throws NoSuchFieldException, IllegalAccessException {
        final Field field = GitExtractor.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(cut, object);
    }

    private static void delete(final File f) throws IOException {
        final File[] files;
        if (f.isDirectory() && (files = f.listFiles()) != null)
            for (File c : files)
                delete(c);

        if (!f.delete())
            throw new IOException("Failed to delete file: " + f);
    }

    private void initGitAndClone() throws GitAPIException {
        Git.init().setDirectory(gitDirectory).setBare(true).call().close();
        Git.cloneRepository().setURI(gitDirectory.getAbsolutePath()).setDirectory(gitCloneDirectory).call().close();
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

        try (FileWriter writer = new FileWriter(file2, true)) {
            writer.write("hi world");
        }
        git.add().addFilepattern(".").call();
        git.commit().setMessage("updated").call();
        git.push().setRemote("origin").add("master").call();

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

        try (FileWriter writer = new FileWriter(file2, true)) {
            writer.write("world");
        }
        git.add().addFilepattern(".").call();
        git.commit().setMessage("updated file2").call();

        git.push().setRemote("origin").add("master").call();

        git.close();
    }

    private void renameFile() throws IOException, GitAPIException {
        final Git git = Git.open(gitCloneDirectory);
        git.pull().setRemote("origin").setRemoteBranchName("master").call();

        if (!file2.renameTo(file2.toPath().resolveSibling("file3.adoc").toFile()))
            throw new IOException("Could not rename file2 to file3");

        git.add().setUpdate(true).addFilepattern(".").call();
        git.add().addFilepattern(".").call();
        git.commit().setMessage("renamed file2 to file3").call();
        git.push().setRemote("origin").add("master").call();
        git.close();
    }

    private void deleteFile() throws IOException, GitAPIException {
        final Git git = Git.open(gitCloneDirectory);
        git.pull().setRemote("origin").setRemoteBranchName("master").call();

        if (!file2.delete())
            throw new IOException("Could not delete file2");

        git.add().setUpdate(true).addFilepattern(".").call();
        git.commit().setMessage("deleted file2").call();
        git.push().setRemote("origin").add("master").call();
        git.close();
    }

    private void addNotRelevantFile(final String fileName) throws GitAPIException, IOException {
        final Git git = Git.open(gitCloneDirectory);
        git.pull().setRemote("origin").setRemoteBranchName("master").call();

        try (FileWriter writer = new FileWriter(file1.toPath().resolveSibling(fileName + ".mustache").toFile(), true)) {
            writer.write("hello {{world}}");
        }

        git.add().addFilepattern(".").call();
        git.commit().setMessage("added template").call();
        git.push().setRemote("origin").add("master").call();
        git.close();
    }

    private void deactivateTransportCallback() throws ReflectiveOperationException {
        final Field field = GitExtractor.class.getDeclaredField("transportConfigCallback");

        field.setAccessible(true);
        field.set(cut, null);
    }

}