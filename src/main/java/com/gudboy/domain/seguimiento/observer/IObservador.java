package com.gudboy.domain.seguimiento.observer;

import com.gudboy.domain.seguimiento.model.Visita;

public interface IObservador {
    void enviarRecordatorio(Visita visita);
}
