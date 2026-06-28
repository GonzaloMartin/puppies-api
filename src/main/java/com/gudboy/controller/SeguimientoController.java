package com.gudboy.controller;

import com.gudboy.domain.seguimiento.model.*;
import com.gudboy.domain.seguimiento.service.ServicioRecordatorios;
import com.gudboy.dto.AdopcionDTO;
import com.gudboy.dto.UsuarioDTO;
import com.gudboy.dto.SeguimientoDTO;
import com.gudboy.dto.EncuestaDTO;
import com.gudboy.service.SeguimientoService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class SeguimientoController {

    private final SeguimientoService seguimientoService;

    public SeguimientoController(SeguimientoService seguimientoService) {
        this.seguimientoService = seguimientoService;
    }

    public void evaluarTodosLosRecordatorios(ServicioRecordatorios recordatorios) {
        seguimientoService.evaluarTodosLosRecordatorios(recordatorios);
    }

    public SeguimientoDTO crearSeguimiento(AdopcionDTO adopcion, UsuarioDTO responsable, DiaSemana diaSemana, String horarioDesde, String horarioHasta, PreferenciaRecordatorio preferenciaRecordatorio, int cantVisitasIniciales) {
        Seguimiento s = seguimientoService.crearSeguimiento(adopcion, responsable, diaSemana, horarioDesde, horarioHasta, preferenciaRecordatorio, cantVisitasIniciales);
        return s != null ? s.toDTO() : null;
    }

    public void registrarResultadoVisita(UUID visitaId, EncuestaDTO encuesta, String comentarios, boolean continuarVisitas) {
        seguimientoService.registrarResultadoVisita(visitaId, encuesta, comentarios, continuarVisitas);
    }

    public void finalizarSeguimiento(UUID id) {
        seguimientoService.finalizarSeguimiento(id);
    }

    public boolean estaActivo(UUID id) {
        return seguimientoService.estaActivo(id);
    }

    public Optional<SeguimientoDTO> getById(UUID id) {
        return seguimientoService.getById(id).map(Seguimiento::toDTO);
    }

    public List<SeguimientoDTO> listarTodos() {
        return seguimientoService.listarTodos().stream().map(Seguimiento::toDTO).toList();
    }
}
