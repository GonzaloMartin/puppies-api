package com.gudboy.service;

import com.gudboy.domain.Usuario.Usuario;
import com.gudboy.domain.animal.model.Adopcion;
import com.gudboy.domain.animal.model.AnimalDomestico;
import com.gudboy.domain.fichaMedica.model.FichaMedica;
import com.gudboy.domain.seguimiento.model.*;
import com.gudboy.repository.IFichaMedicaRepository;
import com.gudboy.repository.ISeguimientoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SeguimientoService {

    private final ISeguimientoRepository seguimientoRepository;
    private final IFichaMedicaRepository fichaMedicaRepository;

    public SeguimientoService(ISeguimientoRepository seguimientoRepository, IFichaMedicaRepository fichaMedicaRepository) {
        this.seguimientoRepository = seguimientoRepository;
        this.fichaMedicaRepository = fichaMedicaRepository;
    }

    public Seguimiento crearSeguimiento(Adopcion adopcion, Usuario responsable, DiaSemana diaSemana, String horarioDesde, String horarioHasta, PreferenciaRecordatorio preferenciaRecordatorio, int cantVisitasIniciales) {
        Seguimiento s = new Seguimiento(adopcion, responsable, diaSemana, horarioDesde, horarioHasta, preferenciaRecordatorio);
        
        LocalDate baseDate = LocalDate.now();
        for (int i = 1; i <= cantVisitasIniciales; i++) {
            Visita v = new Visita(s, baseDate.plusWeeks(i));
            s.agregarVisita(v);
        }

        seguimientoRepository.guardar(s);
        log("[SEGUIMIENTO POST-ADOPCION] Creado nuevo seguimiento (" + s.getId() + ") para " + adopcion.getAdoptante().getNombre() + " " + adopcion.getAdoptante().getApellido() + ". Programadas " + cantVisitasIniciales + " visitas.");
        return s;
    }

    public Seguimiento crear(Adopcion adopcion, Usuario responsable, DiaSemana diaSemana, String horarioDesde, String horarioHasta, PreferenciaRecordatorio preferenciaRecordatorio) {
        return crearSeguimiento(adopcion, responsable, diaSemana, horarioDesde, horarioHasta, preferenciaRecordatorio, 3);
    }

    public void registrarResultadoVisita(UUID visitaId, Encuesta encuesta, String comentarios, boolean continuarVisitas) {
        Visita visita = seguimientoRepository.buscarVisitaPorId(visitaId)
                .orElseThrow(() -> new IllegalArgumentException("Visita no encontrada: " + visitaId));

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
        if ("true".equalsIgnoreCase(System.getProperty("verbose", "false"))) {
            System.out.println(msg);
        }
    }
}
