package com.gudboy.domain.tratamiento;

public class Cancelado extends EstadoTratamiento {

    public Cancelado(Tratamiento tratamiento) {
        super(tratamiento);
    }

    @Override
    public void aplicar() {
        throw new IllegalStateException("El tratamiento ya fue cancelado.");
    }

    @Override
    public void finalizar() {
        throw new IllegalStateException("No se puede finalizar un tratamiento finalizado.");
    }

    @Override
    public void cancelar() {
        throw new IllegalStateException("El tratamiento ya está cancelado.");
    }
}
