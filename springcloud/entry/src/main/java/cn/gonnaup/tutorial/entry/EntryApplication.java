package cn.gonnaup.tutorial.entry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author gonnaup
 * @version created at 2022/10/15 9:42
 */
@SpringBootApplication
@EnableDiscoveryClient
public class EntryApplication {
    public static void main(String[] args) {
        SpringApplication.run(EntryApplication.class, args);
    }
}
