package com.cpt.payments.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class AdditionController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdditionController.class);
	
	@Value("${mytestkey}")
	private String keyData;

    @GetMapping("/add")
    public int add(@RequestParam int num1, @RequestParam int num2) {
        System.out.println("num1:" + num1 + "|num2:" + num2);
        
        logger.info("num1:" + num1 + "|num2:" + num2);
        log.info("num1:" + num1 + "|num2:" + num2);
        
        System.out.println("keyData: "+keyData);
    	
        int sumResult = num1 + num2;
        System.out.println("-----------------sumResult:" + sumResult);
        
        logger.trace("This is my TRACE LEVEL logging");
        logger.debug("This is my debug LEVEL logging");
        logger.info("This is my info LEVEL logging");
        logger.warn("This is my warn LEVEL logging");
        logger.error("This is my error LEVEL logging");

        return sumResult;
    }
}
