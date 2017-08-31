package ar.com.financial.event.recorder.writer.database;

import ar.com.financial.event.recorder.domain.RawEvent;
import ar.com.financial.event.recorder.domain.SummaryEvent;
import ar.com.financial.event.recorder.writer.Writer;
import org.apache.commons.lang3.Validate;

public class DatabaseSummaryEventWriter implements Writer<RawEvent> {

    private SummaryEventRepository summaryRepository;

    public DatabaseSummaryEventWriter(final SummaryEventRepository summaryRepository) {
        Validate.notNull(summaryRepository, "The SummaryEventRepository cannot be null");
        this.summaryRepository = summaryRepository;
    }

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
                System.out.println("Storing summary event [" + toStore + "]. Not existent in database.");
                summaryRepository.save(toStore);
            } else {
                System.out.println("Ignoring summary event [" + toStore + "]. Already exist in database.");
            }
        }
    }

}
