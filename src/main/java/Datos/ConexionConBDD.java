package Datos;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ConexionConBDD implements Serializable {

    private final String NameDataBase = "BDD_HundirLaFlota";
    private final String User = "root";
    private final String Password = "root";
    private final String Driver = "com.mysql.cj.jdbc.Driver";
    private final String URL = "jdbc:mysql://localhost:3306/" + NameDataBase;

    public ConexionConBDD() {
    }

    public Connection getConexion() {
        Connection conexion = null;
        try {
            Class.forName(Driver);
            conexion = DriverManager.getConnection(URL, User, Password);
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            System.out.println("Error: Controlador JDBC no encontrado");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.out.println("Error al conectar a la BDD");
        }
        return conexion;
    }

    public void cerrarConexion(Connection conexion) {
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

    public int consultarContraseña(String nombreUsuario) {
        int contraseña = 0;

        try (Connection conexion = getConexion()) {
            String sql = "SELECT contraseña FROM Jugadores WHERE nombre = ?";

            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                // Establecer el parámetro nombreUsuario en la consulta preparada
                statement.setString(1, nombreUsuario);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        contraseña = resultSet.getInt("contraseña");
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

    public HashMap<Integer, String> obtenerPartidasTerminadasPorJugador(int idJugador) {
        HashMap<Integer, String> mapaPartidasTerminadas = new HashMap<>();

        try (Connection conexion = getConexion()) {
            String sql = "SELECT id_partida, estado, ganador, ultimo_turno FROM Partidas "
                    + "WHERE (jugador_1 = ? OR jugador_2 = ?) AND estado = 'X'";

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

                        // Crear cadena representativa de la partida
                        String representacionPartida = String.format("ID Partida: %d, Estado: %s, Ganador (ID): %d, Último Turno (ID): %d",
                                idPartida, estado, ganador, ultimoTurno);

                        // Agregar la representación al HashMap
                        mapaPartidasTerminadas.put(idPartida, representacionPartida);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las partidas terminadas: " + e.getMessage());
            e.printStackTrace();
        }

        return mapaPartidasTerminadas;
    }

    public HashMap<Integer, String> obtenerPartidasNoTerminadasConTurno(int idJugador) {
        HashMap<Integer, String> mapaPartidasNoTerminadasConTurno = new HashMap<>();

        try (Connection conexion = getConexion()) {
            String sql = "SELECT id_partida, estado, ganador, ultimo_turno "
                    + "FROM Partidas "
                    + "WHERE (jugador_1 = ? OR jugador_2 = ?) AND estado = 'O' AND ultimo_turno = ?";

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

                        // Crear cadena representativa de la partida
                        String representacionPartida = String.format("ID Partida: %d, Estado: %s, Ganador (ID): %d, Último Turno (ID): %d",
                                idPartida, estado, ganador, ultimoTurno);

                        // Agregar la representación al HashMap
                        mapaPartidasNoTerminadasConTurno.put(idPartida, representacionPartida);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las partidas no terminadas con turno: " + e.getMessage());
            e.printStackTrace();
        }

        return mapaPartidasNoTerminadasConTurno;
    }

    public HashMap<Integer, String> obtenerPartidasNoTerminadasSinTurno(int idJugador) {
        HashMap<Integer, String> mapaPartidasNoTerminadasSinTurno = new HashMap<>();

        try (Connection conexion = getConexion()) {
            String sql = "SELECT id_partida, estado, ganador, ultimo_turno "
                    + "FROM Partidas "
                    + "WHERE (jugador_1 = ? OR jugador_2 = ?) AND estado = 'O' AND ultimo_turno <> ?";

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

                        // Crear cadena representativa de la partida
                        String representacionPartida = String.format("ID Partida: %d, Estado: %s, Ganador (ID): %d, Último Turno (ID): %d",
                                idPartida, estado, ganador, ultimoTurno);

                        // Agregar la representación al HashMap
                        mapaPartidasNoTerminadasSinTurno.put(idPartida, representacionPartida);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las partidas no terminadas sin turno: " + e.getMessage());
            e.printStackTrace();
        }

        return mapaPartidasNoTerminadasSinTurno;
    }

    public ArrayList<String> obtenerDisparosDePartida(int idPartida) {
        ArrayList<String> listaDisparos = new ArrayList<>();

        try (Connection conexion = getConexion()) {
            String sql = "SELECT * FROM Disparos WHERE id_partida = ?";

            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                // Establecer el parámetro idPartida en la consulta preparada
                statement.setInt(1, idPartida);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int idDisparo = resultSet.getInt("id_disparo");
                        int jugadorId = resultSet.getInt("jugador_id");
                        int posicionX = resultSet.getInt("posicion_x");
                        int posicionY = resultSet.getInt("posicion_y");
                        String resultado = resultSet.getString("resultado");

                        // Crear cadena representativa del disparo
                        String representacionDisparo = String.format("ID Disparo: %d, Jugador (ID): %d, Posición: (%d, %d), Resultado: %s",
                                idDisparo, jugadorId, posicionX, posicionY, resultado);

                        // Agregar la representación al ArrayList
                        listaDisparos.add(representacionDisparo);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener los disparos de la partida: " + e.getMessage());
            e.printStackTrace();
        }

        return listaDisparos;
    }

    public boolean rendirseEnPartida(int idJugador, int idPartida) {
        try (Connection conexion = getConexion()) {
            String sql = "UPDATE Partidas "
                    + "SET estado = 'X', ganador = ?, ultimo_turno = ? "
                    + "WHERE id_partida = ? AND estado = 'O' AND ultimo_turno <> ?";

            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                // Establecer los parámetros en la consulta preparada
                statement.setInt(1, obtenerOtroJugador(idPartida, idJugador)); // Obtener la ID del otro jugador
                statement.setInt(2, idJugador); // Establecer al jugador que se rinde como último turno
                statement.setInt(3, idPartida);
                statement.setInt(4, idJugador);

                // Ejecutar la actualización
                int filasAfectadas = statement.executeUpdate();

                if (filasAfectadas > 0) {
                    System.out.println("El Jugador " + idJugador + " se ha rendido en la Partida " + idPartida);
                    return true;  // Rendición exitosa
                } else {
                    System.out.println("La rendición no pudo ser procesada. Asegúrese de que la partida esté en curso y el último turno no sea del jugador que se rinde.");
                    return false; // Rendición no exitosa
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al rendirse en la partida: " + e.getMessage());
            e.printStackTrace();
            return false; // Rendición no exitosa debido a un error
        }
    }

    private int obtenerOtroJugador(int idPartida, int idJugador) {
        int idOtroJugador = -1;

        try (Connection conexion = getConexion()) {
            String sql = "SELECT jugador_1, jugador_2 FROM Partidas WHERE id_partida = ?";

            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                statement.setInt(1, idPartida);

                try (var resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int jugador1 = resultSet.getInt("jugador_1");
                        int jugador2 = resultSet.getInt("jugador_2");

                        idOtroJugador = (jugador1 == idJugador) ? jugador2 : jugador1;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la ID del otro jugador: " + e.getMessage());
            e.printStackTrace();
        }

        return idOtroJugador;
    }

}
