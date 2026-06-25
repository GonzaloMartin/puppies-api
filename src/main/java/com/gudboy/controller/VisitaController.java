package com.gudboy.controller;

import com.gudboy.domain.seguimiento.model.Encuesta;
import com.gudboy.domain.seguimiento.model.Visita;
import com.gudboy.service.VisitaService;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public class VisitaController {

    private final VisitaService visitaService;

    public VisitaController(VisitaService visitaService) {
        this.visitaService = visitaService;
    }

    public void registrarResultado(UUID idVisita, Encuesta encuesta, String comentarios, boolean continuarVisitas) {
        visitaService.registrarResultado(idVisita, encuesta, comentarios, continuarVisitas);
    }

    public void marcarCompletada(UUID idVisita) {
        visitaService.marcarCompletada(idVisita);
    }

    public List<Visita> listarPorSeguimiento(UUID idSeguimiento) {
        return visitaService.listarPorSeguimiento(idSeguimiento);
    }
}
