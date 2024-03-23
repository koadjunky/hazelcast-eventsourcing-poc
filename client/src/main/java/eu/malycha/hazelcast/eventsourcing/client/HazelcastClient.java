package eu.malycha.hazelcast.eventsourcing.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HazelcastClient {

    public static void main(String[] args) {
        SpringApplication.run(HazelcastClient.class, args);
    }
}
