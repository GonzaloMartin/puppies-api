package com.gudboy.repository;

import com.gudboy.domain.seguimiento.model.Visita;

import java.util.*;

public class VisitaRepositoryEnMemoria implements IVisitaRepository {

    private final Map<UUID, Visita> store = new LinkedHashMap<>();

    @Override
    public void guardar(Visita visita) {
        store.put(visita.getId(), visita);
    }

    @Override
    public Optional<Visita> buscarPorId(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Visita> listarPorSeguimiento(UUID seguimientoId) {
        List<Visita> result = new ArrayList<>();
        for (Visita v : store.values()) {
            if (v.getSeguimiento() != null && v.getSeguimiento().getId().equals(seguimientoId)) {
                result.add(v);
            }
        }
        return result;
    }

    @Override
    public void actualizar(Visita visita) {
        store.put(visita.getId(), visita);
    }
}
