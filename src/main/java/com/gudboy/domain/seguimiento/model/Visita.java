package com.gudboy.domain.seguimiento.model;

import com.gudboy.domain.seguimiento.observer.IObservador;
import com.gudboy.dto.VisitaDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "visitas")
public class Visita {
    @Id
    @Column(name = "id")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seguimiento_id")
    private Seguimiento seguimiento;

    @Column(name = "fecha_programada")
    private LocalDate fechaProgramada;

    @Column(name = "fecha_real")
    private LocalDate fechaReal;

    @Column(name = "comentarios")
    private String comentarios;

    @Column(name = "completada")
    private boolean completada;

    @Column(name = "continuar_visitas")
    private boolean continuarVisitas;

    @Embedded
    private Encuesta encuesta;

    @Transient
    private final List<IObservador> observadores;

    protected Visita() {
        this.observadores = new ArrayList<>();
    }

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
    public void suscribir(IObservador o) {
        if (!observadores.contains(o)) {
            observadores.add(o);
        }
    }

    public void desuscribir(IObservador o) {
        observadores.remove(o);
    }

    public void notificarRecordatorio() {
        for (IObservador o : observadores) {
            o.enviarRecordatorio(this);
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

    public VisitaDTO toDTO() {
        return new VisitaDTO(
            id,
            seguimiento != null ? seguimiento.getId() : null,
            fechaProgramada,
            fechaReal,
            comentarios,
            completada,
            continuarVisitas,
            encuesta != null ? encuesta.toDTO() : null
        );
    }
    @Override
    public String toString() {
        String estado = completada ? "✓ Completada " + fechaReal : "⏳ Pendiente " + fechaProgramada;
        return String.format("Visita %s | %s", id.toString().substring(0,8), estado);
    }

}
