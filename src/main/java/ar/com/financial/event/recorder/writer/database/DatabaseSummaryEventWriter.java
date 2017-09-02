package ar.com.financial.event.recorder.writer.database;

import ar.com.financial.event.recorder.domain.RawEvent;
import ar.com.financial.event.recorder.domain.SummaryEvent;
import ar.com.financial.event.recorder.writer.Writer;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

public class DatabaseSummaryEventWriter implements Writer<RawEvent> {

    private static final Logger logger = Logger.getLogger(DatabaseSummaryEventWriter.class);

    private SummaryEventRepository summaryRepository;

    public DatabaseSummaryEventWriter(final SummaryEventRepository summaryRepository) {
        Validate.notNull(summaryRepository, "The SummaryEventRepository cannot be null");
        this.summaryRepository = summaryRepository;
    }

    @Override
    public void start() {
        logger.info("Starting database summary event writer.");
    }

    @Override
    public void stop() {
        logger.info("Stopping database summary event writer.");
    }

    @Override
    public synchronized void write(final RawEvent event) {
        if (event.isSummary()) {
            try {
                SummaryEvent toStore = event.toSummary();
                logger.debug(String.format("Writing summary event in database [%s].", toStore));
                summaryRepository.save(toStore);
            } catch (Exception e) {
                logger.error(String.format("Error writing summary event in database [%s].", event));
            }
        }
    }

}
