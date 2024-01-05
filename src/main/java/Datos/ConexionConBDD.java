package Datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public int consultarIDJugador(String nombreUsuario, int contraseña) {
        int idJugador = -1;

        try (Connection conexion = getConexion()) {
            String sql = "SELECT id_jugador FROM Jugadores WHERE nombre = ? AND contraseña = ?";

            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                // Establecer los parámetros en la consulta preparada
                statement.setString(1, nombreUsuario);
                statement.setInt(2, contraseña);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        idJugador = resultSet.getInt("id_jugador");
                    } else {
                        // No se encontró un jugador con las credenciales proporcionadas
                        System.out.println("Nombre de usuario o contraseña incorrectos.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la ID del jugador: " + e.getMessage());
            e.printStackTrace();
        }

        return idJugador;
    }

    public void mostrarPartidasTerminadasPorJugador(int idJugador) {
        try (Connection conexion = getConexion()) {
            String sql = "SELECT id_partida, estado, ganador, ultimo_turno FROM Partidas "
                    + "WHERE (jugador_1 = ? OR jugador_2 = ?) AND estado = 'Terminada'";

            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                // Establecer el parámetro idJugador en la consulta preparada
                statement.setInt(1, idJugador);
                statement.setInt(2, idJugador);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int idPartida = resultSet.getInt("id_partida");
                        String estado = resultSet.getString("estado");
                        int ganador = resultSet.getInt("ganador");
                        int ultimoTurno = resultSet.getInt("ultimo_turno");

                        System.out.println("ID Partida: " + idPartida);
                        System.out.println("Estado: " + estado);
                        System.out.println("Ganador (ID): " + ganador);
                        System.out.println("Último Turno (ID): " + ultimoTurno);
                        System.out.println("------------------------------");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar las partidas terminadas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void mostrarPartidasNoTerminadasConTurno(int idJugador) {
        try (Connection conexion = getConexion()) {
            String sql = "SELECT id_partida, estado, ganador, ultimo_turno "
                    + "FROM Partidas "
                    + "WHERE (jugador_1 = ? OR jugador_2 = ?) AND estado = 'En curso' AND ultimo_turno = ?";

            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                // Establecer los parámetros en la consulta preparada
                statement.setInt(1, idJugador);
                statement.setInt(2, idJugador);
                statement.setInt(3, idJugador);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int idPartida = resultSet.getInt("id_partida");
                        String estado = resultSet.getString("estado");
                        int ganador = resultSet.getInt("ganador");
                        int ultimoTurno = resultSet.getInt("ultimo_turno");

                        System.out.println("ID Partida: " + idPartida);
                        System.out.println("Estado: " + estado);
                        System.out.println("Ganador (ID): " + ganador);
                        System.out.println("Último Turno (ID): " + ultimoTurno);
                        System.out.println("------------------------------");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar las partidas no terminadas con turno: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void mostrarPartidasNoTerminadasSinTurno(int idJugador) {
        try (Connection conexion = getConexion()) {
            String sql = "SELECT id_partida, estado, ganador, ultimo_turno "
                    + "FROM Partidas "
                    + "WHERE (jugador_1 = ? OR jugador_2 = ?) AND estado = 'En curso' AND ultimo_turno <> ?";

            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                // Establecer los parámetros en la consulta preparada
                statement.setInt(1, idJugador);
                statement.setInt(2, idJugador);
                statement.setInt(3, idJugador);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int idPartida = resultSet.getInt("id_partida");
                        String estado = resultSet.getString("estado");
                        int ganador = resultSet.getInt("ganador");
                        int ultimoTurno = resultSet.getInt("ultimo_turno");

                        System.out.println("ID Partida: " + idPartida);
                        System.out.println("Estado: " + estado);
                        System.out.println("Ganador (ID): " + ganador);
                        System.out.println("Último Turno (ID): " + ultimoTurno);
                        System.out.println("------------------------------");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar las partidas no terminadas sin turno: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
