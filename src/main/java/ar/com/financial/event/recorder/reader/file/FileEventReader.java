package ar.com.financial.event.recorder.reader.file;

import ar.com.financial.event.recorder.domain.RawEvent;
import ar.com.financial.event.recorder.reader.Reader;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Collection;

public class FileEventReader implements Reader<RawEvent> {

    private FileDataReader file;
    private FileEventParser parser;

    public FileEventReader(final String fileName, final Collection<String> eventCodes) {
        Validate.notBlank(fileName, "The file name cannot be blank");
        Validate.notEmpty(eventCodes, "The event codes cannot be empty");
        this.file = new FileDataReader(fileName);
        this.parser = new FileEventParser(new ArrayList<>(eventCodes));
    }

    @Override
    public void start() {
        file.start();
    }

    @Override
    public void stop() {
        file.stop();
    }

    @Override
    public boolean hasNext() {
        return file.hasNext();
    }

    @Override
    public RawEvent read() {
        String data = file.read();
        if (StringUtils.isBlank(data)) {
            return null;
        }
        return parser.parse(data);
    }

}
