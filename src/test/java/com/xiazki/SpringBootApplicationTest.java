package com.xiazki;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by xiang.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.xiazki")
@Slf4j
public class SpringBootApplicationTest {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(SpringBootApplicationTest.class);
        log.info("ApplicationContext init finish.");
    }

    @Test
    public void test() {

    }
}
