package ar.com.financial.event.recorder.writer.database;

import ar.com.financial.event.recorder.domain.RawEvent;
import ar.com.financial.event.recorder.domain.SummaryEvent;
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
    public synchronized void write(final RawEvent event) {
        if (event.isSummary()) {
            SummaryEvent toStore = event.toSummary();
            SummaryEvent fromStore = summaryRepository.findOne(toStore.getSession());
            if (fromStore == null) {
                summaryRepository.save(toStore);
            }
        }
    }

}
