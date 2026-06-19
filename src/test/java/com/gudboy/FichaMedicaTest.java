package com.gudboy;

import com.gudboy.domain.animal.factory.FabricaAnimalDomestico;
import com.gudboy.domain.animal.factory.FabricaAnimalSalvaje;
import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.fichaMedica.exportador.Exportador;
import com.gudboy.domain.fichaMedica.exportador.ExportadorExcel;
import com.gudboy.domain.fichaMedica.exportador.ExportadorPDF;
import com.gudboy.domain.fichaMedica.model.FichaMedica;
import com.gudboy.repository.IFichaMedicaRepository;
import com.gudboy.service.FichaMedicaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FichaMedicaTest {

    // Repositorio en memoria para los tests (sin frameworks de mock)
    private static class RepositorioEnMemoria implements IFichaMedicaRepository {
        private final Map<UUID, FichaMedica> store = new HashMap<>();

        @Override public void guardar(FichaMedica f)   { store.put(f.getFichaMedicaId(), f); }
        @Override public void actualizar(FichaMedica f) { store.put(f.getFichaMedicaId(), f); }
        @Override public Optional<FichaMedica> buscarPorId(UUID id) { return Optional.ofNullable(store.get(id)); }
        @Override public List<FichaMedica> listarTodas() { return new ArrayList<>(store.values()); }
    }

    private FichaMedicaService service;
    private Animal perro;
    private Animal halcon;

    @BeforeEach
    void setUp() {
        service = new FichaMedicaService(new RepositorioEnMemoria());
        perro  = new FabricaAnimalDomestico().crearAnimal("Rex",   "Perro",  0.6, 25.0, 5, "Sano");
        halcon = new FabricaAnimalSalvaje()  .crearAnimal("Falco", "Halcón", 0.4,  1.2, 2, "Sano");
    }

    // --- creación ---

    @Test
    void crearFicha_inicializaDatosDesdeAnimal() {
        FichaMedica ficha = service.crearFicha(perro);

        assertNotNull(ficha.getFichaMedicaId());
        assertEquals(25.0, ficha.getPeso());
        assertEquals(0.6f, ficha.getAltura(), 0.001f);
        assertEquals(5,    ficha.getEdad());
        assertEquals(perro, ficha.getAnimal());
    }

    @Test
    void crearFicha_persisteEnRepositorio() {
        FichaMedica ficha = service.crearFicha(perro);

        assertTrue(service.buscarPorId(ficha.getFichaMedicaId()).isPresent());
    }

    // --- actualización ---

    @Test
    void actualizarDatos_modificaValoresYRegistraHistorial() {
        FichaMedica ficha = service.crearFicha(perro);

        service.actualizarDatos(ficha.getFichaMedicaId(), 27.5f, 0.65f, 6);

        assertEquals(27.5, ficha.getPeso());
        assertEquals(0.65f, ficha.getAltura(), 0.001f);
        assertEquals(6, ficha.getEdad());
        assertFalse(ficha.getHistorial().getEntradas().isEmpty());
    }

    @Test
    void actualizarDatos_fichaInexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> service.actualizarDatos(UUID.randomUUID(), 10.0, 0.3f, 1));
    }

    // --- exportación (Strategy) ---

    @Test
    void exportar_PDF_noLanzaExcepcion() {
        FichaMedica ficha = service.crearFicha(perro);
        Exportador pdf = new ExportadorPDF("A4");

        assertDoesNotThrow(() -> service.exportarFicha(ficha.getFichaMedicaId(), pdf));
    }

    @Test
    void exportar_Excel_noLanzaExcepcion() {
        FichaMedica ficha = service.crearFicha(halcon);
        Exportador excel = new ExportadorExcel("Ficha-Halcon");

        assertDoesNotThrow(() -> service.exportarFicha(ficha.getFichaMedicaId(), excel));
    }

    @Test
    void exportar_mismafichaDiferentesEstrategias_sonIntercambiables() {
        FichaMedica ficha = service.crearFicha(perro);
        UUID id = ficha.getFichaMedicaId();

        // El mismo ID exporta con cualquier estrategia sin cambiar la ficha
        assertDoesNotThrow(() -> service.exportarFicha(id, new ExportadorPDF("A4")));
        assertDoesNotThrow(() -> service.exportarFicha(id, new ExportadorExcel("Hoja1")));
    }

    // --- listado ---

    @Test
    void listarTodas_devuelveFichasCreadas() {
        service.crearFicha(perro);
        service.crearFicha(halcon);

        assertEquals(2, service.listarTodas().size());
    }
}
