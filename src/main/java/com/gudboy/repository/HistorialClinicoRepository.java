package com.gudboy.repository;

import com.gudboy.domain.historialClinico.HistorialClinico;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class HistorialClinicoRepository {

    private final Map<UUID, HistorialClinico> store = new LinkedHashMap<>();

    public void guardar(HistorialClinico historial) {
        store.put(historial.getHistorialID(), historial);
    }

    public HistorialClinico buscarPorId(UUID historialId) {
        return store.get(historialId);
    }

    /** Busca el historial clínico asociado al animal con ese UUID. */
    public HistorialClinico buscarPorAnimal(UUID animalId) {
        return store.values().stream()
                .filter(h -> h.getAnimal().getId().equals(animalId))
                .findFirst()
                .orElse(null);
    }

    public ArrayList<HistorialClinico> listarTodos() {
        return new ArrayList<>(store.values());
    }
}
