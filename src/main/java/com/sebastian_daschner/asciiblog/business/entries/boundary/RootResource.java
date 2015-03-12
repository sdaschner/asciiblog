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

import com.sebastian_daschner.asciiblog.business.entries.control.HtmlGenerator;
import com.sebastian_daschner.asciiblog.business.entries.entity.Entry;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Stateless
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class RootResource {

    @Context
    ResourceContext rc;

    @Inject
    EntriesStore entriesStore;

    @Inject
    HtmlGenerator htmlGenerator;

    @GET
    public String index() {
        final List<Entry> teaserEntries = entriesStore.getTeaserEntries();
        return htmlGenerator.generateIndex(teaserEntries);
    }

    @Path("entries")
    public EntriesResource entries() {
        return rc.getResource(EntriesResource.class);
    }

}
