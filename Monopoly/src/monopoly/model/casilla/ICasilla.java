package monopoly.model.casilla;

import monopoly.model.jugador.IJugador;

public interface ICasilla {
    String getNombre();
    void ejecutarAccion(IJugador jugador);
}
