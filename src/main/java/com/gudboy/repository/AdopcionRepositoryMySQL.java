package com.gudboy.repository;

import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.Usuario.Visitador;
import com.gudboy.domain.animal.model.AnimalDomestico;
import com.gudboy.domain.animal.model.Adopcion;
import com.gudboy.infrastructure.ConexionMySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdopcionRepositoryMySQL implements IAdopcionRepository {

    private final IAnimalRepository animalRepository;
    private final IUsuarioRepository usuarioRepository;

    public AdopcionRepositoryMySQL(IAnimalRepository animalRepository, IUsuarioRepository usuarioRepository) {
        this.animalRepository = animalRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private Connection conn() {
        return ConexionMySQL.getInstancia().getConnection();
    }

    @Override
    public void guardar(Adopcion adopcion) {
        String sqlAdopcion = "INSERT INTO adopcion (adoptante_email, responsable_email) VALUES (?,?)";
        try (PreparedStatement stmt = conn().prepareStatement(sqlAdopcion, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, adopcion.getAdoptante().getEmail());
            stmt.setString(2, adopcion.getResponsable().getEmail());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                int adopcionId = keys.getInt(1);
                insertarAnimales(adopcionId, adopcion.getAnimales());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Adopcion adopcion) {
        String sqlId = "SELECT id FROM adopcion WHERE adoptante_email = ? AND responsable_email = ?";
        try (PreparedStatement stmt = conn().prepareStatement(sqlId)) {
            stmt.setString(1, adopcion.getAdoptante().getEmail());
            stmt.setString(2, adopcion.getResponsable().getEmail());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int adopcionId = rs.getInt("id");
                conn().prepareStatement("DELETE FROM adopcion_animal WHERE adopcion_id = " + adopcionId).executeUpdate();
                insertarAnimales(adopcionId, adopcion.getAnimales());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Adopcion> listarTodos() {
        List<Adopcion> lista = new ArrayList<>();
        String sql = "SELECT a.id, a.adoptante_email, a.responsable_email FROM adopcion a";
        try (Statement stmt = conn().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int adopcionId = rs.getInt("id");
                String adoptanteEmail = rs.getString("adoptante_email");
                String responsableEmail = rs.getString("responsable_email");

                Visitador adoptante = buscarVisitador(adoptanteEmail);
                Veterinario responsable = buscarVeterinario(responsableEmail);
                List<AnimalDomestico> animales = buscarAnimalesDeAdopcion(adopcionId);

                if (adoptante != null && responsable != null && !animales.isEmpty()) {
                    AnimalDomestico primero = animales.get(0);
                    AnimalDomestico segundo = animales.size() > 1 ? animales.get(1) : null;
                    lista.add(new Adopcion(primero, segundo, adoptante, responsable));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private void insertarAnimales(int adopcionId, List<AnimalDomestico> animales) throws SQLException {
        String sqlAnimal = "INSERT INTO adopcion_animal (adopcion_id, animal_id) VALUES (?,?)";
        try (PreparedStatement stmt = conn().prepareStatement(sqlAnimal)) {
            for (AnimalDomestico animal : animales) {
                stmt.setInt(1, adopcionId);
                stmt.setString(2, animal.getId().toString());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private Visitador buscarVisitador(String email) {
        return usuarioRepository.listarTodos().stream()
                .filter(u -> u instanceof Visitador && u.getEmail().equals(email))
                .map(u -> (Visitador) u)
                .findFirst().orElse(null);
    }

    private Veterinario buscarVeterinario(String email) {
        return usuarioRepository.listarTodos().stream()
                .filter(u -> u instanceof Veterinario && u.getEmail().equals(email))
                .map(u -> (Veterinario) u)
                .findFirst().orElse(null);
    }

    private List<AnimalDomestico> buscarAnimalesDeAdopcion(int adopcionId) {
        List<AnimalDomestico> animales = new ArrayList<>();
        String sql = "SELECT animal_id FROM adopcion_animal WHERE adopcion_id = ?";
        try (PreparedStatement stmt = conn().prepareStatement(sql)) {
            stmt.setInt(1, adopcionId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                java.util.UUID animalId = java.util.UUID.fromString(rs.getString("animal_id"));
                animalRepository.buscarPorId(animalId)
                        .filter(a -> a instanceof AnimalDomestico)
                        .map(a -> (AnimalDomestico) a)
                        .ifPresent(animales::add);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return animales;
    }
}
