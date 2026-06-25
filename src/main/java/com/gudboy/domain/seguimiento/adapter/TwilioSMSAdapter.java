package com.gudboy.domain.seguimiento.adapter;

@SuppressWarnings("unused")
public class TwilioSMSAdapter implements ISMSAdapter {
    @Override
    public void enviarSMS(String numero, String texto) {
        if ("true".equalsIgnoreCase(System.getProperty("verbose", "false"))) {
            System.out.println("[Twilio SMS] Enviando mensaje a " + numero + ": " + texto);
        }
    }
}
