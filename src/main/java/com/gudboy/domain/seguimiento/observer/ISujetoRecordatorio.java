package com.gudboy.domain.seguimiento.observer;

public interface ISujetoRecordatorio {
    void suscribir(IObservador o);
    void desuscribir(IObservador o);
    void notificarRecordatorio();
}
