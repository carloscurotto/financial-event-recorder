package ar.com.financial.event.recorder.writer.console;

import ar.com.financial.event.recorder.domain.RawEvent;
import ar.com.financial.event.recorder.writer.Writer;
import org.apache.log4j.Logger;

public class ConsoleSummaryEventWriter implements Writer<RawEvent> {

    private static final Logger logger = Logger.getLogger(ConsoleSummaryEventWriter.class);

    @Override
    public void start() {
        logger.info("Starting the console summary writer.");
    }

    @Override
    public void stop() {
        logger.info("Stopping the console summary writer.");
    }

    @Override
    public void write(final RawEvent event) {
        if (event.isSummary()) {
            logger.info(event.toSummary());
        }
    }

}
