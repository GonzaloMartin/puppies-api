package com.gudboy.repository;

import com.gudboy.domain.tratamiento.Tratamiento;

import java.util.ArrayList;
import java.util.UUID;

public interface IRepositoryHistTratComen<T> {
    void guardar(UUID id);
    void actualizar(UUID id);
    void eliminar(UUID id); //ID de el objeto correspondiente. Aplica para todos.
    T buscarPorId(UUID id);
    ArrayList<T> listarTodos();
}
