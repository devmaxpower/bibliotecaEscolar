package vista;

import controlador.ControladorReportes;
import controlador.ControladorEstudiantes;
import modelo.Estudiante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Reportes extends JFrame {

    private final ControladorReportes controlador =
            new ControladorReportes();

    private final ControladorEstudiantes controladorEstudiantes =
            new ControladorEstudiantes();

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JComboBox<EstudianteItem> cmbEstudiantes;
    private JLabel lblTitulo;

    public Reportes() {

        setTitle("Biblioteca Escolar - Reportes");
        setSize(1050, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        cargarEstudiantes();
        mostrarLibrosMasPrestados();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout(10, 10));

        // ENCABEZADO
        lblTitulo = new JLabel(
                "REPORTES DE BIBLIOTECA",
                SwingConstants.CENTER
        );

        lblTitulo.setFont(
                new Font("Arial", Font.BOLD, 20)
        );

        lblTitulo.setBorder(
                BorderFactory.createEmptyBorder(20, 10, 10, 10)
        );

        add(lblTitulo, BorderLayout.NORTH);

        // TABLA
        modeloTabla = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(25);
        tabla.setAutoCreateRowSorter(true);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // PANEL DE CONTROLES
        JPanel panelControles = new JPanel(
                new GridLayout(2, 1, 10, 10)
        );

        panelControles.setBorder(
                BorderFactory.createEmptyBorder(15, 20, 20, 20)
        );

        JPanel panelEstudiantes = new JPanel(
                new FlowLayout(FlowLayout.CENTER)
        );

        cmbEstudiantes = new JComboBox<>();

        JButton btnHistorial =
                new JButton("Consultar historial");

        panelEstudiantes.add(new JLabel("Estudiante:"));
        panelEstudiantes.add(cmbEstudiantes);
        panelEstudiantes.add(btnHistorial);

        JPanel panelBotones = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 15, 5)
        );

        JButton btnMasPrestados =
                new JButton("Libros más prestados");

        JButton btnActivos =
                new JButton("Préstamos activos");

        panelBotones.add(btnMasPrestados);
        panelBotones.add(btnActivos);

        panelControles.add(panelEstudiantes);
        panelControles.add(panelBotones);

        add(panelControles, BorderLayout.SOUTH);

        // EVENTOS
        btnMasPrestados.addActionListener(
                e -> mostrarLibrosMasPrestados()
        );

        btnHistorial.addActionListener(
                e -> mostrarHistorial()
        );

        btnActivos.addActionListener(
                e -> mostrarPrestamosActivos()
        );
    }

    // CARGAR ESTUDIANTES
    private void cargarEstudiantes() {

        cmbEstudiantes.removeAllItems();

        for (Estudiante estudiante :
                controladorEstudiantes.listarEstudiantes()) {

            cmbEstudiantes.addItem(
                    new EstudianteItem(estudiante)
            );
        }
    }

    // REPORTE: LIBROS MÁS PRESTADOS
    private void mostrarLibrosMasPrestados() {

        lblTitulo.setText("LIBROS MÁS PRESTADOS");

        String[] columnas = {
                "ID", "Título", "Autor", "Total préstamos"
        };

        actualizarTabla(
                columnas,
                controlador.obtenerLibrosMasPrestados()
        );
    }

    // REPORTE: HISTORIAL
    private void mostrarHistorial() {

        EstudianteItem seleccionado =
                (EstudianteItem) cmbEstudiantes.getSelectedItem();

        if (seleccionado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un estudiante."
            );

            return;
        }

        lblTitulo.setText(
                "HISTORIAL DE " +
                        seleccionado.estudiante.getNombre().toUpperCase()
        );

        String[] columnas = {
                "ID", "Libro", "Fecha préstamo",
                "Fecha vencimiento", "Estado"
        };

        actualizarTabla(
                columnas,
                controlador.obtenerHistorialEstudiante(
                        seleccionado.estudiante.getId()
                )
        );
    }

    // REPORTE: PRÉSTAMOS ACTIVOS
    private void mostrarPrestamosActivos() {

        lblTitulo.setText("PRÉSTAMOS ACTIVOS");

        String[] columnas = {
                "ID", "Estudiante", "Libro",
                "Fecha préstamo", "Fecha vencimiento", "Estado"
        };

        actualizarTabla(
                columnas,
                controlador.obtenerPrestamosActivos()
        );
    }

    // ACTUALIZAR TABLA
    private void actualizarTabla(
            String[] columnas,
            List<Object[]> datos) {

        modeloTabla.setRowCount(0);
        modeloTabla.setColumnIdentifiers(columnas);

        for (Object[] fila : datos) {
            modeloTabla.addRow(fila);
        }
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

    // EJECUTAR
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new Reportes().setVisible(true)
        );
    }
}