package ar.com.financial.event.recorder.writer.database;

import ar.com.financial.event.recorder.domain.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, Long> {
}
