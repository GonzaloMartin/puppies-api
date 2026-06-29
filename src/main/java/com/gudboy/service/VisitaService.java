package com.gudboy.service;

import com.gudboy.domain.animal.model.AnimalDomestico;
import com.gudboy.domain.fichaMedica.model.FichaMedica;
import com.gudboy.domain.seguimiento.model.*;
import com.gudboy.repository.IFichaMedicaRepository;
import com.gudboy.repository.ISeguimientoRepository;
import com.gudboy.repository.IVisitaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import com.gudboy.dto.EncuestaDTO;

public class VisitaService {

    private final IVisitaRepository visitaRepository;
    private final ISeguimientoRepository seguimientoRepository;
    private final IFichaMedicaRepository fichaMedicaRepository;

    public VisitaService(IVisitaRepository visitaRepository, ISeguimientoRepository seguimientoRepository, IFichaMedicaRepository fichaMedicaRepository) {
        this.visitaRepository = visitaRepository;
        this.seguimientoRepository = seguimientoRepository;
        this.fichaMedicaRepository = fichaMedicaRepository;
    }

    public void registrarResultado(UUID visitaId, EncuestaDTO encuestaDto, String comentarios, boolean continuarVisitas) {
        Visita visita = visitaRepository.buscarPorId(visitaId)
                .orElseThrow(() -> new IllegalArgumentException("Visita no encontrada: " + visitaId));

        Encuesta encuesta = new Encuesta(encuestaDto.getEstadoGeneralAnimal(), encuestaDto.getLimpiezaLugar(), encuestaDto.getAmbiente());
        visita.registrarResultado(encuesta, comentarios, continuarVisitas);
        visitaRepository.actualizar(visita);

        log("[VISITA REGISTRADA] Se registró el resultado de la visita " + visita.getId() + " con comentarios: " + comentarios);

        Seguimiento s = visita.getSeguimiento();
        for (AnimalDomestico animal : s.getAdopcion().getAnimales()) {
            FichaMedica ficha = fichaMedicaRepository.getByAnimalId(animal.getId());
            if (ficha != null) {
                ficha.registrarVisitaDomicilio(visita);
                fichaMedicaRepository.actualizar(ficha);
                log("[VINCULO CLINICO] Visita registrada " + visita.getId() + " inyectada en la Ficha Médica de: " + animal.getNombre());
            }
        }

        if (!continuarVisitas) {
            s.finalizarSeguimiento();
            seguimientoRepository.actualizar(s);
            log("[SEGUIMIENTO FINALIZADO] El seguimiento " + s.getId() + " ha finalizado formalmente.");
        }
    }

    public void registrarResultado(Visita visita, EncuestaDTO encuesta, String comentarios, boolean continuarVisitas) {
        if (visita == null) {
            throw new IllegalArgumentException("Visita no puede ser nula");
        }
        registrarResultado(visita.getId(), encuesta, comentarios, continuarVisitas);
    }

    public void marcarCompletada(Visita visita) {
        visita.marcarCompletada();
        visitaRepository.actualizar(visita);
    }

    public void marcarCompletada(UUID idVisita) {
        Visita visita = visitaRepository.buscarPorId(idVisita)
                .orElseThrow(() -> new IllegalArgumentException("Visita no encontrada: " + idVisita));
        marcarCompletada(visita);
    }

    public List<Visita> listarPorSeguimiento(Seguimiento seguimiento) {
        return visitaRepository.listarPorSeguimiento(seguimiento.getId());
    }

    public List<Visita> listarPorSeguimiento(UUID idSeguimiento) {
        return visitaRepository.listarPorSeguimiento(idSeguimiento);
    }

    public boolean correspondeRecordatorio(Visita visita, LocalDate fechaActual, int diasPrevios) {
        return visita.correspondeRecordatorio(fechaActual, diasPrevios);
    }

    private void log(String msg) {
        com.gudboy.infrastructure.ActividadRegistry.publicar(msg);
    }
}
