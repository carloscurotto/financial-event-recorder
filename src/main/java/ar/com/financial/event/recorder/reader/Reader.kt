package ar.com.financial.event.recorder.reader

interface Reader<T> {

    fun start()

    fun stop()

    fun hasNext(): Boolean

    fun read(): T?

}
