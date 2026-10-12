package vista;

import controlador.ControladorPrestamos;
import controlador.ControladorEstudiantes;
import controlador.ControladorLibros;

import modelo.Estudiante;
import modelo.Libro;
import modelo.Prestamo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class GestionPrestamos extends JFrame {

    private final ControladorPrestamos controlador =
            new ControladorPrestamos();

    private final ControladorEstudiantes controladorEstudiantes =
            new ControladorEstudiantes();

    private final ControladorLibros controladorLibros =
            new ControladorLibros();

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JComboBox<EstudianteItem> cmbEstudiantes;
    private JComboBox<LibroItem> cmbLibros;

    private JTextField txtFechaDevolucion;

    private JButton btnPrestar;
    private JButton btnDevolver;
    private JButton btnRecargar;

    public GestionPrestamos() {

        setTitle("Biblioteca Escolar - Gestión de Préstamos");
        setSize(1050, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        cargarCombos();
        cargarPrestamos();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout(10, 10));

        // TABLA DE PRÉSTAMOS
        modeloTabla = new DefaultTableModel(
                new String[]{
                        "ID",
                        "Estudiante",
                        "Libro",
                        "Fecha préstamo",
                        "Fecha vencimiento",
                        "Devuelto",
                        "Estado"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(25);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // FORMULARIO
        JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 10));

        formulario.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        cmbEstudiantes = new JComboBox<>();
        cmbLibros = new JComboBox<>();

        txtFechaDevolucion = new JTextField(
                LocalDate.now().plusDays(7).toString()
        );

        formulario.add(new JLabel("Estudiante:"));
        formulario.add(cmbEstudiantes);

        formulario.add(new JLabel("Libro:"));
        formulario.add(cmbLibros);

        formulario.add(new JLabel("Fecha vencimiento (AAAA-MM-DD):"));
        formulario.add(txtFechaDevolucion);

        // BOTONES
        JPanel botones = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 15, 10)
        );

        btnPrestar = new JButton("Registrar préstamo");
        btnDevolver = new JButton("Devolver libro");
        btnRecargar = new JButton("Recargar");

        botones.add(btnPrestar);
        botones.add(btnDevolver);
        botones.add(btnRecargar);

        JPanel panelInferior = new JPanel(new BorderLayout());

        panelInferior.add(formulario, BorderLayout.CENTER);
        panelInferior.add(botones, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);

        // EVENTOS
        btnPrestar.addActionListener(e -> registrarPrestamo());
        btnDevolver.addActionListener(e -> devolverLibro());

        btnRecargar.addActionListener(e -> {
            cargarCombos();
            cargarPrestamos();
        });
    }

    // CARGAR ESTUDIANTES Y LIBROS
    private void cargarCombos() {

        cmbEstudiantes.removeAllItems();
        cmbLibros.removeAllItems();

        for (Estudiante estudiante :
                controladorEstudiantes.listarEstudiantes()) {

            cmbEstudiantes.addItem(new EstudianteItem(estudiante));
        }

        for (Libro libro : controladorLibros.listarLibros()) {

            if (libro.getStock() > 0) {
                cmbLibros.addItem(new LibroItem(libro));
            }
        }
    }

    // LISTAR PRÉSTAMOS
    private void cargarPrestamos() {

        modeloTabla.setRowCount(0);

        for (Prestamo prestamo : controlador.listarPrestamos()) {

            Estudiante estudiante =
                    controladorEstudiantes.buscarEstudiante(
                            prestamo.getIdEstudiante()
                    );

            Libro libro =
                    controladorLibros.buscarLibro(
                            prestamo.getIdLibro()
                    );

            String nombreEstudiante = estudiante != null
                    ? estudiante.getNombre()
                    : "No disponible";

            String tituloLibro = libro != null
                    ? libro.getTitulo()
                    : "No disponible";

            String estado;

            if (prestamo.isDevuelto()) {
                estado = "Devuelto";
            } else if (prestamo.estaAtrasado()) {
                estado = "ATRASADO";
            } else {
                estado = "Activo";
            }

            modeloTabla.addRow(new Object[]{
                    prestamo.getId(),
                    nombreEstudiante,
                    tituloLibro,
                    prestamo.getFechaPrestamo(),
                    prestamo.getFechaDevolucion(),
                    prestamo.isDevuelto() ? "Sí" : "No",
                    estado
            });
        }
    }

    // REGISTRAR PRÉSTAMO
    private void registrarPrestamo() {

        EstudianteItem estudianteItem =
                (EstudianteItem) cmbEstudiantes.getSelectedItem();

        LibroItem libroItem =
                (LibroItem) cmbLibros.getSelectedItem();

        if (estudianteItem == null || libroItem == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un estudiante y un libro."
            );

            return;
        }

        LocalDate fecha;

        try {
            fecha = LocalDate.parse(
                    txtFechaDevolucion.getText().trim()
            );

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese una fecha válida (AAAA-MM-DD)."
            );

            return;
        }

        if (!fecha.isAfter(LocalDate.now())) {

            JOptionPane.showMessageDialog(
                    this,
                    "La fecha de vencimiento debe ser posterior a hoy."
            );

            return;
        }

        bloquearBotones(true);

        controlador.registrarPrestamo(
                estudianteItem.estudiante.getId(),
                libroItem.libro.getId(),
                fecha
        ).whenComplete((resultado, error) ->

                SwingUtilities.invokeLater(() -> {

                    bloquearBotones(false);

                    if (error != null || !Boolean.TRUE.equals(resultado)) {

                        JOptionPane.showMessageDialog(
                                this,
                                "No se pudo registrar el préstamo.\n" +
                                        "Compruebe el stock y los datos.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );

                    } else {

                        JOptionPane.showMessageDialog(
                                this,
                                "Préstamo registrado correctamente."
                        );
                    }

                    cargarCombos();
                    cargarPrestamos();
                })
        );
    }

    // DEVOLVER LIBRO
    private void devolverLibro() {

        int fila = tabla.getSelectedRow();

        if (fila < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un préstamo de la tabla."
            );

            return;
        }

        int filaModelo = tabla.convertRowIndexToModel(fila);

        int idPrestamo = (int) modeloTabla.getValueAt(filaModelo, 0);

        String estado = modeloTabla.getValueAt(
                filaModelo, 6
        ).toString();

        if ("Devuelto".equals(estado)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Este préstamo ya fue devuelto."
            );

            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Confirmar devolución del libro?",
                "Devolución",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        bloquearBotones(true);

        controlador.devolverLibro(idPrestamo)
                .whenComplete((resultado, error) ->

                        SwingUtilities.invokeLater(() -> {

                            bloquearBotones(false);

                            if (error != null
                                    || !Boolean.TRUE.equals(resultado)) {

                                JOptionPane.showMessageDialog(
                                        this,
                                        "No se pudo registrar la devolución.",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE
                                );

                            } else {

                                JOptionPane.showMessageDialog(
                                        this,
                                        "Libro devuelto correctamente."
                                );
                            }

                            cargarCombos();
                            cargarPrestamos();
                        })
                );
    }

    // BLOQUEAR BOTONES DURANTE OPERACIONES
    private void bloquearBotones(boolean bloquear) {

        btnPrestar.setEnabled(!bloquear);
        btnDevolver.setEnabled(!bloquear);
        btnRecargar.setEnabled(!bloquear);
    }

    // CERRAR SERVICIO AL CERRAR VENTANA
    @Override
    public void dispose() {
        controlador.cerrar();
        super.dispose();
    }

    // ELEMENTOS DEL COMBOBOX
    private static class EstudianteItem {

        private final Estudiante estudiante;

        public EstudianteItem(Estudiante estudiante) {
            this.estudiante = estudiante;
        }

        @Override
        public String toString() {
            return estudiante.getNombre()
                    + " - " + estudiante.getRut();
        }
    }

    private static class LibroItem {

        private final Libro libro;

        public LibroItem(Libro libro) {
            this.libro = libro;
        }

        @Override
        public String toString() {
            return libro.getTitulo()
                    + " (Stock: " + libro.getStock() + ")";
        }
    }

    // EJECUTAR VENTANA
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new GestionPrestamos().setVisible(true)
        );
    }
}