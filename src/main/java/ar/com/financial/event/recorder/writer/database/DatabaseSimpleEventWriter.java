package ar.com.financial.event.recorder.writer.database;

import ar.com.financial.event.recorder.domain.RawEvent;
import ar.com.financial.event.recorder.domain.SimpleEvent;
import ar.com.financial.event.recorder.writer.Writer;
import org.apache.commons.lang3.Validate;

public class DatabaseSimpleEventWriter implements Writer<RawEvent> {

    private SimpleEventRepository eventRepository;

    public DatabaseSimpleEventWriter(final SimpleEventRepository eventRepository) {
        Validate.notNull(eventRepository, "The SimpleEventRepository cannot be null");
        this.eventRepository = eventRepository;
    }

    @Override
    public void open() {
    }

    @Override
    public void close() {
    }

    @Override
    public synchronized void write(final RawEvent event) {
        SimpleEvent toStore = event.toSimple();
        SimpleEvent fromStore = eventRepository.findOne(toStore.getSequence());
        if (fromStore == null) {
            System.out.println("Storing simple event [" + toStore + "]. Not existent in database.");
            eventRepository.save(toStore);
        } else {
            System.out.println("Ignoring simple event [" + toStore + "]. Already exist in database.");
        }
    }

}
