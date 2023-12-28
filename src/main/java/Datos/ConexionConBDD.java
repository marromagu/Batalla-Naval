package Datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public String consultarContraseña(String nombreUsuario) {
        String contraseña = "";

        try (Connection conexion = getConexion()) {
            String sql = "SELECT contraseña FROM Jugadores WHERE nombre = ?";

            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                // Establecer el parámetro nombreUsuario en la consulta preparada
                statement.setString(1, nombreUsuario);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        contraseña = resultSet.getString("contraseña");
                    } else {
                        // El usuario no existe
                        System.out.println("El usuario '" + nombreUsuario + "' no existe.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar la contraseña: " + e.getMessage());
            e.printStackTrace();
        }

        return contraseña;
    }
}
