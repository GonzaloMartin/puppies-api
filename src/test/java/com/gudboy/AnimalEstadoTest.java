package com.gudboy;

import com.gudboy.domain.animal.State.EstadoEnTratamiento;
import com.gudboy.domain.animal.State.EstadoSaludable;
import com.gudboy.domain.animal.factory.FabricaAnimalDomestico;
import com.gudboy.domain.animal.factory.FabricaAnimalSalvaje;
import com.gudboy.dto.AnimalDTO;
import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.animal.model.AnimalDomestico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalEstadoTest {

    private AnimalDomestico domestico;
    private Animal salvaje;

    @BeforeEach
    void setUp() {
        domestico = (AnimalDomestico) new FabricaAnimalDomestico()
                .crearAnimal(new AnimalDTO("Rex",   "Perro",  0.6, 25.0, 5, "Sano"));
        salvaje = new FabricaAnimalSalvaje()
                .crearAnimal(new AnimalDTO("Falco", "Halcón", 0.4,  1.2, 2, "Sano"));
    }

    // --- estado de salud inicial ---

    @Test
    void animal_creadoConEstadoSaludable() {
        assertInstanceOf(EstadoSaludable.class, domestico.getEstadoDeSalud());
    }

    @Test
    void animal_creadoEsAdoptable() {
        assertTrue(domestico.esAdoptable());
    }

    // --- transiciones de salud ---

    @Test
    void ponerEnTratamiento_cambiaEstadoAEnTratamiento() {
        domestico.ponerEnTratamiento();

        assertInstanceOf(EstadoEnTratamiento.class, domestico.getEstadoDeSalud());
    }

    @Test
    void ponerEnTratamiento_animalYaNoEsAdoptable() {
        domestico.ponerEnTratamiento();

        assertFalse(domestico.esAdoptable());
    }

    @Test
    void disponibilizar_desde_tratamiento_vuelveSaludable() {
        domestico.ponerEnTratamiento();
        domestico.disponibilizar();

        assertInstanceOf(EstadoSaludable.class, domestico.getEstadoDeSalud());
    }

    @Test
    void disponibilizar_restauraAdoptabilidad() {
        domestico.ponerEnTratamiento();
        domestico.disponibilizar();

        assertTrue(domestico.esAdoptable());
    }

    @Test
    void ponerEnTratamiento_estadoYaEnTratamiento_noHaceNada() {
        domestico.ponerEnTratamiento();
        domestico.ponerEnTratamiento(); // segunda llamada no debe romper

        assertInstanceOf(EstadoEnTratamiento.class, domestico.getEstadoDeSalud());
    }

    // --- transiciones de adopción ---

    @Test
    void adoptar_cambia_a_estadoAdoptado() {
        domestico.adoptar();

        assertFalse(domestico.esAdoptable());
    }

    @Test
    void adoptar_animalEnTratamiento_lanzaExcepcion() {
        domestico.ponerEnTratamiento();

        assertThrows(IllegalStateException.class, () -> domestico.adoptar());
    }

    @Test
    void adoptar_dosVeces_lanzaExcepcion() {
        domestico.adoptar();

        assertThrows(IllegalStateException.class, () -> domestico.adoptar());
    }

    @Test
    void disponibilizarAdopcion_despuesDeAdoptar_permiteAdoptarDeNuevo() {
        domestico.adoptar();
        domestico.disponibilizarAdopcion();

        assertTrue(domestico.esAdoptable());
    }

    // --- salvaje siempre inAdoptable ---

    @Test
    void salvaje_nuncaEsAdoptable_independientementeDelEstadoDeSalud() {
        assertFalse(salvaje.esAdoptable());

        salvaje.ponerEnTratamiento();
        assertFalse(salvaje.esAdoptable());

        salvaje.disponibilizar();
        assertFalse(salvaje.esAdoptable());
    }
}
