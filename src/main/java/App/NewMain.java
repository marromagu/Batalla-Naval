/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package App;

import Datos.ConexionConBDD;
import Servidor.ConexionServidor;
import java.net.Socket;
import Logica.Juego;

/**
 *
 * @author DAM_M
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Juego miJuego = new Juego();
        miJuego.menuOp();
    }

}
