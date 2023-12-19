/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package App;

import Servidor.ConexionServidor;
import java.net.Socket;
/**
 *
 * @author DAM_M
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Socket miSocket = null;
        ConexionServidor miServidor = new ConexionServidor(miSocket);
        miServidor.EstablecerConexcion();
    }
    
}
