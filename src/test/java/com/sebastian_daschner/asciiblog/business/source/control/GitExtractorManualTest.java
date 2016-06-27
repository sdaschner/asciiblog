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

import com.sebastian_daschner.asciiblog.business.source.entity.ChangeSet;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Ignore
public class GitExtractorManualTest {

    private GitExtractor cut;

    @Before
    public void setUp() throws IOException, GitAPIException {
        cut = new GitExtractor();
        cut.gitDirectory = Paths.get("/home/asciiblog/source/").toFile();
        cut.fileNameNormalizer = new FileNameNormalizer();
    }

    @Test
    public void test() throws GitAPIException, IOException {
        cut.openGit();

        final ChangeSet changes = cut.getChanges();
        System.out.println("changed entries = " + changes.getChangedFiles().keySet());
        System.out.println("changes = " + changes);
        assertFalse(changes.getChangedFiles().isEmpty());
        assertTrue(cut.getChanges().getChangedFiles().isEmpty());

        cut.closeGit();
    }

}