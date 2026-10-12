package controlador;

import dao.EstudianteDAO;
import dao.impl.EstudianteDAOImpl;
import modelo.Estudiante;

import java.util.List;

public class ControladorEstudiantes {

    private final EstudianteDAO estudianteDAO;

    public ControladorEstudiantes() {
        this.estudianteDAO = new EstudianteDAOImpl();
    }

    public List<Estudiante> listarEstudiantes() {
        return estudianteDAO.listar();
    }

    public Estudiante buscarEstudiante(int id) {
        return estudianteDAO.buscarPorId(id);
    }

    public boolean agregarEstudiante(Estudiante estudiante) {

        if (!validarEstudiante(estudiante)) {
            return false;
        }

        return estudianteDAO.agregar(estudiante);
    }

    public boolean actualizarEstudiante(Estudiante estudiante) {

        if (estudiante == null || estudiante.getId() <= 0) {
            return false;
        }

        if (!validarEstudiante(estudiante)) {
            return false;
        }

        return estudianteDAO.actualizar(estudiante);
    }

    public boolean eliminarEstudiante(int id) {

        if (id <= 0) {
            return false;
        }

        return estudianteDAO.eliminar(id);
    }

    private boolean validarEstudiante(Estudiante estudiante) {

        if (estudiante == null) {
            return false;
        }

        return estudiante.getNombre() != null
                && !estudiante.getNombre().isBlank()
                && estudiante.getRut() != null
                && !estudiante.getRut().isBlank()
                && estudiante.getCurso() != null
                && !estudiante.getCurso().isBlank()
                && estudiante.getCorreo() != null
                && !estudiante.getCorreo().isBlank();
    }
}