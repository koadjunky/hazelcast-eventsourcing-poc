package eu.malycha.hazelcast.eventsourcing.server;

import com.google.protobuf.InvalidProtocolBufferException;
import com.hazelcast.map.MapStore;
import eu.malycha.hazelcast.eventsourcing.domain.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DelegatedMapStore implements MapStore<String, OrderStatus> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DelegatedMapStore.class);

    private static final int QUEUE_CAPACITY = 10;
    private static final int THREADS = 10;

    private final JdbcTemplate jdbc;
    private final ExecutorService executor = new ThreadPoolExecutor(THREADS, THREADS, 0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>(QUEUE_CAPACITY),
        new ThreadPoolExecutor.CallerRunsPolicy());

    public DelegatedMapStore(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void store(String key, OrderStatus value) {
        executor.submit(() ->
            jdbc.update("insert into order_status (order_id, int_id, ext_id, venue_account, serial, closed, payload) values (?, ?, ?, ?, ?, ?, ?)",
                value.getOrderId(),
                value.getIntId(),
                value.getExtId(),
                value.getVenueAccount(),
                value.getSerial(),
                value.getClosed(),
                value.toByteArray())
        );
    }

    @Override
    public void storeAll(Map<String, OrderStatus> map) {
        map.forEach(this::store);
    }

    @Override
    public void delete(String key) {
        // no-op
    }

    @Override
    public void deleteAll(Collection<String> keys) {
        // no-op
    }

    @Override
    public OrderStatus load(String key) {
        return jdbc.query("select payload from order_status where order_id=? order by serial desc limit 1", new OrderStatusMapStore.OrderStatusRowMapper(), key).stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public Map<String, OrderStatus> loadAll(Collection<String> keys) {
        Map<String, OrderStatus> result = new HashMap<>();
        for (String key : keys) {
            try {
                result.put(key, load(key));
            } catch (Exception ex) {
                LOGGER.warn("Loading record failed for key {}", key, ex);
            }
        }
        return result;
    }

    @Override
    public Iterable<String> loadAllKeys() {
        return List.of();
    }

    public static class OrderStatusRowMapper implements RowMapper<OrderStatus> {

        @Override
        public OrderStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
            try {
                return OrderStatus.parseFrom(rs.getBytes("payload"));
            } catch (InvalidProtocolBufferException e) {
                LOGGER.error("Invalid protobuf", e);
                throw new SQLException("Invalid protobuf", e);
            }
        }
    }
}
