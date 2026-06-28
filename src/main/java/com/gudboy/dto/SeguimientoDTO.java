package com.gudboy.dto;

import com.gudboy.domain.seguimiento.model.DiaSemana;
import com.gudboy.domain.seguimiento.model.EstadoSeguimiento;
import com.gudboy.domain.seguimiento.model.PreferenciaRecordatorio;

import java.util.List;
import java.util.UUID;

public class SeguimientoDTO {
    private final UUID id;
    private final AdopcionDTO adopcion;
    private final UsuarioDTO responsable;
    private final DiaSemana diaSemana;
    private final String horarioDesde;
    private final String horarioHasta;
    private final EstadoSeguimiento estado;
    private final PreferenciaRecordatorio preferenciaRecordatorio;
    private final List<VisitaDTO> visitas;

    public SeguimientoDTO(UUID id, AdopcionDTO adopcion, UsuarioDTO responsable, DiaSemana diaSemana,
                          String horarioDesde, String horarioHasta, EstadoSeguimiento estado,
                          PreferenciaRecordatorio preferenciaRecordatorio, List<VisitaDTO> visitas) {
        this.id = id;
        this.adopcion = adopcion;
        this.responsable = responsable;
        this.diaSemana = diaSemana;
        this.horarioDesde = horarioDesde;
        this.horarioHasta = horarioHasta;
        this.estado = estado;
        this.preferenciaRecordatorio = preferenciaRecordatorio;
        this.visitas = visitas;
    }

    public UUID getId() {
        return id;
    }

    public AdopcionDTO getAdopcion() {
        return adopcion;
    }

    public UsuarioDTO getResponsable() {
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

    public List<VisitaDTO> getVisitas() {
        return visitas;
    }
}
