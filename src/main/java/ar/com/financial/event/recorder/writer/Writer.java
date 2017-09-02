package ar.com.financial.event.recorder.writer;

public interface Writer<T> {

    void start();

    void stop();

    void write(T data);

}
