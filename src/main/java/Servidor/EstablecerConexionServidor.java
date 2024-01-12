
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
public class EstablecerConexionServidor extends Thread {

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
    public EstablecerConexionServidor(Socket skCliente) {
        this.skCliente = skCliente;

    }

    public void EstablecerConexcion() {
        try {
            ServerSocket skServidor = new ServerSocket(Puerto); // Inicializamos el servidor en el puerto
            System.out.println("-> Puerto: " + Puerto + " en escucha.");
            usuariosConectados = new HashMap<>();
            // listaUsuarios = new HashMap<>(); // Mueve la inicialización aquí fuera del bucle
            while (true) {
                Socket skCliente = skServidor.accept(); // Se conecta un Cliente.
                System.out.println("+ Cliente conectado.");
                
                new ConexionServidor(skCliente,usuariosConectados).start(); // Atendemos al Cliente con un Thread
            }
        } catch (IOException e) {
            System.out.println("-> Ups, ha ocurrido algo inesperado: " + e.getMessage());
        }
    }
}