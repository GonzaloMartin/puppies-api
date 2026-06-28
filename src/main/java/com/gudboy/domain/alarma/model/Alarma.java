package com.gudboy.domain.alarma.model;

import com.gudboy.domain.tratamiento.TipoTratamiento;
import com.gudboy.dto.AlarmaDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "alarmas")
public class Alarma {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "frecuencia_dias")
    private int frecuenciaDias;

    @Column(name = "fecha_proximo_disparo")
    private LocalDateTime fechaProximoDisparo;

    @Column(name = "estado")
    private String estado;

    @Column(name = "completada")
    private boolean completada;

    @Column(name = "fecha_completado")
    private LocalDateTime fechaCompletado;

    @Convert(converter = TipoTratamientoListConverter.class)
    @Column(name = "acciones")
    private List<TipoTratamiento> acciones;

    @Column(name = "id_animal")
    @JdbcTypeCode(SqlTypes.CHAR)
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

    public AlarmaDTO toDTO() {
        return new AlarmaDTO(
            this.id,
            this.idAnimal,
            this.titulo,
            this.descripcion,
            this.frecuenciaDias,
            this.fechaProximoDisparo,
            this.estado,
            this.completada,
            this.fechaCompletado,
            this.acciones
        );
    }

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
    public void setEstado(String estado) { this.estado = estado; }
    public boolean isCompletada() { return completada; }
    public void setCompletada(boolean completada) { this.completada = completada; }
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
