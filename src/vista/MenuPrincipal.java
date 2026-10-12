package vista;

import modelo.Usuario;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    private final Usuario usuario;

    public MenuPrincipal(Usuario usuario) {

        this.usuario = usuario;

        setTitle("Biblioteca Escolar - Menú Principal");
        setSize(550, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout(15, 15));

        JLabel lblBienvenida = new JLabel(
                "Bienvenido, " + usuario.getNombre(),
                SwingConstants.CENTER
        );

        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel lblRol = new JLabel(
                "Rol: " + usuario.getRol(),
                SwingConstants.CENTER
        );

        JPanel encabezado = new JPanel(new GridLayout(2, 1, 5, 5));

        encabezado.setBorder(
                BorderFactory.createEmptyBorder(25, 10, 10, 10)
        );

        encabezado.add(lblBienvenida);
        encabezado.add(lblRol);

        add(encabezado, BorderLayout.NORTH);

        JPanel opciones = new JPanel(new GridLayout(0, 1, 10, 10));

        opciones.setBorder(
                BorderFactory.createEmptyBorder(20, 80, 20, 80)
        );

        boolean esBibliotecario =
                "bibliotecario".equalsIgnoreCase(usuario.getRol());

        if (esBibliotecario) {

            JButton btnLibros = new JButton("Gestión de Libros");
            JButton btnEstudiantes = new JButton("Gestión de Estudiantes");
            JButton btnCategorias = new JButton("Gestión de Categorías");
            JButton btnPrestamos = new JButton("Gestión de Préstamos");
            JButton btnReportes = new JButton("Reportes");




            opciones.add(btnPrestamos);

            btnLibros.addActionListener(e ->
                    new GestionLibros().setVisible(true)
            );

            btnEstudiantes.addActionListener(e ->
                    new GestionEstudiantes().setVisible(true)
            );

            btnCategorias.addActionListener(e ->
                    new GestionCategorias().setVisible(true)
            );

            btnPrestamos.addActionListener(e ->
                    new GestionPrestamos().setVisible(true)
            );
            btnReportes.addActionListener(e ->
                    new Reportes().setVisible(true)
            );



            opciones.add(btnLibros);
            opciones.add(btnEstudiantes);
            opciones.add(btnCategorias);
            opciones.add(btnReportes);

        } else {

            JButton btnConsultarLibros =
                    new JButton("Consultar Libros");

            btnConsultarLibros.addActionListener(e ->
                    mostrarCatalogo()
            );

            opciones.add(btnConsultarLibros);
        }

        JButton btnCerrarSesion = new JButton("Cerrar sesión");

        btnCerrarSesion.addActionListener(e -> {

            new Login().setVisible(true);
            dispose();
        });

        opciones.add(btnCerrarSesion);

        add(opciones, BorderLayout.CENTER);
    }

    private void mostrarCatalogo() {

        dao.LibroDAO libroDAO = new dao.impl.LibroDAOImpl();

        String[] columnas = {
                "Título", "Autor", "ISBN", "Stock"
        };

        javax.swing.table.DefaultTableModel modelo =
                new javax.swing.table.DefaultTableModel(columnas, 0) {

                    @Override
                    public boolean isCellEditable(int fila, int columna) {
                        return false;
                    }
                };

        for (modelo.Libro libro : libroDAO.listar()) {

            modelo.addRow(new Object[]{
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getIsbn(),
                    libro.getStock()
            });
        }

        JTable tabla = new JTable(modelo);

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(tabla),
                "Catálogo de Libros",
                JOptionPane.PLAIN_MESSAGE
        );
    }
}