package com.sebastian_daschner.asciiblog.entries.control;

import com.sebastian_daschner.asciiblog.entries.entity.Entry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class EntryCacheTest {

    private EntryCache cut;
    private Map<String, Entry> entriesMap;

    private Entry entry1;
    private Entry entry2;
    private Entry entry3;
    private Entry entry4;
    private Entry entry5;

    @BeforeEach
    public void setUp() throws ReflectiveOperationException {
        cut = new EntryCache();
        entriesMap = new HashMap<>();

        injectFields();
        initTestEntries();
    }

    @Test
    public void testGet() {
        Entry actual = cut.get("entry3");
        assertThat(actual).isEqualTo(entry3);

        actual = cut.get("entry4");
        assertThat(actual).isEqualTo(entry4);

        actual = cut.get("entry1");
        assertThat(actual).isEqualTo(entry1);

        actual = cut.get("entry2");
        assertThat(actual).isEqualTo(entry2);
    }

    @Test
    public void testGetAllEntries() {
        final List<Entry> actual = cut.getAllEntries();
        assertThat(actual).isEqualTo(asList(entry3, entry4, entry1, entry2));
    }

    @Test
    public void testGetLastEntries() {
        List<Entry> actual = cut.getLastEntries(3);
        assertThat(actual).isEqualTo(asList(entry3, entry4, entry1));

        actual = cut.getLastEntries(1);
        assertThat(actual).isEqualTo(singletonList(entry3));

        actual = cut.getLastEntries(0);
        assertThat(actual).isEqualTo(emptyList());

        actual = cut.getLastEntries(5);
        assertThat(actual).isEqualTo(asList(entry3, entry4, entry1, entry2));
    }

    @Test
    public void testGetAllEntriesSameDay() {
        insertSameDayEntry();

        final List<Entry> actual = cut.getAllEntries();
        assertThat(actual).isEqualTo(asList(entry3, entry5, entry4, entry1, entry2));
    }

    @Test
    public void testStore() {
        final Entry newEntry = new Entry("entry5", "Entry 5", dateTime(2016, 5, 3), "foobar 5", "foobar <html><h5>", Set.of(), false);
        cut.store(List.of(newEntry));

        assertThat(cut.get("entry5")).isEqualTo(newEntry);

        final List<Entry> allEntries = cut.getAllEntries();
        assertThat(allEntries.size()).isEqualTo(5);
        assertThat(allEntries.get(0)).isEqualTo(newEntry);
    }

    @Test
    public void testRemove() {
        cut.remove(List.of("entry1"));

        assertThat(cut.get("entry1")).isNull();

        final List<Entry> allEntries = cut.getAllEntries();
        assertThat(allEntries).isEqualTo(asList(entry3, entry4, entry2));
    }

    private void injectFields() throws ReflectiveOperationException {
        final Field entriesField = cut.getClass().getDeclaredField("entries");
        entriesField.setAccessible(true);
        entriesField.set(cut, entriesMap);
    }

    private void initTestEntries() {
        entry1 = new Entry("entry1", "Entry 1", dateTime(2016, 1, 3), "foobar 1", "foobar <html><h1>", Set.of(), false);
        entry2 = new Entry("entry2", "Entry 2", dateTime(2016, 1, 1), "foobar 2", "foobar <html><h2>", Set.of(), false);
        entry3 = new Entry("entry3", "Entry 3", dateTime(2016, 2, 3), "foobar 3", "foobar <html><h3>", Set.of(), false);
        entry4 = new Entry("entry4", "Entry 4", dateTime(2016, 1, 4), "foobar 4", "foobar <html><h4>", Set.of(), false);

        entriesMap.put("entry1", entry1);
        entriesMap.put("entry2", entry2);
        entriesMap.put("entry3", entry3);
        entriesMap.put("entry4", entry4);
    }

    public void insertSameDayEntry() {
        entry5 = new Entry("entry5", "Entry 1.5", dateTime(2016, 1, 4), "foobar 5", "foobar <html><h5>", Set.of(), false);
        entriesMap.put("entry5", entry5);
    }

    private LocalDateTime dateTime(int year, int month, int dayOfMonth) {
        return LocalDateTime.of(year, month, dayOfMonth, 0, 0);
    }

}