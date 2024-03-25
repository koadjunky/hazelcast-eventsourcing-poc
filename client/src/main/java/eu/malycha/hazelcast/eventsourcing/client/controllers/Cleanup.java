package eu.malycha.hazelcast.eventsourcing.client.controllers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Cleanup {

    private final JdbcTemplate jdbc;

    public Cleanup(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @PostMapping("/cleanup")
    public void cleanup() {
        jdbc.update("delete from order_status");
    }
}
