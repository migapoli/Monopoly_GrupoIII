package monopoly.model.jugador;

public interface IJugador {
    String getNombre();
    int getPosicion();
    int getDinero();
    void mover(int casillas);
    void cobrar(int cantidad);
    void pagar(int cantidad);
}
