package controlador;

import modelo.Prestamo;
import servicio.ServicioPrestamos;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ControladorPrestamos {

    private final ServicioPrestamos servicio = new ServicioPrestamos();

    public List<Prestamo> listarPrestamos() {
        return servicio.listarPrestamos();
    }

    public CompletableFuture<Boolean> registrarPrestamo(
            int idEstudiante,
            int idLibro,
            LocalDate fechaDevolucion) {

        if (idEstudiante <= 0 || idLibro <= 0
                || fechaDevolucion == null
                || !fechaDevolucion.isAfter(LocalDate.now())) {

            return CompletableFuture.completedFuture(false);
        }

        Prestamo prestamo = new Prestamo(
                0,
                idEstudiante,
                idLibro,
                LocalDate.now(),
                fechaDevolucion,
                false
        );

        return servicio.registrarPrestamoAsync(prestamo);
    }

    public CompletableFuture<Boolean> devolverLibro(int idPrestamo) {

        if (idPrestamo <= 0) {
            return CompletableFuture.completedFuture(false);
        }

        return servicio.devolverLibroAsync(idPrestamo);
    }

    public void cerrar() {
        servicio.cerrarServicio();
    }
}