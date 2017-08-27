package ar.com.financial.event.recorder.writer.console;

import ar.com.financial.event.recorder.domain.RawEvent;
import ar.com.financial.event.recorder.writer.Writer;
import org.apache.log4j.Logger;

public class ConsoleSimpleEventWriter implements Writer<RawEvent> {

    private static final Logger logger = Logger.getLogger(ConsoleSimpleEventWriter.class);

    @Override
    public void open() {
        logger.info("Opening the console simple event writer.");
    }

    @Override
    public void close() {
        logger.info("Closing the console simple event writer.");
    }

    @Override
    public void write(final RawEvent event) {
        logger.info(event.toSimple());
    }

}
