package com.gudboy.domain.seguimiento.adapter;

public interface IEmailAdapter {
    void enviarEmail(String destinatario, String asunto, String cuerpo);
}
