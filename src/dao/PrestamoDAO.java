package dao;

import modelo.Prestamo;
import java.util.List;

public interface PrestamoDAO {

    List<Prestamo> listar();

    boolean registrarPrestamo(Prestamo prestamo);

    boolean devolverLibro(int idPrestamo);
}