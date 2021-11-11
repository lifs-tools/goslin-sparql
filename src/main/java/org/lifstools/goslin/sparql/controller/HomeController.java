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
package org.lifstools.goslin.sparql.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.lifstools.goslin.sparql.config.NewsPropertyConfig;
import org.lifstools.goslin.sparql.config.SparqlQueriesPropertyConfig;
import org.lifstools.goslin.sparql.services.PageBuilderService;
import org.lifstools.jgoslin.domain.LipidClasses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author nilshoffmann
 */
@Controller
public class HomeController {

    private final PageBuilderService pageBuilderService;
    private final LocaleResolver localeResolver;
    private final NewsPropertyConfig newsPropertyConfig;
    private final SparqlQueriesPropertyConfig sparqlQueryPropertiesConfig;

    @Autowired
    public HomeController(PageBuilderService pageBuilderService, LocaleResolver localeResolver, NewsPropertyConfig newsPropertyConfig, SparqlQueriesPropertyConfig sparqlQueryPropertiesConfig) {
        this.pageBuilderService = pageBuilderService;
        this.localeResolver = localeResolver;
        this.newsPropertyConfig = newsPropertyConfig;
        this.sparqlQueryPropertiesConfig = sparqlQueryPropertiesConfig;
    }

    @GetMapping("/")
    public ModelAndView index(@RequestParam(value = "lang", required = false) String language, HttpServletRequest request, HttpServletResponse response, Principal principal) throws IOException {
        if (language != null) {
            localeResolver.setLocale(request, response, Locale.forLanguageTag(language));
        }
        ModelAndView model = new ModelAndView("index");
        model.addObject("page", pageBuilderService.createPage("Goslin Sparql Endpoint"));
        return model;
    }
    
    @GetMapping("/documentation")
    public ModelAndView handleInfo(@RequestParam(value = "lang", required = false) String language, HttpServletRequest request, HttpServletResponse response, Principal principal) throws IOException {
        if (language != null) {
            localeResolver.setLocale(request, response, Locale.forLanguageTag(language));
        }
        ModelAndView model = new ModelAndView("documentation");
        model.addObject("page", pageBuilderService.createPage("Goslin SPARQL Webapp Information"));
        model.addObject("news", newsPropertyConfig.getNews());
        model.addObject("lipidClasses", LipidClasses.getInstance().stream().sorted((t, t1) -> {
            int cmp = t.lipidCategory.name().compareTo(t1.lipidCategory.name());
            if (cmp != 0) {
                return cmp;
            }
            return t.className.compareTo(t1.className);
        }).collect(Collectors.toList()));
        model.addObject("sparqlQueries", sparqlQueryPropertiesConfig);
        return model;
    }
}
