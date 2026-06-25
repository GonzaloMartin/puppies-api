package com.gudboy.controller;

import com.gudboy.domain.tratamiento.TipoTratamiento;
import com.gudboy.domain.tratamiento.Tratamiento;
import com.gudboy.service.ComentarioServices;
import com.gudboy.service.HistorialClinicoService;
import com.gudboy.service.TratamientoService;

import java.util.UUID;

public class GestorSaludTratamientoController {
    private final UUID idController;
    private TratamientoService tratamientoService;
    private HistorialClinicoService historialClinicoService;
    private ComentarioServices comentarioServices;

    public GestorSaludTratamientoController(TratamientoService tS,
                                            HistorialClinicoService hCS,
                                            ComentarioServices cS) {
        this.idController = UUID.randomUUID();
        this.tratamientoService = tS;
        this.historialClinicoService = hCS;
        this.comentarioServices = cS;
    }

    public Tratamiento registrarTratamiento(UUID animalId, TipoTratamiento tipo){

        tratamientoService.registrarTratamiento(animalId, tipo);

        return null;
    }




}
