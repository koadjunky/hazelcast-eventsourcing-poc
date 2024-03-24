package eu.malycha.hazelcast.eventsourcing.client.controllers;

import eu.malycha.hazelcast.eventsourcing.client.scenarios.StoreLoadScenario;
import eu.malycha.hazelcast.eventsourcing.client.utils.TimedResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LocalGenerate {

    private final StoreLoadScenario scenario = new StoreLoadScenario(null);

    @PostMapping("/local/fill")
    public TimedResult localFill() throws Exception {
        return scenario.fillMap();
    }

    @PostMapping("/local/verify")
    public TimedResult localVerify() throws Exception {
        return scenario.verifyMap();
    }
}
