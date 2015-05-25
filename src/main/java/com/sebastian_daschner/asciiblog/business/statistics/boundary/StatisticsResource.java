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

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Comparator;
import java.util.Map;

@Path("statistics")
@Produces(MediaType.TEXT_PLAIN)
public class StatisticsResource {

    @Inject
    Statistics statistics;

    @GET
    public String getStatistics() {
        final StringBuilder builder = new StringBuilder();

        statistics.getDailyAccesses().entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .forEach(e -> {
                    builder.append(e.getKey()).append(":\n");
                    e.getValue().entrySet().stream()
                            .sorted(Comparator.comparing(Map.Entry::getKey))
                            .forEach(a -> builder.append(a.getKey()).append(": ").append(a.getValue()).append('\n'));
                    builder.append('\n');
                });

        return builder.toString();
    }

}
