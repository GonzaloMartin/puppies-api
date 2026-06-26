package com.gudboy.repository;

import com.gudboy.domain.animal.model.Adopcion;
import java.util.List;
import java.util.Optional;

public interface IAdopcionRepository {
    void guardar(Adopcion adopcion);
    void actualizar(Adopcion adopcion);
    List<Adopcion> listarTodos();
    Optional<Adopcion> buscarPorId(int id);
}
