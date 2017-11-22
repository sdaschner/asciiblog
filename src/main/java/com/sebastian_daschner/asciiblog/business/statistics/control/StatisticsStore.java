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

import io.prometheus.client.Counter;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class StatisticsStore {

    private Counter entryAccesses;

    @PostConstruct
    private void initCounter() {
        entryAccesses = Counter.build("entry_requests_total", "Total number of entry requests")
                .labelNames("path")
                .register();
    }

    public void onNewEntryAccess(@Observes final String access) {
        entryAccesses.labels(access).inc();
    }

}
