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
import java.util.List;
import java.util.Map;

@Path("tags")
@Produces(MediaType.TEXT_HTML)
@ApplicationScoped
public class TagsController {

    @Inject
    EntryStore entryStore;

    @ResourcePath("tags.html")
    Template tagsTemplate;

    @ResourcePath("tag-entries.html")
    Template tagEntriesTemplate;

    @GET
    public TemplateInstance getAllTags() {
        Map<String, Integer> tags = entryStore.getAllTags();
        return tagsTemplate.data("tags", tags);
    }

    @GET
    @Path("{tag}")
    public TemplateInstance getTag(@PathParam("tag") String tag) {
        List<Entry> entries = entryStore.getAllEntries(tag);
        return tagEntriesTemplate.data("tag", tag)
                .data("entries", entries);
    }

}
