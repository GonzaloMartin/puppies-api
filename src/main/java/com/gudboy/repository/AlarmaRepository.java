package com.gudboy.repository;

import com.gudboy.domain.alarma.model.Alarma;
import com.gudboy.domain.tratamiento.TipoTratamiento;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AlarmaRepository implements IAlarmaRepository {

    private final Connection connection; // Tu "DatabaseContext"

    public AlarmaRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void add(Alarma alarma) {
        String sql = "INSERT INTO alarmas (id_animal, titulo, descripcion, frecuencia_dias, fecha_proximo_disparo, estado, acciones, completada) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // CORRECCIÓN AQUÍ: setString en lugar de setInt, convirtiendo el UUID a String
            stmt.setString(1, alarma.getIdAnimal().toString());
            stmt.setString(2, alarma.getTitulo());
            stmt.setString(3, alarma.getDescripcion());
            stmt.setInt(4, alarma.getFrecuenciaDias());
            stmt.setTimestamp(5, Timestamp.valueOf(alarma.getFechaProximoDisparoOriginal()));
            stmt.setString(6, alarma.getEstado());

            // Convertimos la lista de Enums a String separada por comas
            String accionesStr = alarma.getAcciones().stream()
                    .map(Enum::name)
                    .collect(java.util.stream.Collectors.joining(","));
            stmt.setString(7, accionesStr);
            stmt.setBoolean(8, alarma.isCompletada());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Alarma alarma) {
        String sql = "DELETE FROM alarmas WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, alarma.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Alarma getById(int id) {
        String sql = "SELECT * FROM alarmas WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToAlarma(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Alarma> getAll() {
        List<Alarma> alarmas = new ArrayList<>();
        String sql = "SELECT * FROM alarmas";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                alarmas.add(mapResultSetToAlarma(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alarmas;
    }

    @Override
    public void update(Alarma alarma) {
        String sql = "UPDATE alarmas SET id_animal = ?, titulo = ?, descripcion = ?, estado = ?, acciones = ?, completada = ?, fecha_completado = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // CORRECCIÓN AQUÍ TAMBIÉN: setString en lugar de setInt
            stmt.setString(1, alarma.getIdAnimal().toString());
            stmt.setString(2, alarma.getTitulo());
            stmt.setString(3, alarma.getDescripcion());
            stmt.setString(4, alarma.getEstado());

            String accionesStr = alarma.getAcciones().stream()
                    .map(Enum::name)
                    .collect(java.util.stream.Collectors.joining(","));
            stmt.setString(5, accionesStr);

            stmt.setBoolean(6, alarma.isCompletada());
            stmt.setTimestamp(7, alarma.isCompletada() ? Timestamp.valueOf(LocalDateTime.now()) : null);
            stmt.setInt(8, alarma.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Alarma mapResultSetToAlarma(ResultSet rs) throws SQLException {
        Alarma alarma = new Alarma();
        alarma.setId(rs.getInt("id"));
        alarma.setIdAnimal(UUID.fromString(rs.getString("id_animal"))); // De String a UUID
        alarma.setTitulo(rs.getString("titulo"));
        alarma.setDescripcion(rs.getString("descripcion"));
        alarma.setFrecuenciaDias(rs.getInt("frecuencia_dias"));
        alarma.setFechaProximoDisparo(rs.getTimestamp("fecha_proximo_disparo").toLocalDateTime());
        alarma.setEstado(rs.getString("estado"));
        alarma.setCompletada(rs.getBoolean("completada"));

        // Reconstruimos la lista desde el String
        String accionesStr = rs.getString("acciones");
        if (accionesStr != null && !accionesStr.isEmpty()) {
            List<TipoTratamiento> acciones = java.util.Arrays.stream(accionesStr.split(","))
                    .map(TipoTratamiento::valueOf)
                    .collect(java.util.stream.Collectors.toList());
            alarma.setAcciones(acciones);
        } else {
            alarma.setAcciones(new java.util.ArrayList<>());
        }

        return alarma;
    }
}