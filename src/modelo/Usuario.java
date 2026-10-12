package modelo;

public class Usuario extends Persona {

    private String contrasena;
    private String rol;

    public Usuario(int id, String nombre, String rut,
                   String correo, String contrasena, String rol) {

        super(id, nombre, rut, correo);
        this.contrasena = contrasena;
        this.rol = rol;
    }

    @Override
    public String obtenerDescripcion() {
        return nombre + " - " + rol;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getRol() {
        return rol;
    }
}