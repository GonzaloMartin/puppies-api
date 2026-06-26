package com.gudboy.domain.seguimiento.adapter;

/** Adapter real para la API de Meta/WhatsApp (simulado para el TP). */
public class MetaWhatsAppAdapter implements IWhatsAppAdapter {
    @Override
    public void enviarWhatsApp(String numero, String mensaje) {
        System.out.println("[Meta WhatsApp] → " + numero + ": " + mensaje);
    }
}
