package com.sebastian_daschner.asciiblog.entries.boundary;

import com.sebastian_daschner.asciiblog.entries.entity.Entry;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.api.ResourcePath;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("entries")
@Produces(MediaType.TEXT_HTML)
@ApplicationScoped
public class EntriesController {

    @Inject
    EntryStore entryStore;

    @ResourcePath("entries.html")
    Template entriesTemplate;

    @ResourcePath("entry.html")
    Template entryTemplate;

    @ResourcePath("404.html")
    Template notFoundTemplate;

    @GET
    public TemplateInstance getAllEntries() {
        List<Entry> entries = entryStore.getAllEntries();
        return entriesTemplate.data("entries", entries);
    }

    @GET
    @Path("{entry}")
    public Response getEntry(@PathParam("entry") final String entryName) {
        Entry entry = entryStore.getEntry(entryName);

        if (entry == null)
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(notFoundTemplate.instance())
                    .build();

        return Response.ok(entryTemplate.data("entry", entry)).build();
    }

}
