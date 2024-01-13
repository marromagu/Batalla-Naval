/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.Arrays;

/**
 *
 * @author DAM_M
 */
class Tablero {

    private int filas;
    private int columnas;
    private char[][] matrizTablero;

    public Tablero(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.matrizTablero = new char[filas][columnas];
        // Inicializa el tablero con '-'.
        for (int i = 0; i < filas; i++) {
            Arrays.fill(matrizTablero[i], '-');
        }
    }

    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    public char[][] getMatriz() {
        return matrizTablero;
    }

    public void setMatriz(char[][] matriz) {
        this.matrizTablero = matriz;
    }

    public String verTablero() {
        StringBuilder tableroString = new StringBuilder();

        // Imprime el tablero con números de filas
        for (int fila = 0; fila < filas; fila++) {
            tableroString.append("F ").append(fila).append("| "); // Imprime el número de fila
            for (int col = 0; col < columnas; col++) {
                tableroString.append(matrizTablero[fila][col]).append(" ");
            }
            tableroString.append("\n");
        }
        tableroString.append("  C  ");
        for (int col = 0; col < columnas; col++) {
            tableroString.append(col).append("|");// Imprime los números de columnas
        }
        tableroString.append("\n");

        return tableroString.toString();
    }

}
