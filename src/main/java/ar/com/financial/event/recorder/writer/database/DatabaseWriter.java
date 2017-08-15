package ar.com.financial.event.recorder.writer.database;

import ar.com.financial.event.recorder.domain.Event;
import ar.com.financial.event.recorder.writer.Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseWriter implements Writer<Event> {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public void open() {
    }

    @Override
    public void close() {
    }

    @Override
    public void write(final Event event) {
        eventRepository.save(event);
    }

}
