package com.gudboy.service;

import com.gudboy.domain.historialClinico.HistorialClinico;
import com.gudboy.domain.tratamiento.TipoTratamiento;
import com.gudboy.domain.tratamiento.Tratamiento;
import com.gudboy.repository.HistorialClinicoRepository;
import com.gudboy.repository.TratamientoRepository;

import java.util.ArrayList;
import java.util.UUID;

public class TratamientoService {

    private TratamientoRepository tratRepo;
    private HistorialClinicoRepository histCliRepo;

    public TratamientoService(TratamientoRepository tratRepo, HistorialClinicoRepository histCliRepo) {
        this.tratRepo = tratRepo;
        this.histCliRepo = histCliRepo;
    }

    public Tratamiento registrarTratamiento(UUID animalId, TipoTratamiento tipo) {
        HistorialClinico historialClinico = histCliRepo.buscarPorAnimal(animalId);
        Tratamiento tratamiento = new Tratamiento(tipo);

        if (historialClinico != null) {
            historialClinico.agregarTratamiento(tratamiento);
        }

        tratRepo.guardar(tratamiento);
        return tratamiento;
    }

    public void finalizarTratamiento(UUID tratamientoId) {
        Tratamiento tratamiento = tratRepo.buscarPorId(tratamientoId);
        if (tratamiento != null) {
            tratamiento.finalizarTratamiento();
        }
    }

    public void cancelarTratamiento(UUID tratamientoId) {
        Tratamiento tratamiento = tratRepo.buscarPorId(tratamientoId);
        if (tratamiento != null) {
            tratamiento.cancelarTratamiento();
        }
    }

    public ArrayList<Tratamiento> buscarPorTipo(TipoTratamiento tipoTratamiento) {
        ArrayList<Tratamiento> resultado = new ArrayList<>();
        for (Tratamiento t : tratRepo.listarTodos()) {
            if (tipoTratamiento == t.getTipoTratamientoEnum()) {
                resultado.add(t);
            }
        }
        return resultado;
    }

}
