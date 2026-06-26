package com.gudboy.repository;

import com.gudboy.domain.animal.model.Adopcion;
import com.gudboy.domain.seguimiento.model.Seguimiento;
import com.gudboy.domain.seguimiento.model.Visita;

import java.util.*;

public class SeguimientoRepositoryEnMemoria implements ISeguimientoRepository {

    private final Map<UUID, Seguimiento> seguimientos = new LinkedHashMap<>();
    private final Map<UUID, Visita> visitas = new LinkedHashMap<>();

    @Override
    public void guardar(Seguimiento seguimiento) {
        seguimientos.put(seguimiento.getId(), seguimiento);
        for (Visita v : seguimiento.getVisitas()) {
            visitas.put(v.getId(), v);
        }
    }

    @Override
    public void actualizar(Seguimiento seguimiento) {
        seguimientos.put(seguimiento.getId(), seguimiento);
    }

    @Override
    public Optional<Seguimiento> buscarPorId(UUID id) {
        return Optional.ofNullable(seguimientos.get(id));
    }

    @Override
    public List<Seguimiento> listarTodos() {
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
            if (v.getSeguimiento() != null && v.getSeguimiento().getId().equals(seguimientoId)) {
                result.add(v);
            }
        }
        return result;
    }
}
