/*
 * Copyright 2020 Leibniz Institut für Analytische Wissenschaften - ISAS e.V..
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
package org.lifstools.goslin.sparql.config;

import java.util.ArrayList;
import java.util.List;
import org.lifstools.goslin.sparql.domain.SparqlQuery;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 *
 * @author nilshoffmann
 */
@Configuration
@PropertySource(name = "sparqlqueries", value = "classpath:sparqlqueries.yml", ignoreResourceNotFound = false)
@ConfigurationProperties(prefix = "sparqlqueries")
public class SparqlQueriesPropertyConfig {

    private List<SparqlQuery> sparqlQueries = new ArrayList<>();

    /**
     * Returns the queries in reverse order.
     *
     * @return the queries.
     */
    public List<SparqlQuery> getSparqlQueries() {
//        int num = sparqlQueries.size() - 1;
//        return IntStream.rangeClosed(0, num).mapToObj(i -> sparqlQueries.get(num - i)).collect(Collectors.toList());
        return sparqlQueries;
    }

    public void setSparqlQueries(List<SparqlQuery> sparqlQueries) {
        this.sparqlQueries = sparqlQueries;
    }

}
