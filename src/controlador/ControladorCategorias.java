package controlador;

import dao.CategoriaDAO;
import dao.impl.CategoriaDAOImpl;
import modelo.Categoria;

import java.util.List;

public class ControladorCategorias {

    private final CategoriaDAO categoriaDAO;

    public ControladorCategorias() {
        this.categoriaDAO = new CategoriaDAOImpl();
    }

    public List<Categoria> listarCategorias() {
        return categoriaDAO.listar();
    }

    public Categoria buscarCategoria(int id) {
        return categoriaDAO.buscarPorId(id);
    }

    public boolean agregarCategoria(Categoria categoria) {

        if (!validarCategoria(categoria)) {
            return false;
        }

        return categoriaDAO.agregar(categoria);
    }

    public boolean actualizarCategoria(Categoria categoria) {

        if (categoria == null || categoria.getId() <= 0) {
            return false;
        }

        if (!validarCategoria(categoria)) {
            return false;
        }

        return categoriaDAO.actualizar(categoria);
    }

    public boolean eliminarCategoria(int id) {

        if (id <= 0) {
            return false;
        }

        return categoriaDAO.eliminar(id);
    }

    private boolean validarCategoria(Categoria categoria) {

        return categoria != null
                && categoria.getNombre() != null
                && !categoria.getNombre().isBlank();
    }
}