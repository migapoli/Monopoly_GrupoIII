package monopoly.controller;

import monopoly.model.jugador.IJugador;
import monopoly.model.casilla.Casilla;
import monopoly.model.casilla.Propiedad;
import monopoly.model.tablero.Partida;

public interface IControladorJuego {
    void iniciarPartida();
    void tirarDados();
    void comprarPropiedad();
    void finalizarTurno();
    Partida getPartida();
    void agregarObservador(ObservadorJuego observador);
    void eliminarObservador(ObservadorJuego observador);
    
    interface ObservadorJuego {
        void onActualizacionJuego();
        void onCambioTurno(IJugador jugador);
        void onDadosLanzados(int dado1, int dado2);
        void onPropiedadComprada(Propiedad propiedad, IJugador jugador);
        void onMensaje(String mensaje);
    }
}
