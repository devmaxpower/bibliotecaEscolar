package dao.impl;

import conexion.ConexionDB;
import dao.PrestamoDAO;
import modelo.Prestamo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAOImpl implements PrestamoDAO {

    @Override
    public List<Prestamo> listar() {

        List<Prestamo> prestamos = new ArrayList<>();

        String sql = "SELECT * FROM prestamos ORDER BY id DESC";

        try (
                Connection conn = ConexionDB.getInstance().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                prestamos.add(new Prestamo(
                        rs.getInt("id"),
                        rs.getInt("id_estudiante"),
                        rs.getInt("id_libro"),
                        rs.getDate("fecha_prestamo").toLocalDate(),
                        rs.getDate("fecha_devolucion").toLocalDate(),
                        rs.getBoolean("devuelto")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prestamos;
    }

    @Override
    public boolean registrarPrestamo(Prestamo prestamo) {

        String sqlStock = """
                UPDATE libros
                SET stock = stock - 1
                WHERE id = ? AND stock > 0
                """;

        String sqlPrestamo = """
                INSERT INTO prestamos
                (id_estudiante, id_libro, fecha_prestamo,
                 fecha_devolucion, devuelto)
                VALUES (?, ?, ?, ?, FALSE)
                """;

        try (Connection conn =
                     ConexionDB.getInstance().getConexion()) {

            conn.setAutoCommit(false);

            try (
                    PreparedStatement psStock =
                            conn.prepareStatement(sqlStock);

                    PreparedStatement psPrestamo =
                            conn.prepareStatement(sqlPrestamo)
            ) {

                // DESCONTAR STOCK
                psStock.setInt(1, prestamo.getIdLibro());

                int filas = psStock.executeUpdate();

                if (filas == 0) {
                    conn.rollback();
                    return false;
                }

                // REGISTRAR PRÉSTAMO
                psPrestamo.setInt(1, prestamo.getIdEstudiante());
                psPrestamo.setInt(2, prestamo.getIdLibro());

                psPrestamo.setDate(
                        3,
                        Date.valueOf(prestamo.getFechaPrestamo())
                );

                psPrestamo.setDate(
                        4,
                        Date.valueOf(prestamo.getFechaDevolucion())
                );

                psPrestamo.executeUpdate();

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean devolverLibro(int idPrestamo) {

        String sqlPrestamo = """
                SELECT id_libro
                FROM prestamos
                WHERE id = ? AND devuelto = FALSE
                FOR UPDATE
                """;

        String sqlDevolucion = """
                UPDATE prestamos
                SET devuelto = TRUE
                WHERE id = ? AND devuelto = FALSE
                """;

        String sqlStock = """
                UPDATE libros
                SET stock = stock + 1
                WHERE id = ?
                """;

        try (Connection conn =
                     ConexionDB.getInstance().getConexion()) {

            conn.setAutoCommit(false);

            try (
                    PreparedStatement psBuscar =
                            conn.prepareStatement(sqlPrestamo);

                    PreparedStatement psDevolver =
                            conn.prepareStatement(sqlDevolucion);

                    PreparedStatement psStock =
                            conn.prepareStatement(sqlStock)
            ) {

                psBuscar.setInt(1, idPrestamo);

                int idLibro;

                try (ResultSet rs = psBuscar.executeQuery()) {

                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }

                    idLibro = rs.getInt("id_libro");
                }

                // MARCAR COMO DEVUELTO
                psDevolver.setInt(1, idPrestamo);

                if (psDevolver.executeUpdate() != 1) {
                    conn.rollback();
                    return false;
                }

                // RECUPERAR STOCK
                psStock.setInt(1, idLibro);

                if (psStock.executeUpdate() != 1) {
                    conn.rollback();
                    return false;
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}