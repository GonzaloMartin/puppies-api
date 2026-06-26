package com.gudboy.controller;

import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.historialClinico.HistorialClinico;
import com.gudboy.service.HistorialClinicoService;

import java.util.UUID;

public class HistorialClinicoController {

    private HistorialClinicoService historialClinicoService;

    public HistorialClinicoController(HistorialClinicoService historialClinicoService){
        this.historialClinicoService = historialClinicoService;
    }

    public HistorialClinico crearHistorial(Animal animal){
        return historialClinicoService.crearHistorial(animal);
    }

    public HistorialClinico obtenerHistorial(UUID historialId){
        return historialClinicoService.obtenerHistorial(historialId);
    }
}
