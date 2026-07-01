package com.gudboy.dto;

import java.util.Date;
import java.util.UUID;

public class TratamientoDTO {

    private final UUID tratamientoID;
    private final String tipoTratamiento;
    private final String estado;
    private final Date fechaInicio;
    private final Date fechaFin;

    public TratamientoDTO(
            UUID tratamientoID,
            String tipoTratamiento,
            String estado,
            Date fechaInicio,
            Date fechaFin) {

        this.tratamientoID = tratamientoID;
        this.tipoTratamiento = tipoTratamiento;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public UUID getTratamientoID() {
        return tratamientoID;
    }

    public String getTipoTratamiento() {
        return tipoTratamiento;
    }

    public String getEstado() {
        return estado;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }
}