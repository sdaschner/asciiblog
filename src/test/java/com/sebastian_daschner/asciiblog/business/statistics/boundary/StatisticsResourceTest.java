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

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StatisticsResourceTest {

    private StatisticsResource cut;
    private Statistics statistics;

    @Before
    public void setUp() throws Exception {
        cut = new StatisticsResource();
        statistics = mock(Statistics.class);
        cut.statistics = statistics;
    }

    @Test
    public void testGetEmptyStatistics() {
        when(statistics.getDailyAccesses()).thenReturn(emptyMap());

        assertThat(cut.getStatistics().length(), is(0));
    }

    @Test
    public void testGetStatistics() {
        when(statistics.getDailyAccesses()).thenReturn(createTestData());

        assertThat(cut.getStatistics(), is("firstEntry:\n" +
                "2015-01-01: 3\n" +
                "2015-01-04: 2\n" +
                "2015-03-01: 1\n" +
                "\n" +
                "secondEntry:\n" +
                "2015-01-02: 2\n" +
                "2015-01-04: 1\n" +
                "2015-04-01: 4\n" +
                "\n" +
                "thirdEntry:\n" +
                "2015-01-03: 2\n" +
                "2015-03-01: 1\n" +
                "2015-04-01: 1\n" +
                "\n"));
    }

    private Map<String, Map<LocalDate, Integer>> createTestData() {
        final Map<String, Map<LocalDate, Integer>> data = new HashMap<>();

        final Map<LocalDate, Integer> firstData = new HashMap<>();
        firstData.put(LocalDate.of(2015, 1, 1), 3);
        firstData.put(LocalDate.of(2015, 3, 1), 1);
        firstData.put(LocalDate.of(2015, 1, 4), 2);
        data.put("firstEntry", firstData);

        final Map<LocalDate, Integer> secondData = new HashMap<>();
        secondData.put(LocalDate.of(2015, 1, 2), 2);
        secondData.put(LocalDate.of(2015, 4, 1), 4);
        secondData.put(LocalDate.of(2015, 1, 4), 1);
        data.put("secondEntry", secondData);

        final Map<LocalDate, Integer> thirdData = new HashMap<>();
        thirdData.put(LocalDate.of(2015, 4, 1), 1);
        thirdData.put(LocalDate.of(2015, 3, 1), 1);
        thirdData.put(LocalDate.of(2015, 1, 3), 2);
        data.put("thirdEntry", thirdData);

        return data;
    }

}