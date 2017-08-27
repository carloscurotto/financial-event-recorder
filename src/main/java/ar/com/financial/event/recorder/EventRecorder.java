package ar.com.financial.event.recorder;

import ar.com.financial.event.recorder.domain.RawEvent;
import ar.com.financial.event.recorder.reader.Reader;
import ar.com.financial.event.recorder.writer.Writer;
import org.apache.commons.lang3.Validate;

public class EventRecorder {

    private volatile boolean started = false;
    private Reader<RawEvent> eventReader;
    private Writer<RawEvent> eventWriter;

    public EventRecorder(final Reader eventReader, final Writer eventWriter) {
        Validate.notNull(eventReader, "The event reader cannot be null");
        Validate.notNull(eventWriter, "The event writer cannot be null");
        this.eventReader = eventReader;
        this.eventWriter = eventWriter;
    }

    public void start() {
        startWriter();
        startReader();
        markStarted();
        run();
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
        stopReader();
        stopWriter();
        markStopped();
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
