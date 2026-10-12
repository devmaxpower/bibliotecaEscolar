package controlador;

import dao.ReporteDAO;
import dao.impl.ReporteDAOImpl;

import java.util.List;

public class ControladorReportes {

    private final ReporteDAO reporteDAO;

    public ControladorReportes() {
        this.reporteDAO = new ReporteDAOImpl();
    }

    public List<Object[]> obtenerLibrosMasPrestados() {
        return reporteDAO.librosMasPrestados();
    }

    public List<Object[]> obtenerHistorialEstudiante(int idEstudiante) {

        if (idEstudiante <= 0) {
            return List.of();
        }

        return reporteDAO.historialEstudiante(idEstudiante);
    }

    public List<Object[]> obtenerPrestamosActivos() {
        return reporteDAO.prestamosActivos();
    }
}