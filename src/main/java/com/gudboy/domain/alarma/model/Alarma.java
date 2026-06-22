package com.gudboy.domain.alarma.model;

import com.gudboy.domain.tratamiento.TipoTratamiento;

import java.time.LocalDateTime;

public class Alarma {
    private int id;
    private String titulo;
    private String descripcion;
    private int frecuenciaDias;
    private LocalDateTime fechaProximoDisparo;
    private String estado;
    private TipoTratamiento tipoTratamiento;
    private boolean completada;
    private LocalDateTime fechaCompletado;

    public Alarma(int id, String titulo, String descripcion, int frecuenciaDias, LocalDateTime fechaProximoDisparo, TipoTratamiento tipoTratamiento) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.frecuenciaDias = frecuenciaDias;
        this.fechaProximoDisparo = fechaProximoDisparo;
        this.tipoTratamiento = tipoTratamiento;
        this.estado = "ACTIVA";
        this.completada = false;
    }

    // Constructor vacío para uso del Repository
    public Alarma() {}

    public boolean verificarFecha() {
        return LocalDateTime.now().isAfter(this.fechaProximoDisparo) || LocalDateTime.now().isEqual(this.fechaProximoDisparo);
    }

    public void reprogramar() {
        this.fechaProximoDisparo = this.fechaProximoDisparo.plusDays(this.frecuenciaDias);
        this.estado = "ACTIVA";
        this.completada = false;
    }

    public void marcarCompletado() {
        this.completada = true;
        this.estado = "COMPLETADA";
        this.fechaCompletado = LocalDateTime.now();
    }

    public void escribirComentario(String comentario) {
        this.descripcion = this.descripcion + " | Nota: " + comentario;
    }

    public void marcarTratamientoFinalizado() {
        this.estado = "FINALIZADO";
        this.completada = true;
    }

    // Getters y Setters correspondientes
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public boolean isCompletada() { return completada; }
    public String getEstado() { return estado; }

    public void setTitulo(String titulo) {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getFrecuenciaDias() {
        return frecuenciaDias;
    }

    public String getFechaProximoDisparo() {
        return fechaProximoDisparo.toString();
    }

    public TipoTratamiento getTipoTratamiento() {
        return tipoTratamiento;
    }

    @Override
    public String toString() {
        return titulo + " - " + estado + (completada ? " (Completada)" : " (Próximo: " + fechaProximoDisparo + ")");
    }
}