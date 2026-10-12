package dao;

import modelo.Estudiante;
import java.util.List;

public interface EstudianteDAO {

    List<Estudiante> listar();

    Estudiante buscarPorId(int id);

    boolean agregar(Estudiante estudiante);

    boolean actualizar(Estudiante estudiante);

    boolean eliminar(int id);
}