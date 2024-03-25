package eu.malycha.hazelcast.eventsourcing.client.controllers;

import com.hazelcast.core.HazelcastInstance;
import eu.malycha.hazelcast.eventsourcing.client.scenarios.StoreLoadScenario;
import eu.malycha.hazelcast.eventsourcing.client.utils.TimedResult;
import eu.malycha.hazelcast.eventsourcing.domain.MapName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DelegatedMap {

    private static final Logger LOGGER = LoggerFactory.getLogger(DelegatedMap.class);

    private final StoreLoadScenario scenario;

    public DelegatedMap(HazelcastInstance hazelcast) {
        this.scenario = new StoreLoadScenario(hazelcast.getMap(MapName.DELEGATE));
    }

    @PostMapping("/delegated/fill")
    public TimedResult basic_map() throws Exception {
        return scenario.fillMap();
    }

    @PostMapping("/delegated/verify")
    public TimedResult verify_map() throws Exception {
        return scenario.verifyMap();
    }
}
