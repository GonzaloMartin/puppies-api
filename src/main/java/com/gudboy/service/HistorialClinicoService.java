package com.gudboy.service;

import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.historialClinico.HistorialClinico;
import com.gudboy.repository.HistorialClinicoRepository;

import java.util.UUID;

public class HistorialClinicoService {
    private HistorialClinicoRepository histRepo;

    public HistorialClinicoService(HistorialClinicoRepository histRepo){
        this.histRepo = histRepo;
    }

    public HistorialClinico obtenerHistorial(UUID historialId){

        HistorialClinico historial = histRepo.buscarPorId(historialId);

        return historial;
    }

    public HistorialClinico crearHistorial(Animal animal){
        HistorialClinico historial = new HistorialClinico(animal);
        histRepo.guardar(historial);

        return historial;
    }

}
