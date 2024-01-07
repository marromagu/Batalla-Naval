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

    private final Tablero miTablero;
    private final Tablero miTableroDeGuerra;

    public Jugador() {
        miTablero = new Tablero(10, 10);
        miTableroDeGuerra = new Tablero(10, 10);
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

    public void disparar(int fila, int columna, Tablero tuTablero) {
        // Verifica si las coordenadas están dentro de los límites del tablero
        if (fila < 0 || fila >= tuTablero.getFilas() || columna < 0 || columna >= tuTablero.getColumnas()) {
            System.out.println("Coordenadas fuera de los límites del tablero. Disparo invalido.");
            return;
        }
        // Verifica si ya se había disparado en estas coordenadas
        if (miTableroDeGuerra.getMatriz()[fila][columna] == 'X' || miTableroDeGuerra.getMatriz()[fila][columna] == 'O') {
            System.out.println("Ya has disparado en estas coordenadas.");
            return;
        }
        // Marca la casilla como disparada
        char resultadoDisparo = dispararEnCoordenadas(fila, columna, tuTablero);
        // Actualiza el tablero con el resultado del disparo
        tuTablero.getMatriz()[fila][columna] = 'X';
        miTableroDeGuerra.getMatriz()[fila][columna] = 'X';
        miTableroDeGuerra.verTablero();
        // Verifica si se ha hundido algún barco
        switch (resultadoDisparo) {
            case 'H' ->
                System.out.println("¡Barco hundido!");
            case 'T' ->
                System.out.println("¡Tocado!");
            default -> {
                tuTablero.getMatriz()[fila][columna] = 'O';
                miTableroDeGuerra.getMatriz()[fila][columna] = 'O';
                System.out.println("Agua.");

            }

        }
    }

    public Tablero getMiTablero() {
        return miTablero;
    }

    public void verTablero() {
        miTablero.verTablero();
    }

    private boolean colocarBarco(Barco barco) {
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

        for (int i = -1; i <= tamaño; i++) {
            for (int j = -1; j <= 1; j++) {
                int filaActual = orientacion ? fila + i : fila;
                int columnaActual = orientacion ? columna : columna + i;

                if (filaActual >= 0 && filaActual < miTablero.getFilas() && columnaActual >= 0 && columnaActual < miTablero.getColumnas()) {
                    if (miTablero.getMatriz()[filaActual][columnaActual] == 'B') {
                        System.out.println("Error: Superposición con otro barco.");
                        return false; // Hay superposición con otro barco
                    }
                    //TODO
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

        return true;
    }

    private char dispararEnCoordenadas(int fila, int columna, Tablero tuTablero) {
        // Verifica si hay un barco en las coordenadas del disparo
        if (tuTablero.getMatriz()[fila][columna] == 'B') {
            // Marcamos la parte del barco como tocada
            tuTablero.getMatriz()[fila][columna] = 'T';
            miTableroDeGuerra.getMatriz()[fila][columna] = 'T';

            // Verificamos si el barco ha sido completamente tocado
            if (barcoCompletamenteTocado(fila, columna, tuTablero)) {
                // Marcamos todas las partes del barco como hundidas
                marcarBarcoHundido(fila, columna, tuTablero);
                return 'H';  // Barco hundido
            } else {
                return 'T';  // Barco tocado
            }
        } else {
            return 'O';  // Agua
        }
    }

    private boolean barcoCompletamenteTocado(int fila, int columna, Tablero tuTablero) {
        char[][] matriz = tuTablero.getMatriz();
        char marca = matriz[fila][columna];
        // Verifica horizontalmente
        for (char[] matriz1 : matriz) {
            //for-loop
            if (matriz1[columna] == 'B' && matriz1[columna] != marca) {
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

    private void marcarBarcoHundido(int fila, int columna, Tablero tuTablero) {
        char[][] matriz = tuTablero.getMatriz();
        char[][] matrizGuerra = tuTablero.getMatriz();
        // Marcar horizontalmente
        for (int i = 0; i < matriz.length; i++) {
            if (matriz[i][columna] == 'T') {
                matriz[i][columna] = 'H';
                matrizGuerra[i][columna] = 'H';
            }
        }
        // Marcar verticalmente
        for (int j = 0; j < matriz[0].length; j++) {
            if (matriz[fila][j] == 'T') {
                matriz[fila][j] = 'H';
                matrizGuerra[fila][j] = 'H';
            }
        }
    }
}
