package com.gudboy.repository;

import com.gudboy.domain.comentarioMedico.ComentarioMedico;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class ComentarioMedicoRepository {

    private final Map<UUID, ComentarioMedico> store = new LinkedHashMap<>();

    public void guardar(ComentarioMedico comentario) {
        store.put(comentario.getComentarioID(), comentario);
    }

    public ComentarioMedico buscarPorId(UUID id) {
        return store.get(id);
    }

    /** Persiste los cambios del comentario (ya está en el map por referencia,
     *  pero se mantiene la firma que usa ComentarioServices). */
    public void actualizar(UUID id) {

        // referencia ya actualizada en memoria; no-op en implementación en memoria
    }

    public ArrayList<ComentarioMedico> listarTodos() {
        return new ArrayList<>(store.values());
    }
}
