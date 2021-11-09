package org.lifstools.goslin.sparql;

import org.eclipse.rdf4j.http.server.readonly.QueryResponder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Configuration
@Import(QueryResponder.class)
@ComponentScan(basePackages = {"org.eclipse.rdf4j", "org.lifstools.goslin.sparql"})
public class Server {
    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }
}
