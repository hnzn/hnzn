package com.jq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Created by hnznw on 2017/12/11.
 */
@SpringBootApplication(scanBasePackages = {"com.jq"})
public class WApplicaion extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(WApplicaion.class,args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(WApplicaion.class);
    }
}
