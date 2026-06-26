package com.gudboy.repository;

import com.gudboy.repository.IUsuarioRepository;
import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.tratamiento.Tratamiento;
import com.gudboy.domain.tratamiento.TipoTratamiento;
import com.gudboy.domain.comentarioMedico.ComentarioMedico;
import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.fichaMedica.model.FichaMedica;
import com.gudboy.infrastructure.ConexionMySQL;
import com.gudboy.domain.seguimiento.model.Visita;
import com.gudboy.domain.seguimiento.model.Encuesta;
import com.gudboy.domain.seguimiento.model.CalificacionEnum;
import java.time.LocalDate;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FichaMedicaRepositoryMySQL implements IFichaMedicaRepository {

    private final IAnimalRepository animalRepository;
    private final IUsuarioRepository usuarioRepository;

    public FichaMedicaRepositoryMySQL(IAnimalRepository animalRepository, IUsuarioRepository usuarioRepository) {
        this.animalRepository  = animalRepository;
        this.usuarioRepository = usuarioRepository;
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
            e.printStackTrace();
            throw new RuntimeException("Error al persistir tratamientos: " + e.getMessage(), e);
        }

        String sqlTrat = "INSERT IGNORE INTO tratamiento (id, ficha_id, tipo, estado, fecha_inicio, fecha_fin) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = conn().prepareStatement(sqlTrat)) {
            for (Tratamiento t : ficha.getHistorial().getListaTratamiento()) {
                ps.setString(1, t.getTratamientoID().toString());
                ps.setString(2, ficha.getFichaMedicaId().toString());
                ps.setString(3, t.getTipoTratamientoEnum().name());
                ps.setString(4, t.getEstado().getClass().getSimpleName());
                ps.setTimestamp(5, t.getFechaInicio() != null ? new java.sql.Timestamp(t.getFechaInicio().getTime()) : null);
                ps.setTimestamp(6, t.getFechaFin()    != null ? new java.sql.Timestamp(t.getFechaFin().getTime())    : null);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Error al persistir tratamientos", e);
        }

        String sqlCom = "INSERT IGNORE INTO comentario_medico (id, ficha_id, veterinario_email, texto, fecha) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn().prepareStatement(sqlCom)) {
            for (ComentarioMedico c : ficha.getHistorial().getListaComentario()) {
                ps.setString(1, c.getComentarioID().toString());
                ps.setString(2, ficha.getFichaMedicaId().toString());
                ps.setString(3, c.getVeterinario() != null ? c.getVeterinario().getEmail() : null);
                ps.setString(4, c.getCasillaComentario());
                ps.setTimestamp(5, java.sql.Timestamp.valueOf(c.getFecha()));
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Error al persistir comentarios", e);
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
        ficha.setFichaMedicaId(UUID.fromString(rs.getString("id")));

        String sqlTrat = "SELECT * FROM tratamiento WHERE ficha_id = ?";
        try (PreparedStatement psTrat = conn().prepareStatement(sqlTrat)) {
            psTrat.setString(1, ficha.getFichaMedicaId().toString());
            ResultSet rsTrat = psTrat.executeQuery();
            while (rsTrat.next()) {
                TipoTratamiento tipo = TipoTratamiento.valueOf(rsTrat.getString("tipo"));
                Tratamiento t = new Tratamiento(tipo);
                String estado = rsTrat.getString("estado");
                if ("EnCurso".equals(estado))    t.aplicarTratamiento();
                else if ("Finalizado".equals(estado)) t.finalizarTratamiento();
                else if ("Cancelado".equals(estado))  t.cancelarTratamiento();
                ficha.agregarTratamiento(t);
            }
        } catch (SQLException ignored) {}

        String sqlCom = "SELECT * FROM comentario_medico WHERE ficha_id = ?";
        try (PreparedStatement psCom = conn().prepareStatement(sqlCom)) {
            psCom.setString(1, ficha.getFichaMedicaId().toString());
            ResultSet rsCom = psCom.executeQuery();
            while (rsCom.next()) {
                String email = rsCom.getString("veterinario_email");
                Veterinario vet = null;
                if (email != null) {
                    vet = usuarioRepository.listarTodos().stream()
                            .filter(u -> u instanceof Veterinario && u.getEmail().equals(email))
                            .map(u -> (Veterinario) u)
                            .findFirst().orElse(null);
                }
                ComentarioMedico cm = new ComentarioMedico(
                        vet,
                        rsCom.getString("texto"),
                        rsCom.getTimestamp("fecha").toLocalDateTime());
                ficha.agregarComentarioMedico(cm);
            }
        } catch (SQLException ignored) {}

        return ficha;
    }
}
