package com.sebastian_daschner.asciiblog.entries.boundary;

import com.sebastian_daschner.asciiblog.entries.entity.Entry;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.api.ResourcePath;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/")
@ApplicationScoped
@Produces(MediaType.TEXT_HTML)
public class RootController {

    @Inject
    EntryStore entryStore;

    @ResourcePath("index.html")
    Template index;

    @GET
    public TemplateInstance index() {
        List<Entry> teaserEntries = entryStore.getTeaserEntries();
        return index.data("entries", teaserEntries);
    }

}
