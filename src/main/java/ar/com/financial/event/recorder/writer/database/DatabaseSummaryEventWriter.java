package ar.com.financial.event.recorder.writer.database;

import ar.com.financial.event.recorder.domain.RawEvent;
import ar.com.financial.event.recorder.writer.Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseSummaryEventWriter implements Writer<RawEvent> {

    @Autowired
    private SummaryEventRepository summaryRepository;

    @Override
    public void open() {
    }

    @Override
    public void close() {
    }

    @Override
    public void write(final RawEvent event) {
        if (event.isSummary()) {
            summaryRepository.save(event.toSummary());
        }
    }

}
