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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NameNormalizerTest {

    private NameNormalizer cut;

    @Before
    public void setUp() {
        cut = new NameNormalizer();
    }

    @Test
    public void testNormalizeNormal() {
        final String actual = cut.normalize("file.adoc");

        assertEquals("file", actual);
    }

    @Test
    public void testNormalizeComplex() {
        final String actual = cut.normalize("name_with_more_charact3ers.adoc");

        assertEquals("name_with_more_charact3ers", actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormalizeNoSuffix() {
        cut.normalize("nameadoc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormalizeWrongSuffix() {
        cut.normalize("name.asciidoc");
    }

}