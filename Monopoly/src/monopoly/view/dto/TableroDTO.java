package monopoly.view.dto;

import java.util.List;

/**
 * Data Transfer Object para el estado completo del Tablero.
 * Contiene toda la información que la Vista necesita para renderizar
 * el estado actual del juego SIN acceder al Modelo directamente.
 */
public class TableroDTO {
    private final List<CasillaDTO> casillas;
    private final List<JugadorDTO> jugadores;
    private final String nombreJugadorActual;
    private final int turnoNumero;
    private final int fondoComun;
    
    public TableroDTO(List<CasillaDTO> casillas, List<JugadorDTO> jugadores, 
                      String nombreJugadorActual, int turnoNumero, int fondoComun) {
        this.casillas = casillas;
        this.jugadores = jugadores;
        this.nombreJugadorActual = nombreJugadorActual;
        this.turnoNumero = turnoNumero;
        this.fondoComun = fondoComun;
    }
    
    // Getters
    public List<CasillaDTO> getCasillas() { return casillas; }
    public List<JugadorDTO> getJugadores() { return jugadores; }
    public String getNombreJugadorActual() { return nombreJugadorActual; }
    public int getTurnoNumero() { return turnoNumero; }
    public int getFondoComun() { return fondoComun; }
    
    public CasillaDTO getCasilla(int posicion) {
        return casillas.get(posicion);
    }
    
    public JugadorDTO getJugadorActual() {
        return jugadores.stream()
            .filter(j -> j.getNombre().equals(nombreJugadorActual))
            .findFirst()
            .orElse(null);
    }
}
