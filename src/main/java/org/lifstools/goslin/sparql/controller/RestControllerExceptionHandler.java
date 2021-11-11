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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nilshoffmann
 */
// should handle all exception for classes annotated with         
@ControllerAdvice(annotations = RestController.class)
@Order(1) // NOTE: order 1 here
public class RestControllerExceptionHandler {

    private final static Logger log = LoggerFactory.getLogger(RestControllerExceptionHandler.class);
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingRequestParameterException(MissingServletRequestParameterException e) {
        log.error("Caught exception for RestController:", e);
        return new ResponseEntity<>("Request failed! " + e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnexpectedException(Exception e) {
        log.error("Caught exception for RestController:", e);
        return new ResponseEntity<>("Request failed! " + e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
