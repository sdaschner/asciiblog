package com.sebastian_daschner.asciiblog.statistics.control;

import io.quarkus.runtime.Startup;
import org.eclipse.microprofile.metrics.Metadata;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.Tag;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
@Startup
public class StatisticsListener {

    @Inject
    MetricRegistry registry;

    private static final Metadata COUNTER = Metadata.builder()
            .withName("entry_requests_total")
            .withDisplayName("Total number of entry requests")
            .build();

    public void onNewEntryAccess(@Observes final String access) {
        registry.counter(COUNTER, new Tag("path", access)).inc();
    }

}
