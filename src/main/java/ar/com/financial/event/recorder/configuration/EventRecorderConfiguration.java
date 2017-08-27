package ar.com.financial.event.recorder.configuration;

import ar.com.financial.event.recorder.EventRecorder;
import ar.com.financial.event.recorder.domain.RawEvent;
import ar.com.financial.event.recorder.reader.Reader;
import ar.com.financial.event.recorder.reader.file.FileEventReader;
import ar.com.financial.event.recorder.writer.MultipleEventWriter;
import ar.com.financial.event.recorder.writer.Writer;
import ar.com.financial.event.recorder.writer.console.ConsoleSimpleEventWriter;
import ar.com.financial.event.recorder.writer.console.ConsoleSummaryEventWriter;
import ar.com.financial.event.recorder.writer.database.DatabaseSimpleEventWriter;
import ar.com.financial.event.recorder.writer.database.DatabaseSummaryEventWriter;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventRecorderConfiguration {

    @Bean
    @Qualifier("reader")
    public Reader<RawEvent> createEventReader() {
        return getEventReader(getEventReaderType());
    }

    private String getEventReaderType () {
        String type = System.getProperty("reader-type");
        if (StringUtils.isNotBlank(type)) {
            return type.trim();
        } else {
            return "file";
        }
    }

    private Reader<RawEvent> getEventReader(final String type) {
        if (type.equalsIgnoreCase("file")) {
            return new FileEventReader("config/event-log.txt");
        } else if (type.equalsIgnoreCase("snmp")) {
            throw new NotImplementedException("The snmp reader is not available yet");
        }
        throw new RuntimeException(String.format("The event reader of type [%s] is not supported", type));
    }

    @Bean
    @Qualifier("writer")
    public Writer<RawEvent> createEventWriter() {
        return getEventWriter(getEventWriterType());
    }

    private String getEventWriterType () {
        String type = System.getProperty("writer-type");
        if (StringUtils.isNotBlank(type)) {
            return type.trim();
        } else {
            return "console";
        }
    }

    @Bean
    public Writer<RawEvent> createDatabaseEventWriter() {
        return new DatabaseSimpleEventWriter();
    }

    @Bean
    public Writer<RawEvent> createDatabaseSummaryWriter() {
        return new DatabaseSummaryEventWriter();
    }

    private Writer<RawEvent> getEventWriter(final String type) {
        if (type.equalsIgnoreCase("console")) {
            return new MultipleEventWriter(new ConsoleSimpleEventWriter(), new ConsoleSummaryEventWriter());
        } else if (type.equalsIgnoreCase("database")) {
            return new MultipleEventWriter(createDatabaseEventWriter(), createDatabaseSummaryWriter());
        }
        throw new RuntimeException(String.format("The event writer of type [%s] is not supported", type));
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public EventRecorder createEventRecorder(@Qualifier("reader") final Reader<RawEvent> eventReader,
                                             @Qualifier("writer") final Writer<RawEvent> eventWriter) {
        return new EventRecorder(eventReader, eventWriter);
    }

}
