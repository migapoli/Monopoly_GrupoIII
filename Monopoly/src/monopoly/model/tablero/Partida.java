package monopoly.model.tablero;

import monopoly.model.jugador.Dado;
import monopoly.model.jugador.IJugador;
import java.util.ArrayList;
import java.util.List;

public class Partida {
    private Tablero tablero;
    private List<IJugador> jugadores;
    private int turnoActual;
    private Dado dado;
    private List<String> historial;
    private boolean juegoTerminado;
    
    public Partida() {
        this.tablero = new Tablero();
        this.jugadores = new ArrayList<>();
        this.turnoActual = 0;
        this.dado = new Dado();
        this.historial = new ArrayList<>();
        this.juegoTerminado = false;
    }
    
    public void agregarJugador(IJugador jugador) {
        if (jugadores.size() < 6) {
            jugadores.add(jugador);
            agregarHistorial("Jugador " + jugador.getNombre() + " se ha unido a la partida");
        }
    }
    
    public Tablero getTablero() {
        return tablero;
    }
    
    public List<IJugador> getJugadores() {
        return new ArrayList<>(jugadores);
    }
    
    public IJugador getJugadorActual() {
        if (jugadores.isEmpty()) {
            return null;
        }
        return jugadores.get(turnoActual);
    }
    
    public int getTurnoActual() {
        return turnoActual;
    }
    
    public void siguienteTurno() {
        turnoActual = (turnoActual + 1) % jugadores.size();
    }
    
    public Dado getDado() {
        return dado;
    }
    
    public List<String> getHistorial() {
        return new ArrayList<>(historial);
    }
    
    public void agregarHistorial(String mensaje) {
        historial.add(mensaje);
    }
    
    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }
    
    public void setJuegoTerminado(boolean juegoTerminado) {
        this.juegoTerminado = juegoTerminado;
    }
}
