package com.gudboy.domain.seguimiento.adapter;

public class JavaMailAdapter implements IEmailAdapter {
    @Override
    public void enviarEmail(String destinatario, String asunto, String cuerpo) {
        if ("true".equalsIgnoreCase(System.getProperty("verbose", "false"))) {
            System.out.println("[JavaMail] Enviando email a " + destinatario + " | Asunto: " + asunto + " | Cuerpo: " + cuerpo);
        }
    }
}
