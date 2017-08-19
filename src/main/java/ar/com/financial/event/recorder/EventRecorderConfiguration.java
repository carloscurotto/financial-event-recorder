package ar.com.financial.event.recorder;

import ar.com.financial.event.recorder.domain.Event;
import ar.com.financial.event.recorder.reader.Reader;
import ar.com.financial.event.recorder.reader.file.FileEventReader;
import ar.com.financial.event.recorder.writer.Writer;
import ar.com.financial.event.recorder.writer.console.ConsoleEventWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventRecorderConfiguration {

    @Bean
    @Qualifier("reader")
    public Reader<Event> createEventReader() {
        return new FileEventReader("config/event-log.txt");
    }

    @Bean
    @Qualifier("writer")
    public Writer<Event> createEventWriter() {
        return new ConsoleEventWriter();
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public EventRecorder createEventRecorder(@Qualifier("reader") final Reader<Event> eventReader,
                                             @Qualifier("writer") final Writer<Event> eventWriter) {
        return new EventRecorder(eventReader, eventWriter);
    }

}
