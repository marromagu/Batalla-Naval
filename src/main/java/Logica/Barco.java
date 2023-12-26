/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

/**
 *
 * @author DAM_M
 */
class Barco {

    private int fila;
    private int columna;
    private int tamaño;
    private boolean orientacion; // true para horizontal, false para vertical

    public Barco(int fila, int columna, int tamaño, boolean orientacion) {
        this.fila = fila;
        this.columna = columna;
        this.tamaño = tamaño;
        this.orientacion = orientacion;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public int getTamaño() {
        return tamaño;
    }

    public void setTamaño(int tamaño) {
        this.tamaño = tamaño;
    }

    public boolean isOrientacion() {
        return orientacion;
    }

    public void setOrientacion(boolean orientacion) {
        this.orientacion = orientacion;
    }

}
