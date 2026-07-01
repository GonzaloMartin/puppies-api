package com.gudboy.dto;

import java.util.List;
import java.util.UUID;

public class HistorialClinicoDTO {

    private final UUID historialID;

    private final AnimalDTO animal;

    private final List<TratamientoDTO> tratamientos;

    private final List<ComentarioMedicoDTO> comentarios;

    private final List<VisitaDTO> visitas;

    public HistorialClinicoDTO(
            UUID historialID,
            AnimalDTO animal,
            List<TratamientoDTO> tratamientos,
            List<ComentarioMedicoDTO> comentarios,
            List<VisitaDTO> visitas) {

        this.historialID = historialID;
        this.animal = animal;
        this.tratamientos = tratamientos;
        this.comentarios = comentarios;
        this.visitas = visitas;
    }

    public UUID getHistorialID() {
        return historialID;
    }

    public AnimalDTO getAnimal() {
        return animal;
    }

    public List<TratamientoDTO> getTratamientos() {
        return tratamientos;
    }

    public List<ComentarioMedicoDTO> getComentarios() {
        return comentarios;
    }

    public List<VisitaDTO> getVisitas() {
        return visitas;
    }
}