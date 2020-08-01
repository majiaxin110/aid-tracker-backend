package org.aidtracker.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class AidTrackerBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AidTrackerBackendApplication.class, args);
    }

}
