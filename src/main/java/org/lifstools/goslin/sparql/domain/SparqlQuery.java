/*
 * Copyright 2021 Lipidomics Informatics for Life Sciences.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lifstools.goslin.sparql.domain;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author nilshoffmann
 */
public class SparqlQuery {

    @Value("${description}")
    private String description;
    @Value("${query}")
    private String query;

    public SparqlQuery(String description, String query) {
        this.description = description;
        this.query = query;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.description);
        hash = 37 * hash + Objects.hashCode(this.query);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SparqlQuery other = (SparqlQuery) obj;
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.query, other.query)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SparqlQuery{" + "description=" + description + ", query=" + query + '}';
    }

}
