package ar.com.financial.event.recorder.writer.database;

import ar.com.financial.event.recorder.domain.RawEvent;
import ar.com.financial.event.recorder.writer.Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseSimpleEventWriter implements Writer<RawEvent> {

    @Autowired
    private SimpleEventRepository eventRepository;

    @Override
    public void open() {
    }

    @Override
    public void close() {
    }

    @Override
    public void write(final RawEvent event) {
        eventRepository.save(event.toSimple());
    }

}
