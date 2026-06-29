package com.gudboy.domain.seguimiento.adapter;

import com.gudboy.infrastructure.ActividadRegistry;

/** Adapter real para Twilio SMS (simulado para el TP). */
public class TwilioSMSAdapter implements ISMSAdapter {
    @Override
    public void enviarSMS(String numero, String texto) {
        String msg = "[Adapter Twilio SMS] → " + numero + ": " + texto;
        ActividadRegistry.publicar(msg);
    }
}

