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
            System.out.println("Error en ConexionConBDD: Controlador JDBC no encontrado");
            cnfe.printStackTrace(); // Imprimir detalles de la excepción
        } catch (SQLException sqle) {
            System.out.println("Error en ConexionConBDD: al conectar a la BDD");
            sqle.printStackTrace(); // Imprimir detalles de la excepción
        }

        return conexion;
    }

    public void cerrarConexion(Connection conexion) {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Error en ConexionConBDD: Se cerró la conexión a la BDD.");
            }
        } catch (SQLException sqle) {
            System.out.println("Error en ConexionConBDD: al cerrar la conexión a la BDD");
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
                        System.out.println("Error en ConexionConBDD: El usuario '" + nombreUsuario + "' no existe.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en ConexionConBDD: al consultar la contraseña: " + e.getMessage());
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
                        System.out.println("Error en ConexionConBDD: Nombre de usuario o contraseña incorrectos.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en ConexionConBDD: al obtener la ID del jugador: " + e.getMessage());
        }

        return idJugador;
    }

    public HashMap<Integer, String> obtenerPartidasTerminadasPorJugador(int idJugador) {
        HashMap<Integer, String> mapaPartidasTerminadas = new HashMap<>();

        try (Connection conexion = getConexion()) {
            String sql = "SELECT id_partida, jugador_1, jugador_2, ganador, ultimo_turno FROM Partidas "
                    + "WHERE (jugador_1 = ? OR jugador_2 = ?) AND estado = 'X'";

            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                // Establecer el parámetro idJugador en la consulta preparada
                statement.setInt(1, idJugador);
                statement.setInt(2, idJugador);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int idPartida = resultSet.getInt("id_partida");
                        int jugador1ID = resultSet.getInt("jugador_1");
                        int jugador2ID = resultSet.getInt("jugador_2");
                        int ganadorID = resultSet.getInt("ganador");
                        int ultimoTurnoID = resultSet.getInt("ultimo_turno");

                        // Obtener los nombres de los jugadores utilizando el método creado anteriormente
                        String nombreJugador1 = obtenerNombreJugadorPorID(jugador1ID);
                        String nombreJugador2 = obtenerNombreJugadorPorID(jugador2ID);
                        String nombreGanador = obtenerNombreJugadorPorID(ganadorID);
                        String nombreUltimoTurno = obtenerNombreJugadorPorID(ultimoTurnoID);

                        // Crear cadena representativa de la partida con nombres de jugadores
                        String representacionPartida = String.format("%d;%s;%s;%s;%s",
                                idPartida, nombreJugador1, nombreJugador2, nombreGanador, nombreUltimoTurno);

                        // Agregar la representación al HashMap
                        mapaPartidasTerminadas.put(idPartida, representacionPartida);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en ConexionConBDD: al obtener las partidas terminadas: " + e.getMessage());
        }

        return mapaPartidasTerminadas;
    }

    public HashMap<Integer, String> obtenerPartidasNoTerminadasConTurno(int idJugador) {
        HashMap<Integer, String> mapaPartidasNoTerminadasConTurno = new HashMap<>();

        try (Connection conexion = getConexion()) {
            String sql = "SELECT id_partida, jugador_1, jugador_2, ultimo_turno "
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
                        int jugador1ID = resultSet.getInt("jugador_1");
                        int jugador2ID = resultSet.getInt("jugador_2");
                        int ultimoTurnoID = resultSet.getInt("ultimo_turno");

                        // Obtener los nombres de los jugadores utilizando el método creado anteriormente
                        String nombreJugador1 = obtenerNombreJugadorPorID(jugador1ID);
                        String nombreJugador2 = obtenerNombreJugadorPorID(jugador2ID);
                        String nombreJugadorUltimoTurno = obtenerNombreJugadorPorID(ultimoTurnoID);

                        // Crear cadena representativa de la partida con nombres de jugadores
                        String representacionPartida = String.format("%d,%s,%s,%s",
                                idPartida, nombreJugador1, nombreJugador2, nombreJugadorUltimoTurno);

                        // Agregar la representación al HashMap
                        mapaPartidasNoTerminadasConTurno.put(idPartida, representacionPartida);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en ConexionConBDD: al obtener las partidas no terminadas con turno: " + e.getMessage());
        }

        return mapaPartidasNoTerminadasConTurno;
    }

    public HashMap<Integer, String> obtenerPartidasNoTerminadasSinTurno(int idJugador) {
        HashMap<Integer, String> mapaPartidasNoTerminadasSinTurno = new HashMap<>();

        try (Connection conexion = getConexion()) {
            String sql = "SELECT id_partida, jugador_1, jugador_2, ultimo_turno "
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
                        int jugador1ID = resultSet.getInt("jugador_1");
                        int jugador2ID = resultSet.getInt("jugador_2");
                        int ultimoTurno = resultSet.getInt("ultimo_turno");

                        // Obtener los nombres de los jugadores utilizando el método creado anteriormente
                        String nombreJugador1 = obtenerNombreJugadorPorID(jugador1ID);
                        String nombreJugador2 = obtenerNombreJugadorPorID(jugador2ID);
                        String nombreUltimoTurno = obtenerNombreJugadorPorID(ultimoTurno);
                        // Crear cadena representativa de la partida con nombres de jugadores
                        String representacionPartida = String.format("%d,%s,%s,%s",
                                idPartida, nombreJugador1, nombreJugador2, nombreUltimoTurno);

                        // Agregar la representación al HashMap
                        mapaPartidasNoTerminadasSinTurno.put(idPartida, representacionPartida);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en ConexionConBDD: al obtener las partidas no terminadas sin turno: " + e.getMessage());
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
            System.out.println("Error en ConexionConBDD: al obtener los disparos de la partida: " + e.getMessage());
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
                    System.out.println("---> El Jugador " + idJugador + " se ha rendido en la Partida " + idPartida);
                    return true;  // Rendición exitosa
                } else {
                    System.out.println("Error en ConexionConBDD: La rendición no pudo ser procesada. Asegúrese de que la partida esté en curso y el último turno no sea del jugador que se rinde.");
                    return false; // Rendición no exitosa
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en ConexionConBDD: al rendirse en la partida: " + e.getMessage());
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
            System.out.println("Error en ConexionConBDD: al obtener la ID del otro jugador: " + e.getMessage());
        }

        return idOtroJugador;
    }

    public boolean hayBarcoEnemigoEnCoordenada(int idJugador, int idPartida, int posicionX, int posicionY) {
        boolean hayBarcoEnemigo = false;

        try (Connection conexion = getConexion()) {
            String sql = "SELECT B.id_barco "
                    + "FROM Barcos B "
                    + "JOIN Disparos D ON B.id_partida = D.id_partida "
                    + "               AND B.posicion_x = D.posicion_x "
                    + "               AND B.posicion_y = D.posicion_y "
                    + "               AND B.jugador_id != D.jugador_id "
                    + "WHERE B.id_partida = ? "
                    + "  AND B.jugador_id = ? "
                    + "  AND B.posicion_x = ? "
                    + "  AND B.posicion_y = ?";

            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                statement.setInt(1, idPartida);
                statement.setInt(2, idJugador);
                statement.setInt(3, posicionX);
                statement.setInt(4, posicionY);

                try (ResultSet resultSet = statement.executeQuery()) {
                    hayBarcoEnemigo = resultSet.next(); // Devuelve true si hay al menos una fila en el resultado
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en ConexionConBDD: al verificar la presencia de un barco enemigo en la coordenada: " + e.getMessage());
        }

        return hayBarcoEnemigo;
    }

    public void registrarDisparo(int idPartida, int idJugador, int posicionX, int posicionY) {
        try (Connection conexion = getConexion()) {
            boolean hayBarcoEnemigo = hayBarcoEnemigoEnCoordenada(idJugador, idPartida, posicionX, posicionY);

            // Determinar el resultado del disparo (Tocado o Agua)
            String resultadoFinal = hayBarcoEnemigo ? "T" : "A";

            // Insertar un nuevo disparo en la tabla Disparos
            String insertDisparoSql = "INSERT INTO Disparos (id_partida, jugador_id, posicion_x, posicion_y, resultado) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement insertDisparoStatement = conexion.prepareStatement(insertDisparoSql)) {
                insertDisparoStatement.setInt(1, idPartida);
                insertDisparoStatement.setInt(2, idJugador);
                insertDisparoStatement.setInt(3, posicionX);
                insertDisparoStatement.setInt(4, posicionY);
                insertDisparoStatement.setString(5, resultadoFinal);

                insertDisparoStatement.executeUpdate();
            }

            // Actualizar la tabla Partidas con el último turno
            String updatePartidaSql = "UPDATE Partidas SET ultimo_turno = ? WHERE id_partida = ?";
            try (PreparedStatement updatePartidaStatement = conexion.prepareStatement(updatePartidaSql)) {
                int idOtroJugador = obtenerOtroJugador(idPartida, idJugador);
                updatePartidaStatement.setInt(1, idOtroJugador);
                updatePartidaStatement.setInt(2, idPartida);

                updatePartidaStatement.executeUpdate();
            }
            System.out.println("---> Disparo: Jugador " + idJugador + " Parida " + idPartida);
        } catch (SQLException e) {
            System.out.println("Error en ConexionConBDD: al registrar el disparo: " + e.getMessage());
        }
    }

    public int crearNuevaPartida(int idJugador1, int idJugador2) {
        int idNuevaPartida = -1;

        try (Connection conexion = getConexion()) {
            String sql = "INSERT INTO Partidas (jugador_1, jugador_2, estado, ultimo_turno) VALUES (?, ?, 'O', ?)";

            try (PreparedStatement statement = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, idJugador1);
                statement.setInt(2, idJugador2);
                statement.setInt(3, idJugador1);  // Inicializar con la id del primer jugador como último turno

                int filasAfectadas = statement.executeUpdate();

                if (filasAfectadas > 0) {
                    // Obtener la ID generada para la nueva partida
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        idNuevaPartida = generatedKeys.getInt(1);
                        System.out.println("---> Se creó una nueva partida con ID: " + idNuevaPartida);
                    } else {
                        System.out.println("Error en ConexionConBDD: No se pudo obtener la ID de la nueva partida.");
                    }
                } else {
                    System.out.println("Error en ConexionConBDD: No se pudo crear la nueva partida.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en ConexionConBDD: al crear la nueva partida: " + e.getMessage());
            e.printStackTrace();
        }

        return idNuevaPartida;
    }

    public HashMap<Integer, String> obtenerUsuarios() {
        HashMap<Integer, String> mapaUsuarios = new HashMap<>();

        try (Connection conexion = getConexion()) {
            String sql = "SELECT id_jugador, nombre FROM Jugadores";

            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int idJugador = resultSet.getInt("id_jugador");
                        String nombre = resultSet.getString("nombre");

                        // Crear cadena representativa del usuario
                        //String representacionUsuario = String.format("%d %s", idJugador, nombre);
                        // Agregar la representación al HashMap
                        mapaUsuarios.put(idJugador, nombre);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en ConexionConBDD: al obtener la lista de usuarios: " + e.getMessage());
            e.printStackTrace();
        }

        return mapaUsuarios;
    }

    public String obtenerNombreJugadorPorID(int idJugador) {
        String nombreJugador = null;

        try (Connection conexion = getConexion()) {
            String sql = "SELECT nombre FROM Jugadores WHERE id_jugador = ?";

            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                // Establecer el parámetro idJugador en la consulta preparada
                statement.setInt(1, idJugador);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        nombreJugador = resultSet.getString("nombre");
                    } else {
                        // No se encontró un jugador con la ID proporcionada
                        System.out.println("No se encontró un jugador con la ID: " + idJugador);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en ConexionConBDD: al obtener el nombre del jugador: " + e.getMessage());
        }

        return nombreJugador;
    }

}
