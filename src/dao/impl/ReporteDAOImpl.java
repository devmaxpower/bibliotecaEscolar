package dao.impl;

import conexion.ConexionDB;
import dao.ReporteDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAOImpl implements ReporteDAO {

    // REPORTE 1: LIBROS MÁS PRESTADOS
    @Override
    public List<Object[]> librosMasPrestados() {

        List<Object[]> resultados = new ArrayList<>();

        String sql = """
                SELECT
                    l.id,
                    l.titulo,
                    l.autor,
                    COUNT(p.id) AS total_prestamos
                FROM libros l
                LEFT JOIN prestamos p ON l.id = p.id_libro
                GROUP BY l.id, l.titulo, l.autor
                ORDER BY total_prestamos DESC, l.titulo
                """;

        try (
                Connection conn = ConexionDB.getInstance().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                resultados.add(new Object[]{
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("total_prestamos")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultados;
    }

    // REPORTE 2: HISTORIAL POR ESTUDIANTE
    @Override
    public List<Object[]> historialEstudiante(int idEstudiante) {

        List<Object[]> resultados = new ArrayList<>();

        String sql = """
                SELECT
                    p.id,
                    l.titulo,
                    p.fecha_prestamo,
                    p.fecha_devolucion,
                    CASE
                        WHEN p.devuelto = TRUE THEN 'Devuelto'
                        WHEN p.fecha_devolucion < CURDATE() THEN 'Atrasado'
                        ELSE 'Activo'
                    END AS estado
                FROM prestamos p
                INNER JOIN libros l ON p.id_libro = l.id
                WHERE p.id_estudiante = ?
                ORDER BY p.fecha_prestamo DESC
                """;

        try (
                Connection conn = ConexionDB.getInstance().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idEstudiante);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    resultados.add(new Object[]{
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getDate("fecha_prestamo"),
                            rs.getDate("fecha_devolucion"),
                            rs.getString("estado")
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultados;
    }

    // REPORTE 3: PRÉSTAMOS ACTIVOS
    @Override
    public List<Object[]> prestamosActivos() {

        List<Object[]> resultados = new ArrayList<>();

        String sql = """
                SELECT
                    p.id,
                    e.nombre AS estudiante,
                    l.titulo AS libro,
                    p.fecha_prestamo,
                    p.fecha_devolucion,
                    CASE
                        WHEN p.fecha_devolucion < CURDATE()
                        THEN 'ATRASADO'
                        ELSE 'Activo'
                    END AS estado
                FROM prestamos p
                INNER JOIN estudiantes e ON p.id_estudiante = e.id
                INNER JOIN libros l ON p.id_libro = l.id
                WHERE p.devuelto = FALSE
                ORDER BY p.fecha_devolucion ASC
                """;

        try (
                Connection conn = ConexionDB.getInstance().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                resultados.add(new Object[]{
                        rs.getInt("id"),
                        rs.getString("estudiante"),
                        rs.getString("libro"),
                        rs.getDate("fecha_prestamo"),
                        rs.getDate("fecha_devolucion"),
                        rs.getString("estado")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultados;
    }
}