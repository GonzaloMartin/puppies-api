package com.gudboy.domain.seguimiento.observer;

import com.gudboy.domain.seguimiento.model.Visita;

public interface INotificacionStrategy extends IObservador {
    void enviarRecordatorio(Visita visita);
}
