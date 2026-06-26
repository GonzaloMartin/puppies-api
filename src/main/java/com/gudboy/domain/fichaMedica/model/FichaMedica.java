package com.gudboy.domain.fichaMedica.model;

import java.util.UUID;

import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.comentarioMedico.ComentarioMedico;
import com.gudboy.domain.fichaMedica.exportador.Exportador;
import com.gudboy.domain.historialClinico.HistorialClinico;
import com.gudboy.domain.tratamiento.Tratamiento;

public class FichaMedica {

    private final UUID fichaMedicaId;
    private double peso;
    private float altura;
    private int edad;
    private final Animal animal;
    private final HistorialClinico historial;

    public FichaMedica(Animal animal) {
        this.fichaMedicaId = UUID.randomUUID();
        this.animal = animal;
        this.peso = animal.getPeso();
        this.altura = (float) animal.getAltura();
        this.edad = animal.getEdad();
        this.historial = new HistorialClinico(animal);
    }

    public String obtenerDatosTecnicos() {
        return "Ficha[" + fichaMedicaId + "] " +
               animal.getNombre() + " | " + animal.getTipoAnimal() +
               " | peso=" + peso + "kg | altura=" + altura + "m | edad=" + edad + " años";
    }

    public void exportar(Exportador estrategia) {
        estrategia.exportar(this);
    }

    public void actualizarDatos(double peso, float altura, int edad) {
        this.peso = peso;
        this.altura = altura;
        this.edad = edad;
    }

    public void agregarTratamiento(Tratamiento tratamiento) {
        historial.agregarTratamiento(tratamiento);
    }

    public void agregarComentarioMedico(ComentarioMedico comentario) {
        historial.agregarComentario(comentario);
    }

    public void registrarVisitaDomicilio(com.gudboy.domain.seguimiento.model.Visita visita) {
        historial.agregarVisita(visita);
    }

    // --- getters ---

    public UUID getFichaMedicaId()       { return fichaMedicaId; }
    public double getPeso()              { return peso; }
    public float getAltura()             { return altura; }
    public int getEdad()                 { return edad; }
    public Animal getAnimal()            { return animal; }
    public HistorialClinico getHistorial() { return historial; }
}