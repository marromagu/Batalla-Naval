
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import Logica.Juego;
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
    private ObjectOutputStream output;
    Juego miJuego = new Juego();

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
            System.out.println("-> Ups, ha ocurrido algo inesperado...");
        }
    }

    @Override
    public void run() {
        try {
            flujo_salida = new DataOutputStream(skCliente.getOutputStream());
            flujo_entrada = new DataInputStream(skCliente.getInputStream());
            output = new ObjectOutputStream(skCliente.getOutputStream());
            boolean loggedIn = false;

            while (!loggedIn) {
                loggedIn = login();
            }

        } catch (IOException e) {
            System.out.println("--> Error en run: " + e.getMessage());
        }
    }

    private boolean login() {
        boolean validarContraseña = false;
        try {
            String usuario = flujo_entrada.readUTF();
            String contraseña = flujo_entrada.readUTF();

            validarContraseña = miJuego.validarContraseña(usuario, contraseña);
            System.out.println("Usuario: " + usuario + " Contraseña: " + contraseña + " " + validarContraseña);
            flujo_salida.writeBoolean(validarContraseña);
            if (validarContraseña) {
                flujo_salida.writeInt(miJuego.obtenerIdJugador(usuario, contraseña));
            }
        } catch (IOException ex) {
            Logger.getLogger(ConexionServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return validarContraseña;

    }

    private void cerrarConexiones() {
        try {
            // Cierra la conexión del cliente y otros recursos
            if (skCliente != null && !skCliente.isClosed()) {
                skCliente.close();
                System.out.println("Conexión del Servidor cerrada.");
            }

            // Cierra flujos de entrada y salida
            if (flujo_entrada != null) {
                flujo_entrada.close();
            }
            if (flujo_salida != null) {
                flujo_salida.close();
            }
        } catch (IOException e) {
            System.out.println("Error al cerrar conexiones: " + e.getMessage());
        }
    }
}
