package com.gudboy.domain.Usuario;

public class Veterinario extends Usuario {
    private int matriculaProfesional;
    private String especialidad;

    public Veterinario(String nombre, String apellido, String email, String telefono, int matriculaProfesional, String especialidad) {
        super(nombre, apellido, email, telefono);
        this.matriculaProfesional = matriculaProfesional;
        this.especialidad = especialidad;
    }
    
    public int getMatriculaProfesional() {
        return matriculaProfesional;
    }
    public String getEspecialidad() {
        return especialidad;
    }
}
