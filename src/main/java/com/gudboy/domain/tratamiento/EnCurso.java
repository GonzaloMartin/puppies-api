package com.gudboy.domain.tratamiento;

import java.util.Date;

public class EnCurso extends EstadoTratamiento {

    public EnCurso(Tratamiento tratamiento) {
        super(tratamiento);
    }

    @Override
    public void aplicar() {
        throw new IllegalStateException("Ya está en curso.");
    }

    @Override
    public void finalizar() {
        tratamiento.setFechaFin(new Date());
        tratamiento.setEstado(new Finalizado(tratamiento));
    }

    @Override
    public void cancelar() {
        tratamiento.setEstado(new Cancelado(tratamiento));
    }
}
