package com.gudboy.controller;


import com.gudboy.domain.tratamiento.TipoTratamiento;
import com.gudboy.domain.tratamiento.Tratamiento;
import com.gudboy.service.TratamientoService;

import java.util.ArrayList;
import java.util.UUID;

public class TratamientoController {
    private TratamientoService tratamientoService;

    public TratamientoController(TratamientoService tratamientoService){
        this.tratamientoService = tratamientoService;
    }

    public Tratamiento registrarTratamiento(UUID animalId, TipoTratamiento tipo){
        return tratamientoService.registrarTratamiento(animalId, tipo);
    }

    public void finalizarTratamiento(UUID tratamiento){
        tratamientoService.finalizarTratamiento(tratamiento);
    }

    public void cancelarTratamiento(UUID tratamiento){
        tratamientoService.cancelarTratamiento(tratamiento);
    }

    public ArrayList<Tratamiento> buscarTratamientoPorTipo(TipoTratamiento tipo){
        return tratamientoService.buscarPorTipo(tipo);
    }

}
