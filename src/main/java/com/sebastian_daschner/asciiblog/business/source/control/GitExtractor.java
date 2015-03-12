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

import com.sebastian_daschner.asciiblog.business.environment.control.Environment;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.*;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.eclipse.jgit.lib.Constants.MASTER;

@Startup
@Singleton
public class GitExtractor {

    @Inject
    Environment env;

    // TODO change to (remote) URL / SSH etc.
    @Inject
    File gitDirectory;

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

    /**
     * Returns the files which have been changed since the last call.
     *
     * @return The changed files with filename as key and content as value
     */
    public Map<String, String> getChangedFiles() {
        ObjectId currentCommit = null;
        try {
            updateGit();
            currentCommit = getLatestCommit();

            if (currentCommit == null)
                return Collections.emptyMap();

            if (lastCommit == null) {
                return getChangedFiles(currentCommit);
            }

            return getChangedFiles(lastCommit, currentCommit);

        } catch (IOException | GitAPIException e) {
            return Collections.emptyMap();
        } finally {
            lastCommit = currentCommit;
        }
    }

    private void updateGit() throws GitAPIException {
        git.pull().call();
    }

    private ObjectId getLatestCommit() throws IOException {
        final boolean tagsOnly = env == Environment.PRODUCTION;
        final Repository repository = git.getRepository();

        final ObjectId currentHead = repository.resolve(MASTER);
        if (!tagsOnly)
            return currentHead;

        final RevWalk revWalk = new RevWalk(repository);

        try {
            final Set<ObjectId> tagCommitIds = repository.getTags().values().stream()
                    .map(Ref::getObjectId).map(id -> {
                        try {
                            return revWalk.parseTag(id);
                        } catch (IOException e) {
                            return null;
                        }
                    }).filter(Objects::nonNull)
                    .map(RevTag::getObject).map(RevObject::getId)
                    .collect(Collectors.toSet());

            revWalk.markStart(revWalk.parseCommit(currentHead));

            for (final RevCommit currentCommit : revWalk) {
                if (tagCommitIds.contains(currentCommit.getId()))
                    return currentCommit.getId();
            }

        } finally {
            revWalk.dispose();
        }
        return null;
    }

    /**
     * Returns the files which have been changed between the first commit and {@code commit}.
     */
    private Map<String, String> getChangedFiles(final ObjectId commit) throws GitAPIException, IOException {
        final RevWalk revWalk = new RevWalk(git.getRepository());
        try {
            final RevCommit revCommit = revWalk.parseCommit(commit);

            git.checkout().setName(revCommit.getName()).call();

            return readFileContent(Stream.of(git.getRepository().getWorkTree().listFiles(File::isFile)).collect(Collectors.toSet()));
        } finally {
            revWalk.dispose();
            git.checkout().setName(MASTER).call();
        }
    }

    /**
     * Returns the files which have been changed between {@code firstCommit} and {@code secondCommit}.
     */
    private Map<String, String> getChangedFiles(final ObjectId firstCommit, final ObjectId secondCommit) throws GitAPIException, IOException {
        final List<DiffEntry> diffs = git.diff().setShowNameAndStatusOnly(true)
                .setOldTree(prepareTreeParser(firstCommit))
                .setNewTree(prepareTreeParser(secondCommit)).call();
        try {
            git.checkout().setName(secondCommit.getName()).call();

            final Set<File> changedFiles = diffs.stream().map(DiffEntry::getNewPath).distinct()
                    .map(p -> Paths.get(git.getRepository().getWorkTree().getAbsolutePath(), p).toFile())
                    .filter(File::isFile).collect(Collectors.toSet());

            return readFileContent(changedFiles);
        } finally {
            git.checkout().setName(MASTER).call();
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

    private Map<String, String> readFileContent(final Set<File> files) {
        return files.stream().collect(HashMap::new, (m, f) -> {
            try {
                final String content = new String(Files.readAllBytes(f.toPath()));
                m.put(f.getName(), content);
            } catch (IOException e) {
                // ignore file
            }
        }, Map::putAll);
    }

    @PreDestroy
    public void closeGit() {
        final File directory = git.getRepository().getWorkTree();
        git.close();
        delete(directory);
    }

    private void delete(final File file) {
        if (file.isDirectory())
            Stream.of(file.listFiles()).forEach(this::delete);
        if (!file.delete())
            throw new IllegalStateException("Could not delete file: " + file);
    }

}
