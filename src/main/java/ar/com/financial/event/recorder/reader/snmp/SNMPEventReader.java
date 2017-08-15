package ar.com.financial.event.recorder.reader.snmp;

import ar.com.financial.event.recorder.domain.Event;
import ar.com.financial.event.recorder.reader.Reader;

public class SNMPEventReader implements Reader<Event> {

    @Override
    public void open() {
        //TODO connect to the SNMP socket
    }

    @Override
    public void close() {
        //TODO disconnect from the SNMP socket
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Event read() {
        //TODO read from the SNMP socket
        return null;
    }

}
