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
// Clase Tablero que representa el tablero de juego
class Tablero {

    private int filas;
    private int columnas;
    private char[][] matrizTablero;

    public Tablero(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.matrizTablero = new char[filas][columnas];
        // Inicializa el tablero con espacios en blanco
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

    public void verTablero() {
        // Imprime el tablero con números de filas
        for (int fila = 0; fila < filas; fila++) {
            System.out.print("F " + fila + "| "); // Imprime el número de fila
            for (int col = 0; col < columnas; col++) {
                System.out.print(matrizTablero[fila][col] + " ");
            }
            System.out.println();
        }
        System.out.print("  C  ");
        for (int col = 0; col < columnas; col++) {
            System.out.print(col + "|");// Imprime los números de columnas
        }
        System.out.println("");
    }

}
