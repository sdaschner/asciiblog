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
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GitExtractorIT {

    private GitExtractor cut;

    @Before
    public void setUp() throws IOException, GitAPIException {
        cut = new GitExtractor();
        cut.gitDirectory = Paths.get("/home/asciiblog/source/").toFile();
    }

    @Test
    public void testIntegration() throws GitAPIException, IOException {
        cut.env = Environment.INTEGRATION;
        cut.openGit();

        final Map<String, String> changedFiles = cut.getChangedFiles();
        System.out.println("changedFiles = " + changedFiles.keySet());
        assertFalse(changedFiles.isEmpty());
        assertTrue(cut.getChangedFiles().isEmpty());

        cut.closeGit();
    }

    @Test
    public void testProduction() throws GitAPIException, IOException {
        cut.env = Environment.PRODUCTION;
        cut.openGit();

        final Map<String, String> changedFiles = cut.getChangedFiles();
        System.out.println("changedFiles = " + changedFiles.keySet());
        assertFalse(changedFiles.isEmpty());
        assertTrue(cut.getChangedFiles().isEmpty());

        cut.closeGit();
    }

}