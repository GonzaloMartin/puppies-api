package com.gudboy.domain.seguimiento.model;

import com.gudboy.domain.seguimiento.observer.IObservador;
import com.gudboy.domain.seguimiento.observer.ISujetoRecordatorio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Visita implements ISujetoRecordatorio {
    private final UUID id;
    private final Seguimiento seguimiento;
    private final LocalDate fechaProgramada;
    private LocalDate fechaReal;
    private String comentarios;
    private boolean completada;
    private boolean continuarVisitas;
    private Encuesta encuesta;
    private final List<IObservador> observadores;

    public Visita(Seguimiento seguimiento, LocalDate fechaProgramada) {
        this.id = UUID.randomUUID();
        this.seguimiento = seguimiento;
        this.fechaProgramada = fechaProgramada;
        this.completada = false;
        this.continuarVisitas = true;
        this.observadores = new ArrayList<>();
    }

    public Visita(UUID id, Seguimiento seguimiento, LocalDate fechaProgramada, LocalDate fechaReal, String comentarios, boolean completada, boolean continuarVisitas) {
        this.id = id;
        this.seguimiento = seguimiento;
        this.fechaProgramada = fechaProgramada;
        this.fechaReal = fechaReal;
        this.comentarios = comentarios;
        this.completada = completada;
        this.continuarVisitas = continuarVisitas;
        this.observadores = new ArrayList<>();
    }

    public void registrarResultado(Encuesta encuesta, String comentarios, boolean continuarVisitas) {
        this.encuesta = encuesta;
        this.comentarios = comentarios;
        this.continuarVisitas = continuarVisitas;
        this.fechaReal = LocalDate.now();
        this.completada = true;
    }

    public void marcarCompletada() {
        this.completada = true;
        this.fechaReal = LocalDate.now();
    }

    public boolean requiereContinuarVisitas() {
        return this.continuarVisitas;
    }

    public boolean correspondeRecordatorio(LocalDate fechaActual, int diasPrevios) {
        if (completada) return false;
        return fechaProgramada.minusDays(diasPrevios).isEqual(fechaActual);
    }

    // --- IMPLEMENTACIÓN DE OBSERVER ---
    @Override
    public void suscribir(IObservador o) {
        if (!observadores.contains(o)) {
            observadores.add(o);
        }
    }

    @Override
    public void desuscribir(IObservador o) {
        observadores.remove(o);
    }

    @Override
    public void notificarRecordatorio() {
        for (IObservador o : observadores) {
            o.update(this);
        }
    }

    // --- Getters ---
    public UUID getId() {
        return id;
    }

    public Seguimiento getSeguimiento() {
        return seguimiento;
    }

    public LocalDate getFechaProgramada() {
        return fechaProgramada;
    }

    public LocalDate getFechaReal() {
        return fechaReal;
    }

    public String getComentarios() {
        return comentarios;
    }

    public boolean isCompletada() {
        return completada;
    }

    public boolean isContinuarVisitas() {
        return continuarVisitas;
    }

    public Encuesta getEncuesta() {
        return encuesta;
    }

    public List<IObservador> getObservadores() {
        return observadores;
    }
}
