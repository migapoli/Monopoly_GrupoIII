package monopoly.model.tablero;

import monopoly.model.cartas.MazoSuerte;
import monopoly.model.jugador.Dado;
import monopoly.model.jugador.IJugador;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Representa una partida de Monopoly.
 * Gestiona los jugadores, turnos, tablero e historial del juego.
 */
public class Partida {
    private Tablero tablero;
    private List<IJugador> jugadores;
    private List<IJugador> jugadoresEliminados;
    private int turnoActual;
    private Dado dado;
    private MazoSuerte mazoSuerte;
    private List<String> historial;
    private boolean juegoTerminado;
    private IJugador ganador;

    // Constantes del juego
    private static final int DINERO_SALIDA = 200;
    private static final int MIN_JUGADORES = 2;
    private static final int MAX_JUGADORES = 4;

    public Partida() {
        this.tablero = new Tablero();
        this.jugadores = new ArrayList<>();
        this.jugadoresEliminados = new ArrayList<>();
        this.turnoActual = 0;
        this.dado = new Dado();
        this.mazoSuerte = new MazoSuerte();
        this.historial = new ArrayList<>();
        this.juegoTerminado = false;
        this.ganador = null;
    }

    // ===== GESTIÓN DE JUGADORES =====

    /**
     * Agrega un jugador a la partida.
     * 
     * @param jugador El jugador a agregar
     * @return true si se agregó correctamente
     */
    public boolean agregarJugador(IJugador jugador) {
        if (jugadores.size() < MAX_JUGADORES) {
            jugadores.add(jugador);
            agregarHistorial("Jugador " + jugador.getNombre() + " se ha unido a la partida");
            return true;
        }
        return false;
    }

    /**
     * Elimina un jugador de la partida (bancarrota).
     * 
     * @param jugador El jugador a eliminar
     */
    public void eliminarJugador(IJugador jugador) {
        if (jugadores.contains(jugador)) {
            jugadores.remove(jugador);
            jugadoresEliminados.add(jugador);

            // Liberar las propiedades del jugador
            for (var propiedad : jugador.getPropiedades()) {
                propiedad.setPropietario(null);
            }

            agregarHistorial(jugador.getNombre() + " ha sido eliminado por bancarrota!");

            // Verificar condición de victoria
            verificarVictoria();

            // Ajustar turno si es necesario
            if (turnoActual >= jugadores.size() && !jugadores.isEmpty()) {
                turnoActual = 0;
            }
        }
    }

    /**
     * Verifica si un jugador está en bancarrota y lo elimina.
     * 
     * @param jugador El jugador a verificar
     * @return true si el jugador fue eliminado
     */
    public boolean verificarBancarrota(IJugador jugador) {
        if (jugador.estaBancarrota()) {
            eliminarJugador(jugador);
            return true;
        }
        return false;
    }

    // ===== CONDICIÓN DE VICTORIA =====

    /**
     * Verifica si hay un ganador (último jugador en pie).
     */
    public void verificarVictoria() {
        if (jugadores.size() == 1) {
            ganador = jugadores.get(0);
            juegoTerminado = true;
            agregarHistorial("🎉 " + ganador.getNombre() + " HA GANADO LA PARTIDA! 🎉");
        } else if (jugadores.isEmpty()) {
            juegoTerminado = true;
            agregarHistorial("Todos los jugadores han sido eliminados. ¡Fin del juego!");
        }
    }

    /**
     * Obtiene el ranking actual de jugadores por patrimonio.
     * 
     * @return Lista de jugadores ordenada por patrimonio (mayor a menor)
     */
    public List<IJugador> getRanking() {
        List<IJugador> todosLosJugadores = new ArrayList<>(jugadores);

        // Ordenar por patrimonio total (dinero + valor de propiedades)
        todosLosJugadores.sort((j1, j2) -> {
            int patrimonio1 = calcularPatrimonio(j1);
            int patrimonio2 = calcularPatrimonio(j2);
            return Integer.compare(patrimonio2, patrimonio1);
        });

        return todosLosJugadores;
    }

    /**
     * Calcula el patrimonio total de un jugador.
     */
    private int calcularPatrimonio(IJugador jugador) {
        int patrimonio = jugador.getDinero();
        for (var propiedad : jugador.getPropiedades()) {
            patrimonio += propiedad.getPrecio();
        }
        return patrimonio;
    }

    // ===== GESTIÓN DE TURNOS =====

    public Tablero getTablero() {
        return tablero;
    }

    public List<IJugador> getJugadores() {
        return new ArrayList<>(jugadores);
    }

    public List<IJugador> getJugadoresEliminados() {
        return new ArrayList<>(jugadoresEliminados);
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

    public void setTurnoActual(int turno) {
        this.turnoActual = turno;
    }

    /**
     * Avanza al siguiente turno.
     */
    public void siguienteTurno() {
        if (!jugadores.isEmpty()) {
            // Buscar el siguiente jugador que no esté eliminado
            do {
                turnoActual = (turnoActual + 1) % jugadores.size();
            } while (jugadoresEliminados.contains(jugadores.get(turnoActual)) && jugadores.size() > 1);

            IJugador siguiente = getJugadorActual();
            if (siguiente != null) {
                agregarHistorial("Turno de " + siguiente.getNombre());
            }
        }
    }

    public Dado getDado() {
        return dado;
    }

    public MazoSuerte getMazoSuerte() {
        return mazoSuerte;
    }

    // ===== HISTORIAL =====

    public List<String> getHistorial() {
        return new ArrayList<>(historial);
    }

    public void agregarHistorial(String mensaje) {
        historial.add(mensaje);
    }

    public void limpiarHistorial() {
        historial.clear();
    }

    // ===== ESTADO DEL JUEGO =====

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    public void setJuegoTerminado(boolean juegoTerminado) {
        this.juegoTerminado = juegoTerminado;
    }

    public IJugador getGanador() {
        return ganador;
    }

    public boolean puedeIniciar() {
        return jugadores.size() >= MIN_JUGADORES;
    }

    public int getNumeroJugadores() {
        return jugadores.size();
    }

    // ===== INFORMACIÓN DE LA PARTIDA =====

    /**
     * Obtiene un resumen del estado actual de la partida.
     */
    public String getResumenPartida() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ESTADO DE LA PARTIDA ===\n");
        sb.append("Jugadores activos: ").append(jugadores.size()).append("\n");
        sb.append("Jugadores eliminados: ").append(jugadoresEliminados.size()).append("\n");
        sb.append("Turno actual: ").append(turnoActual + 1).append("\n");

        if (getJugadorActual() != null) {
            sb.append("Jugador actual: ").append(getJugadorActual().getNombre()).append("\n");
        }

        sb.append("\n--- Patrimonio de jugadores ---\n");
        for (IJugador jugador : getRanking()) {
            sb.append(jugador.getNombre())
                    .append(": ").append(calcularPatrimonio(jugador))
                    .append(" EUR (Dinero: ").append(jugador.getDinero())
                    .append(", Propiedades: ").append(jugador.getPropiedades().size())
                    .append(")\n");
        }

        if (juegoTerminado && ganador != null) {
            sb.append("\n🏆 GANADOR: ").append(ganador.getNombre()).append(" 🏆\n");
        }

        return sb.toString();
    }
}
