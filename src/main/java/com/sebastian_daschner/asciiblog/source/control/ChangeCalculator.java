package com.sebastian_daschner.asciiblog.source.control;

import com.sebastian_daschner.asciiblog.source.entity.ChangeSet;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.eclipse.jgit.lib.Constants.MASTER;

class ChangeCalculator {

    private static final String ASCIIDOC_SUFFIX = ".adoc";

    private final Git git;

    ChangeCalculator(final Git git) {
        this.git = git;
    }

    /**
     * Returns a change set with all files which exist at {@code commit}.
     */
    ChangeSet getChanges(final ObjectId commit) throws GitAPIException {
        try {
            checkout(commit.getName());

            final ChangeSet changes = new ChangeSet();

            getCurrentFiles().forEach(f -> changes.getChangedFiles().put(normalizePath(f.getName()), readFileContent(f)));

            return changes;
        } finally {
            resetGit();
        }
    }

    private void checkout(final String commitHash) throws GitAPIException {
        git.checkout().setName(commitHash).call();
    }

    private Stream<File> getCurrentFiles() {
        return Stream.of(git.getRepository().getWorkTree().listFiles(f -> f.isFile() && isRelevant(f.getName())));
    }

    private boolean isRelevant(final String fileName) {
        return fileName.endsWith(ASCIIDOC_SUFFIX);
    }

    private String normalizePath(final String fileName) {
        if (!fileName.endsWith(ASCIIDOC_SUFFIX))
            throw new IllegalArgumentException("File name '" + fileName + "' does not end with " + ASCIIDOC_SUFFIX);

        return fileName.substring(0, fileName.length() - ASCIIDOC_SUFFIX.length());
    }

    private String readFileContent(final File file) {
        try {
            return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Could not read AsciiDoc file content", e);
        }
    }

    /**
     * Returns the files which have been changed between {@code firstCommit} and {@code secondCommit}.
     */
    ChangeSet getChanges(final ObjectId firstCommit, final ObjectId secondCommit) throws GitAPIException, IOException {
        final List<DiffEntry> diff = calculateDiff(firstCommit, secondCommit);
        try {
            checkout(secondCommit.getName());

            final ChangeSet changes = new ChangeSet();

            getChangedFiles(diff).forEach(f -> changes.getChangedFiles().put(normalizePath(f.getName()), readFileContent(f)));

            getRemovedFilePaths(diff).map(this::normalizePath).forEach(changes.getRemovedFiles()::add);

            return changes;
        } finally {
            resetGit();
        }
    }

    private List<DiffEntry> calculateDiff(final ObjectId firstCommit, final ObjectId secondCommit) throws GitAPIException, IOException {
        return git.diff().setShowNameAndStatusOnly(true)
                .setOldTree(prepareTreeParser(firstCommit))
                .setNewTree(prepareTreeParser(secondCommit)).call();
    }

    private Stream<File> getChangedFiles(final List<DiffEntry> diff) {
        return diff.stream().map(DiffEntry::getNewPath).distinct()
                .filter(this::isRelevant)
                .map(p -> Paths.get(git.getRepository().getWorkTree().getAbsolutePath(), p).toFile())
                .filter(File::isFile);
    }

    private Stream<String> getRemovedFilePaths(final List<DiffEntry> diff) {
        return diff.stream()
                .filter(d -> d.getChangeType() == DiffEntry.ChangeType.DELETE || d.getChangeType() == DiffEntry.ChangeType.RENAME)
                .map(DiffEntry::getOldPath)
                .filter(this::isRelevant);
    }

    private AbstractTreeIterator prepareTreeParser(final ObjectId commit) throws IOException {
        final RevWalk walk = new RevWalk(git.getRepository());
        final RevTree tree = walk.parseTree(walk.parseCommit(commit).getTree().getId());
        final CanonicalTreeParser treeParser = new CanonicalTreeParser();

        try (ObjectReader reader = git.getRepository().newObjectReader()) {
            treeParser.reset(reader, tree.getId());
        } finally {
            walk.dispose();
        }
        return treeParser;
    }

    private void resetGit() throws GitAPIException {
        checkout(MASTER);
    }

}
