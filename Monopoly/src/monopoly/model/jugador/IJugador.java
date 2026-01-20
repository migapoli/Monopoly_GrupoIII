package monopoly.model.jugador;

import monopoly.model.casilla.Propiedad;
import java.awt.Color;
import java.util.List;

/**
 * Interface que define el contrato para un jugador de Monopoly.
 * Establece los métodos esenciales para gestionar el estado del jugador.
 */
public interface IJugador {
    // Información básica
    String getNombre();

    Color getColor();

    // Gestión de dinero
    int getDinero();

    void setDinero(int dinero);

    void sumarDinero(int cantidad);

    void restarDinero(int cantidad);

    boolean puedePermitirse(int cantidad);

    boolean estaBancarrota();

    // Gestión de posición y movimiento
    int getPosicion();

    void setPosicion(int posicion);

    void mover(int casillas, int totalCasillasTablero);

    boolean pasoPorSalida();

    // Gestión de propiedades
    List<Propiedad> getPropiedades();

    void agregarPropiedad(Propiedad propiedad);

    void quitarPropiedad(Propiedad propiedad);

    // Gestión de cárcel
    boolean isEnCarcel();

    void setEnCarcel(boolean enCarcel);

    int getTurnosEnCarcel();

    void incrementarTurnosEnCarcel();

    void resetearTurnosEnCarcel();
}
