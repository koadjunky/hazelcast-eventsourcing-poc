package eu.malycha.hazelcast.eventsourcing.client.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TimedResult implements AutoCloseable {

    private long milliseconds;
    private final Map<String, String> messages = new HashMap<>();

    public TimedResult() {
        this.milliseconds = System.currentTimeMillis();
    }

    public void addMessage(String key, String value) {
        messages.put(key, value);
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    @Override
    public void close() throws Exception {
        long stop = System.currentTimeMillis();
        milliseconds = stop - milliseconds;
    }
}
