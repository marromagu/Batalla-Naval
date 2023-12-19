/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import java.io.*;
import java.net.*;

/**
 *
 * @author DAM_M
 */
public class ConexionServidor extends Thread {

    Socket skCliente; //Instanciamos el Socket del Cliente.
    static final int Puerto = 2001; //Creamos una constante etatica del puerto por donde se conctara el Cliente.

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
            System.out.println("->Ups, ha ocurrido algo inesperado...");
        }
    }

    @Override
    public void run() {
        try {
            DataOutputStream flujo_salida = new DataOutputStream(skCliente.getOutputStream());
            flujo_salida.writeUTF("-> Se ha conectado correctamente"); //Atendemos la peticion del cliente

            skCliente.close(); //Cerramos conexion
            System.out.println("- Cliente desconectado.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
