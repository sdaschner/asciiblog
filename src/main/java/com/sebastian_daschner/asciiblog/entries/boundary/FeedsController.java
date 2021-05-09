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
import java.time.LocalDateTime;
import java.util.List;

@Path("feeds")
@ApplicationScoped
public class FeedsController {

    @Inject
    EntryStore entryStore;

    @ResourcePath("rss.xml")
    Template rssTemplate;

    @GET
    @Path("rss")
    @Produces(MediaType.APPLICATION_XML)
    public TemplateInstance rss() {
        final List<Entry> teaserEntries = entryStore.getTeaserEntries();
        final LocalDateTime latestDate = teaserEntries.isEmpty() ? LocalDateTime.now() : teaserEntries.get(0).getDate();

        return rssTemplate.data("entries", teaserEntries)
                .data("latestDate", latestDate);
    }

}
