package cn.gonnaup.tutorial.datafactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author gonnaup
 * @version created at 2022/10/13 20:23
 */
@SpringBootApplication
@EntityScan(basePackages = "cn.gonnaup.tutorial.common.entity.datafactory")
@EnableDiscoveryClient
public class DatafactoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(DatafactoryApplication.class, args);
    }
}
