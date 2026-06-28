package com.gudboy.dto;

import com.gudboy.domain.seguimiento.model.CalificacionEnum;

public class EncuestaDTO {
    private final CalificacionEnum estadoGeneralAnimal;
    private final CalificacionEnum limpiezaLugar;
    private final CalificacionEnum ambiente;

    public EncuestaDTO(CalificacionEnum estadoGeneralAnimal, CalificacionEnum limpiezaLugar, CalificacionEnum ambiente) {
        this.estadoGeneralAnimal = estadoGeneralAnimal;
        this.limpiezaLugar = limpiezaLugar;
        this.ambiente = ambiente;
    }

    public CalificacionEnum getEstadoGeneralAnimal() {
        return estadoGeneralAnimal;
    }

    public CalificacionEnum getLimpiezaLugar() {
        return limpiezaLugar;
    }

    public CalificacionEnum getAmbiente() {
        return ambiente;
    }
}
