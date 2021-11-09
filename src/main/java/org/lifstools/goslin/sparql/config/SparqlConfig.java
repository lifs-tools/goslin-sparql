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
package org.lifstools.goslin.sparql.config;

import org.eclipse.rdf4j.http.server.readonly.QueryResponder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.lifstools.goslin.sparql.GoslinSparqlStore;
import org.lifstools.goslin.sparql.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 *
 * @author nilshoffmann
 */
@Configuration
public class SparqlConfig {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    @Bean(destroyMethod = "shutDown")
    public Repository repository() {
        logger.info("Starting goslin sparql translator");
        SailRepository sailRepository = new SailRepository(new GoslinSparqlStore());
        logger.info("Initializing repository");
        sailRepository.init();
        sailRepository.getValueFactory().createStatement(RDF.ALT, RDF.BAG, RDF.FIRST);
        return sailRepository;
    }

//    @Bean
//    public QueryResponder queryResponder() {
//        return new QueryResponder(repository());
//    }
}
