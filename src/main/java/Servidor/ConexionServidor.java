
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
    public ConexionServidor(Socket skCliente, HashMap<Integer, String> usuariosC) {
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
                    case 0 -> {
                        int id = flujo_entrada.readInt();
                        eliminarUsuario(id);
                    }
                    case 1 ->
                        enviarRepeticion();
                    case 2 ->
                        recibirRendicion();
                    case 3 ->
                        recibirCoordenadas();
                    case 4 ->
                        enviarListaTerminada();
                    case 5 ->
                        enviarListaSinTerminadaSuTurno();
                    case 6 ->
                        enviarListaSinTerminadaMiTurno();
                    case 7 ->
                        crearPartida();
                    case 8 ->
                        enviarTableroConBarcos();
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
        boolean contraseñaYusuarioOnline = false;
        try {
            // Recogemos los datos de usuario y contraseña
            String usuario = flujo_entrada.readUTF();
            int contraseña = flujo_entrada.readInt();

            // Creamos un objeto DatosJugador con los datos proporcionados
            misDatos = new DatosJugador(usuario, contraseña);
            listaUsuarios = misDatos.tablaUsuarios();
            // Validamos la contraseña y mostramos un mensaje por consola
            contraseñaYusuarioOnline = misDatos.validarContraseña();
            System.out.println("-- Usuario: " + usuario + " Contraseña: " + contraseña + " - " + (contraseñaYusuarioOnline ? "Correcta" : "Incorrecta"));

            //Enviamos confirmacion de contraseña
            enviarBoolean(contraseñaYusuarioOnline);
            if (contraseñaYusuarioOnline) {
                //Añadimos el usuario conectado a la lista y verificamos que no este conectado ya
                contraseñaYusuarioOnline = agregarUsuario(misDatos.obtenerIdJugador(), usuario);
                System.out.println("--> " + misDatos.obtenerIdJugador() + ", " + usuario);
                //Enviamos confirmacion de usuario conectado
                enviarBoolean(contraseñaYusuarioOnline);
                if (contraseñaYusuarioOnline) {
                    System.out.println("--> Correcto!");
                    // Mandamos la lista de los usuarios
                    enviarObjeto(listaUsuarios);
                    //Mandamos el Objeto con los datos necesarios al Jugador
                    enviarObjeto(misDatos);
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(ConexionServidor.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("--> Error en Login: " + ex.getMessage());
        }
        return contraseñaYusuarioOnline;
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

    private void recibirCoordenadas() {
        try {
            int id_jugador = flujo_entrada.readInt();
            int id_partida = flujo_entrada.readInt();
            int coordenada_x = flujo_entrada.readInt();
            int coordenada_y = flujo_entrada.readInt();
            misDatos.disparar(id_partida, id_jugador, coordenada_x, coordenada_y);
           boolean resultado = misDatos.hayBarco(id_partida, id_jugador, coordenada_x, coordenada_y);
            enviarBoolean(resultado);
        } catch (IOException ex) {
            System.out.println("--> Error al recibir Coordenadas: " + ex.getMessage());
        }
    }

    private void enviarListaTerminada() {
        enviarObjeto(misDatos.getListaPartidaTermindas());
    }

    private void enviarListaSinTerminadaSuTurno() {
        enviarObjeto(misDatos.getListaPartidasSuTurno());
    }

    private void enviarListaSinTerminadaMiTurno() {
        enviarObjeto(misDatos.getListaPartidasMiTurno());
    }

    private void crearPartida() {
        try {
            int id_jugador1 = flujo_entrada.readInt();
            int id_jugador2 = flujo_entrada.readInt();
            flujo_salida.writeInt(misDatos.crearPartida(id_jugador1, id_jugador2));
            System.out.println("--> Partida creada");
        } catch (IOException ex) {
            Logger.getLogger(ConexionServidor.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("--> Error al crear Partida: " + ex.getMessage());
        }
    }

    private void enviarTableroConBarcos() {
        
    }

    // Método para enviar un Booleano por socket
    private void enviarBoolean(boolean valor) {
        System.out.println(valor ? "---> Verdadero" : "---> Falso");
        try {
            flujo_salida.writeBoolean(valor);
            flujo_salida.flush();
        } catch (IOException ex) {
            System.out.println("--> Error al enviar booleano: " + ex.getMessage());
        }
    }

    // Método para enviar un objeto por socket
    public void enviarObjeto(Object objeto) {
        try {
            objeto_salida.writeObject(objeto);
            objeto_salida.flush();
        } catch (IOException e) {
            System.err.println("--> Error al enviar el objeto por el socket: " + e.getMessage());
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
            System.out.println("- Uno menos: " + id);
        } else {
            System.out.println("--> Error: Usuario no encontrado.");
        }
    }

    public String getNombreUsuario(int id) {
        return usuariosConectados.get(id);
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

}
