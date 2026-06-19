package com.gudboy.domain.Usuario;

public class Visitador extends Usuario {
    private Ocupacion ocupacion;
    private EstadoCivil estadoCivil;
    private boolean otrasMascotas;
    private String motivoAdopcion;
    private String animalesInteres;

    public Visitador(String nombre, String apellido, String email, String telefono, EstadoCivil estadoCivil, Ocupacion ocupacion) {
        super(nombre, apellido, email, telefono);
        this.ocupacion = ocupacion;
        this.estadoCivil = estadoCivil;
    }

    public Ocupacion getOcupacion() {
        return ocupacion;
    }

    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    public boolean tieneOtrasMascotas() {
        return otrasMascotas;
    }

    public String getMotivoAdopcion() {
        return motivoAdopcion;
    }
    public String getAnimalesInteres() {
        return animalesInteres;
    }
}
