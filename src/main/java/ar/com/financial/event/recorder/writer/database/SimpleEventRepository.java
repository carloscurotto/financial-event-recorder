package ar.com.financial.event.recorder.writer.database;

import ar.com.financial.event.recorder.domain.SimpleEvent;
import ar.com.financial.event.recorder.domain.SimpleEventKey;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface SimpleEventRepository extends CrudRepository<SimpleEvent, SimpleEventKey> {

    List<SimpleEvent> findAll();


    //List<SimpleEvent> findByLocalBicAndInputOutput(String localBic, String inputOutput);

    List<SimpleEvent> findByLocalBicAndOriginTimeBetweenAndInputOutput(String localBic,
                                                                       Date originTimeStart,
                                                                       Date originTimeEnd,
                                                                       String inputOutput);

}
