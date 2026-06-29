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
import com.gudboy.repository.*;
import com.gudboy.service.SeguimientoService;
import com.gudboy.service.VisitaService;
import com.gudboy.controller.*;
import com.gudboy.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SeguimientoTest {

    private static final boolean USE_MYSQL = false;

    private SeguimientoService seguimientoService;
    private IFichaMedicaRepository fichaMedicaRepository;
    private IVisitaRepository visitaRepository;
    private VisitaService visitaService;
    private SeguimientoController seguimientoController;
    private VisitaController visitaController;

    private IAdopcionRepository adopcionRepository;
    private IUsuarioRepository usuarioRepository;

    private AnimalDomestico animal1;
    private AnimalDomestico animal2;
    private Visitador adoptante;
    private Adopcion adopcion;

    private AdopcionDTO adopcionDto;
    private UsuarioDTO adoptanteDto;

    @BeforeEach
    void setUp() {
        ISeguimientoRepository seguimientoRepository;
        if (USE_MYSQL) {
            // Limpio la base de datos en el caso de que los test corran con mySQL.
            // Ver el valor de USE_MYSQL = true
            try (java.sql.Statement stmt = com.gudboy.infrastructure.ConexionMySQL.getInstancia().getConnection().createStatement()) {
                stmt.executeUpdate("DELETE FROM visitas");
                stmt.executeUpdate("DELETE FROM seguimiento");
                stmt.executeUpdate("DELETE FROM alarmas");
                stmt.executeUpdate("DELETE FROM adopcion_animal");
                stmt.executeUpdate("DELETE FROM adopcion");
                stmt.executeUpdate("DELETE FROM ficha_medica");
                stmt.executeUpdate("DELETE FROM animal");
                stmt.executeUpdate("DELETE FROM usuario");
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }

            IAnimalRepository animalRepoMySQL = new AnimalRepositoryMySQL();
            usuarioRepository = new UsuarioRepositoryMySQL();
            adopcionRepository = new AdopcionRepositoryMySQL(animalRepoMySQL, usuarioRepository);

            seguimientoRepository = new SeguimientoRepositoryHibernate();
            fichaMedicaRepository = new FichaMedicaRepositoryHibernate(usuarioRepository);
            visitaRepository = new VisitaRepositoryHibernate();

            // Creo entidades para probar
            animal1 = new AnimalDomestico("Firulais", "Perro", 0.5, 12.0, 3, "SALUDABLE");
            animal2 = new AnimalDomestico("Michi", "Gato", 0.25, 4.0, 2, "SALUDABLE");

            FichaMedica ficha1 = new FichaMedica(animal1);
            FichaMedica ficha2 = new FichaMedica(animal2);

            adoptante = new Visitador(
                    "Gonzalo", "Martin", "gmontalvo@uade.edu.ar", "+5491112345678",
                    EstadoCivil.SOLTERO, Ocupacion.ESTUDIANTE, "Compañía", "Perros y Gatos", false
            );

            Veterinario responsableAdopcion = new Veterinario("Juan", "Perez", "juan@gudboy.com", "+5491187654321", 12345, "Clínica general");

            adopcion = new Adopcion(animal1, animal2, adoptante, responsableAdopcion);

            // Persistir entities en mySQL
            usuarioRepository.guardar(adoptante);
            usuarioRepository.guardar(responsableAdopcion);
            animalRepoMySQL.guardar(animal1);
            animalRepoMySQL.guardar(animal2);
            fichaMedicaRepository.guardar(ficha1);
            fichaMedicaRepository.guardar(ficha2);
            adopcionRepository.guardar(adopcion);

        } else {
            seguimientoRepository = new SeguimientoRepositoryEnMemoria();
            fichaMedicaRepository = new FichaMedicaRepositoryEnMemoria();
            visitaRepository = new VisitaRepositoryEnMemoria();
            adopcionRepository = new AdopcionRepositoryEnMemoria();
            usuarioRepository = new UsuarioRepositoryEnMemoria();

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

            usuarioRepository.guardar(adoptante);
            usuarioRepository.guardar(responsableAdopcion);
            adopcionRepository.guardar(adopcion);
        }

        adopcionDto = new AdopcionDTO(adopcion.getId(), adopcion.getAnimales(), adopcion.getResponsable(), adopcion.getAdoptante());
        adoptanteDto = new UsuarioDTO(
                adoptante.getNombre(), adoptante.getApellido(), adoptante.getEmail(), adoptante.getTelefono(),
                adoptante.getEstadoCivil(), adoptante.getOcupacion(), adoptante.getMotivoAdopcion(),
                adoptante.getAnimalesInteres(), adoptante.tieneOtrasMascotas()
        );

        seguimientoService = new SeguimientoService(seguimientoRepository, fichaMedicaRepository, adopcionRepository, usuarioRepository);
        visitaService = new VisitaService(visitaRepository, seguimientoRepository, fichaMedicaRepository);
        seguimientoController = new SeguimientoController(seguimientoService);
        visitaController = new VisitaController(visitaService);
    }

    @Test
    void crearSeguimiento_DebeProgramarVisitasSemanalesYPersistir() {
        Seguimiento s = seguimientoService.crearSeguimiento(
                adopcionDto, adoptanteDto, DiaSemana.SABADO, "10:00", "12:00", PreferenciaRecordatorio.WHATSAPP, 3
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
                adopcionDto, adoptanteDto, DiaSemana.MIERCOLES, "14:00", "16:00", PreferenciaRecordatorio.WHATSAPP, 1
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
                adopcionDto, adoptanteDto, DiaSemana.LUNES, "09:00", "11:00", PreferenciaRecordatorio.WHATSAPP, 1
        );
        Visita visita = s.getVisitas().get(0);

        EncuestaDTO encuestaFavorable = new EncuestaDTO(CalificacionEnum.BUENO, CalificacionEnum.BUENO, CalificacionEnum.REGULAR);
        assertTrue(new Encuesta(CalificacionEnum.BUENO, CalificacionEnum.BUENO, CalificacionEnum.REGULAR).esFavorable(), "La encuesta debe ser considerada favorable");

        seguimientoService.registrarResultadoVisita(visita.getId(), encuestaFavorable, "Excelente cuidado, patio amplio y limpio.", true);

        Visita visitaGuardada = visitaRepository.buscarPorId(visita.getId()).orElse(visita);
        assertTrue(visitaGuardada.isCompletada(), "La visita debe quedar marcada como completada");
        assertNotNull(visitaGuardada.getFechaReal(), "La fecha real debe estar registrada");
        assertEquals("Excelente cuidado, patio amplio y limpio.", visitaGuardada.getComentarios());

        FichaMedica fm1 = fichaMedicaRepository.getByAnimalId(animal1.getId());
        FichaMedica fm2 = fichaMedicaRepository.getByAnimalId(animal2.getId());

        assertEquals(1, fm1.getHistorial().getListaVisitas().size(), "El historial del animal 1 debe tener 1 visita registrada");
        assertEquals(1, fm2.getHistorial().getListaVisitas().size(), "El historial del animal 2 debe tener 1 visita registrada");

        assertEquals(visita.getId(), fm1.getHistorial().getListaVisitas().get(0).getId(), "La visita registrada debe ser la completada");
    }

    @Test
    void registrarResultadoVisita_ConContinuarVisitasFalso_DebeFinalizarSeguimiento() {
        Seguimiento s = seguimientoService.crearSeguimiento(
                adopcionDto, adoptanteDto, DiaSemana.VIERNES, "16:00", "18:00", PreferenciaRecordatorio.WHATSAPP, 1
        );
        Visita visita = s.getVisitas().get(0);

        EncuestaDTO encuestaFinal = new EncuestaDTO(CalificacionEnum.BUENO, CalificacionEnum.BUENO, CalificacionEnum.BUENO);

        seguimientoService.registrarResultadoVisita(visita.getId(), encuestaFinal, "Todo excelente. Seguimiento concluido con éxito.", false);

        Seguimiento sGuardado = seguimientoService.getById(s.getId()).orElse(s);
        assertEquals(EstadoSeguimiento.FINALIZADO, sGuardado.getEstado(), "El seguimiento debe estar en estado FINALIZADO");
        assertFalse(seguimientoService.estaActivo(s.getId()), "El seguimiento ya no debe figurar como activo");
    }

    @Test
    void encuesta_NoFavorable_DebeCalcularCorrectamente() {
        Encuesta encuestaMala = new Encuesta(CalificacionEnum.MALO, CalificacionEnum.BUENO, CalificacionEnum.MALO);
        assertFalse(encuestaMala.esFavorable(), "La encuesta debe ser desfavorable");
    }

    @Test
    void testVisitaControllerAndService_UmlStrict() {
        // 1. Trackear via SeguimientoController (delega a SeguimientoService)
        SeguimientoDTO s = seguimientoController.crearSeguimiento(
                adopcionDto, adoptanteDto, DiaSemana.MIERCOLES, "10:00", "12:00", PreferenciaRecordatorio.SMS, 3
        );
        
        // Guardamos visita en visitaRepository
        Seguimiento sEntity = seguimientoService.getById(s.getId()).orElseThrow();
        for (Visita v : sEntity.getVisitas()) {
            visitaRepository.guardar(v);
        }
        
        // 2. Test VisitaController.listarPorSeguimiento
        List<VisitaDTO> list = visitaController.listarPorSeguimiento(s.getId());
        assertEquals(3, list.size(), "Debe haber 3 visitas programadas");
        
        VisitaDTO v1 = list.get(0);
        
        // 3. Test VisitaController.marcarCompletada
        visitaController.marcarCompletada(v1.getId());
        Visita v1Guardada = visitaRepository.buscarPorId(v1.getId()).orElseThrow();
        assertTrue(v1Guardada.isCompletada(), "La visita 1 debe estar completada");
        
        // 4. Test VisitaController.registrarResultado
        VisitaDTO v2 = list.get(1);
        EncuestaDTO encuesta = new EncuestaDTO(CalificacionEnum.BUENO, CalificacionEnum.BUENO, CalificacionEnum.BUENO);
        visitaController.registrarResultado(v2.getId(), encuesta, "Excelente todo", true);
        Visita v2Guardada = visitaRepository.buscarPorId(v2.getId()).orElseThrow();
        assertTrue(v2Guardada.isCompletada(), "La visita 2 debe estar completada");
        assertEquals("Excelente todo", v2Guardada.getComentarios());
        assertTrue(v2Guardada.requiereContinuarVisitas());
    }

    @Test
    void testRecordatorioSMS_DebeEnviarSMSCorrectamente() {
        Seguimiento s = seguimientoService.crearSeguimiento(
                adopcionDto, adoptanteDto, DiaSemana.JUEVES, "14:00", "16:00", PreferenciaRecordatorio.SMS, 1
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
                adopcionDto, adoptanteDto, DiaSemana.VIERNES, "15:00", "17:00", PreferenciaRecordatorio.EMAIL, 1
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
                adopcionDto, adoptanteDto, DiaSemana.SABADO, "10:00", "12:00", PreferenciaRecordatorio.WHATSAPP, 1
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

        // 2. Notificar y validar que ambos reciben la actualización
        visita.notificarRecordatorio();
        assertEquals(1, llamadasSMS[0], "Observer SMS debió recibir 1 notificación");
        assertEquals(1, llamadasEmail[0], "Observer Email debió recibir 1 notificación");

        // 3. Desuscribir el observer de SMS
        visita.desuscribir(smsStrategy);

        // 4. Notificar de nuevo y verificar que solo el de Email es notificado por segunda vez
        visita.notificarRecordatorio();
        assertEquals(1, llamadasSMS[0], "Observer SMS desuscrito NO debió recibir otra notificación");
        assertEquals(2, llamadasEmail[0], "Observer Email aún suscrito debió recibir una segunda notificación");
    }

    @Test
    void testUmlRelleno() {
        // 1. Test SeguimientoService.crear
        Seguimiento s = seguimientoService.crear(adopcionDto, adoptanteDto, DiaSemana.LUNES, "10:00", "12:00", PreferenciaRecordatorio.WHATSAPP);
        assertNotNull(s);
        
        // Guardamos visitaRepository
        for (Visita v : s.getVisitas()) {
            visitaRepository.guardar(v);
        }

        // 2. Test Visita.getObservadores()
        assertTrue(s.getVisitas().get(0).getObservadores().isEmpty(), "No debería tener observadores inicialmente");

        // 3. Test VisitaService.registrarResultado(Visita, Encuesta, String, boolean)
        Visita v = s.getVisitas().get(0);
        EncuestaDTO encuesta = new EncuestaDTO(CalificacionEnum.BUENO, CalificacionEnum.BUENO, CalificacionEnum.BUENO);
        visitaService.registrarResultado(v, encuesta, "Impecable", true);
        Visita vGuardada = visitaRepository.buscarPorId(v.getId()).orElse(v);
        assertTrue(vGuardada.isCompletada());

        // 4. Test VisitaService.listarPorSeguimiento(Seguimiento)
        List<Visita> visitas = visitaService.listarPorSeguimiento(s);
        assertFalse(visitas.isEmpty());

        // 5. Test VisitaService.correspondeRecordatorio(Visita, LocalDate, int)
        Visita vNext = s.getVisitas().get(1);
        boolean corresponde = visitaService.correspondeRecordatorio(vNext, vNext.getFechaProgramada().minusDays(2), 2);
        assertTrue(corresponde);

        // 6. Test SeguimientoService.finalizar
        seguimientoService.finalizar(s.getId());
        Seguimiento sGuardado = seguimientoService.getById(s.getId()).orElse(s);
        assertEquals(EstadoSeguimiento.FINALIZADO, sGuardado.getEstado());
    }
}