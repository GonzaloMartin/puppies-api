package com.gudboy.domain.alarma.model;

import com.gudboy.domain.tratamiento.TipoTratamiento;
import com.gudboy.domain.Usuario.Veterinario;
import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;

public class Alarma {
    private int id;
    private String titulo;
    private String descripcion;
    private int frecuenciaDias;
    private LocalDateTime fechaProximoDisparo;
    private String estado;
    private boolean completada;
    private LocalDateTime fechaCompletado;
    private List<TipoTratamiento> acciones;
    private UUID idAnimal;

    public Alarma(int id, UUID idAnimal, String titulo, String descripcion, int frecuenciaDias,
                  LocalDateTime fechaProximoDisparo, List<TipoTratamiento> acciones) {
        this.id = id;
        this.idAnimal = idAnimal;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.frecuenciaDias = frecuenciaDias;
        this.fechaProximoDisparo = fechaProximoDisparo;
        this.acciones = acciones;
        this.estado = "ACTIVA";
        this.completada = false;
    }

    public Alarma() {}

    public boolean verificarFecha() {
        return LocalDateTime.now().isAfter(this.fechaProximoDisparo)
                || LocalDateTime.now().isEqual(this.fechaProximoDisparo);
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

    public void escribirComentario(String comentario, Veterinario veterinario) {
        String firma = (veterinario != null)
                ? veterinario.getNombre() + " " + veterinario.getApellido()
                : "Desconocido";
        this.descripcion = this.descripcion + " | Nota (" + firma + "): " + comentario;
    }

    public void marcarTratamientoFinalizado() {
        this.estado = "FINALIZADO";
        this.completada = true;
        this.fechaCompletado = LocalDateTime.now();
    }

    // --- Getters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getFrecuenciaDias() { return frecuenciaDias; }
    public void setFrecuenciaDias(int frecuenciaDias) { this.frecuenciaDias = frecuenciaDias; }
    public LocalDateTime getFechaProximoDisparoOriginal() { return fechaProximoDisparo; }
    public void setFechaProximoDisparo(LocalDateTime fechaProximoDisparo) { this.fechaProximoDisparo = fechaProximoDisparo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }          // FIX: cuerpo vacío → asignación real
    public boolean isCompletada() { return completada; }
    public void setCompletada(boolean completada) { this.completada = completada; } // FIX: ídem
    public UUID getIdAnimal() { return idAnimal; }
    public void setIdAnimal(UUID idAnimal) { this.idAnimal = idAnimal; }
    public List<TipoTratamiento> getAcciones() { return acciones; }
    public void setAcciones(List<TipoTratamiento> acciones) { this.acciones = acciones; }

    /** Compatibilidad con tests que llaman getFechaProximoDisparo() como String */
    public String getFechaProximoDisparo() { return fechaProximoDisparo.toString(); }

    @Override
    public String toString() {
        return titulo + " - " + estado
                + (completada ? " (Completada)" : " (Próximo: " + fechaProximoDisparo + ")");
    }
}
