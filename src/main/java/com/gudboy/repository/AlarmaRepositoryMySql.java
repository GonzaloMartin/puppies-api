package com.gudboy.repository;

import com.gudboy.domain.alarma.model.Alarma;
import com.gudboy.domain.tratamiento.TipoTratamiento;
import com.gudboy.infrastructure.ConexionMySQL;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AlarmaRepositoryMySql implements IAlarmaRepository {

    // Usamos el Singleton directamente aquí para cumplir con el alcance del TP
    private Connection conn() {
        return ConexionMySQL.getInstancia().getConnection();
    }

    @Override
    public void add(Alarma alarma) {
        String sql = "INSERT INTO alarmas (id_animal, titulo, descripcion, frecuencia_dias, fecha_proximo_disparo, estado, acciones, completada) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn().prepareStatement(sql)) {
            stmt.setString(1, alarma.getIdAnimal().toString());
            stmt.setString(2, alarma.getTitulo());
            stmt.setString(3, alarma.getDescripcion());
            stmt.setInt(4, alarma.getFrecuenciaDias());
            stmt.setTimestamp(5, Timestamp.valueOf(alarma.getFechaProximoDisparoOriginal()));
            stmt.setString(6, alarma.getEstado());

            String accionesStr = alarma.getAcciones().stream()
                    .map(Enum::name)
                    .collect(Collectors.joining(","));
            stmt.setString(7, accionesStr);
            stmt.setBoolean(8, alarma.isCompletada());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la alarma", e);
        }
    }

    @Override
    public void remove(Alarma alarma) {
        String sql = "DELETE FROM alarmas WHERE id = ?";
        try (PreparedStatement stmt = conn().prepareStatement(sql)) {
            stmt.setInt(1, alarma.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la alarma", e);
        }
    }

    @Override
    public Alarma getById(int id) {
        String sql = "SELECT * FROM alarmas WHERE id = ?";
        try (PreparedStatement stmt = conn().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAlarma(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la alarma por ID", e);
        }
        return null;
    }

    @Override
    public List<Alarma> getAll() {
        List<Alarma> alarmas = new ArrayList<>();
        String sql = "SELECT * FROM alarmas";
        try (PreparedStatement stmt = conn().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                alarmas.add(mapResultSetToAlarma(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todas las alarmas", e);
        }
        return alarmas;
    }

    @Override
    public void update(Alarma alarma) {
        String sql = "UPDATE alarmas SET id_animal = ?, titulo = ?, descripcion = ?, estado = ?, acciones = ?, completada = ?, fecha_completado = ? WHERE id = ?";
        try (PreparedStatement stmt = conn().prepareStatement(sql)) {
            stmt.setString(1, alarma.getIdAnimal().toString());
            stmt.setString(2, alarma.getTitulo());
            stmt.setString(3, alarma.getDescripcion());
            stmt.setString(4, alarma.getEstado());

            String accionesStr = alarma.getAcciones().stream()
                    .map(Enum::name)
                    .collect(Collectors.joining(","));
            stmt.setString(5, accionesStr);

            stmt.setBoolean(6, alarma.isCompletada());
            stmt.setTimestamp(7, alarma.isCompletada() ? Timestamp.valueOf(LocalDateTime.now()) : null);
            stmt.setInt(8, alarma.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la alarma", e);
        }
    }

    private Alarma mapResultSetToAlarma(ResultSet rs) throws SQLException {
        Alarma alarma = new Alarma();
        alarma.setId(rs.getInt("id"));
        alarma.setIdAnimal(UUID.fromString(rs.getString("id_animal")));
        alarma.setTitulo(rs.getString("titulo"));
        alarma.setDescripcion(rs.getString("descripcion"));
        alarma.setFrecuenciaDias(rs.getInt("frecuencia_dias"));
        alarma.setFechaProximoDisparo(rs.getTimestamp("fecha_proximo_disparo").toLocalDateTime());
        alarma.setEstado(rs.getString("estado"));
        alarma.setCompletada(rs.getBoolean("completada"));

        String accionesStr = rs.getString("acciones");
        if (accionesStr != null && !accionesStr.isEmpty()) {
            List<TipoTratamiento> acciones = java.util.Arrays.stream(accionesStr.split(","))
                    .map(TipoTratamiento::valueOf)
                    .collect(Collectors.toList());
            alarma.setAcciones(acciones);
        } else {
            alarma.setAcciones(new ArrayList<>());
        }
        return alarma;
    }
}