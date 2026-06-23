package com.gudboy.repository;

import com.gudboy.domain.animal.State.EstadoEnTratamiento;
import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.animal.model.AnimalDomestico;
import com.gudboy.domain.animal.model.AnimalSalvaje;
import com.gudboy.infrastructure.ConexionMySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AnimalRepositoryMySQL implements IAnimalRepository {

    private Connection conn() {
        return ConexionMySQL.getInstancia().getConnection();
    }

    @Override
    public void guardar(Animal animal) {
        String sql = "INSERT INTO animal (id, nombre, especie, tipo_animal, altura, peso, edad, condicion_medica, en_tratamiento, habitat_natural) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            setAnimalParams(ps, animal);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar animal", e);
        }
    }

    @Override
    public void actualizar(Animal animal) {
        String sql = "UPDATE animal SET nombre=?, especie=?, tipo_animal=?, altura=?, peso=?, edad=?, condicion_medica=?, en_tratamiento=?, habitat_natural=? WHERE id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, animal.getNombre());
            ps.setString(2, animal.getEspecie());
            ps.setString(3, animal.getTipoAnimal());
            ps.setDouble(4, animal.getAltura());
            ps.setDouble(5, animal.getPeso());
            ps.setInt(6, animal.getEdad());
            ps.setString(7, animal.getEstadoDeSalud().getClass().getSimpleName());
            ps.setBoolean(8, animal.getEstadoDeSalud() instanceof EstadoEnTratamiento);
            ps.setString(9, animal instanceof AnimalSalvaje ? ((AnimalSalvaje) animal).getHabitatNatural() : null);
            ps.setString(10, animal.getId().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar animal", e);
        }
    }

    @Override
    public Optional<Animal> buscarPorId(UUID id) {
        String sql = "SELECT * FROM animal WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, id.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar animal", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Animal> listarTodos() {
        List<Animal> lista = new ArrayList<>();
        String sql = "SELECT * FROM animal";
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar animales", e);
        }
        return lista;
    }

    private void setAnimalParams(PreparedStatement ps, Animal animal) throws SQLException {
        ps.setString(1, animal.getId().toString());
        ps.setString(2, animal.getNombre());
        ps.setString(3, animal.getEspecie());
        ps.setString(4, animal.getTipoAnimal());
        ps.setDouble(5, animal.getAltura());
        ps.setDouble(6, animal.getPeso());
        ps.setInt(7, animal.getEdad());
        ps.setString(8, animal.getEstadoDeSalud().getClass().getSimpleName());
        ps.setBoolean(9, animal.getEstadoDeSalud() instanceof EstadoEnTratamiento);
        ps.setString(10, animal instanceof AnimalSalvaje ? ((AnimalSalvaje) animal).getHabitatNatural() : null);
    }

    private Animal mapear(ResultSet rs) throws SQLException {
        UUID   id      = UUID.fromString(rs.getString("id"));
        String tipo    = rs.getString("tipo_animal");
        String nombre  = rs.getString("nombre");
        String especie = rs.getString("especie");
        double altura  = rs.getDouble("altura");
        double peso    = rs.getDouble("peso");
        int    edad    = rs.getInt("edad");
        String condicion = rs.getString("condicion_medica");
        boolean enTratamiento = rs.getBoolean("en_tratamiento");

        if ("DOMESTICO".equals(tipo)) {
            AnimalDomestico a = new AnimalDomestico(nombre, especie, altura, peso, edad, condicion);
            a.setId(id);
            if (enTratamiento) a.ponerEnTratamiento();
            return a;
        } else {
            AnimalSalvaje a = new AnimalSalvaje(nombre, especie, altura, peso, edad, condicion,
                    rs.getString("habitat_natural"));
            a.setId(id);
            return a;
        }
    }
}
