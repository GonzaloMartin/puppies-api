package com.gudboy.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.comentarioMedico.ComentarioMedico;
import com.gudboy.domain.fichaMedica.model.FichaMedica;
import com.gudboy.domain.seguimiento.model.CalificacionEnum;
import com.gudboy.domain.seguimiento.model.Encuesta;
import com.gudboy.domain.seguimiento.model.Visita;
import com.gudboy.domain.tratamiento.TipoTratamiento;
import com.gudboy.domain.tratamiento.Tratamiento;
import com.gudboy.infrastructure.HibernateUtil;

public class FichaMedicaRepositoryHibernate implements IFichaMedicaRepository {

    private final IUsuarioRepository usuarioRepository;

    public FichaMedicaRepositoryHibernate(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void guardar(FichaMedica ficha) {
        try (Session session = HibernateUtil.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(ficha);
            persistirHistorial(session, ficha);
            tx.commit();
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar ficha médica", e);
        }
    }

    @Override
    public void actualizar(FichaMedica ficha) {
        try (Session session = HibernateUtil.openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(ficha);
            persistirHistorial(session, ficha);
            tx.commit();
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar ficha médica", e);
        }
    }

    @Override
    public void update(FichaMedica ficha) {
        actualizar(ficha);
    }

    @Override
    public Optional<FichaMedica> buscarPorId(UUID id) {
        try (Session session = HibernateUtil.openSession()) {
            FichaMedica ficha = session.get(FichaMedica.class, id);
            if (ficha != null) cargarHistorial(session, ficha);
            return Optional.ofNullable(ficha);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar ficha médica", e);
        }
    }

    @Override
    public List<FichaMedica> listarTodas() {
        try (Session session = HibernateUtil.openSession()) {
            List<FichaMedica> lista = session
                    .createQuery("from FichaMedica", FichaMedica.class)
                    .list();
            lista.forEach(f -> cargarHistorial(session, f));
            return lista;
        } catch (Exception e) {
            throw new RuntimeException("Error al listar fichas médicas", e);
        }
    }

    @Override
    public FichaMedica getByAnimalId(UUID animalId) {
        try (Session session = HibernateUtil.openSession()) {
            FichaMedica ficha = session
                    .createQuery("from FichaMedica f where f.animal.id = :aid", FichaMedica.class)
                    .setParameter("aid", animalId)
                    .uniqueResult();
            if (ficha != null) cargarHistorial(session, ficha);
            return ficha;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar ficha por animal", e);
        }
    }

    // ── Persistencia del historial ────────────────────────────────────────────

    private void persistirHistorial(Session session, FichaMedica ficha) {
        if (ficha.getHistorial() == null) return;
        String fichaId = ficha.getFichaMedicaId().toString();
        persistirTratamientos(session, ficha, fichaId);
        persistirComentarios(session, ficha, fichaId);
    }

    private void persistirTratamientos(Session session, FichaMedica ficha, String fichaId) {
        session.doWork(conn -> {
            String sql =
                    "INSERT INTO tratamiento (id, ficha_id, tipo, estado, fecha_inicio, fecha_fin) " +
                            "VALUES (?, ?, ?, ?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE " +
                            "estado = VALUES(estado), " +
                            "fecha_inicio = VALUES(fecha_inicio), " +
                            "fecha_fin = VALUES(fecha_fin)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (Tratamiento t : ficha.getHistorial().getListaTratamiento()) {
                    ps.setString(1, t.getTratamientoID().toString());
                    ps.setString(2, fichaId);
                    ps.setString(3, t.getTipoTratamientoEnum().name());
                    ps.setString(4, t.getEstado().getClass().getSimpleName());
                    ps.setTimestamp(5, t.getFechaInicio() == null ? null
                            : new java.sql.Timestamp(t.getFechaInicio().getTime()));
                    ps.setTimestamp(6, t.getFechaFin() == null ? null
                            : new java.sql.Timestamp(t.getFechaFin().getTime()));
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        });
    }

    private void persistirComentarios(Session session, FichaMedica ficha, String fichaId) {
        session.doWork(conn -> {
            String sql =
                    "INSERT INTO comentario_medico (id, ficha_id, veterinario_email, texto, fecha) " +
                            "VALUES (?, ?, ?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE " +
                            "texto = VALUES(texto), " +
                            "fecha = VALUES(fecha)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (ComentarioMedico c : ficha.getHistorial().getListaComentario()) {
                    ps.setString(1, c.getComentarioID().toString());
                    ps.setString(2, fichaId);
                    ps.setString(3, c.getVeterinario() != null ? c.getVeterinario().getEmail() : null);
                    ps.setString(4, c.getCasillaComentario());
                    ps.setTimestamp(5, java.sql.Timestamp.valueOf(c.getFecha()));
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        });
    }

    // ── Lectura del historial ─────────────────────────────────────────────────

    private void cargarHistorial(Session session, FichaMedica ficha) {
        ficha.getHistorial().getListaTratamiento().clear();
        ficha.getHistorial().getListaComentario().clear();
        ficha.getHistorial().getListaVisitas().clear();
        String fichaId = ficha.getFichaMedicaId().toString();
        cargarTratamientos(session, ficha, fichaId);
        cargarComentarios(session, ficha, fichaId);
        cargarVisitas(session, ficha, ficha.getAnimal().getId().toString());
    }

    private void cargarTratamientos(Session session, FichaMedica ficha, String fichaId) {
        session.doWork(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT id, tipo, estado FROM tratamiento WHERE ficha_id = ?")) {
                ps.setString(1, fichaId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        TipoTratamiento tipo = TipoTratamiento.valueOf(rs.getString("tipo"));
                        Tratamiento t = new Tratamiento(tipo);
                        switch (rs.getString("estado")) {
                            case "EnCurso"    -> t.aplicarTratamiento();
                            case "Finalizado" -> t.finalizarTratamiento();
                            case "Cancelado"  -> t.cancelarTratamiento();
                        }
                        ficha.agregarTratamiento(t);
                    }
                }
            }
        });
    }

    private void cargarComentarios(Session session, FichaMedica ficha, String fichaId) {
        session.doWork(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT id, veterinario_email, texto, fecha FROM comentario_medico WHERE ficha_id = ?")) {
                ps.setString(1, fichaId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String email = rs.getString("veterinario_email");
                        Veterinario vet = email == null ? null
                                : usuarioRepository.listarTodos().stream()
                                .filter(u -> u instanceof Veterinario && u.getEmail().equals(email))
                                .map(u -> (Veterinario) u).findFirst().orElse(null);
                        ComentarioMedico cm = new ComentarioMedico(
                                vet,
                                rs.getString("texto"),
                                rs.getTimestamp("fecha").toLocalDateTime());
                        ficha.agregarComentarioMedico(cm);
                    }
                }
            }
        });
    }

    private void cargarVisitas(Session session, FichaMedica ficha, String animalId) {
        session.doWork(conn -> {
            String sql =
                    "SELECT v.id, v.fecha_programada, v.fecha_real, v.comentarios, " +
                            "v.completada, v.continuar_visitas, " +
                            "v.estado_general_animal, v.limpieza_lugar, v.ambiente " +
                            "FROM visitas v " +
                            "JOIN seguimiento s ON v.seguimiento_id = s.id " +
                            "JOIN adopcion_animal aa ON s.adopcion_id = aa.adopcion_id " +
                            "WHERE aa.animal_id = ? AND v.completada = TRUE";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, animalId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        UUID      vId  = UUID.fromString(rs.getString("id"));
                        LocalDate prog = rs.getDate("fecha_programada").toLocalDate();
                        Date   sqlReal = rs.getDate("fecha_real");
                        LocalDate real = sqlReal != null ? sqlReal.toLocalDate() : null;
                        String coment  = rs.getString("comentarios");
                        boolean comp   = rs.getBoolean("completada");
                        boolean cont   = rs.getBoolean("continuar_visitas");

                        Visita v = new Visita(vId, null, prog, real, coment, comp, cont);

                        String estAnim = rs.getString("estado_general_animal");
                        String limp    = rs.getString("limpieza_lugar");
                        String amb     = rs.getString("ambiente");
                        if (estAnim != null && limp != null && amb != null) {
                            v.registrarResultado(
                                    new Encuesta(CalificacionEnum.valueOf(estAnim),
                                            CalificacionEnum.valueOf(limp),
                                            CalificacionEnum.valueOf(amb)),
                                    coment, cont);
                        }
                        ficha.registrarVisitaDomicilio(v);
                    }
                }
            } catch (Exception ignored) {
                // tablas de seguimiento aún no presentes
            }
        });
    }
}