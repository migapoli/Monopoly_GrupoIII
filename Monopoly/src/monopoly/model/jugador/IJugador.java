package monopoly.model.jugador;

import monopoly.model.casilla.Propiedad;
import java.awt.Color;
import java.util.List;

public interface IJugador {
    String getNombre();
    int getDinero();
    void setDinero(int dinero);
    void sumarDinero(int cantidad);
    void restarDinero(int cantidad);
    int getPosicion();
    void setPosicion(int posicion);
    Color getColor();
    List<Propiedad> getPropiedades();
    void agregarPropiedad(Propiedad propiedad);
    void quitarPropiedad(Propiedad propiedad);
    boolean isEnCarcel();
    void setEnCarcel(boolean enCarcel);
    int getTurnosEnCarcel();
    void incrementarTurnosEnCarcel();
}
