package ar.com.financial.event.recorder.writer.database;

import ar.com.financial.event.recorder.domain.RawEvent;
import ar.com.financial.event.recorder.domain.SimpleEvent;
import ar.com.financial.event.recorder.writer.Writer;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

public class DatabaseSimpleEventWriter implements Writer<RawEvent> {

    private static final Logger logger = Logger.getLogger(DatabaseSimpleEventWriter.class);

    private SimpleEventRepository eventRepository;

    public DatabaseSimpleEventWriter(final SimpleEventRepository eventRepository) {
        Validate.notNull(eventRepository, "The SimpleEventRepository cannot be null");
        this.eventRepository = eventRepository;
    }

    @Override
    public void start() {
        logger.info("Starting database simple event writer.");
    }

    @Override
    public void stop() {
        logger.info("Stopping database simple event writer.");
    }

    @Override
    public synchronized void write(final RawEvent event) {
        try {
            SimpleEvent toStore = event.toSimple();
            logger.debug(String.format("Writing simple event in database [%s].", toStore));
            eventRepository.save(toStore);
        } catch (Exception e) {
            logger.error(String.format("Error writing simple event in database [%s].", event));
        }
    }

}
