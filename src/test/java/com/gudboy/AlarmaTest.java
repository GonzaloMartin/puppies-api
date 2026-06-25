package com.gudboy;

import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.alarma.IHistorialClinicoService;
import com.gudboy.domain.alarma.model.Alarma;
import com.gudboy.domain.tratamiento.TipoTratamiento;
import com.gudboy.repository.AlarmaRepositoryEnMemoria;
import com.gudboy.service.AlarmaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AlarmaTest {

    private AlarmaService alarmaService;
    private AlarmaRepositoryEnMemoria repository;
    private HistorialClinicoServiceMock historialMock;
    private Veterinario veterinarioDummy;

    class HistorialClinicoServiceMock implements IHistorialClinicoService {
        boolean registrarAtencionLlamado = false;
        boolean finalizarTratamientosLlamado = false;

        @Override
        public void registrarAtencion(UUID idAnimal, String detalle, Veterinario veterinario) {
            registrarAtencionLlamado = true;
        }

        @Override
        public void finalizarTratamientosActivos(UUID idAnimal, List<TipoTratamiento> accionesFinalizadas) {
            finalizarTratamientosLlamado = true;
        }
    }

    @BeforeEach
    void setUp() {
        // 1. Preparamos el repositorio en memoria
        repository = new AlarmaRepositoryEnMemoria();

        // 2. Simulamos el servicio de historial clínico para aislar el test de la BD
        historialMock = new HistorialClinicoServiceMock();

        // 3. Instanciamos el servicio inyectando ambas dependencias
        alarmaService = new AlarmaService(repository, historialMock);

        // 4. Preparamos un Veterinario de prueba para la trazabilidad
        // Nota: Si tu clase Veterinario no tiene este constructor o setters, ajusta esto a tu modelo real.
        // Aquí usamos un objeto anónimo básico para sortear la validación de la firma.
        veterinarioDummy = new Veterinario() {
            @Override
            public String getNombre() { return "Juan"; }
            @Override
            public String getApellido() { return "Perez"; }
        };
    }

    // ==========================================
    // 1. TESTS DE LÓGICA DE DOMINIO (MODELO)
    // ==========================================

    @Test
    void reprogramarAlarma_DebeIncrementarFechaSegunFrecuencia() {
        LocalDateTime fechaInicial = LocalDateTime.of(2026, 6, 1, 10, 0);
        Alarma alarma = new Alarma(1, UUID.randomUUID(), "Antiparasitario", "Dosis mensual", 30, fechaInicial, List.of(TipoTratamiento.COLOCAR_ANTIPARASITARIOS));

        alarma.reprogramar();

        LocalDateTime fechaEsperada = fechaInicial.plusDays(30);
        assertEquals(fechaEsperada.toString(), alarma.getFechaProximoDisparo(), "La fecha de disparo debe sumar exactamente la frecuencia en días.");
        assertEquals("ACTIVA", alarma.getEstado(), "El estado debe volver a ACTIVA al reprogramar.");
        assertFalse(alarma.isCompletada(), "La alarma reprogramada no debe estar completada.");
    }

    @Test
    void marcarCompletado_DebeCambiarEstadoYFlag() {
        Alarma alarma = new Alarma(1, UUID.randomUUID(), "Control Nutricional", "Revisión de dieta", 1, LocalDateTime.now(), List.of(TipoTratamiento.CHEQUEAR_NUTRICION));

        alarma.marcarCompletado();

        assertTrue(alarma.isCompletada(), "El flag completada debe ser true.");
        assertEquals("COMPLETADA", alarma.getEstado(), "El estado interno debe ser COMPLETADA.");
    }

    @Test
    void marcarTratamientoFinalizado_DebeCambiarEstadoAFinalizadoYCompletada() {
        Alarma alarma = new Alarma(1, UUID.randomUUID(), "Control final", "Alta médica", 1, LocalDateTime.now(), List.of(TipoTratamiento.CONTROL_DE_PARASITOS));

        alarma.marcarTratamientoFinalizado();

        assertTrue(alarma.isCompletada(), "El flag completada debe ser true.");
        assertEquals("FINALIZADO", alarma.getEstado(), "El estado interno debe ser FINALIZADO.");
    }

    @Test
    void escribirComentario_DebeConcatenarElComentarioALaDescripcionConFirma() {
        Alarma alarma = new Alarma(1, UUID.randomUUID(), "Peso", "Controlar peso", 1, LocalDateTime.now(), List.of(TipoTratamiento.COMPROBAR_PESO_TAMANIO));

        alarma.escribirComentario("Pesa 15kg, todo en orden", veterinarioDummy);

        assertTrue(alarma.getDescripcion().contains("Controlar peso"), "Debe mantener la descripción original.");
        assertTrue(alarma.getDescripcion().contains("Nota (Juan Perez): Pesa 15kg, todo en orden"), "Debe incluir el comentario formateado con la firma del veterinario.");
    }

    @Test
    void verificarFecha_DebeRetornarTrueSiLaFechaEstaVencida() {
        LocalDateTime fechaPasada = LocalDateTime.now().minusDays(1);
        Alarma alarmaVencida = new Alarma(1, UUID.randomUUID(), "Vacuna", "Aplicar dosis anual", 365, fechaPasada, List.of(TipoTratamiento.COLOCAR_VACUNA));

        assertTrue(alarmaVencida.verificarFecha(), "Debe retornar true porque la fecha programada ya pasó.");
    }

    // ==========================================
    // 2. TESTS DE SERVICIO Y ARQUITECTURA (OBSERVER & HISTORIAL)
    // ==========================================

    @Test
    void crearAlarma_DebePersistirseEnElRepositorio() {
        Alarma nuevaAlarma = new Alarma(0, UUID.randomUUID(), "Test DB", "Prueba de guardado", 1, LocalDateTime.now(), List.of(TipoTratamiento.CONTROL_DE_PARASITOS));

        alarmaService.crearAlarma(nuevaAlarma);

        List<Alarma> alarmasGuardadas = alarmaService.obtenerTodas();
        assertEquals(1, alarmasGuardadas.size(), "Debe haber exactamente 1 alarma en el repositorio.");
        assertEquals("Test DB", alarmasGuardadas.get(0).getTitulo(), "El título debe coincidir con la alarma guardada.");
    }

    @Test
    void actualizarAlarma_DebePersistirCambiosYNotificarAlObserver() {
        // 1. Arrange
        Alarma alarma = new Alarma(0, UUID.randomUUID(), "Original", "Desc", 1, LocalDateTime.now(), List.of(TipoTratamiento.CONTROL_DE_PARASITOS));
        alarmaService.crearAlarma(alarma);
        Alarma guardada = alarmaService.obtenerTodas().get(0);

        final boolean[] fueNotificado = {false};
        alarmaService.suscribir(a -> fueNotificado[0] = true);

        // 2. Act
        guardada.setTitulo("Editado");
        alarmaService.actualizarAlarma(guardada);

        // 3. Assert
        Alarma actualizada = alarmaService.obtenerAlarma(guardada.getId());
        assertEquals("Editado", actualizada.getTitulo(), "El título debió cambiar en el repositorio.");
        assertTrue(fueNotificado[0], "El observer debió ser notificado al actualizar.");
    }

    @Test
    void atenderAlarma_SinFinalizar_DebeMarcarCompletadaAgregarComentarioYNotificar() {
        // 1. Arrange
        Alarma alarma = new Alarma(0, UUID.randomUUID(), "Revisión", "Control de rutina", 1, LocalDateTime.now(), List.of(TipoTratamiento.CHEQUEAR_NUTRICION));
        alarmaService.crearAlarma(alarma);
        int idGenerado = alarmaService.obtenerTodas().get(0).getId();

        final boolean[] fueNotificado = {false};
        alarmaService.suscribir(a -> fueNotificado[0] = true);

        // 2. Act (false = no finaliza el tratamiento)
        alarmaService.atenderAlarma(idGenerado, "Todo normal", false, veterinarioDummy);

        // 3. Assert
        Alarma atendida = alarmaService.obtenerAlarma(idGenerado);
        assertEquals("COMPLETADA", atendida.getEstado());
        assertTrue(atendida.getDescripcion().contains("Todo normal"), "El comentario debió guardarse.");
        assertTrue(atendida.getDescripcion().contains("Juan Perez"), "La firma del veterinario debe estar registrada.");
        assertTrue(fueNotificado[0], "El observer debió ser notificado.");
        assertTrue(historialMock.registrarAtencionLlamado, "Debe registrar la atención en el historial clínico.");
        assertFalse(historialMock.finalizarTratamientosLlamado, "No debe finalizar tratamientos activos en el historial clínico.");
    }

    @Test
    void atenderAlarma_ConFinalizar_DebeMarcarFinalizadoYNotificar() {
        // 1. Arrange
        Alarma alarma = new Alarma(0, UUID.randomUUID(), "Cura", "Última dosis", 1, LocalDateTime.now(), List.of(TipoTratamiento.COLOCAR_ANTIPARASITARIOS));
        alarmaService.crearAlarma(alarma);
        int idGenerado = alarmaService.obtenerTodas().get(0).getId();

        final boolean[] fueNotificado = {false};
        alarmaService.suscribir(a -> fueNotificado[0] = true);

        // 2. Act (true = finaliza el tratamiento)
        alarmaService.atenderAlarma(idGenerado, "Paciente dado de alta", true, veterinarioDummy);

        // 3. Assert
        Alarma atendida = alarmaService.obtenerAlarma(idGenerado);
        assertEquals("FINALIZADO", atendida.getEstado());
        assertTrue(atendida.getDescripcion().contains("Paciente dado de alta"));
        assertTrue(fueNotificado[0], "El observer debió ser notificado.");
        assertTrue(historialMock.registrarAtencionLlamado, "Debe registrar la atención en el historial clínico.");
        assertTrue(historialMock.finalizarTratamientosLlamado, "Debe finalizar tratamientos activos en el historial clínico.");
    }

    @Test
    void verificarEstadoAlarmas_SoloDebeNotificarAlarmasVencidasYNoCompletadas() {
        // 1. Arrange
        alarmaService.crearAlarma(new Alarma(0, UUID.randomUUID(), "Vencida", "Debe disparar", 1, LocalDateTime.now().minusHours(1), List.of(TipoTratamiento.COLOCAR_ANTIPARASITARIOS)));
        alarmaService.crearAlarma(new Alarma(0, UUID.randomUUID(), "Futura", "No debe disparar", 1, LocalDateTime.now().plusDays(1), List.of(TipoTratamiento.CONTROL_DE_PARASITOS)));

        Alarma completada = new Alarma(0, UUID.randomUUID(), "Completada", "Ya se hizo", 1, LocalDateTime.now().minusHours(2), List.of(TipoTratamiento.CHEQUEAR_NUTRICION));
        completada.marcarCompletado();
        alarmaService.crearAlarma(completada);

        List<String> alarmasNotificadas = new ArrayList<>();
        alarmaService.suscribir(alarma -> alarmasNotificadas.add(alarma.getTitulo()));

        // 2. Act
        alarmaService.verificarEstadoAlarmas();

        // 3. Assert
        assertEquals(1, alarmasNotificadas.size(), "Solo una alarma debió disparar notificación por estado vencido.");
        assertEquals("Vencida", alarmasNotificadas.get(0), "La alarma notificada debe ser la que estaba vencida y activa.");
    }
}