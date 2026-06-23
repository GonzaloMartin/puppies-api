package com.gudboy.repository;

import com.gudboy.domain.Usuario.*;
import com.gudboy.infrastructure.ConexionMySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositoryMySQL implements IUsuarioRepository {

    private Connection conn() {
        return ConexionMySQL.getInstancia().getConnection();
    }

    @Override
    public void guardar(Usuario usuario) {
        String sql = "INSERT INTO usuario (email, nombre, apellido, telefono, tipo, " +
                "matricula_profesional, especialidad, estado_civil, ocupacion, otras_mascotas, " +
                "motivo_adopcion, animales_interes) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn().prepareStatement(sql)) {
            stmt.setString(1, usuario.getEmail());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, usuario.getApellido());
            stmt.setString(4, usuario.getTelefono());

            if (usuario instanceof Veterinario v) {
                stmt.setString(5, "VETERINARIO");
                stmt.setInt(6, v.getMatriculaProfesional());
                stmt.setString(7, v.getEspecialidad());
                stmt.setNull(8, Types.VARCHAR);
                stmt.setNull(9, Types.VARCHAR);
                stmt.setNull(10, Types.BOOLEAN);
                stmt.setNull(11, Types.VARCHAR);
                stmt.setNull(12, Types.VARCHAR);
            } else if (usuario instanceof Visitador vis) {
                stmt.setString(5, "VISITADOR");
                stmt.setNull(6, Types.INTEGER);
                stmt.setNull(7, Types.VARCHAR);
                stmt.setString(8, vis.getEstadoCivil().name());
                stmt.setString(9, vis.getOcupacion().name());
                stmt.setBoolean(10, vis.tieneOtrasMascotas());
                stmt.setString(11, vis.getMotivoAdopcion());
                stmt.setString(12, vis.getAnimalesInteres());
            } else {
                stmt.setString(5, "USUARIO");
                for (int i = 6; i <= 12; i++) stmt.setNull(i, Types.VARCHAR);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario";
        try (Statement stmt = conn().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");
        String email = rs.getString("email");
        String telefono = rs.getString("telefono");

        if ("VETERINARIO".equals(tipo)) {
            return new Veterinario(nombre, apellido, email, telefono,
                    rs.getInt("matricula_profesional"),
                    rs.getString("especialidad"));
        } else if ("VISITADOR".equals(tipo)) {
            EstadoCivil estadoCivil = EstadoCivil.valueOf(rs.getString("estado_civil"));
            Ocupacion ocupacion = Ocupacion.valueOf(rs.getString("ocupacion"));
            return new Visitador(nombre, apellido, email, telefono,
                    estadoCivil, ocupacion,
                    rs.getString("motivo_adopcion"),
                    rs.getString("animales_interes"),
                    rs.getBoolean("otras_mascotas"));
        }
        return new Usuario(nombre, apellido, email, telefono);
    }
}
