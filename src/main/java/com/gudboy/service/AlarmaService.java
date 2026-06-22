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
}