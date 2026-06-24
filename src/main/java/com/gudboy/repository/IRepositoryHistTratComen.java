package com.gudboy.repository;

import java.util.List;
import java.util.UUID;

public interface IRepositoryHistTratComen<T> {
    void guardar(T entidad);
    void actualizar(T entidad);
    void eliminar(T entidad);
    T buscarPorId(UUID id);
    List<T> listarTodos();
}
