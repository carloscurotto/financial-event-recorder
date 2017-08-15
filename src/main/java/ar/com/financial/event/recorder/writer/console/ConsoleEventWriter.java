package ar.com.financial.event.recorder.writer.console;

import ar.com.financial.event.recorder.domain.Event;
import ar.com.financial.event.recorder.writer.Writer;

public class ConsoleEventWriter implements Writer<Event> {

    @Override
    public void open() {
        System.out.println("Starting the console event writer...");
    }

    @Override
    public void close() {
        System.out.println("Stopping the console event writer...");
    }

    @Override
    public void write(final Event event) {
        System.out.println(String.format("Event [%s]", event));
    }

}
