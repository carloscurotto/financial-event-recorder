package ar.com.financial.event.recorder.writer;

import ar.com.financial.event.recorder.domain.RawEvent;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Service
public class MultipleEventWriter implements Writer<RawEvent> {

    private Collection<Writer<RawEvent>> writers = new ArrayList<>();

    /**
     * Do not use, framework usage only.
     */
    @Deprecated
    public MultipleEventWriter() {
    }

    @SafeVarargs
    public MultipleEventWriter(final Writer<RawEvent>... writers) {
        this(Arrays.asList(writers));
    }

    private MultipleEventWriter(final Collection<Writer<RawEvent>> writers) {
        Validate.notEmpty(writers, "The writers cannot be empty");
        this.writers.addAll(writers);
    }

    @Override
    public void open() {
        writers.parallelStream().forEach(Writer::open);
    }

    @Override
    public void close() {
        writers.parallelStream().forEach(Writer::close);
    }

    @Override
    public void write(final RawEvent event) {
        writers.forEach(writer -> {
            writer.write(event);
        });
    }

}