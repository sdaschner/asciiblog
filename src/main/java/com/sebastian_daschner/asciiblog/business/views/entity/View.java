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

package com.sebastian_daschner.asciiblog.business.views.entity;

import java.util.Map;

/**
 * A JSP view which will be chosen by the name. Views are located under {@code WEB-INF/views/<name>.jsp}.
 * A map which contains model values will be added to the JSP request context automatically.
 *
 * @author Sebastian Daschner
 */
public class View {

    private static final String VIEW_PREFIX = "/WEB-INF/views/";
    private static final String VIEW_SUFFIX = ".jsp";

    private final String name;
    private final Map<String, Object> model;

    public View(final String name, final Map<String, Object> model) {
        this.name = name;
        this.model = model;
    }

    public String getPath() {
        return VIEW_PREFIX + name + VIEW_SUFFIX;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getModel() {
        return model;
    }

}
