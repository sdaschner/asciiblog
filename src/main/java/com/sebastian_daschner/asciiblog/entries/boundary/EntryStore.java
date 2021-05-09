package com.sebastian_daschner.asciiblog.entries.boundary;

import com.sebastian_daschner.asciiblog.entries.control.EntryCache;
import com.sebastian_daschner.asciiblog.entries.entity.Entry;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class EntryStore {

    private static final int NUMBER_TEASERS = 8;

    @Inject
    EntryCache cache;

    @Inject
    Event<String> accessEvents;

    public List<Entry> getTeaserEntries() {
        return cache.getLastEntries(NUMBER_TEASERS);
    }

    public List<Entry> getAllEntries() {
        return cache.getAllEntries();
    }

    public Entry getEntry(final String name) {
        final Entry entry = cache.get(name);

        if (entry != null)
            accessEvents.fire(name);

        return entry;
    }

    public Map<String, Integer> getAllTags() {
        return cache.getAllTags();
    }

    public List<Entry> getAllEntries(String tag) {
        return cache.getAllEntries(tag);
    }

}
