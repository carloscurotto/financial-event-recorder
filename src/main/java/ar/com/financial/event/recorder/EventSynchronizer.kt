package ar.com.financial.event.recorder

import ar.com.financial.event.recorder.domain.RawEvent
import ar.com.financial.event.recorder.reader.Reader
import ar.com.financial.event.recorder.writer.Writer
import org.apache.log4j.Logger

class EventSynchronizer(
        private val eventReader: Reader<RawEvent>,
        private val eventWriter: Writer<RawEvent>
) {

    private val logger = Logger.getLogger(EventSynchronizer::class.java)

    fun synchronize() {
        logger.info("Synchronizing events...")
        eventReader.start()
        while (eventReader.hasNext()) {
            eventReader.read()?.let { eventWriter.write(it) }
        }
        eventReader.stop()
        logger.info("Events synchronized.")
    }

}
