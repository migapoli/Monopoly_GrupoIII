package monopoly.model.casilla;

import monopoly.model.casilla.Casilla.TipoCasilla;

public interface ICasilla {
    String getNombre();
    int getPosicion();
    TipoCasilla getTipo();
    void ejecutarAccion();
}
