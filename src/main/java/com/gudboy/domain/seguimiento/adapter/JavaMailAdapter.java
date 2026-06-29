package com.gudboy.domain.seguimiento.adapter;

import com.gudboy.infrastructure.ActividadRegistry;

/** Adapter real para JavaMail (simulado para el TP). */
public class JavaMailAdapter implements IEmailAdapter {
    @Override
    public void enviarEmail(String destinatario, String asunto, String cuerpo) {
        String msg = "[Adapter JavaMail] Email → " + destinatario + " | Asunto: " + asunto;
        ActividadRegistry.publicar(msg);
    }
}

