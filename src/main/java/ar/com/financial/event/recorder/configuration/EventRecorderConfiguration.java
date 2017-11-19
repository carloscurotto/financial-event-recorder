package ar.com.financial.event.recorder.configuration;

import ar.com.financial.event.recorder.AsyncEventRecorder;
import ar.com.financial.event.recorder.EventSynchronizer;
import ar.com.financial.event.recorder.domain.RawEvent;
import ar.com.financial.event.recorder.reader.Reader;
import ar.com.financial.event.recorder.reader.file.FileEventReader;
import ar.com.financial.event.recorder.reader.snmp.SNMPEventReader;
import ar.com.financial.event.recorder.writer.MultipleEventWriter;
import ar.com.financial.event.recorder.writer.Writer;
import ar.com.financial.event.recorder.writer.console.ConsoleSimpleEventWriter;
import ar.com.financial.event.recorder.writer.console.ConsoleSummaryEventWriter;
import ar.com.financial.event.recorder.writer.database.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

@Configuration
@EnableTransactionManagement
public class EventRecorderConfiguration {

    private static final String CONSOLE_WRITER_TYPE = "console";
    private static final String DATABASE_WRITER_TYPE = "database";
    private static final String SYNCHRONIZER_EVENT_LOG = "config/event-log.txt";

    @Value("${snmp.host}")
    private String snmpHost;

    @Value("${snmp.port}")
    private String snmpPort;

    @Value("#{'${event.codes}'.split(',')}")
    private List<String> eventCodes;

    @Value("${writer:database}")
    private String writerType;

    @Autowired
    private SimpleEventRepository simpleEventRepository;

    @Autowired
    private SummaryEventRepository summaryEventRepository;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public EventSynchronizer createEventSynchronizer(@Qualifier("synchronizer-event-reader") final Reader<RawEvent> eventReader,
                                                     @Qualifier("synchronizer-event-writer") final Writer<RawEvent> eventWriter) {
        return new EventSynchronizer(eventReader, eventWriter);
    }

    @Qualifier("synchronizer-event-reader")
    @Bean
    public Reader<RawEvent> createSynchronizerEventReader() {
        return new FileEventReader(SYNCHRONIZER_EVENT_LOG, eventCodes);
    }

    @Qualifier("synchronizer-event-writer")
    @Bean
    public Writer<RawEvent> createSynchronizerEventWriter() {
        return createEventWriter();
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public AsyncEventRecorder createAsyncEventRecorder(@Qualifier("recorder-event-reader") final Reader<RawEvent> eventReader,
                                                       @Qualifier("recorder-event-writer") final Writer<RawEvent> eventWriter) {
        return new AsyncEventRecorder(eventReader, eventWriter);
    }

    @Qualifier("recorder-event-reader")
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Reader<RawEvent> createRecorderEventReader() {
        return new FileEventReader(SYNCHRONIZER_EVENT_LOG, eventCodes);
        //return new SNMPEventReader(snmpHost, snmpPort, eventCodes);
    }

    @Qualifier("recorder-event-writer")
    @Bean
    public Writer<RawEvent> createRecorderEventWriter() {
        return createEventWriter();
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Writer<RawEvent> createEventWriter() {
        return getEventWriter(writerType);
    }

    private Writer<RawEvent> getEventWriter(final String type) {
        if (type.equalsIgnoreCase(CONSOLE_WRITER_TYPE)) {
            return new MultipleEventWriter(createConsoleSimpleEventWriter(), createConsoleSummaryEventWriter());
        } else if (type.equalsIgnoreCase(DATABASE_WRITER_TYPE)) {
            return new MultipleEventWriter(createDatabaseSimpleEventWriter(),
                    createDatabaseSummaryEventWriter(), createDatabaseSummaryValidationWriter());
        }
        throw new RuntimeException(String.format("The event writer of type [%s] is not supported", type));
    }

    private Writer<RawEvent> createDatabaseSummaryValidationWriter() {
        return new DatabaseSummaryValidationWriter(simpleEventRepository, summaryEventRepository);
    }

    private Writer<RawEvent> createConsoleSimpleEventWriter() {
        return new ConsoleSimpleEventWriter();
    }

    private Writer<RawEvent> createConsoleSummaryEventWriter() {
        return new ConsoleSummaryEventWriter();
    }

    private Writer<RawEvent> createDatabaseSimpleEventWriter() {
        return new DatabaseSimpleEventWriter(simpleEventRepository);
    }

    private Writer<RawEvent> createDatabaseSummaryEventWriter() {
        return new DatabaseSummaryEventWriter(summaryEventRepository);
    }

}