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

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Stateless
public class Statistics {

    @Inject
    StatisticsCache cache;

    public Map<String, Map<LocalDate, Integer>> getDailyAccesses() {
        return cache.getAll()
                .entrySet().stream()
                .collect(HashMap::new, (m, e) -> m.put(e.getKey(), calculateDailyAccesses(e.getValue())), Map::putAll);
    }

    private Map<LocalDate, Integer> calculateDailyAccesses(final Set<Instant> timestamps) {
        return timestamps.stream()
                .map(t -> t.atZone(ZoneId.systemDefault()).toLocalDate())
                .collect(HashMap::new, (m, d) -> m.merge(d, 1, (oldV, newV) -> oldV + newV), Map::putAll);
    }

}
