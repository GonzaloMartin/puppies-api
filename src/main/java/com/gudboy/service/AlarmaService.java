package com.gudboy.service;

import com.gudboy.domain.alarma.IHistorialClinicoService;
import com.gudboy.domain.alarma.model.Alarma;
import com.gudboy.repository.IAlarmaRepository;

import java.util.List;

public class AlarmaService {

    private final IAlarmaRepository repository;
    private final IHistorialClinicoService historialService;

    public AlarmaService(IAlarmaRepository repository, IHistorialClinicoService historialService) {
        this.repository = repository;
        this.historialService = historialService;
    }

    public void crearAlarma(Alarma alarma) {
        repository.add(alarma);
    }

    public Alarma obtenerAlarma(int id) {
        return repository.getById(id);
    }

    public List<Alarma> obtenerTodas() {
        return repository.getAll();
    }

    public void actualizarAlarma(Alarma alarma) {
        repository.update(alarma);
    }

    public void eliminarAlarma(int id) {
        Alarma alarma = repository.getById(id);
        if (alarma != null) {
            repository.remove(alarma);
        }
    }

    public void marcarCompletado(int id) {
        Alarma alarma = repository.getById(id);
        if (alarma != null) {
            alarma.marcarCompletado();
            repository.update(alarma);
        }
    }

    public void verificarEstadoAlarmas() {
        // Al eliminar el patrón Observer, la verificación de alarmas ya no dispara eventos asíncronos.
    }

    public void atenderAlarma(int id, String comentario, boolean tratamientoFinalizado, com.gudboy.domain.Usuario.Veterinario veterinario) {
        Alarma alarma = repository.getById(id);
        if (alarma != null) {
            if (comentario != null && !comentario.trim().isEmpty()) {
                String firma = (veterinario != null)
                        ? veterinario.getNombre() + " " + veterinario.getApellido()
                        : "Desconocido";
                alarma.setDescripcion(alarma.getDescripcion() + " | Nota (" + firma + "): " + comentario);
            }

            if (tratamientoFinalizado) {
                alarma.marcarTratamientoFinalizado();
                historialService.finalizarTratamientosActivos(alarma.getIdAnimal(), alarma.getAcciones());
            } else {
                alarma.marcarCompletado();
            }

            repository.update(alarma);

            // <-- NUEVO: La acción de inyectar el registro en el módulo vecino
            if (comentario != null && !comentario.trim().isEmpty()) {
                historialService.registrarAtencion(alarma.getIdAnimal(), comentario, veterinario);
            }
        }
    }
}