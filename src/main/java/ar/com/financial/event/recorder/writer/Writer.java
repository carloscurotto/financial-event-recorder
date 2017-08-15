package ar.com.financial.event.recorder.writer;

public interface Writer<T> {

    void open();

    void close();

    void write(T data);

}
