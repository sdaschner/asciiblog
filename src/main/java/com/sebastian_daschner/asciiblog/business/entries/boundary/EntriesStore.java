package com.sebastian_daschner.asciiblog.business.entries.boundary;

import com.sebastian_daschner.asciiblog.business.entries.control.EntriesCache;
import com.sebastian_daschner.asciiblog.business.entries.entity.Entry;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class EntriesStore {

    private static final int NUMBER_TEASERS = 6;

    @Inject
    EntriesCache cache;

    public List<Entry> getTeaserEntries() {
        return cache.getLastEntries(NUMBER_TEASERS);
    }

    public List<Entry> getAllEntries() {
        return cache.getAllEntries();
    }

    public Entry getEntry(final String name) {
        return cache.get(name);
    }

}
