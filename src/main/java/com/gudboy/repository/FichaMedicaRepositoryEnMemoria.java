/*package com.gudboy.repository;

import com.gudboy.domain.fichaMedica.model.FichaMedica;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FichaMedicaRepositoryEnMemoria implements IFichaMedicaRepository {

    private final Map<UUID, FichaMedica> fichas = new LinkedHashMap<>();

    @Override
    public void guardar(FichaMedica ficha) {
        fichas.put(ficha.getFichaMedicaId(), ficha);
    }

    @Override
    public void actualizar(FichaMedica ficha) {
        fichas.put(ficha.getFichaMedicaId(), ficha);
    }

    @Override
    public Optional<FichaMedica> buscarPorId(UUID id) {
        return Optional.ofNullable(fichas.get(id));
    }

    @Override
    public List<FichaMedica> listarTodas() {
        return new ArrayList<>(fichas.values());
    }
}*/