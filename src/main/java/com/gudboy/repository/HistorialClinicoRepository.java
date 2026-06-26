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
    public void actualizar(UUID id) {

    }

    @Override
    public void eliminar(UUID id) {

    }

    @Override
    public HistorialClinico buscarPorId(UUID id) {
        return null;
    }

    @Override
    public ArrayList<HistorialClinico> listarTodos() {
        return null;
    }
}
