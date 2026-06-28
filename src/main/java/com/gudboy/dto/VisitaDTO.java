package com.gudboy.dto;

import java.time.LocalDate;
import java.util.UUID;

public class VisitaDTO {
    private final UUID id;
    private final UUID seguimientoId;
    private final LocalDate fechaProgramada;
    private final LocalDate fechaReal;
    private final String comentarios;
    private final boolean completada;
    private final boolean continuarVisitas;
    private final EncuestaDTO encuesta;

    public VisitaDTO(UUID id, UUID seguimientoId, LocalDate fechaProgramada, LocalDate fechaReal,
                     String comentarios, boolean completada, boolean continuarVisitas, EncuestaDTO encuesta) {
        this.id = id;
        this.seguimientoId = seguimientoId;
        this.fechaProgramada = fechaProgramada;
        this.fechaReal = fechaReal;
        this.comentarios = comentarios;
        this.completada = completada;
        this.continuarVisitas = continuarVisitas;
        this.encuesta = encuesta;
    }

    public UUID getId() {
        return id;
    }

    public UUID getSeguimientoId() {
        return seguimientoId;
    }

    public LocalDate getFechaProgramada() {
        return fechaProgramada;
    }

    public LocalDate getFechaReal() {
        return fechaReal;
    }

    public String getComentarios() {
        return comentarios;
    }

    public boolean isCompletada() {
        return completada;
    }

    public boolean isContinuarVisitas() {
        return continuarVisitas;
    }

    public EncuestaDTO getEncuesta() {
        return encuesta;
    }
}
