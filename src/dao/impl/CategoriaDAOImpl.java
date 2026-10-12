package dao.impl;

import conexion.ConexionDB;
import dao.CategoriaDAO;
import modelo.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAOImpl implements CategoriaDAO {

    @Override
    public List<Categoria> listar() {

        List<Categoria> categorias = new ArrayList<>();

        String sql = "SELECT * FROM categorias ORDER BY id";

        try (
                Connection conn = ConexionDB.getInstance().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                categorias.add(new Categoria(
                        rs.getInt("id"),
                        rs.getString("nombre")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categorias;
    }

    @Override
    public Categoria buscarPorId(int id) {

        String sql = "SELECT * FROM categorias WHERE id = ?";

        try (
                Connection conn = ConexionDB.getInstance().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return new Categoria(
                            rs.getInt("id"),
                            rs.getString("nombre")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean agregar(Categoria categoria) {

        String sql = "INSERT INTO categorias (nombre) VALUES (?)";

        try (
                Connection conn = ConexionDB.getInstance().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, categoria.getNombre());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean actualizar(Categoria categoria) {

        String sql = "UPDATE categorias SET nombre = ? WHERE id = ?";

        try (
                Connection conn = ConexionDB.getInstance().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, categoria.getNombre());
            ps.setInt(2, categoria.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean eliminar(int id) {

        String sql = "DELETE FROM categorias WHERE id = ?";

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