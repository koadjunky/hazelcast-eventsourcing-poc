package eu.malycha.hazelcast.eventsourcing.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.client.config.ConnectionRetryConfig;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.HazelcastInstance;
import eu.malycha.hazelcast.eventsourcing.domain.OrderStatus;
import eu.malycha.hazelcast.eventsourcing.domain.OrderStatusSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfiguration {

    private ClientConfig clientConfig() {
        ClientConfig clientConfig = new ClientConfig();

        ConnectionRetryConfig connectionRetryConfig = clientConfig.getConnectionStrategyConfig().getConnectionRetryConfig();
        connectionRetryConfig.setMaxBackoffMillis(500);
        connectionRetryConfig.setClusterConnectTimeoutMillis(5000);

        ClientNetworkConfig clientNetworkConfig = clientConfig.getNetworkConfig();
        clientNetworkConfig.addAddress("hazelcast1");
        clientNetworkConfig.setSmartRouting(true);
        clientNetworkConfig.setRedoOperation(true);
        clientNetworkConfig.setConnectionTimeout(500);

        SerializerConfig serializerConfig = new SerializerConfig();
        serializerConfig.setTypeClass(OrderStatus.class);
        serializerConfig.setImplementation(new OrderStatusSerializer());
        clientConfig.getSerializationConfig().addSerializerConfig(serializerConfig);

        return clientConfig;
    }

    @Bean
    public HazelcastInstance hazelcast() {
        return HazelcastClient.newHazelcastClient(clientConfig());
    }
}
