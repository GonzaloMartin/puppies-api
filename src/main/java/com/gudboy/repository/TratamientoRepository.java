package com.gudboy.repository;

import com.gudboy.domain.tratamiento.Tratamiento;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class TratamientoRepository {

    private final Map<UUID, Tratamiento> store = new LinkedHashMap<>();

    public void guardar(Tratamiento tratamiento) {
        store.put(tratamiento.getTratamientoID(), tratamiento);
    }

    public Tratamiento buscarPorId(UUID id) {
        return store.get(id);
    }

    public ArrayList<Tratamiento> listarTodos() {
        return new ArrayList<>(store.values());
    }
}
