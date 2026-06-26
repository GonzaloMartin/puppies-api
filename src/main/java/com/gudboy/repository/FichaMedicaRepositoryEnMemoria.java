package com.gudboy.repository;

import com.gudboy.domain.fichaMedica.model.FichaMedica;

import java.util.*;

public class FichaMedicaRepositoryEnMemoria implements IFichaMedicaRepository {

    private final Map<UUID, FichaMedica> store = new LinkedHashMap<>();

    @Override
    public void guardar(FichaMedica ficha) { store.put(ficha.getFichaMedicaId(), ficha); }

    @Override
    public void actualizar(FichaMedica ficha) { store.put(ficha.getFichaMedicaId(), ficha); }

    @Override
    public Optional<FichaMedica> buscarPorId(UUID id) { return Optional.ofNullable(store.get(id)); }

    @Override
    public List<FichaMedica> listarTodas() { return new ArrayList<>(store.values()); }

    @Override
    public FichaMedica getByAnimalId(UUID idAnimal) {
        return store.values().stream()
                .filter(f -> f.getAnimal().getId().equals(idAnimal))
                .findFirst().orElse(null);
    }

    @Override
    public void update(FichaMedica ficha) { actualizar(ficha); }
}
