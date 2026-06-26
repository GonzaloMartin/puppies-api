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
        // FIX: permite finalizar directamente desde pendiente (sin pasar por EnCurso)
        tratamiento.setFechaInicio(new Date());
        tratamiento.setFechaFin(new Date());
        tratamiento.setEstado(new Finalizado(tratamiento));
    }

    @Override
    public void cancelar() {
        tratamiento.setEstado(new Cancelado(tratamiento));
    }
}
