
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import Datos.DatosJugador;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DAM_M
 */
public class ConexionServidor extends Thread {

    static final int Puerto = 2001; //Creamos una constante etatica del puerto por donde se conctara el Cliente.
    private Socket skCliente; //Instanciamos el Socket del Cliente.
    private DataInputStream flujo_entrada;
    private DataOutputStream flujo_salida;
    private ObjectOutputStream objeto_salida;
    private DatosJugador misDatos = null;

    /**
     *
     * @param skCliente es un Objeto Socket que se proporciona al constructor.
     * Cada instancia del servidor tendra su propio socket.
     */
    public ConexionServidor(Socket skCliente) {
        this.skCliente = skCliente;
    }

    public void EstablecerConexcion() {
        try {
            ServerSocket skServidor = new ServerSocket(Puerto); //Inicializamos el servidor en el puerto
            System.out.println("-> Puerto: " + Puerto + " en escucha.");

            while (true) {
                Socket skCliente = skServidor.accept(); //Se conecta un Cliente.
                System.out.println("+ Cliente conectado.");
                new ConexionServidor(skCliente).start(); //Atendemos al Cliente con un Thread
            }
        } catch (Exception e) {
            System.out.println("-> Ups, ha ocurrido algo inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            flujo_salida = new DataOutputStream(skCliente.getOutputStream());
            flujo_entrada = new DataInputStream(skCliente.getInputStream());
            objeto_salida = new ObjectOutputStream(skCliente.getOutputStream());

            if (!login()) {
                cerrarConexiones();
                System.out.println("--> Conexion cerrada por usuario o contraseña erroneos.");
            }
            while (true) {                
                 enviarRepeticion();
            }
           
        } catch (IOException e) {
            System.out.println("--> Error en run: " + e.getMessage());
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

            // Validamos la contraseña y mostramos un mensaje por consola
            contraseñaCorrecta = misDatos.validarContraseña();
            System.out.println("Usuario: " + usuario + " Contraseña: " + contraseña + " - " + (contraseñaCorrecta ? "Correcta" : "Incorrecta"));

            if (contraseñaCorrecta) {
                System.out.println("¡Correcto!");
                // Mandamos el Objeto de los datos del Cliente
                enviarObjeto(misDatos);
                System.out.println(misDatos.toString());//Para verificar que todo este bien.
            }
        } catch (IOException ex) {
            Logger.getLogger(ConexionServidor.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("--> Error en Login: " + ex.getMessage());
        } 
        return contraseñaCorrecta;
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

    // Método para enviar un objeto por socket
    public void enviarObjeto(Object objeto) {
        try {
            objeto_salida.writeObject(objeto);
            objeto_salida.flush();
            System.out.println("Objeto enviado con éxito.");
        } catch (IOException e) {
            System.err.println("Error al enviar el objeto por el socket: " + e.getMessage());
            e.printStackTrace();
        }
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
