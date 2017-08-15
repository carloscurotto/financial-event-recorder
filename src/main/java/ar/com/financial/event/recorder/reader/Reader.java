package ar.com.financial.event.recorder.reader;

public interface Reader<T> {

    void open();

    void close();

    boolean hasNext();

    T read();

}
