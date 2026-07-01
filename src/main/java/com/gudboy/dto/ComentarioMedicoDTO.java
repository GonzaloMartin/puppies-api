package com.gudboy.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ComentarioMedicoDTO {

    private final UUID comentarioID;
    private final String veterinarioEmail; // ✔ ID real en tu sistema
    private final String casillaComentario;
    private final LocalDateTime fecha;

    public ComentarioMedicoDTO(
            UUID comentarioID,
            String veterinarioEmail,
            String casillaComentario,
            LocalDateTime fecha) {

        this.comentarioID = comentarioID;
        this.veterinarioEmail = veterinarioEmail;
        this.casillaComentario = casillaComentario;
        this.fecha = fecha;
    }

    public UUID getComentarioID() { return comentarioID; }
    public String getVeterinarioEmail() { return veterinarioEmail; }
    public String getCasillaComentario() { return casillaComentario; }
    public LocalDateTime getFecha() { return fecha; }

    public static ComentarioMedicoDTO desde(com.gudboy.domain.comentarioMedico.ComentarioMedico c) {
        return new ComentarioMedicoDTO(
                c.getComentarioID(),
                c.getVeterinario() != null ? c.getVeterinario().getEmail() : null,
                c.getCasillaComentario(),
                c.getFecha()
        );
    }
}