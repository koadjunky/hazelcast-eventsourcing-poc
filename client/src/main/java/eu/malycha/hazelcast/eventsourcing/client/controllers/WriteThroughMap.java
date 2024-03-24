package eu.malycha.hazelcast.eventsourcing.client.controllers;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import eu.malycha.hazelcast.eventsourcing.client.scenarios.StoreLoadScenario;
import eu.malycha.hazelcast.eventsourcing.client.utils.TimedResult;
import eu.malycha.hazelcast.eventsourcing.domain.MapName;
import eu.malycha.hazelcast.eventsourcing.domain.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WriteThroughMap {

    private static final Logger LOGGER = LoggerFactory.getLogger(WriteThroughMap.class);

    private final StoreLoadScenario scenario;

    public WriteThroughMap(HazelcastInstance hazelcast) {
        IMap<String, OrderStatus> byOrderId = hazelcast.getMap(MapName.WRITE_THROUGH);
        this.scenario = new StoreLoadScenario(byOrderId);
    }

    @PostMapping("/through/fill")
    public TimedResult basic_map() throws Exception {
        return scenario.fillMap();
    }

    @PostMapping("/through/verify")
    public TimedResult verify_map() throws Exception {
        return scenario.verifyMap();
    }
}
