package ar.com.financial.event.recorder.writer.database;

import ar.com.financial.event.recorder.domain.RawEvent;
import ar.com.financial.event.recorder.domain.SummaryEvent;
import ar.com.financial.event.recorder.writer.Writer;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

public class DatabaseSummaryValidationWriter implements Writer<RawEvent> {

    private static final Logger logger = Logger.getLogger(DatabaseSummaryValidationWriter.class);

    private SimpleEventRepository simpleEventRepository;
    private SummaryEventRepository summaryRepository;

    public DatabaseSummaryValidationWriter(final SimpleEventRepository simpleEventRepository,
                                           final SummaryEventRepository summaryRepository) {
        Validate.notNull(simpleEventRepository, "The simple event repository cannot be null");
        Validate.notNull(summaryRepository, "The summary event repository cannot be null");
        this.simpleEventRepository = simpleEventRepository;
        this.summaryRepository = summaryRepository;
    }

    @Override
    public void start() {
        logger.info("Starting database summary validation writer.");
    }

    @Override
    public void stop() {
        logger.info("Stopping database summary validation writer.");
    }

    @Override
    public synchronized void write(final RawEvent event) {
        if (event.isSummary()) {
            try {
                final SummaryEvent summaryEvent = event.toSummary();

                final String quantityMessagesValidation = this.validateQuantityMessages(summaryEvent);

                this.writeValidationResult(summaryEvent, quantityMessagesValidation);
            } catch (Exception e) {
                logger.error(String.format("Error writing summary validation in database [%s].", event), e);
            }
        }
    }

    private void writeValidationResult(final SummaryEvent event, final String validation) {
        logger.debug(String.format("Writing summary validation [%s] in database for event [%s]", validation, event));
        this.summaryRepository.updateValidation(event.getSession(), validation);
    }

    private String validateQuantityMessages(final SummaryEvent summaryEvent) {
        final Integer quantityMessagesSent = this.getQuantityMessagesSent(summaryEvent);
        final Integer quantityMessagesReceived = this.getQuantityMessagesReceived(summaryEvent);
        final boolean quantityMessagesMatchesSummary =
                quantityMessagesSent.toString().equals(summaryEvent.getQuantityMessagesSent()) &&
                quantityMessagesReceived.toString().equals(summaryEvent.getQuantityMessagesReceived());
        if (quantityMessagesMatchesSummary) {
            return "OK";
        } else {
            return "FAIL";
        }
    }

    private Integer getQuantityMessagesSent(final SummaryEvent summaryEvent) {
        return this.simpleEventRepository.findBySessionAndInputOutput(summaryEvent.getSession(), "I").size();
    }

    private Integer getQuantityMessagesReceived(final SummaryEvent summaryEvent) {
        return this.simpleEventRepository.findBySessionAndInputOutput(summaryEvent.getSession(), "O").size();
    }

}
