package com.gudboy.controller;

import com.gudboy.domain.alarma.model.Alarma;
import com.gudboy.service.AlarmaService;
import com.gudboy.domain.alarma.observer.IAlarmaObserver;

import java.util.List;

public class AlarmaController {

    private final AlarmaService service;

    public AlarmaController(AlarmaService service) {
        this.service = service;
    }

    public void suscribirVista(IAlarmaObserver observer) {
        service.suscribir(observer);
    }

    // --- CAMBIO: Método opcional pero recomendado para desuscribir ---
    public void desuscribirVista(IAlarmaObserver observer) {
        service.desuscribir(observer);
    }

    public List<Alarma> getAll() {
        return service.obtenerTodas();
    }

    public Alarma getById(int id) {
        return service.obtenerAlarma(id);
    }

    public void create(Alarma alarma) {
        service.crearAlarma(alarma);
    }

    public void update(int id, Alarma alarma) {
        Alarma existente = service.obtenerAlarma(id);
        if (existente != null) {
            alarma.setId(id); // Asegura que la ID sea la correcta
            service.actualizarAlarma(alarma);
        }
    }

    public void delete(int id) {
        service.eliminarAlarma(id);
    }

    public void marcarCompletado(int id) {
        service.marcarCompletado(id);
    }

    public void verificarEstadoAlarmas() {
        service.verificarEstadoAlarmas();
    }

    // Reemplaza el método actual por este
    public void atenderAlarma(int id, String comentario, boolean tratamientoFinalizado, com.gudboy.domain.Usuario.Veterinario veterinario) {
        service.atenderAlarma(id, comentario, tratamientoFinalizado, veterinario);
    }
}