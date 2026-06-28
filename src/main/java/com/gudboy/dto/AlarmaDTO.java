package com.gudboy.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.gudboy.domain.tratamiento.TipoTratamiento;

public class AlarmaDTO {

    private final int id;
    private final UUID idAnimal;
    private final String titulo;
    private final String descripcion;
    private final int frecuenciaDias;
    private final LocalDateTime fechaProximoDisparo;
    private final String estado;
    private final boolean completada;
    private final LocalDateTime fechaCompletado;
    private final List<TipoTratamiento> acciones;

    public AlarmaDTO(int id, UUID idAnimal, String titulo, String descripcion,
                     int frecuenciaDias, LocalDateTime fechaProximoDisparo,
                     String estado, boolean completada, LocalDateTime fechaCompletado,
                     List<TipoTratamiento> acciones) {
        this.id = id;
        this.idAnimal = idAnimal;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.frecuenciaDias = frecuenciaDias;
        this.fechaProximoDisparo = fechaProximoDisparo;
        this.estado = estado;
        this.completada = completada;
        this.fechaCompletado = fechaCompletado;
        this.acciones = acciones;
    }

    public int getId() { return id; }
    public UUID getIdAnimal() { return idAnimal; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public int getFrecuenciaDias() { return frecuenciaDias; }
    public LocalDateTime getFechaProximoDisparo() { return fechaProximoDisparo; }
    public String getEstado() { return estado; }
    public boolean isCompletada() { return completada; }
    public LocalDateTime getFechaCompletado() { return fechaCompletado; }
    public List<TipoTratamiento> getAcciones() { return acciones; }

    @Override
    public String toString() {
        return titulo + " - " + estado
                + (completada ? " (Completada)" : " (Próximo: " + fechaProximoDisparo + ")");
    }
}
