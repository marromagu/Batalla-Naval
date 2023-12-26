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
public class Juego {

    private final Jugador miJugador1;
    private final Jugador miJugador2;

    public Juego() {
        miJugador1 = new Jugador();
        miJugador2 = new Jugador();
    }

    public void menuOp() {
        int op;
        Scanner sc = new Scanner(System.in);
        Jugador j = cambiarJugador(1);
        do {
            menu();
            op = sc.nextInt();
            switch (op) {
                case 1 ->
                    ponerBarcos(j);
                case 2 ->
                    verTablero(j);
                case 3 -> {
                    System.out.println("- Coordenadas del disparo.");
                    System.out.println("Columna: ");
                    int c = sc.nextInt();
                    System.out.println("Fila:");
                    int f = sc.nextInt();
                    dispararBarco(j, c, f);
                }
                case 4 -> {
                    System.out.println("Jugador 1 | Jugador 2");
                    int o = sc.nextInt();
                    j = cambiarJugador(o);
                }
                default ->
                    throw new AssertionError();
            }
        } while (op != 0);
    }

    private void menu() {
        System.out.println("\n--------------------------");
        System.out.println("| 1.- Poner Barcos.       |");
        System.out.println("| 2.- Ver Tablero.        |");
        System.out.println("| 3.- Disparar Barcos.    |");
        System.out.println("| 4.- Cambiar Jugador.    |");
        System.out.println("| 0.- Para salir.         |");
        System.out.println("--------------------------");
    }

    private Jugador cambiarJugador(int o) {
        switch (o) {
            case 1 -> {
                return miJugador1;
            }

            case 2 -> {
                return miJugador2;
            }

            default ->
                throw new AssertionError();
        }
    }

    private void ponerBarcos(Jugador j) {
        System.out.println("Jugardor: " + j);
        int c = 0;
        while (c < 2) {
            if (j.pedirBarco()) {
                j.verTablero();
                c++;
            }
        }
    }

    private void dispararBarco(Jugador j, int c, int f) {
        if (j == miJugador1) {
            j.disparar(f, c, miJugador2.getMiTablero());

        } else {
            j.disparar(f, c, miJugador1.getMiTablero());
        }
    }

    private void verTablero(Jugador j) {
        j.verTablero();
    }
}
