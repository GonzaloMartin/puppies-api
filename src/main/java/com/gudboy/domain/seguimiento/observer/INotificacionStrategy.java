package com.gudboy.domain.seguimiento.observer;

import com.gudboy.domain.seguimiento.model.Visita;

/**
 * Strategy de notificación: combina IObservador con la lógica específica
 * de cada canal (SMS, WhatsApp, Email).
 */
public interface INotificacionStrategy extends IObservador {
    void enviarRecordatorio(Visita visita);
}
