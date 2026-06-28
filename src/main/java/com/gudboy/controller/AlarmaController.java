package com.gudboy.controller;

import com.gudboy.domain.alarma.model.Alarma;
import com.gudboy.dto.AlarmaDTO;
import com.gudboy.service.AlarmaService;

import java.util.List;
import java.util.stream.Collectors;

public class AlarmaController {

    private final AlarmaService service;

    public AlarmaController(AlarmaService service) {
        this.service = service;
    }

    public List<AlarmaDTO> getAll() {
        return service.obtenerTodas().stream()
                .map(Alarma::toDTO)
                .collect(Collectors.toList());
    }

    public AlarmaDTO getById(int id) {
        Alarma alarma = service.obtenerAlarma(id);
        return alarma != null ? alarma.toDTO() : null;
    }

    public void create(AlarmaDTO dto) {
        Alarma nuevaAlarma = new Alarma(
            dto.getId(),
            dto.getIdAnimal(),
            dto.getTitulo(),
            dto.getDescripcion(),
            dto.getFrecuenciaDias(),
            dto.getFechaProximoDisparo(),
            dto.getAcciones()
        );
        service.crearAlarma(nuevaAlarma);
    }

    public void update(int id, AlarmaDTO dto) {
        Alarma existente = service.obtenerAlarma(id);
        if (existente != null) {
            existente.setTitulo(dto.getTitulo());
            existente.setDescripcion(dto.getDescripcion());
            existente.setFrecuenciaDias(dto.getFrecuenciaDias());
            existente.setFechaProximoDisparo(dto.getFechaProximoDisparo());
            existente.setAcciones(dto.getAcciones());
            service.actualizarAlarma(existente);
        }
    }

    public void delete(int id) {
        service.eliminarAlarma(id);
    }

    public void marcarCompletado(int id) {
        service.marcarCompletado(id);
    }

    public void verificarEstadoAlarmas() {
        service.verificarEstadoAlarmas();
    }

    public void atenderAlarma(int id, String comentario, boolean tratamientoFinalizado, com.gudboy.domain.Usuario.Veterinario veterinario) {
        service.atenderAlarma(id, comentario, tratamientoFinalizado, veterinario);
    }
}