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

import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import java.time.LocalDate;
import java.util.List;

@Controller
@Path("/")
public class RootController {

    @Context
    ResourceContext rc;

    @Inject
    EntryStore entryStore;

    @Inject
    Models models;

    @GET
    public String index() {
        final List<Entry> teaserEntries = entryStore.getTeaserEntries();

        models.put("entries", teaserEntries);
        return "index.jsp";
    }

    @Path("feeds")
    public FeedsController feeds() {
        return rc.getResource(FeedsController.class);
    }

    @Path("entries")
    public EntriesController entries() {
        return rc.getResource(EntriesController.class);
    }

}
