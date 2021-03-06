/*
 * Copyright 2021 Leibniz Institut für Analytische Wissenschaften - ISAS e.V..
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

import java.security.Principal;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.lifstools.goslin.sparql.services.PageBuilderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author nilshoffmann
 */
//@Controller
public class LifsErrorController implements ErrorController {

    private final static Logger log = LoggerFactory.getLogger(LifsErrorController.class);
    private final PageBuilderService pageBuilderService;

    @Autowired
    public LifsErrorController(PageBuilderService pageBuilderService) {
        this.pageBuilderService = pageBuilderService;
    }

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request, Principal principal) {
        log.debug("Handling an error!");
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String view = "error";
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                view = "errors/404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                view = "errors/500";
            }
        }
        ModelAndView mav = new ModelAndView(view);
        mav.addObject("status", status);
        mav.addObject("error", request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
        mav.addObject("page", pageBuilderService.createPage(view));
        return mav;
    }
    
//    @ExceptionHandler(MaxUploadSizeExceededException.class)
//    public ModelAndView handleMaxUploadSizeExceeded(HttpServletRequest req,
//            HttpServletResponse resp, Exception exception, Principal principal) throws Exception {
//        log.error("Caught exception: ", exception);
//        ModelAndView mav = new ModelAndView();
//        DefaultPage page = pageBuilderService.createPage("error");
//        mav.addObject("page", page);
//        mav.addObject("title", "Uploaded file is too large!");
//        mav.addObject("error", "The file you tried to upload was larger than the current limit: " + page.getMaxFileSize());
//        mav.addObject("url", req.getRequestURL());
//        mav.addObject("timestamp", new Date().toString());
//        mav.addObject("status", 500);
//        mav.setViewName("error");
//        return mav;
//    }

//    @Override
//    public String getErrorPath() {
//        return null;
//    }

}
