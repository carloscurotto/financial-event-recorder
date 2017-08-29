package ar.com.financial.event.recorder.writer.database;

import ar.com.financial.event.recorder.domain.RawEvent;
import ar.com.financial.event.recorder.domain.SimpleEvent;
import org.springframework.data.repository.CrudRepository;

public interface SimpleEventRepository extends CrudRepository<SimpleEvent, String> {
}
