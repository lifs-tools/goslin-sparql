/*
 * Copyright 2018 Leibniz Institut für Analytische Wissenschaften - ISAS e.V..
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

import de.isas.lifs.webapps.common.config.LifsConfig;
import de.isas.lifs.webapps.common.config.ToolConfig;
import de.isas.lifs.webapps.common.config.TrackingConfig;
import de.isas.lifs.webapps.common.domain.DefaultAppInfo;
import java.time.Duration;
import java.util.Locale;
import java.util.concurrent.Executor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;

/**
 *
 * @author Nils Hoffmann &lt;nils.hoffmann@isas.de&gt;
 */
//@Import(QueryResponder.class)
@Configuration
@EnableAsync
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LifsConfig lifsConfiguration;

    @Autowired
    private ToolConfig toolConfiguration;

    @Autowired
    private TrackingConfig trackingConfiguration;

    @Value("keycloak.auth-server-url")
    private String authServerUrl;

    @Value("keycloak.realm")
    private String authServerRealm;

//    @Value("${app.buildDate}")
//    private String buildDate;
//
//    @Value("${app.scm.commit.id}")
//    private String scmCommitId;
//
//    @Value("${app.scm.branch}")
//    private String scmBranch;
//
//    @Value("${app.versionNumber}")
//    private String versionNumber;
//
//    @Value("${ga.id}")
//    private String gaId;
//
//    @Value("${lifs.user.url}")
//    private String lifsUserUrl;
//
//    @Value("${jgoslin.version.number}")
//    private String jgoslinVersionNumber;
    @Autowired
    private Environment environment;

    public WebConfig() {
        super();
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**").
                addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.messages")
    public MessageSourceProperties messageSourceProperties() {
        return new MessageSourceProperties();
    }

    @Bean
    public MessageSource messageSource(MessageSourceProperties properties) {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        if (StringUtils.hasText(properties.getBasename())) {
            messageSource.setBasenames(StringUtils
                    .commaDelimitedListToStringArray(StringUtils.trimAllWhitespace(properties.getBasename())));
        }
        if (properties.getEncoding() != null) {
            messageSource.setDefaultEncoding(properties.getEncoding().name());
        }
        messageSource.setFallbackToSystemLocale(properties.isFallbackToSystemLocale());
        Duration cacheDuration = properties.getCacheDuration();
        if (cacheDuration != null) {
            messageSource.setCacheMillis(cacheDuration.toMillis());
        }
        messageSource.setAlwaysUseMessageFormat(properties.isAlwaysUseMessageFormat());
        messageSource.setUseCodeAsDefaultMessage(properties.isUseCodeAsDefaultMessage());
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver clr = new SessionLocaleResolver();
        clr.setDefaultLocale(Locale.US);
        return clr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Bean
    public DefaultAppInfo appInfo() {
        return DefaultAppInfo.builder().
                buildDate(this.lifsConfiguration.getBuildDate()).
                gaId(this.trackingConfiguration.getId()).
                toolVersionNumber(this.toolConfiguration.getVersion()).
                //                lifsUserUrl(this.lifsUserUrl).
                //                maxFileSize(this.maxFileSize).
                scmBranch(this.lifsConfiguration.getScm().getBranch()).
                scmCommitId(this.lifsConfiguration.getScm().getCommitId()).
                authServerBaseUrl(this.authServerUrl).
                authServerRealm(this.authServerRealm).
                versionNumber(this.lifsConfiguration.getVersionNumber()).build();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean(name = "toolThreadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor tpe = new ThreadPoolTaskExecutor();
        tpe.setCorePoolSize(Math.max(1, Runtime.getRuntime().availableProcessors() - 1));
        return tpe;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate rt = new RestTemplate();
        DefaultResponseErrorHandler eh = new DefaultResponseErrorHandler();
        rt.setErrorHandler(eh);
        return rt;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("org.eclipse.rdf4j.http.server.readonly"))
                .build().apiInfo(apiEndPointsInfo());
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Goslin SPARQL REST API")
                .description("SPARQL services for lipid shorthand name parsing, translation and mapping to structural hierarchies.")
                .contact(new springfox.documentation.service.Contact("LIFS", "https://lifs-tools.org", "contact@lifs-tools.org"))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version(appInfo().getVersionNumber())
                .build();
    }
//
//    @Bean
//    public StorageService storageService(@Autowired StorageProperties storageProperties) {
//        return new FileSystemStorageService(storageProperties);
//    }

}
