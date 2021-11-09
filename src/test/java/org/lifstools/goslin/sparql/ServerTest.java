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

import static org.assertj.core.api.Assertions.assertThat;
import org.eclipse.rdf4j.http.server.readonly.QueryResponder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lifstools.jgoslin.domain.LipidAdduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author nilshoffmann
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ServerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private QueryResponder queryResponder;
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
        String forObject = this.restTemplate.getForObject("http://localhost:" + port + "/sparql/?query={query}",
                String.class, """
                              PREFIX goslin: <https://identifiers.org/lipids/nomenclature/>
                              SELECT ?string
                              WHERE { [] goslin:swisslipids 'Cer(d18:1/20:2)' ;
                                         goslin:lipidClassName ?string . }""");
        System.out.println("Returned object: " + forObject);
        assertThat(forObject).contains("\"value\" : \"Cer\"");
    }

}
