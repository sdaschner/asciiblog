package com.sebastian_daschner.asciiblog.business.entries.control;

import com.sebastian_daschner.asciiblog.business.entries.entity.Entry;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class EntriesCache {

    /**
     * The blog entries identified by the entry name.
     */
    private final Map<String, Entry> entries = new ConcurrentHashMap<>();

    public List<Entry> getLastEntries(final int number) {
        return entries.values().parallelStream()
                .sorted(Comparator.comparing(Entry::getDate).reversed())
                .limit(number)
                .collect(Collectors.toList());
    }

    public List<Entry> getAllEntries() {
        return entries.values().parallelStream()
                .sorted(Comparator.comparing(Entry::getDate).reversed())
                .collect(Collectors.toList());
    }

    public Entry get(final String name) {
        return entries.get(name);
    }

    public void store(final Entry entry) {
        entries.put(entry.getLink(), entry);
    }

}
