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

package com.sebastian_daschner.asciiblog.business.entries.boundary;

import com.sebastian_daschner.asciiblog.business.entries.control.EntryCache;
import com.sebastian_daschner.asciiblog.business.entries.entity.Entry;
import com.sebastian_daschner.asciiblog.business.statistics.entity.EntryAccess;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.time.Instant;
import java.util.List;

@Stateless
public class EntryStore {

    private static final int NUMBER_TEASERS = 8;

    @Inject
    EntryCache cache;

    @Inject
    Event<EntryAccess> accessEvents;

    public List<Entry> getTeaserEntries() {
        return cache.getLastEntries(NUMBER_TEASERS);
    }

    public List<Entry> getAllEntries() {
        return cache.getAllEntries();
    }

    public Entry getEntry(final String name) {
        final Entry entry = cache.get(name);

        if (entry != null)
            accessEvents.fire(new EntryAccess(name, Instant.now()));

        return entry;
    }

}
