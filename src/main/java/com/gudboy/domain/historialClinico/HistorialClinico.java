package com.gudboy.domain.historialClinico;

import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.comentarioMedico.ComentarioMedico;
import com.gudboy.domain.tratamiento.Tratamiento;

import java.util.ArrayList;
import java.util.UUID;

public class HistorialClinico {

    private final UUID historialID;
    private final Animal animal;

    private final ArrayList<Tratamiento> listaTratamiento;
    private final ArrayList<ComentarioMedico> listaComentario;
    private final ArrayList<com.gudboy.domain.seguimiento.model.Visita> listaVisitas;

    public HistorialClinico(Animal animal) {

        this.historialID = UUID.randomUUID();
        this.animal = animal;

        this.listaTratamiento = new ArrayList<>();
        this.listaComentario = new ArrayList<>();
        this.listaVisitas = new ArrayList<>();
    }

    public void agregarTratamiento(
            Tratamiento tratamiento) {

        listaTratamiento.add(tratamiento);
    }

    public void agregarComentario(
            ComentarioMedico comentario) {

        listaComentario.add(comentario);
    }

    public void agregarVisita(
            com.gudboy.domain.seguimiento.model.Visita visita) {
        listaVisitas.add(visita);
    }

    public ArrayList<Tratamiento> getListaTratamiento() {
        return listaTratamiento;
    }

    public ArrayList<ComentarioMedico> getListaComentario() {
        return listaComentario;
    }

    public ArrayList<com.gudboy.domain.seguimiento.model.Visita> getListaVisitas() {
        return listaVisitas;
    }

    public UUID getHistorialID() {
        return historialID;
    }

    public Animal getAnimal() {
        return animal;
    }
}