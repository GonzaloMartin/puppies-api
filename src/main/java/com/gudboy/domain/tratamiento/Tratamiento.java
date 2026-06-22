package com.gudboy.domain.tratamiento;

import java.util.Date;
import java.util.UUID;

public class Tratamiento {
    private final UUID tratamientoID;
    private TipoTratamiento tipoTratamientoEnum;
    private Date fechaInicio;
    private Date fechaFin;
    private EstadoTratamiento estado;


    public Tratamiento(TipoTratamiento tipoTratamientoEnum) {
        this.tratamientoID = UUID.randomUUID();
        this.tipoTratamientoEnum = tipoTratamientoEnum;
        estado = new Pendiente(this);
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

}
