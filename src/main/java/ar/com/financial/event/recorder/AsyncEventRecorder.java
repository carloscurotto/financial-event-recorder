package ar.com.financial.event.recorder;

import ar.com.financial.event.recorder.domain.RawEvent;
import ar.com.financial.event.recorder.reader.Reader;
import ar.com.financial.event.recorder.writer.Writer;
import org.apache.commons.lang3.Validate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class AsyncEventRecorder {

    public static final String EVENT_RECORDER_EXECUTOR = "event-recorder-executor";
    private volatile boolean started = false;
    private ExecutorService executor;
    private Reader<RawEvent> eventReader;
    private Writer<RawEvent> eventWriter;

    public AsyncEventRecorder(final Reader eventReader, final Writer eventWriter) {
        Validate.notNull(eventReader, "The event reader cannot be null");
        Validate.notNull(eventWriter, "The event writer cannot be null");
        this.eventReader = eventReader;
        this.eventWriter = eventWriter;
    }

    public void start() {
        startWriter();
        startReader();
        markStarted();
        startExecutor();
    }

    private void startExecutor() {
        executor = Executors.newSingleThreadExecutor(runnable ->
                new Thread(runnable, EVENT_RECORDER_EXECUTOR));
        executor.submit(this::run);
    }

    private void startWriter() {
        eventWriter.open();
    }

    private void startReader() {
        eventReader.open();
    }

    private void markStarted() {
        this.started = true;
    }

    public void stop() {
        stopExecutor();
        stopReader();
        stopWriter();
        markStopped();
    }

    private void stopExecutor() {
        executor.shutdownNow();
    }

    private void stopReader() {
        eventReader.close();
    }

    private void stopWriter() {
        eventWriter.close();
    }

    private void markStopped() {
        this.started = false;
    }

    private boolean isStarted() {
        return started;
    }

    private void run() {
        while (isStarted() && hasNext()) {
            RawEvent event = readEvent();
            if (event != null) {
                writeEvent(event);
            }
        }
    }

    private boolean hasNext() {
        return eventReader.hasNext();
    }

    private RawEvent readEvent() {
        return eventReader.read();
    }

    private void writeEvent(final RawEvent event) {
        eventWriter.write(event);
    }

}