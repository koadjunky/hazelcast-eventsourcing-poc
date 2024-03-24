package eu.malycha.hazelcast.eventsourcing.client.scenarios;

import com.hazelcast.map.IMap;
import eu.malycha.hazelcast.eventsourcing.client.utils.Configuration;
import eu.malycha.hazelcast.eventsourcing.client.utils.OrderStatusFactory;
import eu.malycha.hazelcast.eventsourcing.client.utils.TimedResult;
import eu.malycha.hazelcast.eventsourcing.domain.OrderStatus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

public class StoreLoadScenario {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoreLoadScenario.class);

    private final IMap<String, OrderStatus> orderStatusMap;

    public StoreLoadScenario(IMap<String, OrderStatus> orderStatusMap) {
        this.orderStatusMap = orderStatusMap;
    }

    public TimedResult fillMap() throws Exception {
        try (TimedResult result = new TimedResult()) {
            for (int i = 0; i < Configuration.REPETITIONS; i++) {
                String orderId = String.format("%010d", i);
                String intId = UUID.randomUUID().toString();
                String extId = UUID.randomUUID().toString();
                OrderStatus order = OrderStatusFactory.createOrderStatus(orderId, StringUtils.EMPTY, StringUtils.EMPTY, "PENDING_NEW", 0, false);
                storeFetchAndCompare(orderId, order);
                OrderStatus pendingNew = OrderStatusFactory.createOrderStatus(orderId, intId, StringUtils.EMPTY, "PENDING_NEW", 1, false);
                storeFetchAndCompare(orderId, pendingNew);
                OrderStatus orderNew = OrderStatusFactory.createOrderStatus(orderId, StringUtils.EMPTY, extId, "NEW", 2, false);
                storeFetchAndCompare(orderId, orderNew);
                OrderStatus partialFill = OrderStatusFactory.createOrderStatus(orderId, intId, extId, "PARTIALLY_FILLED", 3, false);
                storeFetchAndCompare(orderId, partialFill);
                OrderStatus fill = OrderStatusFactory.createOrderStatus(orderId, intId, extId, "FILLED", 4, true);
                storeFetchAndCompare(orderId, fill);
            }
            result.addMessage("Repetitions", String.valueOf(Configuration.REPETITIONS));
            return result;
        }
    }

    public TimedResult verifyMap() throws Exception {
        try (TimedResult result = new TimedResult()) {
            for (int i = 0; i < Configuration.REPETITIONS; i++) {
                if (orderStatusMap != null) {
                    String orderId = String.format("%010d", i);
                    OrderStatus os = orderStatusMap.get(orderId);
                    if (!"FILLED".equals(os.getOrderStatus())) {
                        LOGGER.error("Status is not FILLED: {}\n{}", orderStatusMap.getName(), os);
                    }
                }
            }
            result.addMessage("Repetitions", String.valueOf(Configuration.REPETITIONS));
            return result;
        }
    }

    private void storeFetchAndCompare(String orderId, OrderStatus writeStatus) {
        if (orderStatusMap != null) {
            orderStatusMap.set(orderId, writeStatus);
            OrderStatus readStatus = orderStatusMap.get(orderId);
            if (!writeStatus.equals(readStatus)) {
                LOGGER.error("Read value doesn't match written: {}\n{}", orderStatusMap.getName(), writeStatus);
            }
        }
    }
}
