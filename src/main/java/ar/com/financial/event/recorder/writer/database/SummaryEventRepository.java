package ar.com.financial.event.recorder.writer.database;

import ar.com.financial.event.recorder.domain.SummaryEvent;
import ar.com.financial.event.recorder.domain.SummaryEventKey;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SummaryEventRepository extends CrudRepository<SummaryEvent, SummaryEventKey> {

    List<SummaryEvent> findAll();
}
