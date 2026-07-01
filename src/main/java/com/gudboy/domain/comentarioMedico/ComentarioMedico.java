package com.gudboy.domain.comentarioMedico;

import com.gudboy.domain.Usuario.Veterinario;
import java.time.LocalDateTime;
import java.util.UUID;
import com.gudboy.dto.ComentarioMedicoDTO;

import jakarta.persistence.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "comentario_medico")
public class ComentarioMedico {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)")
    private UUID comentarioID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "veterinario_email", referencedColumnName = "email")
    private Veterinario veterinario;

    @Column(name = "texto", length = 1000, nullable = false)
    private String casillaComentario;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    protected ComentarioMedico() {}

    public ComentarioMedico(Veterinario veterinario, String casillaComentario) {
        this.comentarioID = UUID.randomUUID();
        this.veterinario = veterinario;
        this.casillaComentario = casillaComentario;
        this.fecha = LocalDateTime.now();
    }

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

    public void modificarComentario(String nuevoComentario) {
        this.casillaComentario = nuevoComentario;
        this.fecha = LocalDateTime.now();
    }

    public ComentarioMedicoDTO toDTO() {
        return new ComentarioMedicoDTO(
                comentarioID,
                veterinario != null ? veterinario.getEmail() : null,
                casillaComentario,
                fecha
        );
    }
}