package eu.malycha.hazelcast.eventsourcing.domain;

import com.hazelcast.jet.protobuf.ProtobufSerializer;

public class OrderStatusSerializer extends ProtobufSerializer<OrderStatus> {

    private static final int TYPE_ID = 1;

    public OrderStatusSerializer() {
        super(OrderStatus.class, TYPE_ID);
    }

}
