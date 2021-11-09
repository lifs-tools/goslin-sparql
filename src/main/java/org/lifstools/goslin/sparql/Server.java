package org.lifstools.goslin.sparql;

import org.eclipse.rdf4j.http.server.readonly.QueryResponder;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Configuration
@ComponentScan(basePackages = {"org.eclipse.rdf4j", "org.lifstools.goslin.sparql"})
@Import(QueryResponder.class)
public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    @Bean(destroyMethod = "shutDown")
    public Repository getRepository() {
        logger.info("Starting goslin sparql translator");

        SailRepository sailRepository = new SailRepository(new GoslinSparqlStore());
        logger.info("Initializing repository");
        sailRepository.init();
        return sailRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }
}
