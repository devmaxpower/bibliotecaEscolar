package dao.impl;

import conexion.ConexionDB;
import dao.EstudianteDAO;
import modelo.Estudiante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAOImpl implements EstudianteDAO {

    @Override
    public List<Estudiante> listar() {

        List<Estudiante> estudiantes = new ArrayList<>();

        String sql = "SELECT * FROM estudiantes ORDER BY id";

        try (
                Connection conn = ConexionDB.getInstance().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                estudiantes.add(new Estudiante(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("rut"),
                        rs.getString("correo"),
                        rs.getString("curso")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return estudiantes;
    }

    @Override
    public Estudiante buscarPorId(int id) {

        String sql = "SELECT * FROM estudiantes WHERE id = ?";

        try (
                Connection conn = ConexionDB.getInstance().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Estudiante(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("rut"),
                            rs.getString("correo"),
                            rs.getString("curso")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean agregar(Estudiante estudiante) {

        String sql = """
                INSERT INTO estudiantes
                (nombre, rut, curso, correo)
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection conn = ConexionDB.getInstance().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, estudiante.getNombre());
            ps.setString(2, estudiante.getRut());
            ps.setString(3, estudiante.getCurso());
            ps.setString(4, estudiante.getCorreo());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean actualizar(Estudiante estudiante) {

        String sql = """
                UPDATE estudiantes
                SET nombre = ?,
                    rut = ?,
                    curso = ?,
                    correo = ?
                WHERE id = ?
                """;

        try (
                Connection conn = ConexionDB.getInstance().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, estudiante.getNombre());
            ps.setString(2, estudiante.getRut());
            ps.setString(3, estudiante.getCurso());
            ps.setString(4, estudiante.getCorreo());
            ps.setInt(5, estudiante.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean eliminar(int id) {

        String sql = "DELETE FROM estudiantes WHERE id = ?";

        try (
                Connection conn = ConexionDB.getInstance().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}