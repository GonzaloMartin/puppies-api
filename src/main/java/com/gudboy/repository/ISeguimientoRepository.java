package com.gudboy.repository;

import com.gudboy.domain.seguimiento.model.Seguimiento;
import com.gudboy.domain.seguimiento.model.Visita;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ISeguimientoRepository {
    void guardar(Seguimiento seguimiento);
    void actualizar(Seguimiento seguimiento);
    Optional<Seguimiento> buscarPorId(UUID id);
    List<Seguimiento> listarTodos();
    void guardarVisita(Visita visita);
    void actualizarVisita(Visita visita);
    Optional<Visita> buscarVisitaPorId(UUID id);
    List<Visita> listarVisitasPorSeguimiento(UUID seguimientoId);
}
