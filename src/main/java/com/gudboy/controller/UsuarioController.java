package com.gudboy.controller;

import com.gudboy.domain.Usuario.EstadoCivil;
import com.gudboy.domain.Usuario.Ocupacion;
import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.Usuario.Visitador;
import com.gudboy.service.UsuarioService;

import java.util.List;

public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public Visitador registrarVisitador(String nombre, String apellido, String email, String telefono,
                                         EstadoCivil estadoCivil, Ocupacion ocupacion) {
        return usuarioService.registrarVisitador(nombre, apellido, email, telefono, estadoCivil, ocupacion);
    }

    public Veterinario registrarVeterinario(String nombre, String apellido, String email, String telefono,
                                             int matriculaProfesional, String especialidad) {
        return usuarioService.registrarVeterinario(nombre, apellido, email, telefono, matriculaProfesional, especialidad);
    }

    public List<Visitador> listarVisitadores() {
        return usuarioService.listarVisitadores();
    }

    public List<Veterinario> listarVeterinarios() {
        return usuarioService.listarVeterinarios();
    }
}
