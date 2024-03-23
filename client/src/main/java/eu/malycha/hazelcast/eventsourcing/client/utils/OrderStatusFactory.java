package eu.malycha.hazelcast.eventsourcing.client.utils;

import eu.malycha.hazelcast.eventsourcing.domain.Execution;
import eu.malycha.hazelcast.eventsourcing.domain.OemsRequest;
import eu.malycha.hazelcast.eventsourcing.domain.OrderStatus;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

public class OrderStatusFactory {

    public static OrderStatus createOrderStatus(String orderId, String intId, String extId, String state, boolean closed) {
        return OrderStatus.newBuilder()
            .setOrderId(orderId)
            .setIntId(intId)
            .setExtId(extId)
            .setVenueAccount("Venue")
            .setOrderStatus(state)
            .setQuantity("112")
            .setFilledQuantity("98")
            .setRemainingQuantity("14")
            .setAvgPrice("58831.493301")
            .setClosed(closed)
            .setRequest(createRequest())
            .setExecution(createExecution())
            .build();
    }

    public static Execution createExecution() {
        return Execution.newBuilder()
            .setExecutionId(UUID.randomUUID().toString())
            .setQuantity("100")
            .setPrice("12310.112212")
            .setCurrency("USD")
            .setFee("10.223445")
            .setFeeCurrency("USD")
            .setVenueDateTime(now())
            .setDateTime(now())
            .build();
    }

    public static OemsRequest createRequest() {
        return OemsRequest.newBuilder()
            .setOrderId(UUID.randomUUID().toString())
            .setRequestType("ORDER")
            .setTarget("Venue")
            .setInstrumentId("EURUSD")
            .setClientId("Client")
            .setBaseCurrency("EUR")
            .setQuoteCurrency("USD")
            .setPortfolioId(UUID.randomUUID().toString())
            .setQuantity("112")
            .setPrice("10231.115456")
            .setStopPrice("20112.593124")
            .setExpireTime(now())
            .setPostOnly(false)
            .build();
    }

    public static String now() {
        return ZonedDateTime.now(ZoneOffset.UTC).toString();
    }
}
