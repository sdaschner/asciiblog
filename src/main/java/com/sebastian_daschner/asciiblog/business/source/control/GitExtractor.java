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

import com.sebastian_daschner.asciiblog.business.environment.control.FileConfig;
import com.sebastian_daschner.asciiblog.business.source.entity.ChangeSet;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.eclipse.jgit.lib.Constants.MASTER;

@Startup
@Singleton
public class GitExtractor {

    @Inject
    @FileConfig(FileConfig.Location.GIT)
    File gitDirectory;

    @Inject
    FileNameNormalizer fileNameNormalizer;

    private Git git;
    private ObjectId lastCommit;

    @PostConstruct
    public void openGit() {
        try {
            git = Git.cloneRepository().setDirectory(Files.createTempDirectory("blog-git").toFile()).setURI(gitDirectory.getAbsolutePath()).call();
        } catch (IOException | GitAPIException e) {
            throw new IllegalStateException("Could not open Git repository.", e);
        }
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
                return getChanges(currentCommit);
            }

            return getChanges(lastCommit, currentCommit);

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

    /**
     * Returns a change set with all files which exist at {@code commit}.
     */
    private ChangeSet getChanges(final ObjectId commit) throws GitAPIException, IOException {
        final RevWalk revWalk = new RevWalk(git.getRepository());
        try {
            final RevCommit revCommit = revWalk.parseCommit(commit);

            git.checkout().setName(revCommit.getName()).call();

            final Set<File> changedFiles = Stream.of(git.getRepository().getWorkTree()
                    .listFiles(f -> f.isFile() && fileNameNormalizer.isRelevant(f.getName())))
                    .collect(Collectors.toSet());

            final ChangeSet changes = new ChangeSet();
            changes.getChangedFiles().putAll(readFileContent(changedFiles));
            return changes;
        } finally {
            revWalk.dispose();
            resetGit();
        }
    }

    /**
     * Returns the files which have been changed between {@code firstCommit} and {@code secondCommit}.
     */
    private ChangeSet getChanges(final ObjectId firstCommit, final ObjectId secondCommit) throws GitAPIException, IOException {
        final List<DiffEntry> diffs = git.diff().setShowNameAndStatusOnly(true)
                .setOldTree(prepareTreeParser(firstCommit))
                .setNewTree(prepareTreeParser(secondCommit)).call();
        try {
            git.checkout().setName(secondCommit.getName()).call();

            final Set<File> changedFiles = diffs.stream().map(DiffEntry::getNewPath).distinct()
                    .filter(fileNameNormalizer::isRelevant)
                    .map(p -> Paths.get(git.getRepository().getWorkTree().getAbsolutePath(), p).toFile())
                    .filter(File::isFile).collect(Collectors.toSet());

            final Set<String> removedFiles = diffs.stream()
                    .filter(d -> d.getChangeType() == DiffEntry.ChangeType.DELETE || d.getChangeType() == DiffEntry.ChangeType.RENAME)
                    .map(DiffEntry::getOldPath).map(fileNameNormalizer::normalize).collect(Collectors.toSet());

            final ChangeSet changes = new ChangeSet();
            changes.getChangedFiles().putAll(readFileContent(changedFiles));
            changes.getRemovedFiles().addAll(removedFiles);
            return changes;
        } finally {
            resetGit();
        }
    }

    private AbstractTreeIterator prepareTreeParser(final ObjectId commit) throws IOException {
        final RevWalk walk = new RevWalk(git.getRepository());
        final RevTree tree = walk.parseTree(walk.parseCommit(commit).getTree().getId());
        final CanonicalTreeParser treeParser = new CanonicalTreeParser();

        final ObjectReader reader = git.getRepository().newObjectReader();
        try {
            treeParser.reset(reader, tree.getId());
        } finally {
            reader.release();
            walk.dispose();
        }
        return treeParser;
    }

    private void resetGit() throws GitAPIException {
        git.checkout().setName(MASTER).call();
    }

    private Map<String, String> readFileContent(final Set<File> files) {
        final Map<String, String> fileContent = new HashMap<>();

        files.forEach(f -> {
            try {
                final String content = new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8);
                fileContent.put(fileNameNormalizer.normalize(f.getName()), content);
            } catch (IOException e) {
                throw new RuntimeException("Could not read AsciiDoc file content", e);
            }
        });

        return fileContent;
    }

}
