package com.gudboy.repository;

import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.fichaMedica.model.FichaMedica;
import com.gudboy.infrastructure.ConexionMySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FichaMedicaRepositoryMySQL implements IFichaMedicaRepository {

    private final IAnimalRepository animalRepository;

    public FichaMedicaRepositoryMySQL(IAnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    private Connection conn() {
        return ConexionMySQL.getInstancia().getConnection();
    }

    @Override
    public void guardar(FichaMedica ficha) {
        String sql = "INSERT INTO ficha_medica (id, animal_id, peso, altura, edad) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, ficha.getFichaMedicaId().toString());
            ps.setString(2, ficha.getAnimal().getId().toString());
            ps.setDouble(3, ficha.getPeso());
            ps.setFloat(4, ficha.getAltura());
            ps.setInt(5, ficha.getEdad());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar ficha médica", e);
        }
    }

    @Override
    public void actualizar(FichaMedica ficha) {
        String sql = "UPDATE ficha_medica SET peso=?, altura=?, edad=? WHERE id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setDouble(1, ficha.getPeso());
            ps.setFloat(2, ficha.getAltura());
            ps.setInt(3, ficha.getEdad());
            ps.setString(4, ficha.getFichaMedicaId().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar ficha médica", e);
        }
    }

    @Override
    public Optional<FichaMedica> buscarPorId(UUID id) {
        String sql = "SELECT * FROM ficha_medica WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, id.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar ficha médica", e);
        }
        return Optional.empty();
    }

    @Override
    public List<FichaMedica> listarTodas() {
        List<FichaMedica> lista = new ArrayList<>();
        String sql = "SELECT * FROM ficha_medica";
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar fichas médicas", e);
        }
        return lista;
    }

    @Override
    public FichaMedica getByAnimalId(UUID idAnimal) {
        String sql = "SELECT * FROM ficha_medica WHERE animal_id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, idAnimal.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar ficha por animal", e);
        }
        return null;
    }

    @Override
    public void update(FichaMedica ficha) {
        actualizar(ficha);
    }

    private FichaMedica mapear(ResultSet rs) throws SQLException {
        UUID animalId = UUID.fromString(rs.getString("animal_id"));
        Animal animal = animalRepository.buscarPorId(animalId)
                .orElseThrow(() -> new RuntimeException("Animal no encontrado: " + animalId));

        FichaMedica ficha = new FichaMedica(animal);
        ficha.actualizarDatos(rs.getDouble("peso"), rs.getFloat("altura"), rs.getInt("edad"));
        return ficha;
    }
}
