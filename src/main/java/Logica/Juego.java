package Logica;

import java.util.Scanner;
import Datos.ConexionConBDD;

public class Juego {

    private final ConexionConBDD miCone;
    private final Jugador miJugador1;
    private final Jugador miJugador2;

    public Juego() {
        miCone = new ConexionConBDD();
        miJugador1 = new Jugador();
        miJugador2 = new Jugador();
    }

    // Menú de inicio del juego
    public void menuInicio() {
        System.out.println("\n--------------------------");
        System.out.println("| 1.- Login.              |");
        System.out.println("| 0.- Salir.              |");
        System.out.println("--------------------------");
    }

    // Menú de opciones de partida
    public void menuPartida() {
        System.out.println("\n--------------------------");
        System.out.println("| 1.- Buscar partida.     |");
        System.out.println("| 2.- Cargar partida.     |");
        System.out.println("| 3.- Borrar partida.     |");
        System.out.println("| 0.- Salir.              |");
        System.out.println("--------------------------");
    }

    // Menú de opciones durante el juego
    private void menuJuego() {
        System.out.println("\n--------------------------");
        System.out.println("| 1.- Poner Barcos.       |");
        System.out.println("| 2.- Ver Tablero.        |");
        System.out.println("| 3.- Disparar Barcos.    |");
        System.out.println("| 4.- Cambiar Jugador.    |");
        System.out.println("| 0.- Para salir.         |");
        System.out.println("--------------------------");
    }

    // Método para gestionar las opciones de inicio
    public void opcionesInicio() {
        try (Scanner sc = new Scanner(System.in)) {
            int op;
            do {
                menuInicio();
                op = sc.nextInt();
                switch (op) {
                    case 1 -> {
                        if (pedirContraseña()) {
                            opcionesPartida();
                        }
                    }
                }
            } while (op != 0);
        } catch (Exception e) {
            System.out.println("Error de entrada: " + e.getMessage());
        }
    }

    // Método para gestionar las opciones de partida
    public void opcionesPartida() {
        try (Scanner sc = new Scanner(System.in)) {
            int op;
            do {
                menuPartida();
                op = sc.nextInt();
                switch (op) {
                    case 1 ->
                        buscarPartida();
                    case 2 ->
                        cargarPartida();
                    case 3 ->
                        borrarPartida();
                }
            } while (op != 0);
        } catch (Exception e) {
            System.out.println("Error de entrada: " + e.getMessage());
        }
    }

    // Método para gestionar las opciones durante el juego
    public void opcionesJuego() {
        try (Scanner sc = new Scanner(System.in)) {
            Jugador j = cambiarJugador(1);
            int op;
            do {
                menuJuego();
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
                }
            } while (op != 0);
        } catch (Exception e) {
            System.out.println("Error de entrada: " + e.getMessage());
        }
    }

    // Método para pedir y validar la contraseña del usuario
    private boolean pedirContraseña() {
        try (Scanner leer = new Scanner(System.in)) {
            System.out.println("Usuario:");
            String nombreUsuario = leer.nextLine();
            System.out.println("Contraseña:");
            String contraseña = leer.nextLine();

            if (!validarContraseña(nombreUsuario, contraseña)) {
                System.out.println("Contraseña o Usuario incorrectos.");
                return false;
            } else {
                System.out.println("Bienvenido.");
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error de entrada: " + e.getMessage());
            return false;
        }
    }

    // Método para validar la contraseña del usuario
    private boolean validarContraseña(String nombreUsuario, String contraseña) {
        String contraseñaGuardada = miCone.consultarContraseña(nombreUsuario);
        return contraseñaGuardada != null && contraseñaGuardada.equals(contraseña);
    }

    // Métodos para implementar en el futuro (buscar, cargar y borrar partida)
    private void buscarPartida() {
        System.out.println("Método para buscar partida");
    }

    private void cargarPartida() {
        System.out.println("Método para cargar partida");
    }

    private void borrarPartida() {
        System.out.println("Método para borrar partida");
    }

    // Método para cambiar el jugador activo
    private Jugador cambiarJugador(int o) {
        return switch (o) {
            case 1 ->
                miJugador1;
            case 2 ->
                miJugador2;
            default ->
                null;
        };
    }

    // Método para colocar barcos en el tablero
    private void ponerBarcos(Jugador j) {
        System.out.println("Jugador: " + j);
        int c = 0;
        while (c < 2) {
            if (j.pedirBarco()) {
                j.verTablero();
                c++;
            }
        }
    }

    // Método para realizar un disparo en el tablero del oponente
    private void dispararBarco(Jugador j, int c, int f) {
        if (j == miJugador1) {
            j.disparar(f, c, miJugador2.getMiTablero());
        } else {
            j.disparar(f, c, miJugador1.getMiTablero());
        }
    }

    // Método para mostrar el tablero del jugador
    private void verTablero(Jugador j) {
        j.verTablero();
    }
}
