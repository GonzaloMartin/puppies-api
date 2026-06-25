package com.gudboy.domain.seguimiento.model;

import com.gudboy.domain.animal.model.Adopcion;
import com.gudboy.domain.Usuario.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Seguimiento {
    private final UUID id;
    private final Adopcion adopcion;
    private final Usuario responsable;
    private final DiaSemana diaSemana;
    private final String horarioDesde;
    private final String horarioHasta;
    private EstadoSeguimiento estado;
    private final PreferenciaRecordatorio preferenciaRecordatorio;
    private final List<Visita> visitas;

    public Seguimiento(Adopcion adopcion, Usuario responsable, DiaSemana diaSemana, String horarioDesde, String horarioHasta, PreferenciaRecordatorio preferenciaRecordatorio) {
        this.id = UUID.randomUUID();
        this.adopcion = adopcion;
        this.responsable = responsable;
        this.diaSemana = diaSemana;
        this.horarioDesde = horarioDesde;
        this.horarioHasta = horarioHasta;
        this.estado = EstadoSeguimiento.ACTIVO;
        this.preferenciaRecordatorio = preferenciaRecordatorio;
        this.visitas = new ArrayList<>();
    }

    public Seguimiento(UUID id, Adopcion adopcion, Usuario responsable, DiaSemana diaSemana, String horarioDesde, String horarioHasta, EstadoSeguimiento estado, PreferenciaRecordatorio preferenciaRecordatorio) {
        this.id = id;
        this.adopcion = adopcion;
        this.responsable = responsable;
        this.diaSemana = diaSemana;
        this.horarioDesde = horarioDesde;
        this.horarioHasta = horarioHasta;
        this.estado = estado;
        this.preferenciaRecordatorio = preferenciaRecordatorio;
        this.visitas = new ArrayList<>();
    }

    public void agregarVisita(Visita visita) {
        this.visitas.add(visita);
    }

    public void finalizarSeguimiento() {
        this.estado = EstadoSeguimiento.FINALIZADO;
    }

    // --- Getters and Setters ---
    public UUID getId() {
        return id;
    }

    public Adopcion getAdopcion() {
        return adopcion;
    }

    public Usuario getResponsable() {
        return responsable;
    }

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public String getHorarioDesde() {
        return horarioDesde;
    }

    public String getHorarioHasta() {
        return horarioHasta;
    }

    public EstadoSeguimiento getEstado() {
        return estado;
    }

    public PreferenciaRecordatorio getPreferenciaRecordatorio() {
        return preferenciaRecordatorio;
    }

    public List<Visita> getVisitas() {
        return visitas;
    }
}
