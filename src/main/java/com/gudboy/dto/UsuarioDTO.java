package com.gudboy.dto;

import com.gudboy.domain.Usuario.EstadoCivil;
import com.gudboy.domain.Usuario.Ocupacion;

public class UsuarioDTO {
    private String      nombre;
    private String      apellido;
    private String      email;
    private String      telefono;

    //veterinario
    private int         matriculaProfesional;
    private String      especialidad;

    //Visitador
    private Ocupacion   ocupacion;
    private EstadoCivil estadoCivil;
    private boolean     otrasMascotas;
    private String      motivoAdopcion;
    private String      animalesInteres;

    public UsuarioDTO(String nombre, String apellido, String email, String telefono,
                      EstadoCivil estadoCivil, Ocupacion ocupacion,
                      String motivoAdopcion, String animalesInteres, boolean otrasMascotas) {
        this.nombre             = nombre;
        this.apellido           = apellido;
        this.email              = email;
        this.telefono           = telefono;
        this.estadoCivil        = estadoCivil;
        this.ocupacion          = ocupacion;
        this.motivoAdopcion     = motivoAdopcion;
        this.animalesInteres    = animalesInteres;
        this.otrasMascotas      = otrasMascotas;
    }

    public UsuarioDTO(String nombre, String apellido, String email, String telefono,
                      int matriculaProfesional, String especialidad) {
        this.nombre             = nombre;
        this.apellido           = apellido;
        this.email              = email;
        this.telefono           = telefono;
        this.matriculaProfesional= matriculaProfesional;
        this.especialidad       = especialidad;
    }

    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public int getMatriculaProfesional() { return matriculaProfesional; }
    public String getEspecialidad() { return especialidad; }
    public Ocupacion getOcupacion() { return ocupacion; }
    public EstadoCivil getEstadoCivil() { return estadoCivil; }
    public boolean isOtrasMascotas() { return otrasMascotas; }
    public String getMotivoAdopcion() { return motivoAdopcion; }
    public String getAnimalesInteres() { return animalesInteres; }
}
