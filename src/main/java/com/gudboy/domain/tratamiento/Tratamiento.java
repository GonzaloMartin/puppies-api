package com.gudboy.domain.tratamiento;

import java.util.Date;
import java.util.UUID;

import com.gudboy.domain.fichaMedica.model.FichaMedica;
import com.gudboy.dto.TratamientoDTO;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "tratamiento")
public class Tratamiento {

    @Id
    @Column(name = "id")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID tratamientoID;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoTratamiento tipoTratamientoEnum;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_fin")
    private Date fechaFin;

    @Transient
    private EstadoTratamiento estado;

    @Column(name = "estado")
    private String estadoPersistido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ficha_id", nullable = false)
    private FichaMedica fichaMedica;

    protected Tratamiento() {}

    public Tratamiento(TipoTratamiento tipoTratamientoEnum) {
        this.tratamientoID       = UUID.randomUUID();
        this.tipoTratamientoEnum = tipoTratamientoEnum;
        this.estado              = new Pendiente(this);
        this.estadoPersistido    = "Pendiente";
    }

    public Tratamiento(UUID tratamientoID, TipoTratamiento tipoTratamientoEnum) {
        this.tratamientoID       = tratamientoID;
        this.tipoTratamientoEnum = tipoTratamientoEnum;
        this.estado              = new Pendiente(this);
        this.estadoPersistido    = "Pendiente";
    }

    public void aplicarTratamiento()   { estado.aplicar(); }
    public void finalizarTratamiento() { estado.finalizar(); }
    public void cancelarTratamiento()  { estado.cancelar(); }

    void setEstado(EstadoTratamiento estado) {
        this.estado           = estado;
        this.estadoPersistido = estado.getClass().getSimpleName();
    }

    void setFechaFin(Date fechaFin)       { this.fechaFin    = fechaFin; }
    void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public void setFichaMedica(FichaMedica fichaMedica) {
        this.fichaMedica = fichaMedica;
    }

    @PostLoad
    private void restaurarEstado() {
        switch (estadoPersistido) {
            case "EnCurso"    -> estado = new EnCurso(this);
            case "Finalizado" -> estado = new Finalizado(this);
            case "Cancelado"  -> estado = new Cancelado(this);
            default           -> estado = new Pendiente(this);
        }
    }

    public UUID              getTratamientoID()       { return tratamientoID; }
    public TipoTratamiento   getTipoTratamientoEnum() { return tipoTratamientoEnum; }
    public Date              getFechaFin()            { return fechaFin; }
    public Date              getFechaInicio()         { return fechaInicio; }
    public EstadoTratamiento getEstado()              { return estado; }
    public FichaMedica       getFichaMedica()         { return fichaMedica; }

    public TratamientoDTO toDTO() {
        return new TratamientoDTO(
                tratamientoID,
                tipoTratamientoEnum.name(),
                estado.getClass().getSimpleName(),
                fechaInicio,
                fechaFin
        );
    }

    @Override
    public String toString() {
        return String.format("%s [%s]", tipoTratamientoEnum, estado.getClass().getSimpleName());
    }
}