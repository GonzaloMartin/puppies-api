package com.gudboy.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.gudboy.domain.animal.State.EstadoEnTratamiento;
import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.animal.model.AnimalDomestico;
import com.gudboy.domain.animal.model.AnimalSalvaje;
import com.gudboy.infrastructure.ConexionMySQL;

public class AnimalRepositoryMySQL implements IAnimalRepository {

    private Connection conn() {
        return ConexionMySQL.getInstancia().getConnection();
    }

    @Override
    public void guardar(Animal animal) {
        String sql = "INSERT INTO animal (id, nombre, especie, tipo_animal, altura, peso, edad, condicion_medica, en_tratamiento, habitat_natural, adoptado) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            setAnimalParams(ps, animal);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar animal", e);
        }
    }

    @Override
    public void actualizar(Animal animal) {
        String sql = "UPDATE animal SET nombre=?, especie=?, tipo_animal=?, altura=?, peso=?, edad=?, condicion_medica=?, en_tratamiento=?, habitat_natural=?, adoptado=? WHERE id=?";
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
            ps.setBoolean(10, animal instanceof AnimalDomestico
                    && !(((AnimalDomestico) animal).getEstadoAdopcion()
                            instanceof com.gudboy.domain.animal.State.EstadoDisponible));
            ps.setString(11, animal.getId().toString());
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
        ps.setBoolean(11, animal instanceof AnimalDomestico
                && !(((AnimalDomestico) animal).getEstadoAdopcion()
                        instanceof com.gudboy.domain.animal.State.EstadoDisponible));
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
        boolean adoptado      = rs.getBoolean("adoptado");

        if ("DOMESTICO".equals(tipo)) {
            AnimalDomestico a = new AnimalDomestico(nombre, especie, altura, peso, edad, condicion);
            a.setId(id);
            if (enTratamiento) a.ponerEnTratamiento();
            // Restaurar estado directamente sin pasar por validación de negocio
            // (esAdoptable ya fue chequeado cuando se adoptó originalmente)
            if (adoptado) a.setEstadoAdopcion(new com.gudboy.domain.animal.State.EstadoAdoptado(a));
            return a;
        } else {
            AnimalSalvaje a = new AnimalSalvaje(nombre, especie, altura, peso, edad, condicion,
                    rs.getString("habitat_natural"));
            a.setId(id);
            return a;
        }
    }
}