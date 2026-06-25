package com.gudboy.domain.seguimiento.adapter;

@SuppressWarnings("unused")
public class MetaWhatsAppAdapter implements IWhatsAppAdapter {
    @Override
    public void enviarWhatsApp(String numero, String mensaje) {
        if ("true".equalsIgnoreCase(System.getProperty("verbose", "false"))) {
            System.out.println("[Meta WhatsApp] Enviando mensaje a " + numero + ": " + mensaje);
        }
    }
}
