package com.gudboy.repository;

import com.gudboy.domain.comentarioMedico.ComentarioMedico;
import com.gudboy.domain.historialClinico.HistorialClinico;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ComentarioMedicoRepository implements IRepositoryHistTratComen<ComentarioMedico>{

    @Override
    public void guardar(ComentarioMedico entidad) {

    }

    @Override
    public void actualizar(ComentarioMedico entidad) {

    }

    @Override
    public void eliminar(ComentarioMedico entidad) {

    }

    @Override
    public ComentarioMedico buscarPorId(UUID id) {
        return null;
    }

    @Override
    public ArrayList<ComentarioMedico> listarTodos() {
        return null;
    }

    public ArrayList<ComentarioMedico> listarPorHistorial(HistorialClinico historialClinico){
        return null;
    }
}
