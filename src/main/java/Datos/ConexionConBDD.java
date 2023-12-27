package Datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionConBDD {

    private Connection conexion = null;
    private final String NameDataBase = "BDD_HundirLaFlota";
    private final String User = "root";
    private final String Password = "root";
    private final String Driver = "com.mysql.cj.jdbc.Driver";
    private final String URL = "jdbc:mysql://localhost:3306/" + NameDataBase;

    public ConexionConBDD() {
    }

    public Connection getConexion() {
        try {
            Class.forName(Driver);
            conexion = DriverManager.getConnection(URL, User, Password);
            System.out.println("Se conectó a la BDD.");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            System.out.println("Error: Controlador JDBC no encontrado");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.out.println("Error al conectar a la BDD");
        }
        return conexion;
    }

    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Se cerró la conexión a la BDD.");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.out.println("Error al cerrar la conexión a la BDD");
        }
    }
}
