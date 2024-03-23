package eu.malycha.hazelcast.eventsourcing.client.utils;

public class TimedResult implements AutoCloseable {

    private long milliseconds;

    public TimedResult() {
        this.milliseconds = System.currentTimeMillis();
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    @Override
    public void close() throws Exception {
        long stop = System.currentTimeMillis();
        milliseconds = stop - milliseconds;
    }
}
