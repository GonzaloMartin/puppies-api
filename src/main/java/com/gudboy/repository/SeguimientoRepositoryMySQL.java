package com.gudboy.repository;

import com.gudboy.domain.Usuario.Usuario;
import com.gudboy.domain.animal.model.Adopcion;
import com.gudboy.domain.seguimiento.model.*;
import com.gudboy.infrastructure.ConexionMySQL;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SeguimientoRepositoryMySQL implements ISeguimientoRepository {

    private final IAdopcionRepository adopcionRepository;
    private final IUsuarioRepository usuarioRepository;

    public SeguimientoRepositoryMySQL(IAdopcionRepository adopcionRepository, IUsuarioRepository usuarioRepository) {
        this.adopcionRepository = adopcionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private Connection conn() {
        return ConexionMySQL.getInstancia().getConnection();
    }

    private int obtenerAdopcionId(Adopcion adopcion) throws SQLException {
        String sql = "SELECT id FROM adopcion WHERE adoptante_email = ? AND responsable_email = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, adopcion.getAdoptante().getEmail());
            ps.setString(2, adopcion.getResponsable().getEmail());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        throw new SQLException("No se encontró la adopción en la base de datos");
    }

    @Override
    public void guardar(Seguimiento seguimiento) {
        if (buscarPorId(seguimiento.getId()).isPresent()) {
            actualizar(seguimiento);
            return;
        }
        String sql = "INSERT INTO seguimiento (id, adopcion_id, responsable_email, dia_semana, horario_desde, horario_hasta, estado, preferencia_recordatorio) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            int adopcionId = obtenerAdopcionId(seguimiento.getAdopcion());
            try (PreparedStatement ps = conn().prepareStatement(sql)) {
                ps.setString(1, seguimiento.getId().toString());
                ps.setInt(2, adopcionId);
                ps.setString(3, seguimiento.getResponsable().getEmail());
                ps.setString(4, seguimiento.getDiaSemana().name());
                ps.setString(5, seguimiento.getHorarioDesde());
                ps.setString(6, seguimiento.getHorarioHasta());
                ps.setString(7, seguimiento.getEstado().name());
                ps.setString(8, seguimiento.getPreferenciaRecordatorio().name());
                ps.executeUpdate();
            }

            for (Visita v : seguimiento.getVisitas()) {
                guardarVisita(v);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar seguimiento en MySQL", e);
        }
    }

    @Override
    public void actualizar(Seguimiento seguimiento) {
        String sql = "UPDATE seguimiento SET estado = ? WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, seguimiento.getEstado().name());
            ps.setString(2, seguimiento.getId().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar seguimiento en MySQL", e);
        }
    }

    @Override
    public Optional<Seguimiento> buscarPorId(UUID id) {
        String sql = "SELECT * FROM seguimiento WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, id.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar seguimiento por ID en MySQL", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Seguimiento> listarTodos() {
        List<Seguimiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM seguimiento";
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar seguimientos en MySQL", e);
        }
        return lista;
    }

    @Override
    public void guardarVisita(Visita visita) {
        if (buscarVisitaPorId(visita.getId()).isPresent()) {
            actualizarVisita(visita);
            return;
        }
        String sql = "INSERT INTO visitas (id, seguimiento_id, fecha_programada, fecha_real, comentarios, completada, continuar_visitas, estado_general_animal, limpieza_lugar, ambiente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, visita.getId().toString());
            ps.setString(2, visita.getSeguimiento().getId().toString());
            ps.setDate(3, Date.valueOf(visita.getFechaProgramada()));
            ps.setDate(4, visita.getFechaReal() != null ? Date.valueOf(visita.getFechaReal()) : null);
            ps.setString(5, visita.getComentarios());
            ps.setBoolean(6, visita.isCompletada());
            ps.setBoolean(7, visita.isContinuarVisitas());
            if (visita.getEncuesta() != null) {
                ps.setString(8, visita.getEncuesta().getEstadoGeneralAnimal().name());
                ps.setString(9, visita.getEncuesta().getLimpiezaLugar().name());
                ps.setString(10, visita.getEncuesta().getAmbiente().name());
            } else {
                ps.setNull(8, Types.VARCHAR);
                ps.setNull(9, Types.VARCHAR);
                ps.setNull(10, Types.VARCHAR);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar visita en MySQL", e);
        }
    }

    @Override
    public void actualizarVisita(Visita visita) {
        String sql = "UPDATE visitas SET fecha_real = ?, comentarios = ?, completada = ?, continuar_visitas = ?, estado_general_animal = ?, limpieza_lugar = ?, ambiente = ? WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setDate(1, visita.getFechaReal() != null ? Date.valueOf(visita.getFechaReal()) : null);
            ps.setString(2, visita.getComentarios());
            ps.setBoolean(3, visita.isCompletada());
            ps.setBoolean(4, visita.isContinuarVisitas());
            if (visita.getEncuesta() != null) {
                ps.setString(5, visita.getEncuesta().getEstadoGeneralAnimal().name());
                ps.setString(6, visita.getEncuesta().getLimpiezaLugar().name());
                ps.setString(7, visita.getEncuesta().getAmbiente().name());
            } else {
                ps.setNull(5, Types.VARCHAR);
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.VARCHAR);
            }
            ps.setString(8, visita.getId().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar visita en MySQL", e);
        }
    }

    @Override
    public Optional<Visita> buscarVisitaPorId(UUID id) {
        String sql = "SELECT * FROM visitas WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, id.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearVisita(rs, null));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar visita por ID en MySQL", e);
        }
        return Optional.empty();
    }

    private List<Visita> listarVisitasPorSeguimiento(UUID seguimientoId, Seguimiento s) {
        List<Visita> lista = new ArrayList<>();
        String sql = "SELECT * FROM visitas WHERE seguimiento_id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, seguimientoId.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearVisita(rs, s));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar visitas de seguimiento en MySQL", e);
        }
        return lista;
    }

    @Override
    public List<Visita> listarVisitasPorSeguimiento(UUID seguimientoId) {
        return listarVisitasPorSeguimiento(seguimientoId, null);
    }

    private Seguimiento mapear(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("id"));
        int adopcionId = rs.getInt("adopcion_id");
        String responsableEmail = rs.getString("responsable_email");
        DiaSemana diaSemana = DiaSemana.valueOf(rs.getString("dia_semana"));
        String horarioDesde = rs.getString("horario_desde");
        String horarioHasta = rs.getString("horario_hasta");
        EstadoSeguimiento estado = EstadoSeguimiento.valueOf(rs.getString("estado"));
        PreferenciaRecordatorio pref = PreferenciaRecordatorio.valueOf(rs.getString("preferencia_recordatorio"));

        Adopcion adopcion = buscarAdopcionPorId(adopcionId);
        Usuario responsable = buscarUsuarioPorEmail(responsableEmail);

        Seguimiento s = new Seguimiento(id, adopcion, responsable, diaSemana, horarioDesde, horarioHasta, estado, pref);
        List<Visita> visitasDeSeguimiento = listarVisitasPorSeguimiento(id, s);
        for (Visita v : visitasDeSeguimiento) {
            Visita vConSeguimiento = new Visita(v.getId(), s, v.getFechaProgramada(), v.getFechaReal(), v.getComentarios(), v.isCompletada(), v.isContinuarVisitas());
            if (v.getEncuesta() != null) {
                vConSeguimiento.registrarResultado(v.getEncuesta(), v.getComentarios(), v.isContinuarVisitas());
            }
            s.agregarVisita(vConSeguimiento);
        }
        return s;
    }

    private Visita mapearVisita(ResultSet rs, Seguimiento s) throws SQLException {
        UUID id = UUID.fromString(rs.getString("id"));
        LocalDate fechaProg = rs.getDate("fecha_programada").toLocalDate();
        Date sqlFechaReal = rs.getDate("fecha_real");
        LocalDate fechaReal = sqlFechaReal != null ? sqlFechaReal.toLocalDate() : null;
        String comentarios = rs.getString("comentarios");
        boolean completada = rs.getBoolean("completada");
        boolean continuar = rs.getBoolean("continuar_visitas");

        String estAnimal = rs.getString("estado_general_animal");
        String limpLugar = rs.getString("limpieza_lugar");
        String ambiente = rs.getString("ambiente");
        Encuesta encuesta = null;
        if (estAnimal != null && limpLugar != null && ambiente != null) {
            encuesta = new Encuesta(
                    CalificacionEnum.valueOf(estAnimal),
                    CalificacionEnum.valueOf(limpLugar),
                    CalificacionEnum.valueOf(ambiente)
            );
        }

        if (s == null) {
            UUID segId = UUID.fromString(rs.getString("seguimiento_id"));
            s = buscarPorId(segId).orElse(null);
        }

        Visita v = new Visita(id, s, fechaProg, fechaReal, comentarios, completada, continuar);
        if (encuesta != null) {
            v.registrarResultado(encuesta, comentarios, continuar);
        }
        return v;
    }

    private Adopcion buscarAdopcionPorId(int id) {
        // FIX: usar buscarPorId en lugar de iterar con obtenerAdopcionId()
        return adopcionRepository.buscarPorId(id).orElse(null);
    }

    private Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.listarTodos().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst().orElse(null);
    }
}
