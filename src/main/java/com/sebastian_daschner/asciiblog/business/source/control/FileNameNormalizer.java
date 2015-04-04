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

package com.sebastian_daschner.asciiblog.business.source.control;

public class FileNameNormalizer {

    private static final String ASCIIDOC_SUFFIX = ".adoc";

    /**
     * Checks if the given file name should be considered for futher processing.
     *
     * @param fileName The file name to check
     * @return {@code true} if relevant for further processing
     */
    public boolean isRelevant(final String fileName) {
        return fileName.endsWith(ASCIIDOC_SUFFIX);
    }

    /**
     * Removes the file suffix from the file names
     *
     * @throws IllegalArgumentException If the file name has not the correct AsciiDoc suffix
     */
    public String normalize(final String fileName) {
        if (!fileName.endsWith(ASCIIDOC_SUFFIX))
            throw new IllegalArgumentException("File name '" + fileName + "' does not end with " + ASCIIDOC_SUFFIX);

        return fileName.substring(0, fileName.length() - ASCIIDOC_SUFFIX.length());
    }

}
