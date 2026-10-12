package dao.impl;

import conexion.ConexionDB;
import dao.UsuarioDAO;
import modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public Usuario autenticar(String rut, String contrasena) {

        String sql = """
                SELECT id, nombre, rut, correo, rol
                FROM usuarios
                WHERE rut = ? AND `contraseña` = ?
                """;

        try (
                Connection conn = ConexionDB.getInstance().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, rut);
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("rut"),
                            rs.getString("correo"),
                            null,
                            rs.getString("rol")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}