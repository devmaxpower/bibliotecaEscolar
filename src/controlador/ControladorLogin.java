package controlador;

import dao.UsuarioDAO;
import dao.impl.UsuarioDAOImpl;
import modelo.Usuario;

public class ControladorLogin {

    private final UsuarioDAO usuarioDAO;

    public ControladorLogin() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    public Usuario iniciarSesion(String rut, String contrasena) {

        if (rut == null || rut.isBlank()
                || contrasena == null || contrasena.isBlank()) {
            return null;
        }

        return usuarioDAO.autenticar(
                rut.trim(),
                contrasena
        );
    }
}