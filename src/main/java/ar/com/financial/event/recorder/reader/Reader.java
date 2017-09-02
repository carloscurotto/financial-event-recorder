package ar.com.financial.event.recorder.reader;

public interface Reader<T> {

    void start();

    void stop();

    boolean hasNext();

    T read();

}
