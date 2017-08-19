package ar.com.financial.event.recorder;

import ar.com.financial.event.recorder.domain.Event;
import ar.com.financial.event.recorder.reader.Reader;
import ar.com.financial.event.recorder.reader.file.FileEventReader;
import ar.com.financial.event.recorder.writer.Writer;
import ar.com.financial.event.recorder.writer.console.ConsoleEventWriter;
import ar.com.financial.event.recorder.writer.database.DatabaseWriter;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventRecorderConfiguration {

    @Bean
    @Qualifier("reader")
    public Reader<Event> createEventReader() {
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

    private Reader<Event> getEventReader(final String type) {
        if (type.equalsIgnoreCase("file")) {
            return new FileEventReader("config/event-log.txt");
        } else if (type.equalsIgnoreCase("snmp")) {
            throw new NotImplementedException("The snmp reader is not available yet");
        }
        throw new RuntimeException(String.format("The event reader of type [%s] is not supported", type));
    }

    @Bean
    @Qualifier("writer")
    public Writer<Event> createEventWriter() {
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

    private Writer<Event> getEventWriter(final String type) {
        if (type.equalsIgnoreCase("console")) {
            return new ConsoleEventWriter();
        } else if (type.equalsIgnoreCase("database")) {
            return new DatabaseWriter();
        }
        throw new RuntimeException(String.format("The event writer of type [%s] is not supported", type));
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public EventRecorder createEventRecorder(@Qualifier("reader") final Reader<Event> eventReader,
                                             @Qualifier("writer") final Writer<Event> eventWriter) {
        return new EventRecorder(eventReader, eventWriter);
    }

}
