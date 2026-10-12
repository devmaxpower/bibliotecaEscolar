package vista;

import controlador.ControladorLogin;
import modelo.Usuario;

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

    private final ControladorLogin controlador = new ControladorLogin();

    private JTextField txtRut;
    private JPasswordField txtContrasena;

    public Login() {

        setTitle("Biblioteca Escolar - Inicio de Sesión");
        setSize(420, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel(
                "SISTEMA DE BIBLIOTECA ESCOLAR",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("Arial", Font.BOLD, 17));
        titulo.setBorder(
                BorderFactory.createEmptyBorder(25, 10, 10, 10)
        );

        add(titulo, BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridLayout(2, 2, 10, 15));

        formulario.setBorder(
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        );

        txtRut = new JTextField();
        txtContrasena = new JPasswordField();

        formulario.add(new JLabel("RUT:"));
        formulario.add(txtRut);

        formulario.add(new JLabel("Contraseña:"));
        formulario.add(txtContrasena);

        add(formulario, BorderLayout.CENTER);

        JButton btnIngresar = new JButton("Iniciar sesión");

        JPanel panelBoton = new JPanel();
        panelBoton.setBorder(
                BorderFactory.createEmptyBorder(0, 20, 25, 20)
        );
        panelBoton.add(btnIngresar);

        add(panelBoton, BorderLayout.SOUTH);

        btnIngresar.addActionListener(e -> iniciarSesion());

        txtContrasena.addActionListener(e -> iniciarSesion());
    }

    private void iniciarSesion() {

        String rut = txtRut.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        if (rut.isBlank() || contrasena.isBlank()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese su RUT y contraseña.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Usuario usuario = controlador.iniciarSesion(rut, contrasena);

        if (usuario != null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Bienvenido, " + usuario.getNombre()
            );

            new MenuPrincipal(usuario).setVisible(true);
            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "RUT o contraseña incorrectos.",
                    "Error de autenticación",
                    JOptionPane.ERROR_MESSAGE
            );

            txtContrasena.setText("");
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}