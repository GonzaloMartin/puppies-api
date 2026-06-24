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

    public HistorialClinico(Animal animal) {

        this.historialID = UUID.randomUUID();
        this.animal = animal;

        this.listaTratamiento = new ArrayList<>();
        this.listaComentario = new ArrayList<>();
    }

    public void agregarTratamiento(
            Tratamiento tratamiento) {

        listaTratamiento.add(tratamiento);
    }

    public void agregarComentario(
            ComentarioMedico comentario) {

        listaComentario.add(comentario);
    }

    public ArrayList<Tratamiento> getListaTratamiento() {
        return listaTratamiento;
    }

    public ArrayList<ComentarioMedico> getListaComentario() {
        return listaComentario;
    }

    public UUID getHistorialID() {
        return historialID;
    }

    public Animal getAnimal() {
        return animal;
    }
}