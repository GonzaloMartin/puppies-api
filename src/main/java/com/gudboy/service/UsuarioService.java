package com.gudboy.service;

import com.gudboy.domain.Usuario.EstadoCivil;
import com.gudboy.domain.Usuario.Ocupacion;
import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.Usuario.Visitador;
import com.gudboy.repository.IUsuarioRepository;

import java.util.List;
import java.util.stream.Collectors;

public class UsuarioService {

    private final IUsuarioRepository usuarioRepository;

    public UsuarioService(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Visitador registrarVisitador(String nombre, String apellido, String email, String telefono,
                                         EstadoCivil estadoCivil, Ocupacion ocupacion) {
        Visitador visitador = new Visitador(nombre, apellido, email, telefono, estadoCivil, ocupacion);
        usuarioRepository.guardar(visitador);
        return visitador;
    }

    public Veterinario registrarVeterinario(String nombre, String apellido, String email, String telefono,
                                             int matriculaProfesional, String especialidad) {
        Veterinario veterinario = new Veterinario(nombre, apellido, email, telefono, matriculaProfesional, especialidad);
        usuarioRepository.guardar(veterinario);
        return veterinario;
    }

    public List<Visitador> listarVisitadores() {
        return usuarioRepository.listarTodos().stream()
                .filter(Visitador.class::isInstance)
                .map(Visitador.class::cast)
                .collect(Collectors.toList());
    }

    public List<Veterinario> listarVeterinarios() {
        return usuarioRepository.listarTodos().stream()
                .filter(Veterinario.class::isInstance)
                .map(Veterinario.class::cast)
                .collect(Collectors.toList());
    }
}
