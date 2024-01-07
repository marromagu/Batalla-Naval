/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package App;

import Servidor.ConexionServidor;
import java.net.Socket;
import Datos.ConexionConBDD;

/**
 *
 * @author DAM_M
 */
public class Lanzador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Socket sk = new Socket();
        ConexionServidor server = new ConexionServidor(sk);
        server.EstablecerConexcion();
    }

}
