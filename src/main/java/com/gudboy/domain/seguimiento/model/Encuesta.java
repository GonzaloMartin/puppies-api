package com.gudboy.domain.seguimiento.model;

public class Encuesta {
    private final CalificacionEnum estadoGeneralAnimal;
    private final CalificacionEnum limpiezaLugar;
    private final CalificacionEnum ambiente;

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
