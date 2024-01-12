
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import Datos.DatosJugador;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DAM_M
 */
public class ConexionServidor extends Thread {

    static final int Puerto = 2001; //Creamos una constante etatica del puerto por donde se conctara el Cliente.
    private final Socket skCliente; //Instanciamos el Socket del Cliente.
    private DataInputStream flujo_entrada;
    private DataOutputStream flujo_salida;
    private ObjectOutputStream objeto_salida;
    private DatosJugador misDatos = null;
    private HashMap<Integer, String> listaUsuarios;
    private HashMap<Integer, String> usuariosConectados;

    ;

    /**
     *
     * @param skCliente es un Objeto Socket que se proporciona al constructor.
     * Cada instancia del servidor tendra su propio socket.
     */
    public ConexionServidor(Socket skCliente,HashMap<Integer, String> usuariosC) {
        this.skCliente = skCliente;
        this.usuariosConectados = usuariosC;

    }

    @Override
    public void run() {
        try {
            flujo_salida = new DataOutputStream(skCliente.getOutputStream());
            flujo_entrada = new DataInputStream(skCliente.getInputStream());
            objeto_salida = new ObjectOutputStream(skCliente.getOutputStream());

            if (!login()) {
                System.out.println("--> Usuario o contraseña erroneos.");
            }
            while (true) {
                int op = flujo_entrada.readInt();
                switch (op) {
                    case 1 ->
                        enviarRepeticion();
                    case 2 ->
                        recibirRendicion();
                    case 3 ->
                        recibirCoordenadas();
                    case 4 ->
                        enviarListaTerminada();
                    case 5 ->
                        crearPartida();
                    default ->
                        throw new AssertionError();
                }
            }

        } catch (IOException e) {
            System.out.println("--> Error en run: " + e.getMessage());
            cerrarConexiones();
        }
    }

    private boolean login() {
        boolean contraseñaCorrecta = false;
        try {
            // Recogemos los datos de usuario y contraseña
            String usuario = flujo_entrada.readUTF();
            int contraseña = flujo_entrada.readInt();

            // Creamos un objeto DatosJugador con los datos proporcionados
            misDatos = new DatosJugador(usuario, contraseña);
            listaUsuarios = misDatos.tablaUsuarios();
            // Validamos la contraseña y mostramos un mensaje por consola
            contraseñaCorrecta = misDatos.validarContraseña();
            System.out.println("Usuario: " + usuario + " Contraseña: " + contraseña + " - " + (contraseñaCorrecta ? "Correcta" : "Incorrecta"));
            
            if (contraseñaCorrecta) {
                System.out.println("--> Correcto!");
                // Mandamos el Objeto de los datos del Cliente
                enviarObjeto(listaUsuarios);
                agregarUsuario(misDatos.obtenerIdJugador(), usuario);
                System.out.println(misDatos.obtenerIdJugador() + " " + usuario);
                enviarObjeto(misDatos);
            }

        } catch (IOException ex) {
            Logger.getLogger(ConexionServidor.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("--> Error en Login: " + ex.getMessage());
        }
        return contraseñaCorrecta;
    }

    public void mostrarListaUsuarios() {
        for (Map.Entry<Integer, String> entry : listaUsuarios.entrySet()) {
            System.out.println(entry.getValue());
        }
        enviarObjeto(listaUsuarios);
    }

    public void enviarRepeticion() {
        int id_partida;
        try {
            id_partida = flujo_entrada.readInt();
            enviarObjeto(misDatos.repeticion(id_partida));
        } catch (IOException ex) {
            Logger.getLogger(ConexionServidor.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("--> Error en enviar Repeticion: " + ex.getMessage());
        }

    }

    public void recibirRendicion() {
        try {
            int id_jugador = flujo_entrada.readInt();
            int id_partida = flujo_entrada.readInt();
            flujo_salida.writeBoolean(misDatos.rendirse(id_jugador, id_partida));
            System.out.println("--> Rendicion recibida con éxito.");
        } catch (IOException ex) {
            Logger.getLogger(ConexionServidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // Método para enviar un objeto por socket
    public void enviarObjeto(Object objeto) {
        try {
            objeto_salida.writeObject(objeto);
            objeto_salida.flush();
        } catch (IOException e) {
            System.err.println("Error al enviar el objeto por el socket: " + e.getMessage());
        }
    }

    private void recibirCoordenadas() {
        try {
            int id_jugador = flujo_entrada.readInt();
            int id_partida = flujo_entrada.readInt();
            int coordenada_x = flujo_entrada.readInt();
            int coordenada_y = flujo_entrada.readInt();
            misDatos.disparar(id_partida, id_jugador, coordenada_x, coordenada_y);
        } catch (IOException ex) {
            System.out.println("--> Error al recibir Coordenadas: " + ex.getMessage());
        }
    }

    private void crearPartida() {
        try {
            int id_jugador1 = flujo_entrada.readInt();
            int id_jugador2 = flujo_entrada.readInt();
            flujo_salida.writeInt(misDatos.crearPartida(id_jugador1, id_jugador2));
            System.out.println("Partida creada");
        } catch (IOException ex) {
            Logger.getLogger(ConexionServidor.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("--> Error al crear Partida: " + ex.getMessage());
        }
    }

    private void enviarListaTerminada() {
        enviarObjeto(misDatos.getListaPartidaTermindas());
    }

    private void cerrarConexiones() {
        try {
            // Cierra la conexión del cliente y otros recursos
            if (skCliente != null && !skCliente.isClosed()) {
                skCliente.close();
                System.out.println("--> Conexión del Servidor cerrada.");
            }

            // Cierra flujos de entrada y salida
            if (flujo_entrada != null) {
                flujo_entrada.close();
            }
            if (flujo_salida != null) {
                flujo_salida.close();
            }
        } catch (IOException e) {
            System.out.println("--> Error al cerrar conexiones: " + e.getMessage());
        }
    }

    public boolean agregarUsuario(int id, String nombre) {
        if (!usuariosConectados.containsKey(id)) {
            usuariosConectados.put(id, nombre);
            return true;
        } else {
            System.out.println("--> Error: Usuario ya conectado.");
             return false;
        }
    }

    public void eliminarUsuario(int id) {
        if (usuariosConectados.containsKey(id)) {
            usuariosConectados.remove(id);
        } else {
            System.out.println("--> Error: Usuario no encontrado.");
        }
    }

    public String getNombreUsuario(int id) {
        return usuariosConectados.get(id);
    }
}
