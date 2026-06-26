package com.gudboy.repository;

import com.gudboy.domain.seguimiento.model.Visita;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IVisitaRepository {
    void guardar(Visita visita);
    Optional<Visita> buscarPorId(UUID id);
    List<Visita> listarPorSeguimiento(UUID seguimientoId);
    void actualizar(Visita visita);
}
