package ar.com.financial.event.recorder;

import ar.com.financial.event.recorder.domain.RawEvent;
import ar.com.financial.event.recorder.reader.Reader;
import ar.com.financial.event.recorder.writer.Writer;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

public class EventSynchronizer {

    private static final Logger logger = Logger.getLogger(EventSynchronizer.class);

    private Reader<RawEvent> eventReader;
    private Writer<RawEvent> eventWriter;

    public EventSynchronizer(final Reader eventReader, final Writer eventWriter) {
        Validate.notNull(eventReader, "The event reader cannot be null");
        Validate.notNull(eventWriter, "The event writer cannot be null");
        this.eventReader = eventReader;
        this.eventWriter = eventWriter;
    }

    public void start() {
        logger.info("Starting the event synchronizer...");
    }

    private void startReader() {
        eventReader.start();
    }

    public void synchronize() {
        logger.info("Synchronizing events...");
        startReader();
        while (hasNext()) {
            RawEvent event = null;
            try {
                event = readEvent();
                if (event != null) {
                    writeEvent(event);
                }
            } catch (Exception e) {
                logger.error(String.format("Error processing event [%s].", event), e);
            }
        }
        stopReader();
        logger.info("Events synchronized.");
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

    public void stop() {
        logger.info("Stopping the event synchronizer...");
    }

    private void stopReader() {
        eventReader.stop();
    }

}