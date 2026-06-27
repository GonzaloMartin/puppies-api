package com.gudboy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.animal.factory.FabricaAnimalDomestico;
import com.gudboy.domain.animal.factory.FabricaAnimalSalvaje;
import com.gudboy.dto.AnimalDTO;
import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.comentarioMedico.ComentarioMedico;
import com.gudboy.domain.fichaMedica.exportador.ExportadorExcel;
import com.gudboy.domain.fichaMedica.exportador.ExportadorPDF;
import com.gudboy.domain.fichaMedica.model.FichaMedica;
import com.gudboy.domain.tratamiento.TipoTratamiento;
import com.gudboy.domain.tratamiento.Tratamiento;
import com.gudboy.repository.IFichaMedicaRepository;
import com.gudboy.service.FichaMedicaService;

class FichaMedicaTest {

    private static class RepositorioEnMemoria implements IFichaMedicaRepository {
        private final Map<UUID, FichaMedica> store = new HashMap<>();

        @Override public void guardar(FichaMedica f)    { store.put(f.getFichaMedicaId(), f); }
        @Override public void actualizar(FichaMedica f)  { store.put(f.getFichaMedicaId(), f); }
        @Override public Optional<FichaMedica> buscarPorId(UUID id) { return Optional.ofNullable(store.get(id)); }
        @Override public List<FichaMedica> listarTodas() { return new ArrayList<>(store.values()); }

        @Override
        public FichaMedica getByAnimalId(UUID idAnimal) {
            return null;
        }

        @Override
        public void update(FichaMedica ficha) {

        }
    }

    private FichaMedicaService service;
    private Animal perro;
    private Animal halcon;

    @BeforeEach
    void setUp() {
        service = new FichaMedicaService(new RepositorioEnMemoria());
        perro  = new FabricaAnimalDomestico().crearAnimal(new AnimalDTO("Rex",   "Perro",  0.6, 25.0, 5, "Sano"));
        halcon = new FabricaAnimalSalvaje()  .crearAnimal(new AnimalDTO("Falco", "Halcón", 0.4,  1.2, 2, "Sano"));
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

    @Test
    void crearFicha_historialNoEsNulo() {
        FichaMedica ficha = service.crearFicha(perro);

        assertNotNull(ficha.getHistorial());
        assertEquals(perro, ficha.getHistorial().getAnimal());
    }

    // --- actualización ---

    @Test
    void actualizarDatos_modificaValores() {
        FichaMedica ficha = service.crearFicha(perro);

        service.actualizarDatos(ficha.getFichaMedicaId(), 27.5, 0.65f, 6);

        assertEquals(27.5, ficha.getPeso());
        assertEquals(0.65f, ficha.getAltura(), 0.001f);
        assertEquals(6, ficha.getEdad());
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

        assertDoesNotThrow(() -> service.exportarFicha(ficha.getFichaMedicaId(), new ExportadorPDF("A4")));
    }

    @Test
    void exportar_Excel_noLanzaExcepcion() {
        FichaMedica ficha = service.crearFicha(halcon);

        assertDoesNotThrow(() -> service.exportarFicha(ficha.getFichaMedicaId(), new ExportadorExcel("Ficha-Halcon")));
    }

    @Test
    void exportar_mismafichaDiferentesEstrategias_sonIntercambiables() {
        UUID id = service.crearFicha(perro).getFichaMedicaId();

        assertDoesNotThrow(() -> service.exportarFicha(id, new ExportadorPDF("A4")));
        assertDoesNotThrow(() -> service.exportarFicha(id, new ExportadorExcel("Hoja1")));
    }

    // --- historial: tratamientos ---

    @Test
    void agregarTratamiento_aparece_enHistorial() {
        FichaMedica ficha = service.crearFicha(perro);
        Tratamiento t = new Tratamiento(TipoTratamiento.COLOCAR_VACUNA);

        service.agregarTratamiento(ficha.getFichaMedicaId(), t);

        assertEquals(1, ficha.getHistorial().getListaTratamiento().size());
        assertEquals(TipoTratamiento.COLOCAR_VACUNA,
                ficha.getHistorial().getListaTratamiento().get(0).getTipoTratamientoEnum());
    }

    @Test
    void agregarVariosTratamientos_todosEnHistorial() {
        FichaMedica ficha = service.crearFicha(perro);

        service.agregarTratamiento(ficha.getFichaMedicaId(), new Tratamiento(TipoTratamiento.CONTROL_DE_PARASITOS));
        service.agregarTratamiento(ficha.getFichaMedicaId(), new Tratamiento(TipoTratamiento.CHEQUEAR_NUTRICION));

        assertEquals(2, ficha.getHistorial().getListaTratamiento().size());
    }

    // --- historial: comentarios médicos ---

    @Test
    void agregarComentarioMedico_aparece_enHistorial() {
        FichaMedica ficha = service.crearFicha(perro);
        Veterinario vet = new Veterinario("Ana", "López", "ana@gudboy.com", "1234", 99, "Clínica");
        ComentarioMedico comentario;
        comentario = new ComentarioMedico(vet, "Animal en buen estado", LocalDateTime.now());

        service.agregarComentarioMedico(ficha.getFichaMedicaId(), comentario);

        assertEquals(1, ficha.getHistorial().getListaComentario().size());
        assertEquals("Animal en buen estado",
                ficha.getHistorial().getListaComentario().get(0).getCasillaComentario());
    }

    @Test
    void historialInicial_estaVacio() {
        FichaMedica ficha = service.crearFicha(perro);

        assertTrue(ficha.getHistorial().getListaTratamiento().isEmpty());
        assertTrue(ficha.getHistorial().getListaComentario().isEmpty());
    }

    // --- listado ---

    @Test
    void listarTodas_devuelveFichasCreadas() {
        service.crearFicha(perro);
        service.crearFicha(halcon);

        assertEquals(2, service.listarTodas().size());
    }
}
