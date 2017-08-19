package ar.com.financial.event.recorder.reader.file;

import ar.com.financial.event.recorder.reader.Reader;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.util.Scanner;
import java.util.regex.Pattern;

public class FileDataReader implements Reader<String> {

    private final String fileName;
    private Scanner scanner;

    public FileDataReader(final String fileName) {
        Validate.notBlank(fileName, "The file name cannot be blank");
        this.fileName = fileName;
    }

    public void open() {
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter(Pattern.compile("}\\|"));
            this.scanner = scanner;
        } catch (Exception e) {
            throw new RuntimeException(String.format("Error opening the file [%s].", fileName), e);
        }
    }

    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }

    @Override
    public boolean hasNext() {
        return scanner.hasNext();
    }

    public String read() {
        return scanner.next();
    }

}
