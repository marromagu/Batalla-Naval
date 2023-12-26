/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.Scanner;

/**
 *
 * @author mario
 */
public class Jugador {

    private Tablero miTablero;

    public Jugador() {
        miTablero = new Tablero(10, 10);
    }

    public void verTablero() {
        // Imprime el tablero con números de filas
        for (int fila = 0; fila < miTablero.getFilas(); fila++) {
            System.out.print("F " + fila + "| "); // Imprime el número de fila
            for (int col = 0; col < miTablero.getColumnas(); col++) {
                System.out.print(miTablero.getMatriz()[fila][col] + " ");
            }
            System.out.println();
        }
        System.out.print("  C ");
        for (int col = 0; col < miTablero.getColumnas(); col++) {
            System.out.print(col + "|");// Imprime los números de columnas
        }
    }

    public boolean pedirBarco() {
        System.out.println("\n-Coloca el barco-");
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Introduce la fila del barco:");
            int fila = scanner.nextInt();

            System.out.println("Introduce la columna del barco:");
            int columna = scanner.nextInt();

            System.out.println("Introduce el tamaño del barco:");
            int tamaño = scanner.nextInt();
            System.out.println("Introduce la orientación del barco (V/H):");
            String orientacionInput = scanner.next();
            boolean orientacion = orientacionInput.equalsIgnoreCase("H");

            // Crear el objeto Barco con los datos proporcionados
            Barco miBarco = new Barco(fila, columna, tamaño, orientacion);
            return colocarBarco(miBarco);
        } catch (Exception e) {
            System.out.println("Datos erroneos.");
        }
        return false;
    }

    public boolean colocarBarco(Barco barco) {
        int fila = barco.getFila();
        int columna = barco.getColumna();
        int tamaño = barco.getTamaño();
        boolean orientacion = barco.isOrientacion();

        // Verifica si hay suficiente espacio para colocar el barco
        if (verificarEspacio(fila, columna, tamaño, orientacion)) {
            // Coloca el barco en el tablero
            for (int i = 0; i < tamaño; i++) {
                if (orientacion) {
                    miTablero.getMatriz()[fila][columna + i] = 'B'; // 'B' representa una parte del barco
                } else {
                    miTablero.getMatriz()[fila + i][columna] = 'B';
                }
            }
            return true; // Se colocó el barco exitosamente
        } else {
            System.out.println("No se ha podido colocar el barco.");
            return false; // No se pudo colocar el barco
        }
    }

    private boolean verificarEspacio(int fila, int columna, int tamaño, boolean orientacion) {
        // Verifica que el tamaño del barco sea mayor que 2 y menor que 7
        if (tamaño < 3 || tamaño > 6) {
            System.out.println("Error: El tamaño del barco debe ser mayor que 2 y menor que 7.");
            return false;
        }

        // Verifica si hay suficiente espacio para colocar el barco sin superponerse, sin salirse de los límites
        // y sin tocar otros barcos al principio o al final
        for (int i = -1; i <= tamaño; i++) {
            for (int j = -1; j <= 1; j++) {
                int filaActual = orientacion ? fila + i : fila;
                int columnaActual = orientacion ? columna : columna + i;

                if (filaActual >= 0 && filaActual < miTablero.getFilas() && columnaActual >= 0 && columnaActual < miTablero.getColumnas()) {
                    if (miTablero.getMatriz()[filaActual][columnaActual] == 'B') {
                        System.out.println("Error: Superposición con otro barco.");
                        return false; // Hay superposición con otro barco
                    }
                    if (i == -1 && j == 0) {
                        if (miTablero.getMatriz()[filaActual][columnaActual] == 'B') {
                            System.out.println("Error: El barco toca otro barco al principio.");
                            return false;
                        }
                    }
                    if (i == tamaño && j == 0) {
                        if (miTablero.getMatriz()[filaActual][columnaActual] == 'B') {
                            System.out.println("Error: El barco toca otro barco al final.");
                            return false;
                        }
                    }
                }
            }
        }

        // Verifica si se sale de los límites del tablero
        if (orientacion) {
            if (fila < 0 || fila + tamaño - 1 >= miTablero.getFilas() || columna < 0 || columna >= miTablero.getColumnas()) {
                System.out.println("Error: El barco se sale de los límites del tablero en sentido vertical.");
                return false;
            }
        } else {
            if (fila < 0 || fila >= miTablero.getFilas() || columna < 0 || columna + tamaño - 1 >= miTablero.getColumnas()) {
                System.out.println("Error: El barco se sale de los límites del tablero en sentido horizontal.");
                return false;
            }
        }

        return true; // Hay suficiente espacio y no se sale de los límites
    }

    public void disparar(int fila, int columna) {
        // Verifica si las coordenadas están dentro de los límites del tablero
        if (fila < 0 || fila >= miTablero.getFilas() || columna < 0 || columna >= miTablero.getColumnas()) {
            System.out.println("Coordenadas fuera de los límites del tablero. Disparo invalido.");
            return;
        }
        // Verifica si ya se había disparado en estas coordenadas
        if (miTablero.getMatriz()[fila][columna] == 'X' || miTablero.getMatriz()[fila][columna] == 'O') {
            System.out.println("Ya has disparado en estas coordenadas. Elige otras.");
            return;
        }
        // Marca la casilla como disparada
        char resultadoDisparo = dispararEnCoordenadas(fila, columna);
        // Actualiza el tablero con el resultado del disparo
        miTablero.getMatriz()[fila][columna] = 'X';
        // Imprime el tablero después del disparo
        verTablero();
        // Verifica si se ha hundido algún barco
        if (resultadoDisparo == 'H') {
            System.out.println("¡Barco hundido!");
        } else if (resultadoDisparo == 'T') {
            System.out.println("¡Tocado!");
        } else {
            System.out.println("Agua. El disparo no alcanzó ningún barco.");
        }
    }

    private char dispararEnCoordenadas(int fila, int columna) {
        // Verifica si hay un barco en las coordenadas del disparo
        if (miTablero.getMatriz()[fila][columna] == 'B') {
            // Marcamos la parte del barco como tocada
            miTablero.getMatriz()[fila][columna] = 'T';

            // Verificamos si el barco ha sido completamente tocado
            if (barcoCompletamenteTocado(fila, columna)) {
                // Marcamos todas las partes del barco como hundidas
                marcarBarcoHundido(fila, columna);
                return 'H';  // Barco hundido
            } else {
                return 'T';  // Barco tocado
            }
        } else {
            return 'O';  // Agua
        }
    }

    private boolean barcoCompletamenteTocado(int fila, int columna) {
        char[][] matriz = miTablero.getMatriz();
        char marca = matriz[fila][columna];

        // Verifica horizontalmente
        for (int i = 0; i < matriz.length; i++) {
            if (matriz[i][columna] == 'B' && matriz[i][columna] != marca) {
                return false;
            }
        }

        // Verifica verticalmente
        for (int j = 0; j < matriz[0].length; j++) {
            if (matriz[fila][j] == 'B' && matriz[fila][j] != marca) {
                return false;
            }
        }

        return true;
    }

    private void marcarBarcoHundido(int fila, int columna) {
        char[][] matriz = miTablero.getMatriz();

        // Marcar horizontalmente
        for (int i = 0; i < matriz.length; i++) {
            if (matriz[i][columna] == 'T') {
                matriz[i][columna] = 'H';
            }
        }

        // Marcar verticalmente
        for (int j = 0; j < matriz[0].length; j++) {
            if (matriz[fila][j] == 'T') {
                matriz[fila][j] = 'H';
            }
        }
    }
}
