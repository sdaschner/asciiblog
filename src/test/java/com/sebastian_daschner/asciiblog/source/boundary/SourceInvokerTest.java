package com.sebastian_daschner.asciiblog.source.boundary;

import com.sebastian_daschner.asciiblog.entries.control.EntryCache;
import com.sebastian_daschner.asciiblog.entries.entity.Entry;
import com.sebastian_daschner.asciiblog.source.control.EntryCompiler;
import com.sebastian_daschner.asciiblog.source.control.GitExtractor;
import com.sebastian_daschner.asciiblog.source.entity.ChangeSetBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class SourceInvokerTest {

    private SourceInvoker cut;

    @BeforeEach
    public void setUp() {
        cut = new SourceInvoker();
        cut.gitExtractor = mock(GitExtractor.class);
        cut.entryCompiler = mock(EntryCompiler.class);
        cut.cache = mock(EntryCache.class);
    }

    @Test
    public void testCheckNewEntriesNewFiles() {
        when(cut.gitExtractor.getChanges()).thenReturn(ChangeSetBuilder.withRemovedFiles()
                .andChangedFile("file1", "= Test").andChangedFile("file2", "= Test 2").build());
        when(cut.entryCompiler.compile(anyString(), anyString())).thenReturn(new Entry("test", "Test", LocalDateTime.now(), "", "", Set.of(), false));

        cut.checkNewEntries();

        verify(cut.entryCompiler).compile("file1", "= Test");
        verify(cut.entryCompiler).compile("file2", "= Test 2");

        verify(cut.cache, times(1)).store(any());
    }

    @Test
    public void testCheckNewEntriesDeletedFiles() {
        when(cut.gitExtractor.getChanges()).thenReturn(ChangeSetBuilder.withRemovedFiles("file1").build());

        cut.checkNewEntries();

        verify(cut.entryCompiler, never()).compile(anyString(), anyString());
        verify(cut.cache, never()).store(any());

        final ArgumentCaptor<Set<String>> captor = ArgumentCaptor.forClass(Set.class);
        verify(cut.cache).remove(captor.capture());

        assertEquals(Set.of("file1"), captor.getValue());
    }

    @Test
    public void testCheckNewEntriesNewAndDeletedFiles() {
        when(cut.gitExtractor.getChanges()).thenReturn(ChangeSetBuilder.withRemovedFiles("file3")
                .andChangedFile("file1", "= Test").andChangedFile("file2", "= Test 2").build());
        when(cut.entryCompiler.compile(anyString(), anyString())).thenReturn(new Entry("test", "Test", LocalDateTime.now(), "", "", Set.of(), false));

        cut.checkNewEntries();

        verify(cut.entryCompiler).compile("file1", "= Test");
        verify(cut.entryCompiler).compile("file2", "= Test 2");

        verify(cut.cache, times(1)).store(any());

        final ArgumentCaptor<Set<String>> captor = ArgumentCaptor.forClass(Set.class);
        verify(cut.cache).remove(captor.capture());

        assertEquals(Set.of("file3"), captor.getValue());
    }

}