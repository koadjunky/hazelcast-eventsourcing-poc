package eu.malycha.hazelcast.eventsourcing.client.controllers;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import eu.malycha.hazelcast.eventsourcing.client.scenarios.StoreLoadScenario;
import eu.malycha.hazelcast.eventsourcing.client.utils.Configuration;
import eu.malycha.hazelcast.eventsourcing.client.utils.OrderStatusFactory;
import eu.malycha.hazelcast.eventsourcing.client.utils.TimedResult;
import eu.malycha.hazelcast.eventsourcing.domain.MapName;
import eu.malycha.hazelcast.eventsourcing.domain.OrderStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class BasicHazelcastMap {

    private final StoreLoadScenario scenario;

    public BasicHazelcastMap(HazelcastInstance hazelcast) {
        IMap<String, OrderStatus> basicMap = hazelcast.getMap(MapName.BASIC_MAP);
        this.scenario = new StoreLoadScenario(basicMap);
    }

    @PostMapping("/basic/fill")
    public TimedResult basicFill() throws Exception {
        return scenario.fillMap();
    }

    @PostMapping("/basic/verify")
    public TimedResult localVerify() throws Exception {
        return scenario.verifyMap();
    }
}
