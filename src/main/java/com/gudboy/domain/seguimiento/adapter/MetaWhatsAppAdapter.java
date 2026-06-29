package com.gudboy.domain.seguimiento.adapter;

import com.gudboy.infrastructure.ActividadRegistry;

/** Adapter real para la API de Meta/WhatsApp (simulado para el TP). */
public class MetaWhatsAppAdapter implements IWhatsAppAdapter {
    @Override
    public void enviarWhatsApp(String numero, String mensaje) {
        String msg = "[Adapter Meta WhatsApp] → " + numero + ": " + mensaje;
        ActividadRegistry.publicar(msg);
    }
}
