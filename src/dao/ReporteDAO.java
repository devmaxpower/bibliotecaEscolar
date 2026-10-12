package dao;

import java.util.List;

public interface ReporteDAO {

    List<Object[]> librosMasPrestados();

    List<Object[]> historialEstudiante(int idEstudiante);

    List<Object[]> prestamosActivos();
}