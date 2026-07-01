package com.gudboy.controller;

import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.comentarioMedico.ComentarioMedico;
import com.gudboy.domain.fichaMedica.exportador.Exportador;
import com.gudboy.domain.fichaMedica.model.FichaMedica;
import com.gudboy.domain.tratamiento.Tratamiento;
import com.gudboy.service.FichaMedicaService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FichaMedicaController {

    private final FichaMedicaService fichaMedicaService;

    public FichaMedicaController(FichaMedicaService fichaMedicaService) {
        this.fichaMedicaService = fichaMedicaService;
    }

    public FichaMedica buscarPorAnimalId(UUID animalId) {
        return fichaMedicaService.buscarPorAnimalId(animalId);
    }

    public FichaMedica crearFicha(Animal animal) {
        return fichaMedicaService.crearFicha(animal);
    }

    public void actualizarDatos(UUID id, double peso, float altura, int edad) {
        fichaMedicaService.actualizarDatos(id, peso, altura, edad);
    }

    public void exportarFicha(UUID id, Exportador estrategia) {
        fichaMedicaService.exportarFicha(id, estrategia);
    }

    public void agregarTratamiento(UUID id, Tratamiento tratamiento) {
        fichaMedicaService.agregarTratamiento(id, tratamiento);
    }

    public void agregarComentarioMedico(UUID id, ComentarioMedico comentario) {
        fichaMedicaService.agregarComentarioMedico(id, comentario);
    }

    public Optional<FichaMedica> buscarPorId(UUID id) {
        return fichaMedicaService.buscarPorId(id);
    }

    public List<FichaMedica> listarTodas() {
        return fichaMedicaService.listarTodas();
    }

    public void actualizar(FichaMedica ficha) {
        fichaMedicaService.actualizarFicha(ficha);
    }
}
