package com.gudboy.domain.tratamiento;

import java.util.Date;
import java.util.UUID;
import com.gudboy.dto.TratamientoDTO;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;
import java.util.UUID;

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

    protected Tratamiento() {
    }

    public Tratamiento(TipoTratamiento tipoTratamientoEnum) {
        this.tratamientoID = UUID.randomUUID();
        this.tipoTratamientoEnum = tipoTratamientoEnum;
        estado = new Pendiente(this);
        estadoPersistido = "Pendiente";
    }

    public void aplicarTratamiento(){
        estado.aplicar();
    }

    public void finalizarTratamiento(){
        estado.finalizar();
    }

    public void cancelarTratamiento(){
        estado.cancelar();
    }

    void setEstado(EstadoTratamiento estado){
        this.estado = estado;
        this.estadoPersistido = estado.getClass().getSimpleName();
    }

    void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    void setFechaInicio(Date fechaInicio){
        this.fechaInicio = fechaInicio;
    }

    public UUID getTratamientoID(){
        return tratamientoID;
    }

    public TipoTratamiento getTipoTratamientoEnum() {
        return tipoTratamientoEnum;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public EstadoTratamiento getEstado() {
        return estado;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    @Override
    public String toString() {
        return String.format("%s [%s]", tipoTratamientoEnum, estado.getClass().getSimpleName());
    }

    @PostLoad
    private void restaurarEstado() {

        switch (estadoPersistido) {

            case "Pendiente":
                estado = new Pendiente(this);
                break;

            case "EnCurso":
                estado = new EnCurso(this);
                break;

            case "Finalizado":
                estado = new Finalizado(this);
                break;

            case "Cancelado":
                estado = new Cancelado(this);
                break;

            default:
                estado = new Pendiente(this);
        }
    }
    public TratamientoDTO toDTO() {
        return new TratamientoDTO(
                tratamientoID,
                tipoTratamientoEnum.name(),
                estado.getClass().getSimpleName(),
                fechaInicio,
                fechaFin
        );
    }

}
