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

package com.sebastian_daschner.asciiblog.business.views.control;

import com.sebastian_daschner.asciiblog.business.views.entity.View;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * JAX-RS message body writer for {@link View}s. Forwards the request to the corresponding JSP view.
 *
 * @author Sebastian Daschner
 */
@Provider
@Produces(MediaType.TEXT_HTML)
public class ViewWriterExtension implements MessageBodyWriter<View> {

    @Context
    HttpServletRequest request;

    @Context
    HttpServletResponse response;

    @Override
    public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        return View.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(final View view, final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(final View view, final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType,
                        final MultivaluedMap<String, Object> httpHeaders, final OutputStream entityStream) throws IOException, WebApplicationException {
        view.getModel().entrySet().forEach(e -> request.setAttribute(e.getKey(), e.getValue()));
        try {
            request.getRequestDispatcher(view.getPath()).forward(request, response);
        } catch (ServletException e) {
            throw new WebApplicationException(e);
        }
    }

}
