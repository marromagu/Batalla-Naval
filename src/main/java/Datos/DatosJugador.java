/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author mario
 */
public class DatosJugador implements Serializable {

    private static final long serialVersionUID = 1L;

    private ConexionConBDD miBDD;
    private int idJugador;
    private String usuario;
    private int contraseña;
    private ArrayList<String> listaPartidaTermindas;
    private ArrayList<String> listaPartidasMiTurno;
    private ArrayList<String> listaPartidasSuTurno;

    public DatosJugador(String nombre, int contraseña) {
        miBDD = new ConexionConBDD();
        this.usuario = nombre;
        this.contraseña = contraseña;
        this.idJugador = miBDD.consultarIDJugador(nombre, contraseña);
        this.listaPartidaTermindas = miBDD.obtenerPartidasTerminadasPorJugador(idJugador);
        this.listaPartidasMiTurno = miBDD.obtenerPartidasNoTerminadasConTurno(idJugador);
        this.listaPartidasSuTurno = miBDD.obtenerPartidasNoTerminadasSinTurno(idJugador);
    }

    // Método para obtener la Id del jugador
    public int obtenerIdJugador() {
        return miBDD.consultarIDJugador(usuario, contraseña);
    }

    // Método para validar la contraseña del usuario
    public boolean validarContraseña() {
        int contraseñaGuardada = miBDD.consultarContraseña(usuario);
        return contraseñaGuardada == contraseña;
    }

    public int getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(int idJugador) {
        this.idJugador = idJugador;
    }

    public ArrayList<String> getListaPartidaTermindas() {
        return listaPartidaTermindas;
    }

    public void setListaPartidaTermindas(ArrayList<String> listaPartidaTermindas) {
        this.listaPartidaTermindas = listaPartidaTermindas;
    }

    public ArrayList<String> getListaPartidasMiTurno() {
        return listaPartidasMiTurno;
    }

    public void setListaPartidasMiTurno(ArrayList<String> listaPartidasMiTurno) {
        this.listaPartidasMiTurno = listaPartidasMiTurno;
    }

    public ArrayList<String> getListaPartidasSuTurno() {
        return listaPartidasSuTurno;
    }

    public void setListaPartidasSuTurno(ArrayList<String> listaPartidasSuTurno) {
        this.listaPartidasSuTurno = listaPartidasSuTurno;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nDatosJugador{");
        sb.append("miBDD=").append(miBDD);
        sb.append(", idJugador=").append(idJugador);
        sb.append(", usuario=").append(usuario);
        sb.append(", contrase\u00f1a=").append(contraseña);
        sb.append("\n listaPartidaTermindas=").append(listaPartidaTermindas);
        sb.append("\n listaPartidasMiTurno=").append(listaPartidasMiTurno);
        sb.append("\n listaPartidasSuTurno=").append(listaPartidasSuTurno);
        sb.append('}');
        return sb.toString();
    }

}
