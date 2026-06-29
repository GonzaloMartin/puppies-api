package com.gudboy.service;

import com.gudboy.domain.Usuario.Usuario;
import com.gudboy.domain.animal.model.Adopcion;
import com.gudboy.domain.animal.model.AnimalDomestico;
import com.gudboy.domain.fichaMedica.model.FichaMedica;
import com.gudboy.domain.seguimiento.model.*;
import com.gudboy.domain.seguimiento.observer.IObservador;
import com.gudboy.domain.seguimiento.observer.SMSNotificacion;
import com.gudboy.domain.seguimiento.observer.WhatsAppNotificacion;
import com.gudboy.domain.seguimiento.observer.EmailNotificacion;
import com.gudboy.domain.seguimiento.adapter.TwilioSMSAdapter;
import com.gudboy.domain.seguimiento.adapter.MetaWhatsAppAdapter;
import com.gudboy.domain.seguimiento.adapter.JavaMailAdapter;
import com.gudboy.domain.seguimiento.service.ServicioRecordatorios;
import com.gudboy.repository.IFichaMedicaRepository;
import com.gudboy.repository.ISeguimientoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.gudboy.repository.IAdopcionRepository;
import com.gudboy.repository.IUsuarioRepository;
import com.gudboy.dto.AdopcionDTO;
import com.gudboy.dto.UsuarioDTO;
import com.gudboy.dto.EncuestaDTO;
import com.gudboy.infrastructure.ActividadRegistry;

public class SeguimientoService {

    private final ISeguimientoRepository seguimientoRepository;
    private final IFichaMedicaRepository fichaMedicaRepository;
    private final IAdopcionRepository adopcionRepository;
    private final IUsuarioRepository usuarioRepository;

    public SeguimientoService(ISeguimientoRepository seguimientoRepository, IFichaMedicaRepository fichaMedicaRepository,
                              IAdopcionRepository adopcionRepository, IUsuarioRepository usuarioRepository) {
        this.seguimientoRepository = seguimientoRepository;
        this.fichaMedicaRepository = fichaMedicaRepository;
        this.adopcionRepository = adopcionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void evaluarTodosLosRecordatorios(ServicioRecordatorios recordatorios) {
        ActividadRegistry.publicar("[ServicioRecordatorios] Iniciando evaluación de recordatorios con un umbral de " + recordatorios.getDiasPreviosConfigurable() + " días.");
        List<Seguimiento> segs = seguimientoRepository.listarTodos();
        int totalProcesados = 0;
        int totalSuscritos = 0;
        for (Seguimiento s : segs) {
            if (s.getEstado() != EstadoSeguimiento.ACTIVO) continue;

            IObservador strategy = resolverStrategy(s.getPreferenciaRecordatorio());

            List<Visita> visitas = s.getVisitas();
            for (Visita v : visitas) {
                if (!v.isCompletada()) {
                    v.suscribir(strategy);
                    totalSuscritos++;
                }
            }
            recordatorios.evaluarVisitas(visitas, LocalDate.now());

            for (Visita v : visitas) {
                v.desuscribir(strategy);
            }
            totalProcesados++;
        }
        ActividadRegistry.publicar("[ServicioRecordatorios] Evaluación finalizada. Seguimientos activos evaluados: " + totalProcesados + ", Visitas evaluadas: " + totalSuscritos + ".");
    }

    private IObservador resolverStrategy(PreferenciaRecordatorio pref) {
        return switch (pref) {
            case SMS       -> new SMSNotificacion(new TwilioSMSAdapter());
            case WHATSAPP  -> new WhatsAppNotificacion(new MetaWhatsAppAdapter());
            case EMAIL     -> new EmailNotificacion(new JavaMailAdapter());
        };
    }

    public Seguimiento crearSeguimiento(AdopcionDTO adopcionDto, UsuarioDTO responsableDto, DiaSemana diaSemana, String horarioDesde, String horarioHasta, PreferenciaRecordatorio preferenciaRecordatorio, int cantVisitasIniciales) {
        Adopcion adopcion = adopcionRepository.buscarPorId(adopcionDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Adopción no encontrada: " + adopcionDto.getId()));
        
        Usuario responsable = usuarioRepository.listarTodos().stream()
                .filter(u -> u.getEmail().equals(responsableDto.getEmail()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + responsableDto.getEmail()));

        Seguimiento s = new Seguimiento(adopcion, responsable, diaSemana, horarioDesde, horarioHasta, preferenciaRecordatorio);
        
        LocalDate baseDate = LocalDate.now();
        for (int i = 1; i <= cantVisitasIniciales; i++) {
            Visita v = new Visita(s, baseDate.plusWeeks(i));
            s.agregarVisita(v);
        }

        seguimientoRepository.guardar(s);
        log("[SEGUIMIENTO] Creado nuevo seguimiento (" + s.getId() + ") para " + adopcion.getAdoptante().getNombre() + " " + adopcion.getAdoptante().getApellido() + ". Programadas " + cantVisitasIniciales + " visitas.");
        return s;
    }

    public Seguimiento crear(AdopcionDTO adopcionDto, UsuarioDTO responsableDto, DiaSemana diaSemana, String horarioDesde, String horarioHasta, PreferenciaRecordatorio preferenciaRecordatorio) {
        return crearSeguimiento(adopcionDto, responsableDto, diaSemana, horarioDesde, horarioHasta, preferenciaRecordatorio, 3);
    }

    public void registrarResultadoVisita(UUID visitaId, EncuestaDTO encuestaDto, String comentarios, boolean continuarVisitas) {
        Visita visita = seguimientoRepository.buscarVisitaPorId(visitaId)
                .orElseThrow(() -> new IllegalArgumentException("Visita no encontrada: " + visitaId));

        Encuesta encuesta = new Encuesta(encuestaDto.getEstadoGeneralAnimal(), encuestaDto.getLimpiezaLugar(), encuestaDto.getAmbiente());
        visita.registrarResultado(encuesta, comentarios, continuarVisitas);
        seguimientoRepository.actualizarVisita(visita);

        log("[VISITA DOMICILIARIA] Resultado registrado para visita " + visita.getId() + ". Comentarios: " + comentarios);

        Seguimiento s = visita.getSeguimiento();
        for (AnimalDomestico animal : s.getAdopcion().getAnimales()) {
            FichaMedica ficha = fichaMedicaRepository.getByAnimalId(animal.getId());
            if (ficha != null) {
                ficha.registrarVisitaDomicilio(visita);
                fichaMedicaRepository.actualizar(ficha);
                log("[VINCULO CLINICO] Visita " + visita.getId() + " vinculada exitosamente a la Ficha Médica de: " + animal.getNombre());
            }
        }

        if (!continuarVisitas) {
            s.finalizarSeguimiento();
            seguimientoRepository.actualizar(s);
        }
    }

    public void finalizarSeguimiento(UUID id) {
        Seguimiento s = seguimientoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Seguimiento no encontrado: " + id));
        s.finalizarSeguimiento();
        seguimientoRepository.actualizar(s);
    }

    public void finalizar(UUID id) {
        finalizarSeguimiento(id);
    }

    public boolean estaActivo(UUID id) {
        return seguimientoRepository.buscarPorId(id)
                .map(s -> s.getEstado() == EstadoSeguimiento.ACTIVO)
                .orElse(false);
    }

    public Optional<Seguimiento> getById(UUID id) {
        return seguimientoRepository.buscarPorId(id);
    }

    public List<Seguimiento> listarTodos() {
        return seguimientoRepository.listarTodos();
    }

    private void log(String msg) {
        ActividadRegistry.publicar(msg);
    }

}
