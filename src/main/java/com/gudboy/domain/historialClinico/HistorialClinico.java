package com.gudboy.domain.historialClinico;

import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.comentarioMedico.ComentarioMedico;
import com.gudboy.domain.tratamiento.model.Tratamiento;

import java.util.ArrayList;
import java.util.UUID;

public class HistorialClinico {
    private final UUID historialID;
    private Animal animal;
    private ArrayList<Tratamiento> listaTratamiento;
    private ArrayList<ComentarioMedico> listaComentario;


    public HistorialClinico(Animal animal, ArrayList<Tratamiento> listaTratamiento, ArrayList<ComentarioMedico> listaComentario) {
        this.historialID = UUID.randomUUID();
        this.animal = animal;
        this.listaComentario = listaComentario;
        this.listaTratamiento = listaTratamiento;
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

    private void setAnimal(Animal animal){
        this.animal = animal;
    }

    private void setListaTratamiento(ArrayList<Tratamiento> listaTratamiento){
        this.listaTratamiento = listaTratamiento;
    }

    private void setListaComentario(ArrayList<ComentarioMedico> listaComentario){
        this.listaComentario = listaComentario;
    }

    public void modificarHistorial(Animal animal, ArrayList<Tratamiento> t, ArrayList<ComentarioMedico> c){
        setAnimal(animal);
        setListaTratamiento(t);
        setListaComentario(c);
    }

}
