package eu.malycha.hazelcast.eventsourcing.server;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
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

        MapStoreConfig writeThroughStoreConfig = new MapStoreConfig();
        writeThroughStoreConfig.setEnabled(true);
        writeThroughStoreConfig.setWriteDelaySeconds(0);
        writeThroughStoreConfig.setImplementation(new OrderStatusMapStore(jdbcTemplate));
        writeThroughStoreConfig.setInitialLoadMode(MapStoreConfig.InitialLoadMode.LAZY);

        MapConfig writeThroughMapConfig = config.getMapConfig(MapName.WRITE_THROUGH);
        writeThroughMapConfig.setMapStoreConfig(writeThroughStoreConfig);

        MapStoreConfig writeBehindStoreConfig = new MapStoreConfig();
        writeBehindStoreConfig.setEnabled(true);
        writeBehindStoreConfig.setWriteDelaySeconds(1);
        writeBehindStoreConfig.setImplementation(new OrderStatusMapStore(jdbcTemplate));
        writeBehindStoreConfig.setInitialLoadMode(MapStoreConfig.InitialLoadMode.LAZY);

        MapConfig writeBehindMapConfig = config.getMapConfig(MapName.WRITE_BEHIND);
        writeBehindMapConfig.setMapStoreConfig(writeBehindStoreConfig);

        MapStoreConfig delegatedStoreConfig = new MapStoreConfig();
        delegatedStoreConfig.setEnabled(true);
        delegatedStoreConfig.setWriteDelaySeconds(0);
        delegatedStoreConfig.setImplementation(new DelegatedMapStore(jdbcTemplate));
        delegatedStoreConfig.setInitialLoadMode(MapStoreConfig.InitialLoadMode.LAZY);

        MapConfig delegatedMapConfig = config.getMapConfig(MapName.DELEGATE);
        delegatedMapConfig.setMapStoreConfig(delegatedStoreConfig);

        return config;
    }

    @Bean
    public HazelcastInstance hazelcast(JdbcTemplate jdbcTemplate) {
        return Hazelcast.newHazelcastInstance(serverConfig(jdbcTemplate));
    }
}
