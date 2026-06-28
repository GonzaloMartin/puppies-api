package com.gudboy.domain.seguimiento.observer;

import com.gudboy.domain.seguimiento.adapter.IEmailAdapter;
import com.gudboy.domain.seguimiento.model.Visita;

public class EmailNotificacion implements IObservador {
    private final IEmailAdapter emailAdapter;

    public EmailNotificacion(IEmailAdapter emailAdapter) {
        this.emailAdapter = emailAdapter;
    }

    @Override
    public void enviarRecordatorio(Visita visita) {
        String email = visita.getSeguimiento().getAdopcion().getAdoptante().getEmail();
        String asunto = "Recordatorio de Visita - Gud Boy";
        String mensaje = "Recordatorio de Visita Gud Boy: Programada para el " + visita.getFechaProgramada() + ".";
        emailAdapter.enviarEmail(email, asunto, mensaje);
    }
}
