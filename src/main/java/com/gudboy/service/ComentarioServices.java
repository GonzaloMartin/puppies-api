package com.gudboy.service;

import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.comentarioMedico.ComentarioMedico;
import com.gudboy.domain.historialClinico.HistorialClinico;
import com.gudboy.repository.ComentarioMedicoRepository;
import com.gudboy.repository.HistorialClinicoRepository;

import java.util.ArrayList;
import java.util.UUID;

public class ComentarioServices {
    private HistorialClinicoRepository histoRepo;
    private ComentarioMedicoRepository comenRepo;

    public ComentarioServices(HistorialClinicoRepository histoRepo, ComentarioMedicoRepository comenRepo){
        this.histoRepo = histoRepo;
        this.comenRepo = comenRepo;
    }

    public ComentarioMedico agregarComentario(Veterinario veterinario, String casillaComentario){
        ComentarioMedico comentarioMedico = new ComentarioMedico(veterinario, casillaComentario);
        comenRepo.guardar(comentarioMedico);

        return comentarioMedico;
    }

    public void modificarComentario(UUID comentarioId, String nuevaDescripcion){
        ComentarioMedico comentarioMedico = comenRepo.buscarPorId(comentarioId);

        comentarioMedico.modificarComentario(nuevaDescripcion);
        comenRepo.actualizar(comentarioId);

    }

    public ArrayList<ComentarioMedico> listarComentarios(UUID historialId){
        HistorialClinico historial = histoRepo.buscarPorId(historialId);

        return historial.getListaComentario();
    }
}
