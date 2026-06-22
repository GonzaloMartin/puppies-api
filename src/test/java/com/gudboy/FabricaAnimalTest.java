package com.gudboy;

import com.gudboy.domain.animal.factory.FabricaAnimal;
import com.gudboy.domain.animal.factory.FabricaAnimalDomestico;
import com.gudboy.domain.animal.factory.FabricaAnimalSalvaje;
import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.animal.model.AnimalDomestico;
import com.gudboy.domain.animal.model.AnimalSalvaje;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FabricaAnimalTest {

    // -------------------------------------------------------------------------
    // Fábrica doméstica
    // -------------------------------------------------------------------------

    @Test
    void fabricaDomestica_creaAnimalDomestico() {
        FabricaAnimal fabrica = new FabricaAnimalDomestico();
        Animal animal = fabrica.crearAnimal("Firulais", "Perro", 0.5, 10.0, 3, "Sano");

        assertInstanceOf(AnimalDomestico.class, animal);
        assertEquals("DOMESTICO", animal.getTipoAnimal());
    }

    @Test
    void fabricaDomestica_animalEsAdoptablePorDefecto() {
        FabricaAnimal fabrica = new FabricaAnimalDomestico();
        Animal animal = fabrica.crearAnimal("Michi", "Gato", 0.3, 4.0, 2, "Sano");

        assertTrue(animal.esAdoptable(),
                "Un animal doméstico recién ingresado debe ser adoptable");
    }

    @Test
    void fabricaDomestica_animalNoAdoptableSiEnTratamiento() {
        FabricaAnimal fabrica = new FabricaAnimalDomestico();
        AnimalDomestico animal = (AnimalDomestico)
                fabrica.crearAnimal("Loro", "Loro", 0.2, 0.5, 1, "Fractura ala");

        animal.ponerEnTratamiento();

        assertFalse(animal.esAdoptable(),
                "Un animal doméstico en tratamiento NO debe ser adoptable");
    }

    // -------------------------------------------------------------------------
    // Fábrica salvaje
    // -------------------------------------------------------------------------

    @Test
    void fabricaSalvaje_creaAnimalSalvaje() {
        FabricaAnimal fabrica = new FabricaAnimalSalvaje();
        Animal animal = fabrica.crearAnimal("Falco", "Halcón", 0.4, 1.2, 2, "Sano");

        assertInstanceOf(AnimalSalvaje.class, animal);
        assertEquals("SALVAJE", animal.getTipoAnimal());
    }

    @Test
    void fabricaSalvaje_animalNuncaEsAdoptable() {
        FabricaAnimal fabrica = new FabricaAnimalSalvaje();
        Animal animal = fabrica.crearAnimal("Zorro", "Zorro", 0.6, 5.0, 4, "Sano");

        assertFalse(animal.esAdoptable(),
                "Un animal salvaje NUNCA debe ser adoptable");
    }

    @Test
    void fabricaSalvaje_conHabitat_creaAnimalConHabitat() {
        FabricaAnimalSalvaje fabrica = new FabricaAnimalSalvaje();
        AnimalSalvaje pinguino = (AnimalSalvaje) fabrica.crearAnimal(
                "Pingu", "Pingüino", 0.5, 4.0, 3, "Sano", "Costa patagónica");

        assertEquals("Costa patagónica", pinguino.getHabitatNatural());
    }

    // -------------------------------------------------------------------------
    // Polimorfismo: misma interfaz, distintos comportamientos
    // -------------------------------------------------------------------------

    @Test
    void mismaInterfaz_comportamientoDistintoPorFabrica() {
        FabricaAnimal fabricaDomestica = new FabricaAnimalDomestico();
        FabricaAnimal fabricaSalvaje   = new FabricaAnimalSalvaje();

        Animal perro = fabricaDomestica.crearAnimal("Rex", "Perro", 0.6, 25.0, 5, "Sano");
        Animal halcon = fabricaSalvaje.crearAnimal("Atalaya", "Halcón", 0.3, 0.9, 2, "Sano");

        assertTrue(perro.esAdoptable(),   "El perro debe ser adoptable");
        assertFalse(halcon.esAdoptable(), "El halcón NO debe ser adoptable");
        assertNotEquals(perro.getTipoAnimal(), halcon.getTipoAnimal());
    }
}
