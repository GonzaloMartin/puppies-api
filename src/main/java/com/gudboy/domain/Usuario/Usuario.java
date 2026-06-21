package com.gudboy.domain.Usuario;

public class Usuario {
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;

    public Usuario(String nombre, String apellido, String email, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }
    public String getEmail() {
        return email;
    }
    public String getTelefono() {
        return telefono;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido;
    }
}
