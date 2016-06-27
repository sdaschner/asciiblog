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

import com.sebastian_daschner.asciiblog.business.entries.control.EntryCache;
import com.sebastian_daschner.asciiblog.business.entries.entity.Entry;
import com.sebastian_daschner.asciiblog.business.source.control.EntryCompiler;
import com.sebastian_daschner.asciiblog.business.source.control.GitExtractor;
import com.sebastian_daschner.asciiblog.business.source.entity.ChangeSetBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class SourceInvokerTest {

    private SourceInvoker cut;

    @Before
    public void setUp() {
        cut = new SourceInvoker();
        cut.gitExtractor = mock(GitExtractor.class);
        cut.entryCompiler = mock(EntryCompiler.class);
        cut.cache = mock(EntryCache.class);
        cut.logger = mock(Logger.class);
    }

    @Test
    public void testCheckNewEntriesNewFiles() {
        when(cut.gitExtractor.getChanges()).thenReturn(ChangeSetBuilder.withRemovedFiles()
                .andChangedFile("file1", "= Test").andChangedFile("file2", "= Test 2").build());
        when(cut.entryCompiler.compile(anyString(), anyString())).thenReturn(new Entry("test", "Test", LocalDate.now(), "", ""));

        cut.checkNewEntries();

        verify(cut.entryCompiler).compile("file1", "= Test");
        verify(cut.entryCompiler).compile("file2", "= Test 2");

        verify(cut.cache, times(2)).store(any());
    }

    @Test
    public void testCheckNewEntriesDeletedFiles() {
        when(cut.gitExtractor.getChanges()).thenReturn(ChangeSetBuilder.withRemovedFiles("file1").build());

        cut.checkNewEntries();

        verify(cut.entryCompiler, never()).compile(anyString(), anyString());
        verify(cut.cache, never()).store(any());

        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(cut.cache).remove(captor.capture());

        assertEquals("file1", captor.getValue());
    }

    @Test
    public void testCheckNewEntriesNewAndDeletedFiles() {
        when(cut.gitExtractor.getChanges()).thenReturn(ChangeSetBuilder.withRemovedFiles("file3")
                .andChangedFile("file1", "= Test").andChangedFile("file2", "= Test 2").build());
        when(cut.entryCompiler.compile(anyString(), anyString())).thenReturn(new Entry("test", "Test", LocalDate.now(), "", ""));

        cut.checkNewEntries();

        verify(cut.entryCompiler).compile("file1", "= Test");
        verify(cut.entryCompiler).compile("file2", "= Test 2");

        verify(cut.cache, times(2)).store(any());

        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(cut.cache).remove(captor.capture());

        assertEquals("file3", captor.getValue());
    }

}