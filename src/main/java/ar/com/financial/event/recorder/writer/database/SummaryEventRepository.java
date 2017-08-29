package ar.com.financial.event.recorder.writer.database;

import ar.com.financial.event.recorder.domain.SummaryEvent;
import org.springframework.data.repository.CrudRepository;

public interface SummaryEventRepository extends CrudRepository<SummaryEvent, String> {
}
