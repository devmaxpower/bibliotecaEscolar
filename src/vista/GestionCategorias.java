package vista;

import controlador.ControladorCategorias;
import modelo.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GestionCategorias extends JFrame {

    private final ControladorCategorias controlador =
            new ControladorCategorias();

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JTextField txtId;
    private JTextField txtNombre;

    public GestionCategorias() {

        setTitle("Biblioteca Escolar - Gestión de Categorías");
        setSize(650, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        cargarCategorias();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout(10, 10));

        // TABLA
        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Nombre"}, 0
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
        JPanel formulario = new JPanel(new GridLayout(2, 2, 10, 10));

        formulario.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        txtId = new JTextField();
        txtId.setEditable(false);

        txtNombre = new JTextField();

        formulario.add(new JLabel("ID:"));
        formulario.add(txtId);

        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);

        // BOTONES
        JPanel botones = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 10)
        );

        JButton btnAgregar = new JButton("Agregar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnRecargar = new JButton("Recargar");

        botones.add(btnAgregar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);
        botones.add(btnRecargar);

        JPanel panelInferior = new JPanel(new BorderLayout());

        panelInferior.add(formulario, BorderLayout.CENTER);
        panelInferior.add(botones, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);

        // EVENTOS
        tabla.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && tabla.getSelectedRow() >= 0) {
                seleccionarCategoria();
            }
        });

        btnAgregar.addActionListener(e -> agregarCategoria());
        btnActualizar.addActionListener(e -> actualizarCategoria());
        btnEliminar.addActionListener(e -> eliminarCategoria());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnRecargar.addActionListener(e -> cargarCategorias());
    }

    // LISTAR
    private void cargarCategorias() {

        modeloTabla.setRowCount(0);

        for (Categoria categoria : controlador.listarCategorias()) {

            modeloTabla.addRow(new Object[]{
                    categoria.getId(),
                    categoria.getNombre()
            });
        }
    }

    // SELECCIONAR
    private void seleccionarCategoria() {

        int fila = tabla.getSelectedRow();

        if (fila < 0) {
            return;
        }

        int filaModelo = tabla.convertRowIndexToModel(fila);

        txtId.setText(
                modeloTabla.getValueAt(filaModelo, 0).toString()
        );

        txtNombre.setText(
                modeloTabla.getValueAt(filaModelo, 1).toString()
        );
    }

    // OBTENER DATOS
    private Categoria obtenerCategoriaFormulario() {

        int id = txtId.getText().isBlank()
                ? 0
                : Integer.parseInt(txtId.getText());

        return new Categoria(
                id,
                txtNombre.getText().trim()
        );
    }

    // VALIDAR
    private boolean validarFormulario() {

        if (txtNombre.getText().isBlank()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar el nombre de la categoría.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return false;
        }

        return true;
    }

    // AGREGAR
    private void agregarCategoria() {

        if (!validarFormulario()) {
            return;
        }

        Categoria categoria = obtenerCategoriaFormulario();

        if (controlador.agregarCategoria(categoria)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Categoría agregada correctamente."
            );

            limpiarFormulario();
            cargarCategorias();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo agregar la categoría.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ACTUALIZAR
    private void actualizarCategoria() {

        if (txtId.getText().isBlank()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una categoría."
            );

            return;
        }

        if (!validarFormulario()) {
            return;
        }

        Categoria categoria = obtenerCategoriaFormulario();

        if (controlador.actualizarCategoria(categoria)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Categoría actualizada correctamente."
            );

            limpiarFormulario();
            cargarCategorias();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar la categoría.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ELIMINAR
    private void eliminarCategoria() {

        if (txtId.getText().isBlank()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una categoría."
            );

            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar esta categoría?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        int id = Integer.parseInt(txtId.getText());

        if (controlador.eliminarCategoria(id)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Categoría eliminada correctamente."
            );

            limpiarFormulario();
            cargarCategorias();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar.\n" +
                            "Es posible que tenga libros asociados.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // LIMPIAR
    private void limpiarFormulario() {

        tabla.clearSelection();

        txtId.setText("");
        txtNombre.setText("");

        txtNombre.requestFocus();
    }

    // EJECUTAR
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new GestionCategorias().setVisible(true);
        });
    }
}