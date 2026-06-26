package com.gudboy;

import com.gudboy.domain.Usuario.EstadoCivil;
import com.gudboy.domain.Usuario.Ocupacion;
import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.Usuario.Visitador;
import com.gudboy.domain.animal.model.Adopcion;
import com.gudboy.domain.animal.model.AnimalDomestico;
import com.gudboy.domain.animal.model.AnimalSalvaje;
import com.gudboy.domain.animal.factory.FabricaAnimalDomestico;
import com.gudboy.domain.animal.factory.FabricaAnimalSalvaje;
import com.gudboy.repository.AdopcionRepositoryEnMemoria;
import com.gudboy.service.AdopcionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdopcionTest {

    private AdopcionService adopcionService;
    private Veterinario vet;
    private Visitador adoptante;
    private AnimalDomestico perro;
    private AnimalDomestico gato;

    @BeforeEach
    void setUp() {
        adopcionService = new AdopcionService(new AdopcionRepositoryEnMemoria());
        vet = new Veterinario("Ana", "López", "ana@gudboy.com", "111", 1001, "Clínica");
        adoptante = new Visitador("Juan", "Pérez", "juan@mail.com", "222",
                EstadoCivil.SOLTERO, Ocupacion.EMPLEADO, "Compañía", "Perro", false);
        perro = (AnimalDomestico) new FabricaAnimalDomestico()
                .crearAnimal("Rex", "Perro", 0.6, 25.0, 5, "Sano");
        gato  = (AnimalDomestico) new FabricaAnimalDomestico()
                .crearAnimal("Mishi", "Gato", 0.3, 4.0, 3, "Sano");
    }

    @Test
    void adoptarAnimalDisponible_debeRegistrarseCorrectamente() {
        adopcionService.RegistrarAdopcion(perro, null, adoptante, vet);

        assertEquals(1, adopcionService.listarAdopciones().size());
        assertFalse(perro.esAdoptable(), "Después de adoptado no debe ser adoptable");
    }

    @Test
    void adoptarDosAnimales_debeGuardarAmbos() {
        adopcionService.RegistrarAdopcion(perro, gato, adoptante, vet);

        Adopcion a = adopcionService.listarAdopciones().get(0);
        assertEquals(2, a.getAnimales().size());
    }

    @Test
    void adoptarAnimalEnTratamiento_debeLanzarExcepcion() {
        perro.ponerEnTratamiento();

        assertThrows(IllegalStateException.class,
                () -> adopcionService.RegistrarAdopcion(perro, null, adoptante, vet));
    }

    @Test
    void animalSalvaje_nuncaPuedeAdoptarse() {
        AnimalSalvaje halcon = (AnimalSalvaje) new FabricaAnimalSalvaje()
                .crearAnimal("Falco", "Halcón", 0.4, 1.2, 2, "Sano");

        assertFalse(halcon.esAdoptable());
    }
}
