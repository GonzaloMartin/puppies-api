package com.gudboy.domain.Usuario;

public class Visitador extends Usuario {
    private Ocupacion  ocupacion;
    private EstadoCivil estadoCivil;
    private boolean    otrasMascotas;
    private String     motivoAdopcion;
    private String     animalesInteres;

    public Visitador(String nombre, String apellido, String email, String telefono,
                     EstadoCivil estadoCivil, Ocupacion ocupacion,
                     String motivoAdopcion, String animalesInteres, boolean otrasMascotas) {
        super(nombre, apellido, email, telefono);
        this.estadoCivil    = estadoCivil;
        this.ocupacion      = ocupacion;
        this.motivoAdopcion = motivoAdopcion;
        this.animalesInteres= animalesInteres;
        this.otrasMascotas  = otrasMascotas;
    }

    public Ocupacion   getOcupacion()       { return ocupacion; }
    public EstadoCivil getEstadoCivil()     { return estadoCivil; }
    public boolean     tieneOtrasMascotas() { return otrasMascotas; }
    public String      getMotivoAdopcion()  { return motivoAdopcion; }
    public String      getAnimalesInteres() { return animalesInteres; }

    @Override
    public String toString() {
        return String.format("%s %s <%s> — %s | Interés: %s",
            getNombre(), getApellido(), getEmail(), ocupacion, animalesInteres);
    }
}
