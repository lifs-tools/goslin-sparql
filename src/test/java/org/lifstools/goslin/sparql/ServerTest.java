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
package org.lifstools.goslin.sparql;

import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThat;
import org.eclipse.rdf4j.http.client.SPARQLProtocolSession;
import org.eclipse.rdf4j.http.protocol.UnauthorizedException;
import org.eclipse.rdf4j.query.MalformedQueryException;
import org.eclipse.rdf4j.query.QueryInterruptedException;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.parser.ParsedQuery;
import org.eclipse.rdf4j.query.parser.sparql.SPARQLParser;
import org.eclipse.rdf4j.repository.RepositoryException;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lifstools.goslin.sparql.controller.ReadonlyQueryResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author nilshoffmann
 */
@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = {"test"})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ServerTest {
    
    private static final Logger log = LoggerFactory.getLogger(ServerTest.class);

    @LocalServerPort
    private int port;

    @Autowired
    private ReadonlyQueryResponder queryResponder;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
        assertThat(queryResponder).isNotNull();
    }

//    @Test
//    public void testAskQuery() {
//        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/sparql/?query={query}",
//                String.class, "ASK { ?s ?p ?o }")).contains("true");
//
//    }
    @Test
    public void testSelectQuery() {
        String forObject = this.restTemplate.getForObject("http://localhost:" + port + "/goslin-sparql/sparql/?query={query}",
                String.class, """
                              PREFIX goslin: <https://identifiers.org/lipids/goslin/> 
                              PREFIX grammar: <https://identifiers.org/lipids/goslin/grammar/>
                              SELECT ?string
                              WHERE { [] grammar:swisslipids 'Cer(d18:1/20:2)' ;
                                         goslin:className ?string . }""");
        assertThat(forObject).contains("\"value\" : \"Cer\"");
    }

    @Test
    public void testMalformedQuery() {
        ResponseEntity<String> result = this.restTemplate.getForEntity("http://localhost:" + port + "/goslin-sparql/sparql/", String.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testSelectQueryOptimization() {
        String forObject = this.restTemplate.getForObject("http://localhost:" + port + "/goslin-sparql/sparql/?query={query}",
                String.class, """
                              PREFIX goslin: <https://identifiers.org/lipids/goslin/> 
                              PREFIX grammar: <https://identifiers.org/lipids/goslin/grammar/>
                              SELECT ?string
                              WHERE { [] goslin:className ?string ;
                                         grammar:swisslipids 'Cer(d18:1/20:2)' . }""");
        assertThat(forObject).contains("\"value\" : \"Cer\"");
    }

    @Test
    public void testSelectQueryAnyGrammar() {
        String forObject = this.restTemplate.getForObject("http://localhost:" + port + "/goslin-sparql/sparql/?query={query}",
                String.class, """
                              PREFIX goslin: <https://identifiers.org/lipids/goslin/> 
                              PREFIX grammar: <https://identifiers.org/lipids/goslin/grammar/>
                              SELECT ?string
                              WHERE { [] grammar:any 'Cer(d18:1/20:2)' ;
                                         goslin:className ?string . }""");
        assertThat(forObject).contains("\"value\" : \"Cer\"");
    }

    @Test
    public void testSelectQueryMw() {
        String forObject = this.restTemplate.getForObject("http://localhost:" + port + "/goslin-sparql/sparql/?query={query}",
                String.class, """
                              PREFIX goslin: <https://identifiers.org/lipids/goslin/> 
                              PREFIX grammar: <https://identifiers.org/lipids/goslin/grammar/>
                              SELECT ?double
                              WHERE { [] grammar:any 'Cer(d18:1/20:2)' ;
                                         goslin:exactMass ?double . }""");
        assertThat(forObject).contains("\"value\" : \"589.54339537"); // this is weird, sometimes, values seem to be rounded?
    }

    @Test
    public void testSelectQuerySumFormula() {
        String forObject = this.restTemplate.getForObject("http://localhost:" + port + "/goslin-sparql/sparql/?query={query}",
                String.class, """
                              PREFIX goslin: <https://identifiers.org/lipids/goslin/> 
                              PREFIX grammar: <https://identifiers.org/lipids/goslin/grammar/>
                              SELECT ?string
                              WHERE { [] grammar:any 'Cer(d18:1/20:2)' ;
                                         goslin:sumFormula ?string . }""");
        assertThat(forObject).contains("\"value\" : \"C38H71NO3\"");
    }

    @Test
    public void testGoslin() throws UnauthorizedException, QueryInterruptedException, RepositoryException,
            MalformedQueryException, IOException {
        TestSPARQLRepository rep = new TestSPARQLRepository("http://localhost:" + port + "/goslin-sparql/sparql/");
        rep.init();
        String query = "PREFIX goslin: <https://identifiers.org/lipids/goslin/>\n"
                + "PREFIX grammar: <https://identifiers.org/lipids/goslin/grammar>\n"
                + "SELECT * WHERE {    [] grammar:swisslipids \"NAE (18:1(9Z))\" ;\n"
                + "       goslin:className ?className \n" + "}";
        ParsedQuery parseQuery = new SPARQLParser().parseQuery(query, null);
        SPARQLProtocolSession session = rep.createSPARQLProtocolSession();
        try {
            TupleQueryResult sendTupleQuery = session.sendTupleQuery(QueryLanguage.SPARQL, query, null, false);
            while (sendTupleQuery.hasNext()) {
                assertNotNull(sendTupleQuery.next());
            }
        } catch (Exception re) {
            log.error("Caught exception: ", re);
            throw re;
        } finally {
            session.close();
            rep.shutDown();
        }
    }

}
