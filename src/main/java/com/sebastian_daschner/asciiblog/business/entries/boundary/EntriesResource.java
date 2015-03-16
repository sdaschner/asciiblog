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

import com.sebastian_daschner.asciiblog.business.entries.entity.Entry;
import com.sebastian_daschner.asciiblog.business.views.entity.View;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Produces(MediaType.TEXT_HTML)
public class EntriesResource {

    @Inject
    EntriesStore entriesStore;

    @GET
    public View getAllEntries() {
        final List<Entry> entries = entriesStore.getAllEntries();
        final Map<String, Object> model = new HashMap<>();
        model.put("entries", entries);

        return new View("entries", model);
    }

    @GET
    @Path("{entry}")
    public View getEntry(@PathParam("entry") final String entryName) {
        final Entry entry = entriesStore.getEntry(entryName);

        if (entry == null)
            throw new NotFoundException();

        final Map<String, Object> model = new HashMap<>();
        model.put("entry", entry);

        return new View("entry", model);
    }

}
