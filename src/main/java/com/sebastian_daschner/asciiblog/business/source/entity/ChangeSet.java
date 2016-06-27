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

package com.sebastian_daschner.asciiblog.business.source.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Contains the changes of the repository since the last check.
 *
 * @author Sebastian Daschner
 */
public class ChangeSet {

    /**
     * The changed (incl. new) file names with their content.
     */
    private final Map<String, String> changedFiles = new HashMap<>();

    /**
     * The removed file names.
     */
    private final Set<String> removedFiles = new HashSet<>();

    public Map<String, String> getChangedFiles() {
        return changedFiles;
    }

    public Set<String> getRemovedFiles() {
        return removedFiles;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ChangeSet changeSet = (ChangeSet) o;

        if (!changedFiles.equals(changeSet.changedFiles)) return false;
        return removedFiles.equals(changeSet.removedFiles);
    }

    @Override
    public int hashCode() {
        int result = changedFiles.hashCode();
        result = 31 * result + removedFiles.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ChangeSet{" +
                "changedFiles=" + changedFiles +
                ", removedFiles=" + removedFiles +
                '}';
    }

}
