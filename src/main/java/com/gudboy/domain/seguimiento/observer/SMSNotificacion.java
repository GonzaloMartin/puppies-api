package com.gudboy.domain.seguimiento.observer;

import com.gudboy.domain.seguimiento.adapter.ISMSAdapter;
import com.gudboy.domain.seguimiento.model.Visita;

public class SMSNotificacion implements IObservador {
    private final ISMSAdapter smsAdapter;

    public SMSNotificacion(ISMSAdapter smsAdapter) {
        this.smsAdapter = smsAdapter;
    }

    @Override
    public void enviarRecordatorio(Visita visita) {
        String telefono = visita.getSeguimiento().getAdopcion().getAdoptante().getTelefono();
        String mensaje = "Recordatorio de Visita Gud Boy: Programada para el " + visita.getFechaProgramada() + ".";
        smsAdapter.enviarSMS(telefono, mensaje);
    }
}
