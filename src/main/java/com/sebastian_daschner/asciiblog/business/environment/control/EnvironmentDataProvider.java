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

package com.sebastian_daschner.asciiblog.business.environment.control;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.io.File;
import java.net.URI;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class EnvironmentDataProvider {

    @Produces
    @FileConfig(FileConfig.Location.GIT)
    public File getGitLocation() {
        return Paths.get(System.getProperty("blog.git.location")).toFile();
    }

    @Produces
    @FileConfig(FileConfig.Location.MAP_DB)
    public File getMapDBLocation() {
        return Paths.get(System.getProperty("blog.map_db.location")).toFile();
    }

    @Produces
    public URI getHomeUri() {
        return URI.create(System.getProperty("blog.uri.home"));
    }

    @Produces
    public Logger getLogger(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

}
