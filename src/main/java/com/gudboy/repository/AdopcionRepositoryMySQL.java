package com.gudboy.repository;

import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.Usuario.Visitador;
import com.gudboy.domain.animal.model.AnimalDomestico;
import com.gudboy.domain.animal.model.Adopcion;
import com.gudboy.infrastructure.ConexionMySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdopcionRepositoryMySQL implements IAdopcionRepository {

    private final IAnimalRepository animalRepository;
    private final IUsuarioRepository usuarioRepository;

    public AdopcionRepositoryMySQL(IAnimalRepository animalRepository,
                                   IUsuarioRepository usuarioRepository) {
        this.animalRepository = animalRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private Connection conn() {
        return ConexionMySQL.getInstancia().getConnection();
    }

    @Override
    public void guardar(Adopcion adopcion) {
        String sql = "INSERT INTO adopcion (adoptante_email, responsable_email) VALUES (?,?)";
        try (PreparedStatement stmt = conn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, adopcion.getAdoptante().getEmail());
            stmt.setString(2, adopcion.getResponsable().getEmail());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                int adopcionId = keys.getInt(1);
                adopcion.setId(adopcionId);   // FIX: persistir id en la entidad
                insertarAnimales(adopcionId, adopcion.getAnimales());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar adopción", e);
        }
    }

    @Override
    public void actualizar(Adopcion adopcion) {
        try {
            int adopcionId = resolverIdEnBD(adopcion);
            conn().prepareStatement("DELETE FROM adopcion_animal WHERE adopcion_id = " + adopcionId)
                  .executeUpdate();
            insertarAnimales(adopcionId, adopcion.getAnimales());
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar adopción", e);
        }
    }

    @Override
    public List<Adopcion> listarTodos() {
        List<Adopcion> lista = new ArrayList<>();
        String sql = "SELECT id, adoptante_email, responsable_email FROM adopcion";
        try (Statement stmt = conn().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                mapearFila(rs).ifPresent(a -> lista.add(a));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar adopciones", e);
        }
        return lista;
    }

    @Override
    public Optional<Adopcion> buscarPorId(int id) {
        String sql = "SELECT id, adoptante_email, responsable_email FROM adopcion WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearFila(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar adopción por id", e);
        }
        return Optional.empty();
    }

    // ---------- helpers ----------

    private Optional<Adopcion> mapearFila(ResultSet rs) throws SQLException {
        int adopcionId = rs.getInt("id");
        Visitador adoptante = buscarVisitador(rs.getString("adoptante_email"));
        Veterinario responsable = buscarVeterinario(rs.getString("responsable_email"));
        List<AnimalDomestico> animales = buscarAnimalesDeAdopcion(adopcionId);
        if (adoptante == null || responsable == null || animales.isEmpty()) return Optional.empty();
        AnimalDomestico primero = animales.get(0);
        AnimalDomestico segundo = animales.size() > 1 ? animales.get(1) : null;
        Adopcion a = new Adopcion(primero, segundo, adoptante, responsable);
        a.setId(adopcionId);
        return Optional.of(a);
    }

    private int resolverIdEnBD(Adopcion adopcion) throws SQLException {
        if (adopcion.getId() > 0) return adopcion.getId();
        String sql = "SELECT id FROM adopcion WHERE adoptante_email=? AND responsable_email=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, adopcion.getAdoptante().getEmail());
            ps.setString(2, adopcion.getResponsable().getEmail());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        }
        throw new SQLException("Adopción no encontrada en BD");
    }

    private void insertarAnimales(int adopcionId, List<AnimalDomestico> animales) throws SQLException {
        String sql = "INSERT INTO adopcion_animal (adopcion_id, animal_id) VALUES (?,?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            for (AnimalDomestico a : animales) {
                ps.setInt(1, adopcionId);
                ps.setString(2, a.getId().toString());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private Visitador buscarVisitador(String email) {
        return usuarioRepository.listarTodos().stream()
                .filter(u -> u instanceof Visitador && u.getEmail().equals(email))
                .map(u -> (Visitador) u).findFirst().orElse(null);
    }

    private Veterinario buscarVeterinario(String email) {
        return usuarioRepository.listarTodos().stream()
                .filter(u -> u instanceof Veterinario && u.getEmail().equals(email))
                .map(u -> (Veterinario) u).findFirst().orElse(null);
    }

    private List<AnimalDomestico> buscarAnimalesDeAdopcion(int adopcionId) {
        List<AnimalDomestico> animales = new ArrayList<>();
        String sql = "SELECT animal_id FROM adopcion_animal WHERE adopcion_id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, adopcionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                java.util.UUID animalId = java.util.UUID.fromString(rs.getString("animal_id"));
                animalRepository.buscarPorId(animalId)
                        .filter(a -> a instanceof AnimalDomestico)
                        .map(a -> (AnimalDomestico) a)
                        .ifPresent(animales::add);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar animales de adopción", e);
        }
        return animales;
    }
}
