package ar.com.financial.event.recorder.reader.snmp;

import ar.com.financial.event.recorder.domain.RawEvent;
import ar.com.financial.event.recorder.reader.Reader;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import org.snmp4j.CommandResponderEvent;

import java.util.ArrayList;
import java.util.Collection;

public class SNMPEventReader implements Reader<RawEvent> {

    private static final Logger logger = Logger.getLogger(SNMPEventReader.class);

    private SNMPDataReader snmp;
    private SNMPEventParser parser;

    public SNMPEventReader(final String host, final String port, final Collection<String> eventCodes) {
        Validate.notBlank(host, "The host cannot be blank");
        Validate.notBlank(port, "The port cannot be blank");
        Validate.notEmpty(eventCodes, "The event codes cannot be empty");
        this.snmp = new SNMPDataReader(host, port);
        this.parser = new SNMPEventParser(new ArrayList<>(eventCodes));
    }

    @Override
    public void open() {
        snmp.open();
    }

    @Override
    public void close() {
        snmp.close();
    }

    @Override
    public boolean hasNext() {
        return snmp.hasNext();
    }

    @Override
    public RawEvent read() {
        logger.debug("Waiting to receive message from snmp.");
        CommandResponderEvent event = snmp.read();
        logger.debug(String.format("Message received from snmp [%s].", event));
        if (event == null) {
            logger.debug("Null message received from snmp.");
            return null;
        }
        logger.debug(String.format("Parsing message from snmp [%s].", event));
        return parser.parse(event);
    }

}
