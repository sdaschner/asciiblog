package com.sebastian_daschner.asciiblog.source.boundary;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.context.ManagedExecutor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;

@Path("updates")
@ApplicationScoped
public class UpdateResource {

    @Inject
    SourceInvoker sourceInvoker;

    @Inject
    ManagedExecutor managedExecutor;

    @ConfigProperty(name = "asciiblog.updates.secret")
    String updateSecret;

    @POST
    public Response triggerUpdate(@HeaderParam("secret") String secret) {
        if (!updateSecret.equals(secret))
            return Response.status(FORBIDDEN).build();

        managedExecutor.submit(() -> sourceInvoker.checkNewEntries());
        return Response.accepted().build();
    }

}
