package com.gudboy.domain.seguimiento.model;

import com.gudboy.domain.animal.model.Adopcion;
import com.gudboy.domain.Usuario.Usuario;
import com.gudboy.dto.AdopcionDTO;
import com.gudboy.dto.UsuarioDTO;
import com.gudboy.dto.VisitaDTO;
import com.gudboy.dto.SeguimientoDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "seguimiento")
public class Seguimiento {
    @Id
    @Column(name = "id")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "adopcion_id")
    private Adopcion adopcion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "responsable_email")
    private Usuario responsable;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana")
    private DiaSemana diaSemana;

    @Column(name = "horario_desde")
    private String horarioDesde;

    @Column(name = "horario_hasta")
    private String horarioHasta;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoSeguimiento estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferencia_recordatorio")
    private PreferenciaRecordatorio preferenciaRecordatorio;

    @OneToMany(mappedBy = "seguimiento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Visita> visitas;

    protected Seguimiento() {
        this.visitas = new ArrayList<>();
    }

    public Seguimiento(Adopcion adopcion, Usuario responsable, DiaSemana diaSemana, String horarioDesde, String horarioHasta, PreferenciaRecordatorio preferenciaRecordatorio) {
        this.id = UUID.randomUUID();
        this.adopcion = adopcion;
        this.responsable = responsable;
        this.diaSemana = diaSemana;
        this.horarioDesde = horarioDesde;
        this.horarioHasta = horarioHasta;
        this.estado = EstadoSeguimiento.ACTIVO;
        this.preferenciaRecordatorio = preferenciaRecordatorio;
        this.visitas = new ArrayList<>();
    }

    public Seguimiento(UUID id, Adopcion adopcion, Usuario responsable, DiaSemana diaSemana, String horarioDesde, String horarioHasta, EstadoSeguimiento estado, PreferenciaRecordatorio preferenciaRecordatorio) {
        this.id = id;
        this.adopcion = adopcion;
        this.responsable = responsable;
        this.diaSemana = diaSemana;
        this.horarioDesde = horarioDesde;
        this.horarioHasta = horarioHasta;
        this.estado = estado;
        this.preferenciaRecordatorio = preferenciaRecordatorio;
        this.visitas = new ArrayList<>();
    }


    public void agregarVisita(Visita visita) {
        this.visitas.add(visita);
    }

    public void finalizarSeguimiento() {
        this.estado = EstadoSeguimiento.FINALIZADO;
    }

    // --- Getters and Setters ---
    public UUID getId() {
        return id;
    }

    public Adopcion getAdopcion() {
        return adopcion;
    }

    public Usuario getResponsable() {
        return responsable;
    }

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public String getHorarioDesde() {
        return horarioDesde;
    }

    public String getHorarioHasta() {
        return horarioHasta;
    }

    public EstadoSeguimiento getEstado() {
        return estado;
    }

    public PreferenciaRecordatorio getPreferenciaRecordatorio() {
        return preferenciaRecordatorio;
    }

    public List<Visita> getVisitas() {
        return visitas;
    }

    public SeguimientoDTO toDTO() {
        List<VisitaDTO> visitasDTO = new ArrayList<>();
        for (Visita v : visitas) {
            visitasDTO.add(v.toDTO());
        }

        AdopcionDTO adopcionDTO = null;
        if (adopcion != null) {
            adopcionDTO = new AdopcionDTO(
                adopcion.getId(),
                adopcion.getAnimales(),
                adopcion.getResponsable(),
                adopcion.getAdoptante()
            );
        }

        UsuarioDTO responsableDTO = null;
        if (responsable != null) {
            if (responsable instanceof com.gudboy.domain.Usuario.Veterinario vet) {
                responsableDTO = new UsuarioDTO(
                    vet.getNombre(), vet.getApellido(), vet.getEmail(), vet.getTelefono(),
                    vet.getMatriculaProfesional(), vet.getEspecialidad()
                );
            } else if (responsable instanceof com.gudboy.domain.Usuario.Visitador vis) {
                responsableDTO = new UsuarioDTO(
                    vis.getNombre(), vis.getApellido(), vis.getEmail(), vis.getTelefono(),
                    vis.getEstadoCivil(), vis.getOcupacion(), vis.getMotivoAdopcion(),
                    vis.getAnimalesInteres(), vis.tieneOtrasMascotas()
                );
            } else {
                responsableDTO = new UsuarioDTO(
                    responsable.getNombre(), responsable.getApellido(),
                    responsable.getEmail(), responsable.getTelefono(),
                    0, null
                );
            }
        }

        return new SeguimientoDTO(
            id,
            adopcionDTO,
            responsableDTO,
            diaSemana,
            horarioDesde,
            horarioHasta,
            estado,
            preferenciaRecordatorio,
            visitasDTO
        );
    }
    @Override
    public String toString() {
        String animales = adopcion.getAnimales().stream()
            .map(a -> a.getNombre()).collect(java.util.stream.Collectors.joining(", "));
        return String.format("Seg. %s | %s %s | [%s] | %s %s-%s | %s",
            id.toString().substring(0,8),
            adopcion.getAdoptante().getNombre(), adopcion.getAdoptante().getApellido(),
            animales, diaSemana, horarioDesde, horarioHasta, estado);
    }

}
