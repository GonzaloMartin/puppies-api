package com.gudboy.repository;

import com.gudboy.domain.animal.model.Adopcion;

import java.util.ArrayList;
import java.util.List;

public class AdopcionRepositoryEnMemoria implements IAdopcionRepository {

    private final List<Adopcion> adopciones = new ArrayList<>();

    @Override
    public void guardar(Adopcion adopcion) {
        adopciones.add(adopcion);
    }

    @Override
    public void actualizar(Adopcion adopcion) {
        if (!adopciones.contains(adopcion)) {
            adopciones.add(adopcion);
        }
    }

    @Override
    public List<Adopcion> listarTodos() {
        return new ArrayList<>(adopciones);
    }
}
