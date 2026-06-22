package com.gudboy.repository;

import com.gudboy.domain.Usuario.Usuario;
import java.util.List;

public interface IUsuarioRepository {
    void guardar(Usuario usuario);
    List<Usuario> listarTodos();
}
