package controlador;

import dao.LibroDAO;
import dao.impl.LibroDAOImpl;
import modelo.Libro;

import java.util.List;

public class ControladorLibros {

    private final LibroDAO libroDAO;

    public ControladorLibros() {
        this.libroDAO = new LibroDAOImpl();
    }

    public List<Libro> listarLibros() {
        return libroDAO.listar();
    }

    public Libro buscarLibro(int id) {
        return libroDAO.buscarPorId(id);
    }

    public boolean agregarLibro(Libro libro) {
        if (!validarLibro(libro)) {
            return false;
        }

        return libroDAO.agregar(libro);
    }

    public boolean actualizarLibro(Libro libro) {
        if (libro.getId() <= 0 || !validarLibro(libro)) {
            return false;
        }

        return libroDAO.actualizar(libro);
    }

    public boolean eliminarLibro(int id) {
        if (id <= 0) {
            return false;
        }

        return libroDAO.eliminar(id);
    }

    private boolean validarLibro(Libro libro) {

        if (libro == null) {
            return false;
        }

        return libro.getTitulo() != null
                && !libro.getTitulo().isBlank()
                && libro.getAutor() != null
                && !libro.getAutor().isBlank()
                && libro.getIsbn() != null
                && !libro.getIsbn().isBlank()
                && libro.getEditorial() != null
                && !libro.getEditorial().isBlank()
                && libro.getStock() >= 0
                && libro.getIdCategoria() > 0;
    }
}