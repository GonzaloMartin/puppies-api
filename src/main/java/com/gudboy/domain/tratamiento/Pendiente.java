package com.gudboy.domain.tratamiento;

import java.util.Date;

public class Pendiente extends EstadoTratamiento {

    public Pendiente(Tratamiento tratamiento) {
        super(tratamiento);
    }

    @Override
    public void aplicar() {
        tratamiento.setFechaInicio(new Date());
        tratamiento.setEstado(new EnCurso(tratamiento));
    }

    @Override
    public void finalizar() {
        throw new IllegalStateException("No se puede finalizar.");
    }

    @Override
    public void cancelar() {
        tratamiento.setEstado(new Cancelado(tratamiento));
    }
}
