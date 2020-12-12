package com.personal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
/**
 * jar
 * @author
 * @date 2020/11/24 15:46
 **/
@SpringBootApplication
public class SpingBootApplication extends SpringBootServletInitializer {
    private static final Logger logger = LoggerFactory.getLogger(SpringApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpingBootApplication.class, args);
        logger.info("--------------- Project start completed -----------");
    }
//    protected SpringApplicationBuilder config(SpringApplicationBuilder applicationBuilder){
//        return applicationBuilder.sources(SpingBootApplication.class);
//    }

}
