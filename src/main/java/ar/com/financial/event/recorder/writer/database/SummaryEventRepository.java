package ar.com.financial.event.recorder.writer.database;

import ar.com.financial.event.recorder.domain.SummaryEvent;
import ar.com.financial.event.recorder.domain.SummaryEventKey;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SummaryEventRepository extends CrudRepository<SummaryEvent, SummaryEventKey> {

    List<SummaryEvent> findAll();

    @Transactional
    @Modifying
    @Query("UPDATE SummaryEvent summary SET summary.validation = :validation WHERE summary.session = :session")
    int updateValidation(@Param("session") String session, @Param("validation") String validation);

}
