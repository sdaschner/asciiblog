package com.sebastian_daschner.asciiblog.entries.control;

import com.sebastian_daschner.asciiblog.entries.entity.Entry;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@ApplicationScoped
public class EntryCache {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * The blog entries identified by the entry name.
     */
    private final Map<String, Entry> entries = new HashMap<>();

    /**
     * The tags and number of matching entries.
     */
    private final Map<String, Integer> tags = new HashMap<>();

    private final Comparator<Entry> entryComparator = Comparator
            .comparing(Entry::getDate).reversed()
            .thenComparing(Entry::getTitle);

    public List<Entry> getLastEntries(int number) {
        try {
            lock.readLock().lock();
            return entries.values().stream()
                    .sorted(entryComparator)
                    .limit(number)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Entry> getAllEntries() {
        try {
            lock.readLock().lock();
            return entries.values().stream()
                    .sorted(entryComparator)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public Entry get(String name) {
        try {
            lock.readLock().lock();
            return entries.get(name);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void store(Collection<Entry> entries) {
        try {
            lock.writeLock().lock();
            entries.forEach(e -> this.entries.put(e.getLink(), e));
            recalculateTags();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void remove(Collection<String> names) {
        try {
            lock.writeLock().lock();
            names.forEach(entries::remove);
            recalculateTags();
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void recalculateTags() {
        tags.clear();
        tags.putAll(entries.values().stream()
                .map(Entry::getTags)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(s -> s, Collectors.summingInt(s -> 1))));
    }

    public Map<String, Integer> getAllTags() {
        try {
            lock.readLock().lock();
            return tags
                    .entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Entry> getAllEntries(String tag) {
        try {
            lock.readLock().lock();
            return entries.values().stream()
                    .filter(e -> e.getTags().contains(tag))
                    .sorted(entryComparator)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

}
