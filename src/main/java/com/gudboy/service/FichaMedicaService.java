package com.gudboy.service;

import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.comentarioMedico.ComentarioMedico;
import com.gudboy.domain.fichaMedica.exportador.Exportador;
import com.gudboy.domain.fichaMedica.model.FichaMedica;
import com.gudboy.domain.tratamiento.Tratamiento;
import com.gudboy.repository.IFichaMedicaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FichaMedicaService {

    private final IFichaMedicaRepository repository;

    public FichaMedicaService(IFichaMedicaRepository repository) {
        this.repository = repository;
    }

    public FichaMedica crearFicha(Animal animal) {
        FichaMedica ficha = new FichaMedica(animal);
        repository.guardar(ficha);
        return ficha;
    }

    public void actualizarDatos(UUID id, double peso, float altura, int edad) {
        FichaMedica ficha = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Ficha no encontrada: " + id));
        ficha.actualizarDatos(peso, altura, edad);
        repository.actualizar(ficha);
    }

    public void exportarFicha(UUID id, Exportador estrategia) {
        FichaMedica ficha = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Ficha no encontrada: " + id));
        ficha.exportar(estrategia);
    }

    public void agregarTratamiento(UUID id, Tratamiento tratamiento) {
        FichaMedica ficha = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Ficha no encontrada: " + id));
        ficha.agregarTratamiento(tratamiento);
        repository.actualizar(ficha);
    }

    public void agregarComentarioMedico(UUID id, ComentarioMedico comentario) {
        FichaMedica ficha = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Ficha no encontrada: " + id));
        ficha.agregarComentarioMedico(comentario);
        repository.actualizar(ficha);
    }

    public Optional<FichaMedica> buscarPorId(UUID id) {
        return repository.buscarPorId(id);
    }

    public List<FichaMedica> listarTodas() {
        return repository.listarTodas();
    }
}
