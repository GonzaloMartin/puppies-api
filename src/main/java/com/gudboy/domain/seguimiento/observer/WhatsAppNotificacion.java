package com.gudboy.domain.seguimiento.observer;

import com.gudboy.domain.seguimiento.adapter.IWhatsAppAdapter;
import com.gudboy.domain.seguimiento.model.Visita;

public class WhatsAppNotificacion implements INotificacionStrategy {
    private final IWhatsAppAdapter whatsappAdapter;

    public WhatsAppNotificacion(IWhatsAppAdapter whatsappAdapter) {
        this.whatsappAdapter = whatsappAdapter;
    }

    @Override
    public void update(Visita visita) {
        enviarRecordatorio(visita);
    }

    @Override
    public void enviarRecordatorio(Visita visita) {
        String telefono = visita.getSeguimiento().getAdopcion().getAdoptante().getTelefono();
        String mensaje = "Recordatorio de Visita Gud Boy: Programada para el " + visita.getFechaProgramada() + ".";
        whatsappAdapter.enviarWhatsApp(telefono, mensaje);
    }
}
