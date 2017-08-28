package ar.com.financial.event.recorder.reader.snmp;

import ar.com.financial.event.recorder.domain.RawEvent;
import ar.com.financial.event.recorder.reader.Reader;
import org.apache.commons.lang3.Validate;
import org.snmp4j.CommandResponderEvent;

public class SNMPEventReader implements Reader<RawEvent> {

    private SNMPDataReader snmp;
    private SNMPEventParser parser;

    public SNMPEventReader(final String host, final String port) {
        Validate.notBlank(host, "The host cannot be blank");
        Validate.notBlank(port, "The port cannot be blank");
        this.snmp = new SNMPDataReader(host, port);
        this.parser = new SNMPEventParser();
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
        CommandResponderEvent data = snmp.read();
        if (data == null) {
            return null;
        }
        return parser.parse(data);
    }

}
