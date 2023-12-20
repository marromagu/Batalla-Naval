/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.Scanner;

/**
 *
 * @author DAM_M
 */
// Clase Juego que maneja la lógica del juego
public class Juego {

    private Tablero tablero;
    private Barco barco;

    public Juego(int tamano) {
        this.tablero = new Tablero(tamano);
        this.barco = new Barco("Barco1", 3); // Puedes cambiar el nombre y tamaño del barco según tus necesidades
    }

    public void jugar() {
        Scanner scanner = new Scanner(System.in);

        // Colocar barco en el tablero
        System.out.println("Coloca tu barco en el tablero.");
        colocarBarcoEnTablero();

        // Mostrar el tablero inicial
        tablero.imprimirTablero();

        // Iniciar el juego
        while (!juegoTerminado()) {
            // Obtener coordenadas del usuario
            System.out.print("Ingresa las coordenadas (fila y columna) separadas por espacio: ");
            int fila = scanner.nextInt();
            int columna = scanner.nextInt();

            // Verificar si las coordenadas son válidas
            if (tablero.validarCoordenadas(fila, columna)) {
                // Realizar un disparo
                if (!realizarDisparo(fila, columna)) {
                    System.out.println("¡Agua!");
                }

                // Mostrar el tablero después del disparo
                tablero.imprimirTablero();
            } else {
                System.out.println("Coordenadas inválidas. Intenta de nuevo.");
            }
        }

        scanner.close();
    }

    private void colocarBarcoEnTablero() {
        Scanner scanner = new Scanner(System.in);

        // Obtener coordenadas para el barco
        int[] coordenadas = new int[barco.getTamano()];
        System.out.println("Ingresa las coordenadas para el barco (0-9): ");
        for (int i = 0; i < barco.getTamano(); i++) {
            System.out.print("Coordenada " + (i + 1) + ": ");
            coordenadas[i] = scanner.nextInt();
        }

        // Verificar si las coordenadas son válidas y no ocupadas
        boolean coordenadasValidas = true;
        for (int coord : coordenadas) {
            int fila = coord / 10;
            int columna = coord % 10;
            if (!tablero.validarCoordenadas(fila, columna) || tablero.ocupado(fila, columna)) {
                coordenadasValidas = false;
                break;
            }
        }

        // Colocar el barco en el tablero si las coordenadas son válidas
        if (coordenadasValidas) {
            barco.setCoordenadas(coordenadas);
            tablero.colocarBarco(barco);
        } else {
            System.out.println("Coordenadas inválidas. Reinicia el proceso de colocación del barco.");
            colocarBarcoEnTablero();
        }
    }

    private boolean realizarDisparo(int fila, int columna) {
        int[] coordenadasBarco = barco.getCoordenadas();
        for (int coord : coordenadasBarco) {
            if (coord == fila * 10 + columna) {
                System.out.println("¡Impacto!");
                return true;
            }
        }
        return false;
    }

    private boolean juegoTerminado() {
        int[] coordenadasBarco = barco.getCoordenadas();
        for (int coord : coordenadasBarco) {
            int fila = coord / 10;
            int columna = coord % 10;
            if (tablero.ocupado(fila, columna)) {
                return false;
            }
        }
        System.out.println("¡Felicidades! Hundiste la flota.");
        return true;
    }
}
