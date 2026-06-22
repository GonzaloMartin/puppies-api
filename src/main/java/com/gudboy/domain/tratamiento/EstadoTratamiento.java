package com.gudboy.domain.tratamiento;

public abstract class EstadoTratamiento {
    protected Tratamiento tratamiento;

    public EstadoTratamiento(Tratamiento tratamiento){
        this.tratamiento = tratamiento;
    }

    public abstract void aplicar();

    public abstract void finalizar();

    public abstract void cancelar();
}
