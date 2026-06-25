package com.gudboy.repository;

import com.gudboy.domain.seguimiento.model.Visita;

import java.util.*;

public class VisitaRepositoryEnMemoria implements IVisitaRepository {
    private final Map<UUID, Visita> visitas = new HashMap<>();

    @Override
    public void guardar(Visita v) {
        visitas.put(v.getId(), v);
    }

    @Override
    public Optional<Visita> buscarPorId(UUID id) {
        return Optional.ofNullable(visitas.get(id));
    }

    @Override
    public List<Visita> listarPorSeguimiento(UUID seguimientoId) {
        List<Visita> result = new ArrayList<>();
        for (Visita v : visitas.values()) {
            if (v.getSeguimiento().getId().equals(seguimientoId)) {
                result.add(v);
            }
        }
        return result;
    }

    @Override
    public void actualizar(Visita v) {
        visitas.put(v.getId(), v);
    }
}
