package monopoly.controller;

import monopoly.model.jugador.IJugador;
import monopoly.model.casilla.Propiedad;

/**
 * Interface que define los métodos que la Vista puede invocar en el
 * Controlador.
 */
public interface IControladorJuego {
    void iniciarPartida();

    void tirarDados();

    void finalizarTurno();

    void comprarPropiedad();

    // Métodos de gestión de cárcel
    void pagarFianza();

    void intentarDobles();

    // Acceso a datos
    monopoly.model.tablero.Partida getPartida();

    // Gestión de observadores
    void agregarObservador(ControladorJuego.ObservadorExtendido observador);

    void eliminarObservador(ControladorJuego.ObservadorExtendido observador);
}
