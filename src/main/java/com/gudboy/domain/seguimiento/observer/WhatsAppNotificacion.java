package com.gudboy.domain.seguimiento.observer;

import com.gudboy.domain.seguimiento.adapter.IWhatsAppAdapter;
import com.gudboy.domain.seguimiento.model.Visita;

public class WhatsAppNotificacion implements IObservador {
    private final IWhatsAppAdapter whatsappAdapter;

    public WhatsAppNotificacion(IWhatsAppAdapter whatsappAdapter) {
        this.whatsappAdapter = whatsappAdapter;
    }

    @Override
    public void enviarRecordatorio(Visita visita) {
        String nombresMascotas = visita.getSeguimiento().getAdopcion().getAnimales().stream()
            .map(com.gudboy.domain.animal.model.AnimalDomestico::getNombre)
            .collect(java.util.stream.Collectors.joining(", "));
        com.gudboy.infrastructure.ActividadRegistry.publicar("[Observer WhatsApp] Activado para visita programada el " + visita.getFechaProgramada() + " (Mascotas: " + nombresMascotas + ").");
        String telefono = visita.getSeguimiento().getAdopcion().getAdoptante().getTelefono();
        String mensaje = "Recordatorio de Visita Gud Boy: Programada para el " + visita.getFechaProgramada() + ".";
        whatsappAdapter.enviarWhatsApp(telefono, mensaje);
    }
}
