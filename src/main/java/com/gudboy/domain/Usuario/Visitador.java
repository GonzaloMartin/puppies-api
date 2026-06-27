package com.gudboy.domain.Usuario;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;

@Entity
@DiscriminatorValue("VISITADOR")
public class Visitador extends Usuario {

    @Enumerated(value = jakarta.persistence.EnumType.STRING)
    private Ocupacion  ocupacion;

    @Enumerated(value = jakarta.persistence.EnumType.STRING)
    @Column(name = "estado_civil")
    private EstadoCivil estadoCivil;

    @Column(name = "otras_mascotas")
    private boolean    otrasMascotas;

    @Column(name = "motivo_adopcion")
    private String     motivoAdopcion;

    @Column(name = "animales_interes")
    private String     animalesInteres;

    protected Visitador() { super(); }

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
