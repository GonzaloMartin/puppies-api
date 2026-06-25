package com.gudboy.controller;

import com.gudboy.domain.Usuario.Usuario;
import com.gudboy.domain.animal.model.Adopcion;
import com.gudboy.domain.seguimiento.model.*;
import com.gudboy.service.SeguimientoService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SeguimientoController {

    private final SeguimientoService seguimientoService;

    public SeguimientoController(SeguimientoService seguimientoService) {
        this.seguimientoService = seguimientoService;
    }

    public Seguimiento crearSeguimiento(Adopcion adopcion, Usuario responsable, DiaSemana diaSemana, String horarioDesde, String horarioHasta, PreferenciaRecordatorio preferenciaRecordatorio, int cantVisitasIniciales) {
        return seguimientoService.crearSeguimiento(adopcion, responsable, diaSemana, horarioDesde, horarioHasta, preferenciaRecordatorio, cantVisitasIniciales);
    }

    public void registrarResultadoVisita(UUID visitaId, Encuesta encuesta, String comentarios, boolean continuarVisitas) {
        seguimientoService.registrarResultadoVisita(visitaId, encuesta, comentarios, continuarVisitas);
    }

    public void finalizarSeguimiento(UUID id) {
        seguimientoService.finalizarSeguimiento(id);
    }

    public boolean estaActivo(UUID id) {
        return seguimientoService.estaActivo(id);
    }

    public Optional<Seguimiento> getById(UUID id) {
        return seguimientoService.getById(id);
    }

    public List<Seguimiento> listarTodos() {
        return seguimientoService.listarTodos();
    }
}
