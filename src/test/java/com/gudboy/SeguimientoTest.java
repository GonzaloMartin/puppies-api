package com.gudboy;

import com.gudboy.domain.Usuario.EstadoCivil;
import com.gudboy.domain.Usuario.Ocupacion;
import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.Usuario.Visitador;
import com.gudboy.domain.animal.model.Adopcion;
import com.gudboy.domain.animal.model.AnimalDomestico;
import com.gudboy.domain.fichaMedica.model.FichaMedica;
import com.gudboy.domain.seguimiento.adapter.*;
import com.gudboy.domain.seguimiento.model.*;
import com.gudboy.domain.seguimiento.observer.*;
import com.gudboy.domain.seguimiento.service.*;
import com.gudboy.repository.FichaMedicaRepositoryEnMemoria;
import com.gudboy.repository.SeguimientoRepositoryEnMemoria;
import com.gudboy.repository.VisitaRepositoryEnMemoria;
import com.gudboy.service.SeguimientoService;
import com.gudboy.service.VisitaService;
import com.gudboy.controller.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SeguimientoTest {

    private SeguimientoService seguimientoService;
    private FichaMedicaRepositoryEnMemoria fichaMedicaRepository;
    private VisitaRepositoryEnMemoria visitaRepository;
    private VisitaService visitaService;
    private SeguimientoController seguimientoController;
    private VisitaController visitaController;

    private AnimalDomestico animal1;
    private AnimalDomestico animal2;
    private Visitador adoptante;
    private Adopcion adopcion;

    @BeforeEach
    void setUp() {
        // OPCIÓN POR DEFECTO: En Memoria (Para tests local)
        SeguimientoRepositoryEnMemoria seguimientoRepository = new SeguimientoRepositoryEnMemoria();
        fichaMedicaRepository = new FichaMedicaRepositoryEnMemoria();
        visitaRepository = new VisitaRepositoryEnMemoria();

        // OPCIÓN MYSQL:
        // IAnimalRepository animalRepoMySQL = new AnimalRepositoryMySQL();
        // IUsuarioRepository usuarioRepoMySQL = new UsuarioRepositoryMySQL();
        // IAdopcionRepository adopcionRepoMySQL = new AdopcionRepositoryMySQL(animalRepoMySQL, usuarioRepoMySQL);

        // seguimientoRepository = new SeguimientoRepositoryMySQL(adopcionRepoMySQL, usuarioRepoMySQL);
        // fichaMedicaRepository = new FichaMedicaRepositoryMySQL(animalRepoMySQL);
        // visitaRepository = new VisitaRepositoryEnMemoria(); // (o custom MySQL si hiciera falta)

        seguimientoService = new SeguimientoService(seguimientoRepository, fichaMedicaRepository);
        visitaService = new VisitaService(visitaRepository, seguimientoRepository, fichaMedicaRepository);
        seguimientoController = new SeguimientoController(seguimientoService);
        visitaController = new VisitaController(visitaService);

        animal1 = new AnimalDomestico("Firulais", "Perro", 0.5, 12.0, 3, "SALUDABLE");
        animal2 = new AnimalDomestico("Michi", "Gato", 0.25, 4.0, 2, "SALUDABLE");

        FichaMedica ficha1 = new FichaMedica(animal1);
        FichaMedica ficha2 = new FichaMedica(animal2);
        fichaMedicaRepository.guardar(ficha1);
        fichaMedicaRepository.guardar(ficha2);

        adoptante = new Visitador(
                "Gonzalo", "Martin", "gmontalvo@uade.edu.ar", "+5491112345678",
                EstadoCivil.SOLTERO, Ocupacion.ESTUDIANTE, "Compañía", "Perros y Gatos", false
        );

        Veterinario responsableAdopcion = new Veterinario("Juan", "Perez", "juan@gudboy.com", "+5491187654321", 12345, "Clínica general");

        adopcion = new Adopcion(animal1, animal2, adoptante, responsableAdopcion);
    }

    @Test
    void crearSeguimiento_DebeProgramarVisitasSemanalesYPersistir() {
        Seguimiento s = seguimientoService.crearSeguimiento(
                adopcion, adoptante, DiaSemana.SABADO, "10:00", "12:00", PreferenciaRecordatorio.WHATSAPP, 3
        );

        assertNotNull(s.getId(), "El ID del seguimiento no debe ser nulo");
        assertEquals(EstadoSeguimiento.ACTIVO, s.getEstado(), "El seguimiento debe iniciar en estado ACTIVO");
        assertEquals(DiaSemana.SABADO, s.getDiaSemana(), "El día de semana debe ser el configurado");
        assertEquals(3, s.getVisitas().size(), "Debe haber programado exactamente 3 visitas iniciales");

        assertTrue(seguimientoService.getById(s.getId()).isPresent(), "El seguimiento debe estar persistido en el repositorio");
    }

    @Test
    void evaluarRecordatorios_DebeEnviarAlerta_CuandoFaltanNDias() {
        Seguimiento s = seguimientoService.crearSeguimiento(
                adopcion, adoptante, DiaSemana.MIERCOLES, "14:00", "16:00", PreferenciaRecordatorio.WHATSAPP, 1
        );
        Visita visita = s.getVisitas().get(0);

        final boolean[] mensajeEnviado = {false};
        final String[] destinatarioFinal = {null};
        final String[] mensajeFinal = {null};

        IWhatsAppAdapter mockWhatsapp = (numero, mensaje) -> {
            mensajeEnviado[0] = true;
            destinatarioFinal[0] = numero;
            mensajeFinal[0] = mensaje;
            if ("true".equalsIgnoreCase(System.getProperty("verbose", "false"))) {
                System.out.println("[ALERTA OBSERVER WHATSAPP] Notificando a " + numero + ": " + mensaje);
            }
        };

        WhatsAppNotificacion strategy = new WhatsAppNotificacion(mockWhatsapp);
        visita.suscribir(strategy);

        ServicioRecordatorios servicioRecordatorios = new ServicioRecordatorios(2);

        LocalDate fechaSimuladaAlerta = visita.getFechaProgramada().minusDays(2);
        servicioRecordatorios.evaluarVisitas(List.of(visita), fechaSimuladaAlerta);

        assertTrue(mensajeEnviado[0], "El mensaje debió ser enviado");
        assertEquals("+5491112345678", destinatarioFinal[0], "El número de teléfono del adoptante debe ser el destinatario");
        assertTrue(mensajeFinal[0].contains(visita.getFechaProgramada().toString()), "El mensaje debe contener la fecha programada");

        mensajeEnviado[0] = false;
        servicioRecordatorios.evaluarVisitas(List.of(visita), LocalDate.now());
        assertFalse(mensajeEnviado[0], "No se debe enviar mensaje si la fecha de hoy no coincide con N días previos");
    }

    @Test
    void registrarResultadoVisita_DebeCompletarVisita_YRegistrarEncuestaYVinculoClinico() {
        Seguimiento s = seguimientoService.crearSeguimiento(
                adopcion, adoptante, DiaSemana.LUNES, "09:00", "11:00", PreferenciaRecordatorio.WHATSAPP, 1
        );
        Visita visita = s.getVisitas().get(0);

        Encuesta encuestaFavorable = new Encuesta(CalificacionEnum.BUENO, CalificacionEnum.BUENO, CalificacionEnum.REGULAR);
        assertTrue(encuestaFavorable.esFavorable(), "La encuesta debe ser considerada favorable");

        seguimientoService.registrarResultadoVisita(visita.getId(), encuestaFavorable, "Excelente cuidado, patio amplio y limpio.", true);

        assertTrue(visita.isCompletada(), "La visita debe quedar marcada como completada");
        assertNotNull(visita.getFechaReal(), "La fecha real debe estar registrada");
        assertEquals("Excelente cuidado, patio amplio y limpio.", visita.getComentarios());

        FichaMedica fm1 = fichaMedicaRepository.getByAnimalId(animal1.getId());
        FichaMedica fm2 = fichaMedicaRepository.getByAnimalId(animal2.getId());

        assertEquals(1, fm1.getHistorial().getListaVisitas().size(), "El historial del animal 1 debe tener 1 visita registrada");
        assertEquals(1, fm2.getHistorial().getListaVisitas().size(), "El historial del animal 2 debe tener 1 visita registrada");

        assertEquals(visita.getId(), fm1.getHistorial().getListaVisitas().get(0).getId(), "La visita registrada debe ser la completada");
    }

    @Test
    void registrarResultadoVisita_ConContinuarVisitasFalso_DebeFinalizarSeguimiento() {
        Seguimiento s = seguimientoService.crearSeguimiento(
                adopcion, adoptante, DiaSemana.VIERNES, "16:00", "18:00", PreferenciaRecordatorio.WHATSAPP, 1
        );
        Visita visita = s.getVisitas().get(0);

        Encuesta encuestaFinal = new Encuesta(CalificacionEnum.BUENO, CalificacionEnum.BUENO, CalificacionEnum.BUENO);

        seguimientoService.registrarResultadoVisita(visita.getId(), encuestaFinal, "Todo excelente. Seguimiento concluido con éxito.", false);

        assertEquals(EstadoSeguimiento.FINALIZADO, s.getEstado(), "El seguimiento debe estar en estado FINALIZADO");
        assertFalse(seguimientoService.estaActivo(s.getId()), "El seguimiento ya no debe figurar como activo");
    }

    @Test
    void encuesta_NoFavorable_DebeCalcularCorrectamente() {
        Encuesta encuestaMala = new Encuesta(CalificacionEnum.MALO, CalificacionEnum.BUENO, CalificacionEnum.MALO);
        assertFalse(encuestaMala.esFavorable(), "La encuesta debe ser desfavorable");
    }

    @Test
    void testVisitaControllerAndService_UmlStrict() {
        // 1. Create tracking via SeguimientoController (delegates to SeguimientoService)
        Seguimiento s = seguimientoController.crearSeguimiento(
                adopcion, adoptante, DiaSemana.MIERCOLES, "10:00", "12:00", PreferenciaRecordatorio.SMS, 3
        );
        
        // Let's populate the visitaRepository with the tracking's visits
        for (Visita v : s.getVisitas()) {
            visitaRepository.guardar(v);
        }
        
        // 2. Test VisitaController.listarPorSeguimiento
        List<Visita> list = visitaController.listarPorSeguimiento(s.getId());
        assertEquals(3, list.size(), "Debe haber 3 visitas programadas");
        
        Visita v1 = list.get(0);
        
        // 3. Test VisitaController.marcarCompletada
        visitaController.marcarCompletada(v1.getId());
        assertTrue(v1.isCompletada(), "La visita 1 debe estar completada");
        
        // 4. Test VisitaController.registrarResultado
        Visita v2 = list.get(1);
        Encuesta encuesta = new Encuesta(CalificacionEnum.BUENO, CalificacionEnum.BUENO, CalificacionEnum.BUENO);
        visitaController.registrarResultado(v2.getId(), encuesta, "Excelente todo", true);
        assertTrue(v2.isCompletada(), "La visita 2 debe estar completada");
        assertEquals("Excelente todo", v2.getComentarios());
        assertTrue(v2.requiereContinuarVisitas());
    }

    @Test
    void testRecordatorioSMS_DebeEnviarSMSCorrectamente() {
        Seguimiento s = seguimientoService.crearSeguimiento(
                adopcion, adoptante, DiaSemana.JUEVES, "14:00", "16:00", PreferenciaRecordatorio.SMS, 1
        );
        Visita visita = s.getVisitas().get(0);

        final boolean[] smsEnviado = {false};
        final String[] destinatarioSMS = {null};
        final String[] textoSMS = {null};

        ISMSAdapter mockSms = (numero, texto) -> {
            smsEnviado[0] = true;
            destinatarioSMS[0] = numero;
            textoSMS[0] = texto;
            if ("true".equalsIgnoreCase(System.getProperty("verbose", "false"))) {
                System.out.println("[ALERTA OBSERVER SMS] Enviando SMS a " + numero + ": " + texto);
            }
        };

        SMSNotificacion strategy = new SMSNotificacion(mockSms);
        visita.suscribir(strategy);

        visita.notificarRecordatorio();

        assertTrue(smsEnviado[0], "El SMS debió ser enviado");
        assertEquals("+5491112345678", destinatarioSMS[0], "El número debe coincidir");
        assertTrue(textoSMS[0].contains("Recordatorio de Visita"), "El contenido del SMS debe ser el correcto");
    }

    @Test
    void testRecordatorioEmail_DebeEnviarEmailCorrectamente() {
        Seguimiento s = seguimientoService.crearSeguimiento(
                adopcion, adoptante, DiaSemana.VIERNES, "15:00", "17:00", PreferenciaRecordatorio.EMAIL, 1
        );
        Visita visita = s.getVisitas().get(0);

        final boolean[] emailEnviado = {false};
        final String[] destinatarioEmail = {null};
        final String[] asuntoEmail = {null};

        IEmailAdapter mockEmail = (destinatario, asunto, cuerpo) -> {
            emailEnviado[0] = true;
            destinatarioEmail[0] = destinatario;
            asuntoEmail[0] = asunto;
            if ("true".equalsIgnoreCase(System.getProperty("verbose", "false"))) {
                System.out.println("[ALERTA OBSERVER EMAIL] Enviando Email a " + destinatario + " | Asunto: " + asunto + " | Cuerpo: " + cuerpo);
            }
        };

        EmailNotificacion strategy = new EmailNotificacion(mockEmail);
        visita.suscribir(strategy);

        visita.notificarRecordatorio();

        assertTrue(emailEnviado[0], "El email debió ser enviado");
        assertEquals("gmontalvo@uade.edu.ar", destinatarioEmail[0], "El email debe ser el del adoptante");
        assertEquals("Recordatorio de Visita - Gud Boy", asuntoEmail[0], "El asunto debe coincidir");
    }

    @Test
    void testObserverPattern_SuscripcionYDesuscripcionDinamica() {
        Seguimiento s = seguimientoService.crearSeguimiento(
                adopcion, adoptante, DiaSemana.SABADO, "10:00", "12:00", PreferenciaRecordatorio.WHATSAPP, 1
        );
        Visita visita = s.getVisitas().get(0);

        final int[] llamadasSMS = {0};
        final int[] llamadasEmail = {0};

        ISMSAdapter mockSms = (numero, texto) -> {
            llamadasSMS[0]++;
            if ("true".equalsIgnoreCase(System.getProperty("verbose", "false"))) {
                System.out.println("[ALERTA OBSERVER SMS DINAMICA] Enviando SMS a " + numero + ": " + texto);
            }
        };
        IEmailAdapter mockEmail = (destinatario, asunto, cuerpo) -> {
            llamadasEmail[0]++;
            if ("true".equalsIgnoreCase(System.getProperty("verbose", "false"))) {
                System.out.println("[ALERTA OBSERVER EMAIL DINAMICA] Enviando Email a " + destinatario + " | Asunto: " + asunto);
            }
        };

        SMSNotificacion smsStrategy = new SMSNotificacion(mockSms);
        EmailNotificacion emailStrategy = new EmailNotificacion(mockEmail);

        // 1. Suscribir ambos observadores
        visita.suscribir(smsStrategy);
        visita.suscribir(emailStrategy);

        // 2. Gatillar y verificar que ambos reciben la actualización
        visita.notificarRecordatorio();
        assertEquals(1, llamadasSMS[0], "Observer SMS debió recibir 1 notificación");
        assertEquals(1, llamadasEmail[0], "Observer Email debió recibir 1 notificación");

        // 3. Desuscribir el observer de SMS
        visita.desuscribir(smsStrategy);

        // 4. Gatillar de nuevo y verificar que solo el de Email es notificado por segunda vez
        visita.notificarRecordatorio();
        assertEquals(1, llamadasSMS[0], "Observer SMS desuscrito NO debió recibir otra notificación");
        assertEquals(2, llamadasEmail[0], "Observer Email aún suscrito debió recibir una segunda notificación");
    }

    @Test
    void testUmlStrictOverloadsAndMethods() {
        // 1. Test SeguimientoService.crear (overload)
        Seguimiento s = seguimientoService.crear(adopcion, adoptante, DiaSemana.LUNES, "10:00", "12:00", PreferenciaRecordatorio.WHATSAPP);
        assertNotNull(s);
        
        // Let's populate the visitaRepository with the tracking's visits
        for (Visita v : s.getVisitas()) {
            visitaRepository.guardar(v);
        }

        // 2. Test Visita.getObservadores()
        assertTrue(s.getVisitas().get(0).getObservadores().isEmpty(), "No debería tener observadores inicialmente");

        // 3. Test VisitaService.registrarResultado(Visita, Encuesta, String, boolean) (overload)
        Visita v = s.getVisitas().get(0);
        Encuesta encuesta = new Encuesta(CalificacionEnum.BUENO, CalificacionEnum.BUENO, CalificacionEnum.BUENO);
        visitaService.registrarResultado(v, encuesta, "Impecable", true);
        assertTrue(v.isCompletada());

        // 4. Test VisitaService.listarPorSeguimiento(Seguimiento) (overload)
        List<Visita> visitas = visitaService.listarPorSeguimiento(s);
        assertFalse(visitas.isEmpty());

        // 5. Test VisitaService.correspondeRecordatorio(Visita, LocalDate, int) (overload)
        Visita vNext = s.getVisitas().get(1);
        boolean corresponde = visitaService.correspondeRecordatorio(vNext, vNext.getFechaProgramada().minusDays(2), 2);
        assertTrue(corresponde);

        // 6. Test SeguimientoService.finalizar (overload)
        seguimientoService.finalizar(s.getId());
        assertEquals(EstadoSeguimiento.FINALIZADO, s.getEstado());
    }
}

/*
mvn test -Dtest=SeguimientoTest
mvn test -Dtest=SeguimientoTest -Dverbose=true
 */