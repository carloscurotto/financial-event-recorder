package ar.com.financial.event.recorder.writer.database;

import ar.com.financial.event.recorder.domain.SimpleEvent;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SimpleEventRepository extends CrudRepository<SimpleEvent, String> {

    List<SimpleEvent> findAll();

}
