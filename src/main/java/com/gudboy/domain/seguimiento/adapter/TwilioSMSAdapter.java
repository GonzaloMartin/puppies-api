package com.gudboy.domain.seguimiento.adapter;

/** Adapter real para Twilio SMS (simulado para el TP). */
public class TwilioSMSAdapter implements ISMSAdapter {
    @Override
    public void enviarSMS(String numero, String texto) {
        System.out.println("[Twilio SMS] → " + numero + ": " + texto);
    }
}
