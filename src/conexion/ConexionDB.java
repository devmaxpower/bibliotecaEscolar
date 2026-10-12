package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static ConexionDB instancia;

    private static final String URL =
            "jdbc:mysql://localhost:8889/biblioteca";

    private static final String USUARIO = "Max";
    private static final String PASSWORD = "max";

    private ConexionDB() {
    }

    public static synchronized ConexionDB getInstance() {

        if (instancia == null) {
            instancia = new ConexionDB();
        }

        return instancia;
    }

    public Connection getConexion() throws SQLException {

        return DriverManager.getConnection(
                URL,
                USUARIO,
                PASSWORD
        );
    }
}