package com.sebastian_daschner.asciiblog.business.entries.boundary;

import com.sebastian_daschner.asciiblog.business.entries.control.HtmlGenerator;
import com.sebastian_daschner.asciiblog.business.entries.entity.Entry;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Produces(MediaType.TEXT_HTML)
public class EntriesResource {

    @Inject
    EntriesStore entriesStore;

    @Inject
    HtmlGenerator htmlGenerator;

    @GET
    public String getAllEntries() {
        final List<Entry> entries = entriesStore.getAllEntries();
        return htmlGenerator.generateEntries(entries);
    }

    @GET
    @Path("{entry}")
    public String getEntry(@PathParam("entry") final String entryName) {
        final Entry entry = entriesStore.getEntry(entryName);
        return htmlGenerator.generateEntry(entry);
    }

}
