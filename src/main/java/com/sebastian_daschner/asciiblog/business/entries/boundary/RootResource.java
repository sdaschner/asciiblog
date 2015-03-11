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
