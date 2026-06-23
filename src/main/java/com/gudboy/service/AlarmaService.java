package com.gudboy.service;

import com.gudboy.domain.alarma.model.Alarma;
import com.gudboy.domain.alarma.observer.IAlarmaObserver;
import com.gudboy.repository.IAlarmaRepository;

import java.util.ArrayList;
import java.util.List;

public class AlarmaService {

    private final IAlarmaRepository repository;
    private final List<IAlarmaObserver> observadores;

    public AlarmaService(IAlarmaRepository repository) {
        this.repository = repository;
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

    public void atenderAlarma(int id, String comentario, boolean tratamientoFinalizado) {
        Alarma alarma = repository.getById(id);
        if (alarma != null) {
            // 1. Agregar el comentario a la trazabilidad
            if (comentario != null && !comentario.trim().isEmpty()) {
                alarma.escribirComentario(comentario);
            }

            // 2. Determinar el estado final
            if (tratamientoFinalizado) {
                alarma.marcarTratamientoFinalizado();
            } else {
                alarma.marcarCompletado();
                // Opcional: Aquí podrías añadir la lógica para crear la siguiente alarma
                // automáticamente llamando a alarma.reprogramar() si la regla de negocio lo exige.
            }

            // 3. Persistir y notificar
            repository.update(alarma);
            notificarObservadores(alarma);
        }
    }
}