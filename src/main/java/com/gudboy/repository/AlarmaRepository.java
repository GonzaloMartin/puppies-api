package com.gudboy.repository;

import com.gudboy.domain.alarma.model.Alarma;
import com.gudboy.domain.tratamiento.TipoTratamiento;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlarmaRepository implements IAlarmaRepository {

    private final Connection connection; // Tu "DatabaseContext"

    public AlarmaRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void add(Alarma alarma) {
        String sql = "INSERT INTO alarmas (titulo, descripcion, frecuencia_dias, fecha_proximo_disparo, estado, tipo_tratamiento, completada) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, alarma.getTitulo());
            stmt.setString(2, alarma.getDescripcion());
            stmt.setInt(3, alarma.getFrecuenciaDias());
            stmt.setTimestamp(4, Timestamp.valueOf(alarma.getFechaProximoDisparo()));
            stmt.setString(5, alarma.getEstado());
            stmt.setString(6, alarma.getTipoTratamiento().name());
            stmt.setBoolean(7, alarma.isCompletada());
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
        String sql = "UPDATE alarmas SET titulo = ?, descripcion = ?, estado = ?, completada = ?, fecha_completado = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, alarma.getTitulo());
            stmt.setString(2, alarma.getDescripcion());
            stmt.setString(3, alarma.getEstado());
            stmt.setBoolean(4, alarma.isCompletada());
            stmt.setTimestamp(5, alarma.isCompletada() ? Timestamp.valueOf(LocalDateTime.now()) : null);
            stmt.setInt(6, alarma.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Alarma mapResultSetToAlarma(ResultSet rs) throws SQLException {
        Alarma alarma = new Alarma();
        alarma.setId(rs.getInt("id"));
        alarma.setTitulo(rs.getString("titulo"));
        // Llenar el resto de los campos...
        return alarma;
    }
}