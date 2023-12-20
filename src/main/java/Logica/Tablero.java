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

    private char[][] casillas;
    private int tamano;

    public Tablero(int tamano) {
        this.tamano = tamano;
        this.casillas = new char[tamano][tamano];
        for (char[] fila : casillas) {
            Arrays.fill(fila, ' '); // Inicializar todas las casillas con espacios en blanco
        }
    }

    public void imprimirTablero() {
        System.out.println("  0 1 2 3 4 5 6 7 8 9");
        for (int i = 0; i < tamano; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < tamano; j++) {
                System.out.print(casillas[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean validarCoordenadas(int fila, int columna) {
        return fila >= 0 && fila < tamano && columna >= 0 && columna < tamano;
    }

    public boolean ocupado(int fila, int columna) {
        return casillas[fila][columna] != ' ';
    }

    public void colocarBarco(Barco barco) {
        int[] coordenadas = barco.getCoordenadas();
        for (int i = 0; i < barco.getTamano(); i++) {
            casillas[coordenadas[i] / 10][coordenadas[i] % 10] = 'O';
        }
    }
}
