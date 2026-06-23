package com.gudboy.repository;

import com.gudboy.domain.fichaMedica.model.FichaMedica;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IFichaMedicaRepository {
    void guardar(FichaMedica ficha);
    void actualizar(FichaMedica ficha);
    Optional<FichaMedica> buscarPorId(UUID id);
    List<FichaMedica> listarTodas();

    FichaMedica getByAnimalId(UUID idAnimal);

    void update(FichaMedica ficha);
}
