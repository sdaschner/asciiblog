package com.sebastian_daschner.asciiblog.business.environment.control;

import javax.enterprise.inject.Produces;
import java.io.File;
import java.nio.file.Paths;

public class EnvironmentDataProvider {

    @Produces
    public Environment getEnvironment() {
        System.out.println("producing environment");
        return Environment.valueOf(System.getProperty("blog.environment", "INTEGRATION"));
    }

    @Produces
    public File getGitLocation() {
        System.out.println("producing git location");
        return Paths.get(System.getProperty("blog.git.location")).toFile();
    }

}
