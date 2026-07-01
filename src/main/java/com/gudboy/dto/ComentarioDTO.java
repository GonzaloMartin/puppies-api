package com.gudboy.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ComentarioMedicoDTO {

    private final UUID comentarioID;
    private final UUID veterinarioID;
    private final String casillaComentario;
    private final LocalDateTime fecha;

    public ComentarioMedicoDTO(
            UUID comentarioID,
            UUID veterinarioID,
            String casillaComentario,
            LocalDateTime fecha) {

        this.comentarioID = comentarioID;
        this.veterinarioID = veterinarioID;
        this.casillaComentario = casillaComentario;
        this.fecha = fecha;
    }

    public UUID getComentarioID() {
        return comentarioID;
    }

    public UUID getVeterinarioID() {
        return veterinarioID;
    }

    public String getCasillaComentario() {
        return casillaComentario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}