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

package com.sebastian_daschner.asciiblog.business.source.boundary;

import com.sebastian_daschner.asciiblog.business.entries.control.EntriesCache;
import com.sebastian_daschner.asciiblog.business.entries.entity.Entry;
import com.sebastian_daschner.asciiblog.business.source.control.EntryCompiler;
import com.sebastian_daschner.asciiblog.business.source.control.GitExtractor;
import com.sebastian_daschner.asciiblog.business.source.entity.ChangeSetBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

public class SourceInvokerTest {

    private SourceInvoker cut;

    @Before
    public void setUp() {
        cut = new SourceInvoker();
        cut.gitExtractor = Mockito.mock(GitExtractor.class);
        cut.entryCompiler = Mockito.mock(EntryCompiler.class);
        cut.cache = Mockito.mock(EntriesCache.class);
    }

    @Test
    public void testCheckNewEntriesNewFiles() {
        Mockito.when(cut.gitExtractor.getChanges())
                .thenReturn(ChangeSetBuilder.withRemovedFiles().andChangedFile("file1", "= Test").andChangedFile("file2", "= Test 2").build());
        Mockito.when(cut.entryCompiler.compile(anyString(), anyString())).thenReturn(new Entry("test", "Test", LocalDate.now(), "", ""));

        cut.checkNewEntries();

        Mockito.verify(cut.entryCompiler).compile("file1", "= Test");
        Mockito.verify(cut.entryCompiler).compile("file2", "= Test 2");

        Mockito.verify(cut.cache, times(2)).store(any());
    }

    @Test
    public void testCheckNewEntriesDeletedFiles() {
        Mockito.when(cut.gitExtractor.getChanges())
                .thenReturn(ChangeSetBuilder.withRemovedFiles("file1").build());

        cut.checkNewEntries();

        Mockito.verify(cut.entryCompiler, never()).compile(anyString(), anyString());
        Mockito.verify(cut.cache, never()).store(any());

        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(cut.cache).remove(captor.capture());

        assertEquals("file1", captor.getValue());
    }

    @Test
    public void testCheckNewEntriesNewAndDeletedFiles() {
        Mockito.when(cut.gitExtractor.getChanges())
                .thenReturn(ChangeSetBuilder.withRemovedFiles("file3").andChangedFile("file1", "= Test").andChangedFile("file2", "= Test 2").build());
        Mockito.when(cut.entryCompiler.compile(anyString(), anyString())).thenReturn(new Entry("test", "Test", LocalDate.now(), "", ""));

        cut.checkNewEntries();

        Mockito.verify(cut.entryCompiler).compile("file1", "= Test");
        Mockito.verify(cut.entryCompiler).compile("file2", "= Test 2");

        Mockito.verify(cut.cache, times(2)).store(any());

        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(cut.cache).remove(captor.capture());

        assertEquals("file3", captor.getValue());
    }

}