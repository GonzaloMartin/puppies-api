package com.gudboy.domain.seguimiento.model;

import com.gudboy.dto.EncuestaDTO;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Column;

@Embeddable
public class Encuesta {
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_general_animal")
    private CalificacionEnum estadoGeneralAnimal;

    @Enumerated(EnumType.STRING)
    @Column(name = "limpieza_lugar")
    private CalificacionEnum limpiezaLugar;

    @Enumerated(EnumType.STRING)
    @Column(name = "ambiente")
    private CalificacionEnum ambiente;

    protected Encuesta() {}

    public Encuesta(CalificacionEnum estadoGeneralAnimal, CalificacionEnum limpiezaLugar, CalificacionEnum ambiente) {
        this.estadoGeneralAnimal = estadoGeneralAnimal;
        this.limpiezaLugar = limpiezaLugar;
        this.ambiente = ambiente;
    }

    public boolean esFavorable() {
        int buenas = 0;
        if (estadoGeneralAnimal == CalificacionEnum.BUENO) buenas++;
        if (limpiezaLugar == CalificacionEnum.BUENO) buenas++;
        if (ambiente == CalificacionEnum.BUENO) buenas++;
        return buenas >= 2;
    }

    public EncuestaDTO toDTO() {
        return new EncuestaDTO(estadoGeneralAnimal, limpiezaLugar, ambiente);
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

