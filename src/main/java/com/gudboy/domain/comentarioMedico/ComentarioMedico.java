package com.gudboy.domain.comentarioMedico;
import com.gudboy.domain.Usuario.Veterinario;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class ComentarioMedico {
    private final UUID comentarioID;
    private Veterinario veterinario;
    private String casillaComentario;
    private Date fecha;

    public ComentarioMedico(Veterinario veterinario, String casillaComentario){
        this.comentarioID = UUID.randomUUID();
        this.veterinario = veterinario;
        this.casillaComentario = casillaComentario;
        this.fecha = new Date();
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    private void setCasillaComentario(String casillaComentario) {
        this.casillaComentario = casillaComentario;
    }

    public String getCasillaComentario() {
        return casillaComentario;
    }

    public UUID getComentarioID() {
        return comentarioID;
    }

    public Date getFecha() {
        return fecha;
    }

    public void modificarComentario(String casillaComentario){
        setCasillaComentario(casillaComentario);
        this.fecha = new Date();
    }


}
