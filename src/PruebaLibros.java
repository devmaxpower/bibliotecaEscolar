import dao.LibroDAO;
import dao.impl.LibroDAOImpl;
import modelo.Libro;

public class PruebaLibros {

    public static void main(String[] args) {

        LibroDAO libroDAO = new LibroDAOImpl();

        System.out.println("=== LISTADO DE LIBROS ===");

        for (Libro libro : libroDAO.listar()) {

            System.out.println(
                    libro.getId() + " | " +
                            libro.getTitulo() + " | " +
                            libro.getAutor() + " | Stock: " +
                            libro.getStock()
            );
        }
    }
}