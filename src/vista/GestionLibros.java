package vista;

import controlador.ControladorLibros;
import modelo.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GestionLibros extends JFrame {

    private final ControladorLibros controlador = new ControladorLibros();

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JTextField txtId;
    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtIsbn;
    private JTextField txtEditorial;
    private JTextField txtStock;
    private JTextField txtCategoria;

    public GestionLibros() {

        setTitle("Biblioteca Escolar - Gestión de Libros");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        cargarLibros();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout(10, 10));

        // TABLA DE LIBROS
        modeloTabla = new DefaultTableModel(
                new String[]{
                        "ID", "Título", "Autor", "ISBN",
                        "Editorial", "Stock", "Categoría"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(25);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // FORMULARIO
        JPanel formulario = new JPanel(new GridLayout(7, 2, 10, 10));
        formulario.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        txtId = new JTextField();
        txtId.setEditable(false);

        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtIsbn = new JTextField();
        txtEditorial = new JTextField();
        txtStock = new JTextField();
        txtCategoria = new JTextField();

        formulario.add(new JLabel("ID:"));
        formulario.add(txtId);

        formulario.add(new JLabel("Título:"));
        formulario.add(txtTitulo);

        formulario.add(new JLabel("Autor:"));
        formulario.add(txtAutor);

        formulario.add(new JLabel("ISBN:"));
        formulario.add(txtIsbn);

        formulario.add(new JLabel("Editorial:"));
        formulario.add(txtEditorial);

        formulario.add(new JLabel("Stock:"));
        formulario.add(txtStock);

        formulario.add(new JLabel("ID Categoría:"));
        formulario.add(txtCategoria);

        // BOTONES
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton btnAgregar = new JButton("Agregar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnActualizarTabla = new JButton("Recargar");

        botones.add(btnAgregar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);
        botones.add(btnActualizarTabla);

        // PANEL INFERIOR
        JPanel panelInferior = new JPanel(new BorderLayout());

        panelInferior.add(formulario, BorderLayout.CENTER);
        panelInferior.add(botones, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);

        // EVENTOS
        tabla.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                seleccionarLibro();
            }
        });

        btnAgregar.addActionListener(e -> agregarLibro());
        btnActualizar.addActionListener(e -> actualizarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnActualizarTabla.addActionListener(e -> cargarLibros());
    }

    // LISTAR LIBROS
    private void cargarLibros() {

        modeloTabla.setRowCount(0);

        for (Libro libro : controlador.listarLibros()) {

            modeloTabla.addRow(new Object[]{
                    libro.getId(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getIsbn(),
                    libro.getEditorial(),
                    libro.getStock(),
                    libro.getIdCategoria()
            });
        }
    }

    // SELECCIONAR LIBRO
    private void seleccionarLibro() {

        int fila = tabla.getSelectedRow();

        if (fila < 0) {
            return;
        }

        int filaModelo = tabla.convertRowIndexToModel(fila);

        txtId.setText(modeloTabla.getValueAt(filaModelo, 0).toString());
        txtTitulo.setText(modeloTabla.getValueAt(filaModelo, 1).toString());
        txtAutor.setText(modeloTabla.getValueAt(filaModelo, 2).toString());
        txtIsbn.setText(modeloTabla.getValueAt(filaModelo, 3).toString());
        txtEditorial.setText(modeloTabla.getValueAt(filaModelo, 4).toString());
        txtStock.setText(modeloTabla.getValueAt(filaModelo, 5).toString());
        txtCategoria.setText(modeloTabla.getValueAt(filaModelo, 6).toString());
    }

    // OBTENER DATOS DEL FORMULARIO
    private Libro obtenerLibroFormulario() {

        int id = txtId.getText().isBlank()
                ? 0
                : Integer.parseInt(txtId.getText());

        return new Libro(
                id,
                txtTitulo.getText().trim(),
                txtAutor.getText().trim(),
                txtIsbn.getText().trim(),
                txtEditorial.getText().trim(),
                Integer.parseInt(txtStock.getText().trim()),
                Integer.parseInt(txtCategoria.getText().trim())
        );
    }

    // VALIDAR FORMULARIO
    private boolean validarFormulario() {

        if (txtTitulo.getText().isBlank()
                || txtAutor.getText().isBlank()
                || txtIsbn.getText().isBlank()
                || txtEditorial.getText().isBlank()
                || txtStock.getText().isBlank()
                || txtCategoria.getText().isBlank()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe completar todos los campos.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return false;
        }

        try {

            int stock = Integer.parseInt(txtStock.getText().trim());
            int categoria = Integer.parseInt(txtCategoria.getText().trim());

            if (stock < 0 || categoria <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "El stock no puede ser negativo y la categoría debe ser válida.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );

                return false;
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Stock y categoría deben ser números enteros.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return false;
        }

        return true;
    }

    // AGREGAR LIBRO
    private void agregarLibro() {

        if (!validarFormulario()) {
            return;
        }

        Libro libro = obtenerLibroFormulario();

        if (controlador.agregarLibro(libro)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Libro agregado correctamente."
            );

            limpiarFormulario();
            cargarLibros();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo agregar el libro. Revise el ISBN o la categoría.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ACTUALIZAR LIBRO
    private void actualizarLibro() {

        if (txtId.getText().isBlank()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un libro de la tabla."
            );

            return;
        }

        if (!validarFormulario()) {
            return;
        }

        Libro libro = obtenerLibroFormulario();

        if (controlador.actualizarLibro(libro)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Libro actualizado correctamente."
            );

            limpiarFormulario();
            cargarLibros();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar el libro.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ELIMINAR LIBRO
    private void eliminarLibro() {

        if (txtId.getText().isBlank()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un libro para eliminar."
            );

            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar este libro?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        int id = Integer.parseInt(txtId.getText());

        if (controlador.eliminarLibro(id)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Libro eliminado correctamente."
            );

            limpiarFormulario();
            cargarLibros();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar el libro.\n" +
                            "Es posible que tenga préstamos asociados.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // LIMPIAR FORMULARIO
    private void limpiarFormulario() {

        tabla.clearSelection();

        txtId.setText("");
        txtTitulo.setText("");
        txtAutor.setText("");
        txtIsbn.setText("");
        txtEditorial.setText("");
        txtStock.setText("");
        txtCategoria.setText("");

        txtTitulo.requestFocus();
    }

    // EJECUTAR VENTANA
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new GestionLibros().setVisible(true);
        });
    }
}