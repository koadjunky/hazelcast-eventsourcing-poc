package eu.malycha.hazelcast.eventsourcing.client.scenarios;

import eu.malycha.hazelcast.eventsourcing.client.utils.Configuration;
import eu.malycha.hazelcast.eventsourcing.client.utils.OrderStatusFactory;
import eu.malycha.hazelcast.eventsourcing.client.utils.TimedResult;
import eu.malycha.hazelcast.eventsourcing.domain.OrderStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class LocalGenerate {

    @PostMapping("/local/generate")
    public TimedResult local_generate() throws Exception {
        try (TimedResult result = new TimedResult()) {
            for (int i = 0; i < Configuration.REPETITIONS; i++) {
                String orderId = UUID.randomUUID().toString();
                String intId = UUID.randomUUID().toString();
                String extId = UUID.randomUUID().toString();
                OrderStatusFactory.createOrderStatus(orderId, StringUtils.EMPTY, StringUtils.EMPTY, "PENDING_NEW", false);
                OrderStatusFactory.createOrderStatus(orderId, intId, StringUtils.EMPTY, "PENDING_NEW", false);
                OrderStatusFactory.createOrderStatus(StringUtils.EMPTY, StringUtils.EMPTY, extId, "NEW", false);
                OrderStatusFactory.createOrderStatus(orderId, intId, extId, "PARTIALLY_FILLED", false);
                OrderStatusFactory.createOrderStatus(orderId, intId, extId, "FILLED", true);
            }
            return result;
        }
    }
}
