package com.gudboy.repository;

import com.gudboy.domain.historialClinico.HistorialClinico;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HistorialClinicoRepository implements IRepositoryHistTratComen<HistorialClinico>{

    @Override
    public void guardar(HistorialClinico entidad) {

    }

    @Override
    public void actualizar(HistorialClinico entidad) {

    }

    @Override
    public void eliminar(HistorialClinico entidad) {

    }

    @Override
    public HistorialClinico buscarPorId(UUID id) {
        return null;
    }

    @Override
    public ArrayList<HistorialClinico> listarTodos() {
        return null;
    }

    public HistorialClinico buscarPorAnimal(UUID animalId){
        return null;
    }
}
