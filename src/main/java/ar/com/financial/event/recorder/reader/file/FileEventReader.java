package ar.com.financial.event.recorder.reader.file;

import ar.com.financial.event.recorder.domain.RawEvent;
import ar.com.financial.event.recorder.parser.EventParser;
import ar.com.financial.event.recorder.reader.Reader;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class FileEventReader implements Reader<RawEvent> {

    private FileDataReader file;
    private EventParser parser;

    public FileEventReader(final String fileName) {
        Validate.notBlank(fileName, "The file name cannot be blank");
        this.file = new FileDataReader(fileName);
        this.parser = new EventParser();
    }

    @Override
    public void open() {
        file.open();
    }

    @Override
    public void close() {
        file.close();
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
