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

package com.sebastian_daschner.asciiblog.business.statistics.control;

import com.sebastian_daschner.asciiblog.business.statistics.entity.EntryAccess;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class StatisticsCacheTest {

    private StatisticsCache cut;

    @Before
    public void setUp() {
        cut = new StatisticsCache();
    }

    @Test
    public void testCrud() {
        cut.onNewEntryAccess(new EntryAccess("test", Instant.parse("2015-01-03T10:15:30.00Z")));
        cut.onNewEntryAccess(new EntryAccess("test", Instant.parse("2015-01-04T10:16:30.00Z")));
        cut.onNewEntryAccess(new EntryAccess("foobar", Instant.parse("2015-01-03T10:16:10.00Z")));
        cut.onNewEntryAccess(new EntryAccess("foobar", Instant.parse("2015-01-04T10:16:30.00Z")));

        final Map<String, Set<Instant>> expected = new HashMap<>();
        final Set<Instant> testInstants = new HashSet<>(Arrays.asList(Instant.parse("2015-01-03T10:15:30.00Z"), Instant.parse("2015-01-04T10:16:30.00Z")));
        expected.put("test", testInstants);

        final Set<Instant> foobarInstants = new HashSet<>(Arrays.asList(Instant.parse("2015-01-03T10:16:10.00Z"), Instant.parse("2015-01-04T10:16:30.00Z")));
        expected.put("foobar", foobarInstants);

        final Map<String, Set<Instant>> accesses = cut.getAll();

        assertEquals(expected, accesses);
    }

}