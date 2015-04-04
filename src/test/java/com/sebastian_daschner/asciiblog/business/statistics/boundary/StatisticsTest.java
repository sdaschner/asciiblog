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

package com.sebastian_daschner.asciiblog.business.statistics.boundary;

import com.sebastian_daschner.asciiblog.business.statistics.control.StatisticsCache;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class StatisticsTest {

    private Statistics cut;

    @Before
    public void setUp() {
        cut = new Statistics();
        cut.cache = Mockito.mock(StatisticsCache.class);
    }

    @Test
    public void testGetDailyAccesses() {
        Mockito.when(cut.cache.getAll()).thenReturn(testCacheData());

        final Map<String, Map<LocalDate, Integer>> expected = new HashMap<>();
        final Map<LocalDate, Integer> firstData = new HashMap<>();
        firstData.put(LocalDate.of(1970, 1, 1), 4);

        expected.put("first", firstData);
        final Map<String, Map<LocalDate, Integer>> actual = cut.getDailyAccesses();

        assertEquals(expected, actual);
    }

    private Map<String, Set<Instant>> testCacheData() {
        final Map<String, Set<Instant>> data = new HashMap<>();

        final Instant firstTimestamp = Instant.ofEpochSecond(1l);
        final Instant secondTimestamp = Instant.ofEpochSecond(2l);
        final Instant thirdTimestamp = Instant.ofEpochSecond(3l);
        final Instant fourthTimestamp = Instant.ofEpochSecond(4l);

        data.put("first", new HashSet<>(Arrays.asList(firstTimestamp, secondTimestamp, thirdTimestamp, fourthTimestamp)));
        return data;
    }

}