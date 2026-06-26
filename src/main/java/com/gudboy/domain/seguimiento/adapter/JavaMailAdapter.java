package com.gudboy.domain.seguimiento.adapter;

/** Adapter real para JavaMail (simulado para el TP). */
public class JavaMailAdapter implements IEmailAdapter {
    @Override
    public void enviarEmail(String destinatario, String asunto, String cuerpo) {
        System.out.println("[JavaMail] Email → " + destinatario + " | Asunto: " + asunto);
    }
}
