package eu.malycha.hazelcast.eventsourcing.server;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.map.IMap;
import eu.malycha.hazelcast.eventsourcing.domain.MapName;
import eu.malycha.hazelcast.eventsourcing.domain.OrderStatus;
import eu.malycha.hazelcast.eventsourcing.domain.OrderStatusSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class HazelcastConfiguration {

    private Config serverConfig(JdbcTemplate jdbcTemplate) {
        Config config = new Config();

        SerializerConfig serializerConfig = new SerializerConfig();
        serializerConfig.setTypeClass(OrderStatus.class);
        serializerConfig.setImplementation(new OrderStatusSerializer());
        config.getSerializationConfig().addSerializerConfig(serializerConfig);

        MapStoreConfig byOrderIdStoreConfig = new MapStoreConfig();
        byOrderIdStoreConfig.setEnabled(true);
        byOrderIdStoreConfig.setWriteDelaySeconds(0);
        byOrderIdStoreConfig.setImplementation(new OrderStatusMapStore(jdbcTemplate));
        byOrderIdStoreConfig.setInitialLoadMode(MapStoreConfig.InitialLoadMode.LAZY);

        MapConfig byOrderIdMapConfig = config.getMapConfig(MapName.BY_ORDER_ID);
        byOrderIdMapConfig.setMapStoreConfig(byOrderIdStoreConfig);

        return config;
    }

    @Bean
    public HazelcastInstance hazelcast(JdbcTemplate jdbcTemplate) {
        return Hazelcast.newHazelcastInstance(serverConfig(jdbcTemplate));
    }
}
