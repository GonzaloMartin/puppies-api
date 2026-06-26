package com.gudboy.domain.comentarioMedico;

import com.gudboy.domain.Usuario.Veterinario;
import java.time.LocalDateTime;
import java.util.UUID;

public class ComentarioMedico {
    private final UUID comentarioID;
    private Veterinario veterinario;
    private String casillaComentario;
    private LocalDateTime fecha;

    /** Constructor original (sin fecha) */
    public ComentarioMedico(Veterinario veterinario, String casillaComentario) {
        this.comentarioID = UUID.randomUUID();
        this.veterinario = veterinario;
        this.casillaComentario = casillaComentario;
        this.fecha = LocalDateTime.now();
    }

    /** Constructor con fecha explícita (requerido por FichaMedicaService y tests) */
    public ComentarioMedico(Veterinario veterinario, String casillaComentario, LocalDateTime fecha) {
        this.comentarioID = UUID.randomUUID();
        this.veterinario = veterinario;
        this.casillaComentario = casillaComentario;
        this.fecha = fecha;
    }

    public Veterinario getVeterinario() { return veterinario; }

    public String getCasillaComentario() { return casillaComentario; }

    public UUID getComentarioID() { return comentarioID; }

    public LocalDateTime getFecha() { return fecha; }

    public void modificarComentario(String nuevoComenario) {
        this.casillaComentario = nuevoComenario;
        this.fecha = LocalDateTime.now();
    }
}
