package Logica;

public class Juego {

    private final Jugador miJugador1;
    private final Jugador miJugador2;

    public Juego() {
        miJugador1 = new Jugador();
        miJugador2 = new Jugador();
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
        while (c < 3) {
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
