package ar.com.financial.event.recorder.configuration;

import ar.com.financial.event.recorder.EventRecorder;
import ar.com.financial.event.recorder.domain.RawEvent;
import ar.com.financial.event.recorder.reader.Reader;
import ar.com.financial.event.recorder.reader.file.FileEventReader;
import ar.com.financial.event.recorder.reader.snmp.SNMPEventReader;
import ar.com.financial.event.recorder.writer.MultipleEventWriter;
import ar.com.financial.event.recorder.writer.Writer;
import ar.com.financial.event.recorder.writer.console.ConsoleSimpleEventWriter;
import ar.com.financial.event.recorder.writer.console.ConsoleSummaryEventWriter;
import ar.com.financial.event.recorder.writer.database.DatabaseSimpleEventWriter;
import ar.com.financial.event.recorder.writer.database.DatabaseSummaryEventWriter;
import ar.com.financial.event.recorder.writer.database.SimpleEventRepository;
import ar.com.financial.event.recorder.writer.database.SummaryEventRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventRecorderConfiguration {

    @Value("${snmp.host}")
    private String snmpHost;

    @Value("${snmp.port}")
    private String snmpPort;

    @Autowired
    private SimpleEventRepository simpleEventRepository;

    @Autowired
    private SummaryEventRepository summaryEventRepository;

    @Bean
    public Reader<RawEvent> createEventReader() {
        return getEventReader(getEventReaderType());
    }

    private String getEventReaderType() {
        return getOrDefaultProperty("reader-type", "file");
    }

    private String getOrDefaultProperty(final String name, final String defaultValue) {
        String value = System.getProperty(name);
        return (StringUtils.isNotBlank(value)) ? value.trim() : defaultValue;
    }

    private Reader<RawEvent> getEventReader(final String type) {
        if (type.equalsIgnoreCase("file")) {
            return new FileEventReader("config/event-log.txt");
        } else if (type.equalsIgnoreCase("snmp")) {
            return new SNMPEventReader(snmpHost, snmpPort);
        }
        throw new RuntimeException(String.format("The event reader of type [%s] is not supported", type));
    }

    @Bean
    public Writer<RawEvent> createEventWriter() {
        return getEventWriter(getEventWriterType());
    }

    private String getEventWriterType() {
        return getOrDefaultProperty("writer-type", "console");
    }

    private Writer<RawEvent> getEventWriter(final String type) {
        if (type.equalsIgnoreCase("console")) {
            return new MultipleEventWriter(new ConsoleSimpleEventWriter(), new ConsoleSummaryEventWriter());
        } else if (type.equalsIgnoreCase("database")) {
            return new MultipleEventWriter(createDatabaseEventWriter(), createDatabaseSummaryWriter());
        }
        throw new RuntimeException(String.format("The event writer of type [%s] is not supported", type));
    }

    private Writer<RawEvent> createDatabaseEventWriter() {
        return new DatabaseSimpleEventWriter(simpleEventRepository);
    }

    private Writer<RawEvent> createDatabaseSummaryWriter() {
        return new DatabaseSummaryEventWriter(summaryEventRepository);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public EventRecorder createEventRecorder(final Reader<RawEvent> eventReader,
                                             final Writer<RawEvent> eventWriter) {
        return new EventRecorder(eventReader, eventWriter);
    }

}
