package com.gudboy.service;

import com.gudboy.domain.alarma.IHistorialClinicoService;
import com.gudboy.domain.alarma.model.Alarma;
import com.gudboy.domain.alarma.observer.IAlarmaObserver;
import com.gudboy.repository.IAlarmaRepository;

import java.util.ArrayList;
import java.util.List;

public class AlarmaService {

    private final IAlarmaRepository repository;
    private final List<IAlarmaObserver> observadores;
    private final IHistorialClinicoService historialService; // <-- NUEVO: El puente hacia Ficha Médica

    public AlarmaService(IAlarmaRepository repository, IHistorialClinicoService historialService) {
        this.repository = repository;
        this.historialService = historialService;
        this.observadores = new ArrayList<>();
    }

    public void crearAlarma(Alarma alarma) {
        repository.add(alarma);

        notificarObservadores(alarma);
    }

    public Alarma obtenerAlarma(int id) {
        return repository.getById(id);
    }

    public List<Alarma> obtenerTodas() {
        return repository.getAll();
    }

    public void actualizarAlarma(Alarma alarma) {
        repository.update(alarma);
        notificarObservadores(alarma);
    }

    public void eliminarAlarma(int id) {
        Alarma alarma = repository.getById(id);
        if (alarma != null) {
            repository.remove(alarma);
        }
    }

    public void marcarCompletado(int id) {
        Alarma alarma = repository.getById(id);
        if (alarma != null) {
            alarma.marcarCompletado();
            repository.update(alarma);
            notificarObservadores(alarma);
        }
    }

    public void verificarEstadoAlarmas() {
        List<Alarma> alarmas = repository.getAll();
        for (Alarma alarma : alarmas) {
            if (alarma.verificarFecha() && !alarma.isCompletada()) {
                notificarObservadores(alarma);
            }
        }
    }

    public void suscribir(IAlarmaObserver o) {
        if (!observadores.contains(o)) {
            observadores.add(o);
        }
    }

    public void desuscribir(IAlarmaObserver o) {
        observadores.remove(o);
    }

    private void notificarObservadores(Alarma alarma) {
        for (IAlarmaObserver observer : observadores) {
            observer.actualizarEstado(alarma);
        }
    }

    public void atenderAlarma(int id, String comentario, boolean tratamientoFinalizado, com.gudboy.domain.Usuario.Veterinario veterinario) {
        Alarma alarma = repository.getById(id);
        if (alarma != null) {
            if (comentario != null && !comentario.trim().isEmpty()) {
                alarma.escribirComentario(comentario, veterinario);
            }

            if (tratamientoFinalizado) {
                alarma.marcarTratamientoFinalizado();
                historialService.finalizarTratamientosActivos(alarma.getIdAnimal(), alarma.getAcciones());
            } else {
                alarma.marcarCompletado();
            }

            repository.update(alarma);
            notificarObservadores(alarma);

            // <-- NUEVO: La acción de inyectar el registro en el módulo vecino
            if (comentario != null && !comentario.trim().isEmpty()) {
                historialService.registrarAtencion(alarma.getIdAnimal(), comentario, veterinario);
            }
        }
    }
}