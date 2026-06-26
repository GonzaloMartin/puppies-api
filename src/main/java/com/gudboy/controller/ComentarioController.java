package com.gudboy.controller;

import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.comentarioMedico.ComentarioMedico;
import com.gudboy.domain.historialClinico.HistorialClinico;
import com.gudboy.service.ComentarioServices;

import java.util.ArrayList;
import java.util.UUID;
import com.gudboy.service.HistorialClinicoService;

public class ComentarioController {
    private ComentarioServices comentarioServices;
    private HistorialClinicoService historialClinicoService;

    public ComentarioController(ComentarioServices comentarioServices, HistorialClinicoService historialClinicoService) {
        this.comentarioServices = comentarioServices;
        this.historialClinicoService = historialClinicoService;
    }

    public ComentarioMedico agregarComentario(Veterinario veterinario, String casillaComentario){
        return comentarioServices.agregarComentario(veterinario, casillaComentario);
    }

    public void modificarComentario(UUID comentarioId, String nuevoComentario){
        comentarioServices.modificarComentario(comentarioId, nuevoComentario);
    }

    public ArrayList<ComentarioMedico> listarComentarios(UUID historialid){
        HistorialClinico historia = historialClinicoService.obtenerHistorial(historialid);

        return historia.getListaComentario();
    }
}
