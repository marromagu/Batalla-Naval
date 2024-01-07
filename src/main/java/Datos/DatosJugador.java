package Datos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author mario
 */
public class DatosJugador implements Serializable {

    private static final long serialVersionUID = 1L;

    private final ConexionConBDD miBDD;
    private int idJugador;
    private final String usuario;
    private final int contraseña;
    private HashMap<Integer, String> listaPartidaTermindas;
    private HashMap<Integer, String> listaPartidasMiTurno;
    private HashMap<Integer, String> listaPartidasSuTurno;

    public DatosJugador(String nombre, int contraseña) {
        miBDD = new ConexionConBDD();
        this.usuario = nombre;
        this.contraseña = contraseña;
        this.idJugador = miBDD.consultarIDJugador(nombre, contraseña);
        this.listaPartidaTermindas = miBDD.obtenerPartidasTerminadasPorJugador(idJugador);
        this.listaPartidasMiTurno = miBDD.obtenerPartidasNoTerminadasConTurno(idJugador);
        this.listaPartidasSuTurno = miBDD.obtenerPartidasNoTerminadasSinTurno(idJugador);
    }

    public void disparar(int idPartida, int idJugador, int posicionX, int posicionY){
        miBDD.registrarDisparo(idPartida, idJugador, posicionX, posicionY);
    }
    public boolean rendirse(int idJugador, int id_partida) {
        return miBDD.rendirseEnPartida(idJugador, id_partida);
    }

    public ArrayList<String> repeticion(int id_partida) {
        return miBDD.obtenerDisparosDePartida(id_partida);
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

    public HashMap<Integer, String> getListaPartidaTermindas() {
        return listaPartidaTermindas;
    }

    public void setListaPartidaTermindas(HashMap<Integer, String> listaPartidaTermindas) {
        this.listaPartidaTermindas = listaPartidaTermindas;
    }

    public HashMap<Integer, String> getListaPartidasMiTurno() {
        return listaPartidasMiTurno;
    }

    public void setListaPartidasMiTurno(HashMap<Integer, String> listaPartidasMiTurno) {
        this.listaPartidasMiTurno = listaPartidasMiTurno;
    }

    public HashMap<Integer, String> getListaPartidasSuTurno() {
        return listaPartidasSuTurno;
    }

    public void setListaPartidasSuTurno(HashMap<Integer, String> listaPartidasSuTurno) {
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
