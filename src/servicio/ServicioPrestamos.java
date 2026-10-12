package servicio;

import dao.PrestamoDAO;
import dao.impl.PrestamoDAOImpl;
import modelo.Prestamo;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServicioPrestamos {

    private final PrestamoDAO prestamoDAO;
    private static final Object BLOQUEO_PRESTAMOS = new Object();

    private final ExecutorService executor =
            Executors.newFixedThreadPool(2);

    public ServicioPrestamos() {
        this.prestamoDAO = new PrestamoDAOImpl();
    }

    // REGISTRAR PRÉSTAMO CON SINCRONIZACIÓN
    public boolean registrarPrestamo(Prestamo prestamo) {

        synchronized (BLOQUEO_PRESTAMOS) {
            return prestamoDAO.registrarPrestamo(prestamo);
        }
    }

    // DEVOLVER LIBRO CON SINCRONIZACIÓN
    public boolean devolverLibro(int idPrestamo) {

        synchronized (BLOQUEO_PRESTAMOS) {
            return prestamoDAO.devolverLibro(idPrestamo);
        }
    }

    // EJECUTAR PRÉSTAMO EN HILO INDEPENDIENTE
    public CompletableFuture<Boolean> registrarPrestamoAsync(
            Prestamo prestamo) {

        return CompletableFuture.supplyAsync(
                () -> registrarPrestamo(prestamo),
                executor
        );
    }

    // EJECUTAR DEVOLUCIÓN EN HILO INDEPENDIENTE
    public CompletableFuture<Boolean> devolverLibroAsync(
            int idPrestamo) {

        return CompletableFuture.supplyAsync(
                () -> devolverLibro(idPrestamo),
                executor
        );
    }

    // LISTAR PRÉSTAMOS
    public List<Prestamo> listarPrestamos() {
        return prestamoDAO.listar();
    }

    // CERRAR HILOS AL FINALIZAR
    public void cerrarServicio() {
        executor.shutdown();
    }
}