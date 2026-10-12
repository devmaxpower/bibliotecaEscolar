package dao;

import modelo.Libro;
import java.util.List;

public interface LibroDAO {

    List<Libro> listar();

    Libro buscarPorId(int id);

    boolean agregar(Libro libro);

    boolean actualizar(Libro libro);

    boolean eliminar(int id);
}