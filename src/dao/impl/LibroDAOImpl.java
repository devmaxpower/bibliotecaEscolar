package dao.impl;

import conexion.ConexionDB;
import dao.LibroDAO;
import modelo.Libro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAOImpl implements LibroDAO {

    @Override
    public List<Libro> listar() {

        List<Libro> libros = new ArrayList<>();

        String sql = "SELECT * FROM libros";

        try (
                Connection conn = ConexionDB.getInstance().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Libro libro = new Libro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("isbn"),
                        rs.getString("editorial"),
                        rs.getInt("stock"),
                        rs.getInt("id_categoria")
                );

                libros.add(libro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return libros;
    }

    @Override
    public Libro buscarPorId(int id) {

        String sql = "SELECT * FROM libros WHERE id = ?";

        try (
                Connection conn = ConexionDB.getInstance().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Libro(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getString("isbn"),
                            rs.getString("editorial"),
                            rs.getInt("stock"),
                            rs.getInt("id_categoria")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean agregar(Libro libro) {

        String sql = """
            INSERT INTO libros
            (titulo, autor, isbn, editorial, stock, id_categoria)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (
                Connection conn = ConexionDB.getInstance().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getIsbn());
            ps.setString(4, libro.getEditorial());
            ps.setInt(5, libro.getStock());
            ps.setInt(6, libro.getIdCategoria());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean actualizar(Libro libro) {

        String sql = """
            UPDATE libros
            SET titulo = ?,
                autor = ?,
                isbn = ?,
                editorial = ?,
                stock = ?,
                id_categoria = ?
            WHERE id = ?
            """;

        try (
                Connection conn = ConexionDB.getInstance().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getIsbn());
            ps.setString(4, libro.getEditorial());
            ps.setInt(5, libro.getStock());
            ps.setInt(6, libro.getIdCategoria());
            ps.setInt(7, libro.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean eliminar(int id) {

        String sql = "DELETE FROM libros WHERE id = ?";

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