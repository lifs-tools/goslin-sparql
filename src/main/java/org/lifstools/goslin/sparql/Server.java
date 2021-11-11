package org.lifstools.goslin.sparql;

import org.lifstools.goslin.sparql.config.NewsPropertyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@EnableConfigurationProperties(value = {NewsPropertyConfig.class})
@ComponentScan(basePackages = {"de.isas.lifs.webapps", "org.lifstools.goslin.sparql"})
public class Server {
    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }
}
