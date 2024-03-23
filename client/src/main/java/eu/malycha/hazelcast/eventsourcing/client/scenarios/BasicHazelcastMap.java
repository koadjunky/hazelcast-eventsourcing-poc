package eu.malycha.hazelcast.eventsourcing.client.scenarios;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.org.apache.calcite.linq4j.Ord;
import eu.malycha.hazelcast.eventsourcing.client.utils.Configuration;
import eu.malycha.hazelcast.eventsourcing.client.utils.OrderStatusFactory;
import eu.malycha.hazelcast.eventsourcing.client.utils.TimedResult;
import eu.malycha.hazelcast.eventsourcing.domain.OrderStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class BasicHazelcastMap {

    private final IMap<String, OrderStatus> basicMap;

    public BasicHazelcastMap(HazelcastInstance hazelcast) {
        this.basicMap = hazelcast.getMap("basicMap");
    }

    @PostMapping("/basic/map")
    public TimedResult basic_map() throws Exception {
        try (TimedResult result = new TimedResult()) {
            for (int i = 0; i < Configuration.REPETITIONS; i++) {
                String orderId = UUID.randomUUID().toString();
                String intId = UUID.randomUUID().toString();
                String extId = UUID.randomUUID().toString();
                OrderStatus order = OrderStatusFactory.createOrderStatus(orderId, StringUtils.EMPTY, StringUtils.EMPTY, "PENDING_NEW", false);
                basicMap.put(orderId, order);
                OrderStatus pendingNew = OrderStatusFactory.createOrderStatus(orderId, intId, StringUtils.EMPTY, "PENDING_NEW", false);
                basicMap.put(orderId, pendingNew);
                OrderStatus orderNew = OrderStatusFactory.createOrderStatus(StringUtils.EMPTY, StringUtils.EMPTY, extId, "NEW", false);
                basicMap.put(orderId, orderNew);
                OrderStatus partialFill = OrderStatusFactory.createOrderStatus(orderId, intId, extId, "PARTIALLY_FILLED", false);
                basicMap.put(orderId, partialFill);
                OrderStatus fill = OrderStatusFactory.createOrderStatus(orderId, intId, extId, "FILLED", true);
                basicMap.put(orderId, fill);
            }
            return result;
        }
    }
}
