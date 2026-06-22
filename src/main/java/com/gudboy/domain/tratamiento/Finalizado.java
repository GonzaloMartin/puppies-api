package com.gudboy.domain.tratamiento;

public class Finalizado extends EstadoTratamiento {

    public Finalizado(Tratamiento tratamiento) {
        super(tratamiento);
    }

    @Override
    public void aplicar() {
        throw new IllegalStateException("El tratamiento ya fue finalizado.");
    }

    @Override
    public void finalizar() {
        throw new IllegalStateException("El tratamiento ya está finalizado.");
    }

    @Override
    public void cancelar() {
        throw new IllegalStateException("No se puede cancelar un tratamiento finalizado.");
    }
}
