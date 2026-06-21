package com.gudboy.repository;

import com.gudboy.domain.Usuario.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositoryEnMemoria implements IUsuarioRepository {

    private final List<Usuario> usuarios = new ArrayList<>();

    @Override
    public void guardar(Usuario usuario) {
        usuarios.add(usuario);
    }

    @Override
    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios);
    }
}
