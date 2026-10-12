package vista;

import controlador.ControladorEstudiantes;
import modelo.Estudiante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GestionEstudiantes extends JFrame {

    private final ControladorEstudiantes controlador =
            new ControladorEstudiantes();

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtRut;
    private JTextField txtCurso;
    private JTextField txtCorreo;

    public GestionEstudiantes() {

        setTitle("Biblioteca Escolar - Gestión de Estudiantes");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        cargarEstudiantes();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout(10, 10));

        // TABLA
        modeloTabla = new DefaultTableModel(
                new String[]{
                        "ID", "Nombre", "RUT", "Curso", "Correo"
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
        JPanel formulario = new JPanel(new GridLayout(5, 2, 10, 10));

        formulario.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        txtId = new JTextField();
        txtId.setEditable(false);

        txtNombre = new JTextField();
        txtRut = new JTextField();
        txtCurso = new JTextField();
        txtCorreo = new JTextField();

        formulario.add(new JLabel("ID:"));
        formulario.add(txtId);

        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);

        formulario.add(new JLabel("RUT:"));
        formulario.add(txtRut);

        formulario.add(new JLabel("Curso:"));
        formulario.add(txtCurso);

        formulario.add(new JLabel("Correo:"));
        formulario.add(txtCorreo);

        // BOTONES
        JPanel botones = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 15, 10)
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

                seleccionarEstudiante();
            }
        });

        btnAgregar.addActionListener(e -> agregarEstudiante());
        btnActualizar.addActionListener(e -> actualizarEstudiante());
        btnEliminar.addActionListener(e -> eliminarEstudiante());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnRecargar.addActionListener(e -> cargarEstudiantes());
    }

    // LISTAR
    private void cargarEstudiantes() {

        modeloTabla.setRowCount(0);

        for (Estudiante estudiante : controlador.listarEstudiantes()) {

            modeloTabla.addRow(new Object[]{
                    estudiante.getId(),
                    estudiante.getNombre(),
                    estudiante.getRut(),
                    estudiante.getCurso(),
                    estudiante.getCorreo()
            });
        }
    }

    // SELECCIONAR
    private void seleccionarEstudiante() {

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

        txtRut.setText(
                modeloTabla.getValueAt(filaModelo, 2).toString()
        );

        txtCurso.setText(
                modeloTabla.getValueAt(filaModelo, 3).toString()
        );

        txtCorreo.setText(
                modeloTabla.getValueAt(filaModelo, 4).toString()
        );
    }

    // OBTENER DATOS
    private Estudiante obtenerEstudianteFormulario() {

        int id = txtId.getText().isBlank()
                ? 0
                : Integer.parseInt(txtId.getText());

        return new Estudiante(
                id,
                txtNombre.getText().trim(),
                txtRut.getText().trim(),
                txtCorreo.getText().trim(),
                txtCurso.getText().trim()
        );
    }

    // VALIDAR
    private boolean validarFormulario() {

        if (txtNombre.getText().isBlank()
                || txtRut.getText().isBlank()
                || txtCurso.getText().isBlank()
                || txtCorreo.getText().isBlank()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe completar todos los campos.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return false;
        }

        return true;
    }

    // AGREGAR
    private void agregarEstudiante() {

        if (!validarFormulario()) {
            return;
        }

        Estudiante estudiante = obtenerEstudianteFormulario();

        if (controlador.agregarEstudiante(estudiante)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Estudiante agregado correctamente."
            );

            limpiarFormulario();
            cargarEstudiantes();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo agregar el estudiante. Revise el RUT.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ACTUALIZAR
    private void actualizarEstudiante() {

        if (txtId.getText().isBlank()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un estudiante."
            );

            return;
        }

        if (!validarFormulario()) {
            return;
        }

        Estudiante estudiante = obtenerEstudianteFormulario();

        if (controlador.actualizarEstudiante(estudiante)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Estudiante actualizado correctamente."
            );

            limpiarFormulario();
            cargarEstudiantes();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar el estudiante.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ELIMINAR
    private void eliminarEstudiante() {

        if (txtId.getText().isBlank()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un estudiante."
            );

            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar este estudiante?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        int id = Integer.parseInt(txtId.getText());

        if (controlador.eliminarEstudiante(id)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Estudiante eliminado correctamente."
            );

            limpiarFormulario();
            cargarEstudiantes();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar.\n" +
                            "Es posible que tenga préstamos asociados.",
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
        txtRut.setText("");
        txtCurso.setText("");
        txtCorreo.setText("");

        txtNombre.requestFocus();
    }

    // EJECUTAR
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new GestionEstudiantes().setVisible(true);
        });
    }
}