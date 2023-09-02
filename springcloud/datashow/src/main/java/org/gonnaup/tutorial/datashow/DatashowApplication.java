package org.gonnaup.tutorial.datashow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author gonnaup
 * @version created at 2022/10/13 20:24
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class DatashowApplication {
    public static void main(String[] args) {
        SpringApplication.run(DatashowApplication.class, args);
    }
}
