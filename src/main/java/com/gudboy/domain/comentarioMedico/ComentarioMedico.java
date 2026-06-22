package com.gudboy.domain.comentarioMedico;
import com.gudboy.domain.Usuario.Veterinario;

import java.util.Date;
import java.util.UUID;

public class ComentarioMedico {
    private final UUID comentarioID;
    private Veterinario veterinario;
    private String casillaComentario;
    private Date fecha;

    public ComentarioMedico(Veterinario veterinario, String casillaComentario, Date fecha){
        this.comentarioID = UUID.randomUUID();
        this.veterinario = veterinario;
        this.casillaComentario = casillaComentario;
        this.fecha = fecha;
    }

    private void setVeterinario(Veterinario v){
        this.veterinario = v;
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

    private void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFecha() {
        return fecha;
    }

    public void modificarComentario(Veterinario veterinario, String casillaComentario, Date fecha){
        setVeterinario(veterinario);
        setCasillaComentario(casillaComentario);
        setFecha(fecha);
    }


}
