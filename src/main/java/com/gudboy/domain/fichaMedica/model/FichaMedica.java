package com.gudboy.domain.fichaMedica.model;

import java.util.UUID;

import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.comentarioMedico.ComentarioMedico;
import com.gudboy.domain.fichaMedica.exportador.Exportador;
import com.gudboy.domain.historialClinico.HistorialClinico;
import com.gudboy.domain.tratamiento.Tratamiento;
import com.gudboy.dto.FichaMedicaDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "ficha_medica")
public class FichaMedica {

    @Id
    @Column(name = "id")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID fichaMedicaId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "animal_id")
    private Animal animal;

    @Column(name = "peso")
    private double peso;

    @Column(name = "altura")
    private float altura;

    @Column(name = "edad")
    private int edad;

    @Transient
    private HistorialClinico historial;

    protected FichaMedica() { }

    public FichaMedica(Animal animal) {
        this.fichaMedicaId = UUID.randomUUID();
        this.animal        = animal;
        this.peso          = animal.getPeso();
        this.altura        = (float) animal.getAltura();
        this.edad          = animal.getEdad();
        this.historial     = new HistorialClinico(animal);
    }

    public FichaMedica(UUID id, Animal animal, double peso, float altura, int edad) {
        this.fichaMedicaId = id;
        this.animal        = animal;
        this.peso          = peso;
        this.altura        = altura;
        this.edad          = edad;
        this.historial     = new HistorialClinico(animal);
    }

    @PostLoad
    protected void postLoad() {
        this.historial = new HistorialClinico(this.animal);
    }

    public FichaMedicaDTO toDTO() {
        return new FichaMedicaDTO(
            fichaMedicaId,
            animal.getNombre(),
            animal.getTipoAnimal(),
            animal.getEspecie(),
            peso, altura, edad,
            historial.getListaTratamiento().size(),
            historial.getListaComentario().size(),
            historial.getListaVisitas().size()
        );
    }

    public String obtenerDatosTecnicos() {
        return "Ficha[" + fichaMedicaId + "] " +
               animal.getNombre() + " | " + animal.getTipoAnimal() +
               " | peso=" + peso + "kg | altura=" + altura + "m | edad=" + edad + " años";
    }

    public void exportar(Exportador estrategia) {
        estrategia.exportar(toDTO());
    }

    public void actualizarDatos(double peso, float altura, int edad) {
        this.peso   = peso;
        this.altura = altura;
        this.edad   = edad;
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

    public void setFichaMedicaId(UUID id)      { this.fichaMedicaId = id; }

    public UUID            getFichaMedicaId()  { return fichaMedicaId; }
    public double          getPeso()           { return peso; }
    public float           getAltura()         { return altura; }
    public int             getEdad()           { return edad; }
    public Animal          getAnimal()         { return animal; }
    public HistorialClinico getHistorial()     { return historial; }
}
