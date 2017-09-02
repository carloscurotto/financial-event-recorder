package ar.com.financial.event.recorder.writer;

import ar.com.financial.event.recorder.domain.RawEvent;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class MultipleEventWriter implements Writer<RawEvent> {

    private Collection<Writer<RawEvent>> writers = new ArrayList<>();

    @SafeVarargs
    public MultipleEventWriter(final Writer<RawEvent>... writers) {
        this(Arrays.asList(writers));
    }

    private MultipleEventWriter(final Collection<Writer<RawEvent>> writers) {
        Validate.notEmpty(writers, "The writers cannot be empty");
        this.writers.addAll(writers);
    }

    @Override
    public void start() {
        writers.parallelStream().forEach(Writer::start);
    }

    @Override
    public void stop() {
        writers.parallelStream().forEach(Writer::stop);
    }

    @Override
    public void write(final RawEvent event) {
        writers.forEach(writer -> writer.write(event));
    }

}
