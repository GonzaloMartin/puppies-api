package com.gudboy.repository;

import com.gudboy.domain.seguimiento.model.*;
import com.gudboy.infrastructure.ConexionMySQL;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@SuppressWarnings("unused")
public class VisitaRepositoryMySQL implements IVisitaRepository {

    private final ISeguimientoRepository seguimientoRepository;

    public VisitaRepositoryMySQL(ISeguimientoRepository seguimientoRepository) {
        this.seguimientoRepository = seguimientoRepository;
    }

    private Connection conn() {
        return ConexionMySQL.getInstancia().getConnection();
    }

    @Override
    public void guardar(Visita visita) {
        if (buscarPorId(visita.getId()).isPresent()) {
            actualizar(visita);
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
    public Optional<Visita> buscarPorId(UUID id) {
        String sql = "SELECT * FROM visitas WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, id.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearVisita(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar visita por ID en MySQL", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Visita> listarPorSeguimiento(UUID seguimientoId) {
        List<Visita> lista = new ArrayList<>();
        String sql = "SELECT * FROM visitas WHERE seguimiento_id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, seguimientoId.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearVisita(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar visitas de seguimiento en MySQL", e);
        }
        return lista;
    }

    @Override
    public void actualizar(Visita visita) {
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

    private Visita mapearVisita(ResultSet rs) throws SQLException {
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

        UUID segId = UUID.fromString(rs.getString("seguimiento_id"));
        Seguimiento s = seguimientoRepository.buscarPorId(segId).orElse(null);

        Visita v = new Visita(id, s, fechaProg, fechaReal, comentarios, completada, continuar);
        if (encuesta != null) {
            v.registrarResultado(encuesta, comentarios, continuar);
        }
        return v;
    }
}
