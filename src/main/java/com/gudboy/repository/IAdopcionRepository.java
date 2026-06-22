package com.gudboy.repository;
import java.util.List;
import com.gudboy.domain.animal.model.Adopcion;

public interface IAdopcionRepository {
    void guardar(Adopcion adopcion);
    void actualizar(Adopcion adopcion);
    List<Adopcion> listarTodos();
}
