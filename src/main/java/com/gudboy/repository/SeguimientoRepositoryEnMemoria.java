package com.gudboy.repository;

import com.gudboy.domain.seguimiento.model.Seguimiento;
import com.gudboy.domain.seguimiento.model.Visita;

import java.util.*;

public class SeguimientoRepositoryEnMemoria implements ISeguimientoRepository {
    private final Map<UUID, Seguimiento> seguimientos = new HashMap<>();
    private final Map<UUID, Visita> visitas = new HashMap<>();

    @Override
    public void guardar(Seguimiento seguimiento) {
        seguimientos.put(seguimiento.getId(), seguimiento);
        for (Visita v : seguimiento.getVisitas()) {
            guardarVisita(v);
        }
    }

    @Override
    public void actualizar(Seguimiento seguimiento) {
        seguimientos.put(seguimiento.getId(), seguimiento);
    }

    @Override
    public Optional<Seguimiento> buscarPorId(UUID id) {
        Seguimiento s = seguimientos.get(id);
        if (s != null) {
            s.getVisitas().clear();
            for (Visita v : listarVisitasPorSeguimiento(id)) {
                s.agregarVisita(v);
            }
        }
        return Optional.ofNullable(s);
    }

    @Override
    public List<Seguimiento> listarTodos() {
        for (Seguimiento s : seguimientos.values()) {
            s.getVisitas().clear();
            for (Visita v : listarVisitasPorSeguimiento(s.getId())) {
                s.agregarVisita(v);
            }
        }
        return new ArrayList<>(seguimientos.values());
    }

    @Override
    public void guardarVisita(Visita visita) {
        visitas.put(visita.getId(), visita);
    }

    @Override
    public void actualizarVisita(Visita visita) {
        visitas.put(visita.getId(), visita);
    }

    @Override
    public Optional<Visita> buscarVisitaPorId(UUID id) {
        return Optional.ofNullable(visitas.get(id));
    }

    @Override
    public List<Visita> listarVisitasPorSeguimiento(UUID seguimientoId) {
        List<Visita> result = new ArrayList<>();
        for (Visita v : visitas.values()) {
            if (v.getSeguimiento().getId().equals(seguimientoId)) {
                result.add(v);
            }
        }
        return result;
    }
}
