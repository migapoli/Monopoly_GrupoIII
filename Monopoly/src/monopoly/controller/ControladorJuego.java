package monopoly.controller;

import monopoly.model.cartas.CartaSuerte;
import monopoly.model.casilla.Casilla;
import monopoly.model.casilla.Casilla.TipoCasilla;
import monopoly.model.casilla.CasillaEspecial;
import monopoly.model.casilla.Propiedad;
import monopoly.model.jugador.IJugador;
import monopoly.model.tablero.Partida;

import java.util.ArrayList;
import java.util.List;

public class ControladorJuego implements IControladorJuego {
    private Partida partida;
    private List<ObservadorExtendido> observadores;
    private boolean dadosLanzados;

    public ControladorJuego(Partida partida) {
        this.partida = partida;
        this.observadores = new ArrayList<>();
        this.dadosLanzados = false;
    }

    @Override
    public void iniciarPartida() {
        if (partida.puedeIniciar()) {
            notificarMensaje("¡La partida ha comenzado con " + partida.getNumeroJugadores() + " jugadores!");
            notificarActualizacion();
            notificarCambioTurno(partida.getJugadorActual());
        } else {
            notificarMensaje("Error: Se necesitan al menos 2 jugadores para iniciar.");
        }
    }

    @Override
    public void tirarDados() {
        if (partida.isJuegoTerminado()) {
            notificarMensaje("El juego ha terminado.");
            return;
        }

        if (dadosLanzados) {
            notificarMensaje("Ya has lanzado los dados en este turno.");
            return;
        }

        IJugador jugador = partida.getJugadorActual();

        // Si está en la cárcel, se maneja de forma especial
        if (jugador.isEnCarcel()) {
            // Mostrar diálogo de cárcel si aún no ha decidido
            notificarEstadoCarcel(jugador);
            return;
        }

        realizarTirada(jugador);
    }

    private void realizarTirada(IJugador jugador) {
        int[] resultados = partida.getDado().lanzar();
        dadosLanzados = true;

        notificarDadosLanzados(resultados[0], resultados[1]);
        String mensajeDados = jugador.getNombre() + " ha sacado " + resultados[0] + " y " + resultados[1];
        partida.agregarHistorial(mensajeDados);
        notificarMensaje(mensajeDados);

        boolean esDoble = partida.getDado().esDoble();

        // Mover al jugador
        moverJugador(jugador, partida.getDado().getSuma());

        // Si saca dobles, en teoría podría volver a tirar,
        // pero para simplificar el flujo interactivo, por ahora solo notificamos.
        // En una versión más avanzada se permitiría tirar de nuevo si no son 3 dobles.
        if (esDoble) {
            partida.agregarHistorial("¡Dobles! " + jugador.getNombre() + " ha sacado dobles.");
            notificarMensaje("¡Has sacado dobles!");
        }
    }

    private void moverJugador(IJugador jugador, int casillas) {
        // Mover jugador
        jugador.mover(casillas, partida.getTablero().getTotalCasillas());

        // Verificar si pasó por salida
        if (jugador.pasoPorSalida()) {
            String msg = CasillaEspecial.procesarSalida(jugador, true);
            partida.agregarHistorial(msg);
            notificarMensaje(msg);
        }

        // Obtener casilla actual y ejecutar acción
        Casilla casillaActual = partida.getTablero().getCasilla(jugador.getPosicion());
        notificarMensaje(jugador.getNombre() + " cae en " + casillaActual.getNombre());

        notificarActualizacion();
        ejecutarAccionCasilla(casillaActual, jugador);
    }

    public void ejecutarAccionCasilla(Casilla casilla, IJugador jugador) {
        switch (casilla.getTipo()) {
            case PROPIEDAD:
                gestionarPropiedad((Propiedad) casilla, jugador);
                break;

            case SUERTE:
                gestionarCartaSuerte(jugador);
                break;

            case COMUNIDAD:
                gestionarCartaComunidad(jugador);
                break;

            case IMPUESTO:
                gestionarImpuesto((CasillaEspecial) casilla, jugador);
                break;

            case IR_CARCEL:
                String msgCarcel = CasillaEspecial.procesarIrACarcel(jugador);
                partida.agregarHistorial(msgCarcel);
                notificarMensaje(msgCarcel);
                notificarActualizacion();
                break;

            case PARKING:
                notificarMensaje(jugador.getNombre() + " descansa en el Parking Gratuito.");
                break;

            case CARCEL:
                notificarMensaje(jugador.getNombre() + " está de visita en la Cárcel.");
                break;

            case SALIDA:
                notificarMensaje(jugador.getNombre() + " está en la Salida.");
                break;

            default:
                break;
        }
    }

    // ===== GESTIÓN DE PROPIEDADES =====

    private void gestionarPropiedad(Propiedad propiedad, IJugador jugador) {
        if (propiedad.estaDisponible()) {
            // Ofrecer compra si tiene dinero
            if (jugador.puedePermitirse(propiedad.getPrecio())) {
                notificarOfertaPropiedad(propiedad, jugador);
            } else {
                notificarMensaje("No tienes suficiente dinero para comprar " + propiedad.getNombre());
            }
        } else if (!propiedad.getPropietario().equals(jugador)) {
            // Pagar alquiler
            boolean tieneGrupo = tieneGrupoCompleto(propiedad.getPropietario(), propiedad.getGrupoColor());
            int alquiler = propiedad.cobrarAlquiler(jugador, tieneGrupo);

            String msg = jugador.getNombre() + " paga " + alquiler + " EUR de alquiler a "
                    + propiedad.getPropietario().getNombre();
            partida.agregarHistorial(msg);
            notificarMensaje(msg);
            notificarActualizacion();

            verificarBancarrota(jugador);
        }
    }

    private boolean tieneGrupoCompleto(IJugador propietario, java.awt.Color color) {
        long tiene = propietario.getPropiedades().stream()
                .filter(p -> p.getGrupoColor().equals(color))
                .count();

        long total = partida.getTablero().getCasillas().stream()
                .filter(c -> c instanceof Propiedad && ((Propiedad) c).getGrupoColor().equals(color))
                .count();

        return tiene == total && total > 0;
    }

    @Override
    public void comprarPropiedad() {
        IJugador jugador = partida.getJugadorActual();
        Casilla casilla = partida.getTablero().getCasilla(jugador.getPosicion());

        if (casilla instanceof Propiedad) {
            Propiedad propiedad = (Propiedad) casilla;
            if (propiedad.comprar(jugador)) {
                String msg = jugador.getNombre() + " ha comprado " + propiedad.getNombre() + " por "
                        + propiedad.getPrecio() + " EUR";
                partida.agregarHistorial(msg);
                notificarPropiedadComprada(propiedad, jugador);
                notificarMensaje(msg);
            }
        }
    }

    // ===== GESTIÓN DE CARTAS =====

    private void gestionarCartaSuerte(IJugador jugador) {
        CartaSuerte carta = partida.getMazoSuerte().sacarCartaSuerte();
        procesarCarta(carta, jugador, "SUERTE");
    }

    private void gestionarCartaComunidad(IJugador jugador) {
        CartaSuerte carta = partida.getMazoSuerte().sacarCartaComunidad();
        procesarCarta(carta, jugador, "CAJA DE COMUNIDAD");
    }

    private void procesarCarta(CartaSuerte carta, IJugador jugador, String tipo) {
        notificarCartaEvento(tipo, carta.getDescripcion(), carta.aplicarEfecto(jugador));
        partida.agregarHistorial(jugador.getNombre() + " saca carta de " + tipo + ": " + carta.getDescripcion());
        notificarActualizacion();

        if (carta.getTipoEfecto() == CartaSuerte.TipoEfecto.MOVER_CASILLAS ||
                carta.getTipoEfecto() == CartaSuerte.TipoEfecto.IR_A_CASILLA) {

            Casilla nuevaCasilla = partida.getTablero().getCasilla(jugador.getPosicion());
            if (nuevaCasilla.getTipo() != TipoCasilla.SUERTE && nuevaCasilla.getTipo() != TipoCasilla.COMUNIDAD) {
                ejecutarAccionCasilla(nuevaCasilla, jugador);
            }
        }

        verificarBancarrota(jugador);
    }

    // ===== GESTIÓN DE IMPUESTOS =====

    private void gestionarImpuesto(CasillaEspecial casilla, IJugador jugador) {
        int monto = casilla.getMontoImpuesto();
        notificarPagoImpuesto(casilla.getNombre(), monto, jugador);

        String msg = casilla.cobrarImpuesto(jugador);
        partida.agregarHistorial(msg);
        notificarMensaje(msg);
        notificarActualizacion();

        verificarBancarrota(jugador);
    }

    // ===== GESTIÓN DE CÁRCEL =====

    @Override
    public void pagarFianza() {
        IJugador jugador = partida.getJugadorActual();
        if (jugador.isEnCarcel() && !dadosLanzados) {
            String msg = CasillaEspecial.pagarFianza(jugador);
            partida.agregarHistorial(msg);
            notificarMensaje(msg);
            notificarActualizacion();

            realizarTirada(jugador);
        }
    }

    @Override
    public void intentarDobles() {
        IJugador jugador = partida.getJugadorActual();
        if (jugador.isEnCarcel() && !dadosLanzados) {
            int[] resultados = partida.getDado().lanzar();
            dadosLanzados = true;
            notificarDadosLanzados(resultados[0], resultados[1]);

            boolean esDoble = partida.getDado().esDoble();
            String msg = CasillaEspecial.procesarTurnoCarcel(jugador, esDoble);
            partida.agregarHistorial(msg);
            notificarMensaje(msg);

            if (esDoble) {
                moverJugador(jugador, partida.getDado().getSuma());
            } else {
                notificarMensaje("No sacaste dobles. Te quedas en la cárcel.");
            }

            verificarBancarrota(jugador);
            notificarActualizacion();
        }
    }

    // ===== CONTROL DE TURNOS Y FIN DE JUEGO =====

    @Override
    public void finalizarTurno() {
        if (!dadosLanzados && !partida.getJugadorActual().isEnCarcel()) {
            notificarMensaje("¡Debes lanzar los dados antes de terminar tu turno!");
            return;
        }

        dadosLanzados = false;
        partida.siguienteTurno();

        IJugador siguiente = partida.getJugadorActual();
        notificarCambioTurno(siguiente);

        if (siguiente.isEnCarcel()) {
            notificarMensaje(siguiente.getNombre() + " está en la cárcel.");
            notificarEstadoCarcel(siguiente);
        }
    }

    private void verificarBancarrota(IJugador jugador) {
        if (partida.verificarBancarrota(jugador)) {
            notificarMensaje("¡" + jugador.getNombre() + " HA QUEBRADO!");
            notificarActualizacion();

            if (partida.isJuegoTerminado()) {
                notificarMensaje("¡FIN DEL JUEGO! Ganador: " + partida.getGanador().getNombre());
            }
        }
    }

    @Override
    public Partida getPartida() {
        return partida;
    }

    // ===== PATRÓN OBSERVER =====

    public interface ObservadorExtendido {
        void onActualizacionJuego();

        void onCambioTurno(IJugador jugador);

        void onDadosLanzados(int dado1, int dado2);

        void onPropiedadComprada(Propiedad propiedad, IJugador jugador);

        void onMensaje(String mensaje);

        void onOfertaPropiedad(Propiedad propiedad, IJugador jugador);

        void onCartaEvento(String tipoCarta, String descripcion, String efecto);

        void onPagoImpuesto(String nombreCasilla, int monto, IJugador jugador);

        void onEstadoCarcel(IJugador jugador, int turnos);
    }

    @Override
    public void agregarObservador(ObservadorExtendido observador) {
        observadores.add(observador);
    }

    @Override
    public void eliminarObservador(ObservadorExtendido observador) {
        observadores.remove(observador);
    }

    // Métodos privados de notificación unificados

    private void notificarEstadoCarcel(IJugador jug) {
        for (ObservadorExtendido obs : observadores) {
            obs.onEstadoCarcel(jug, jug.getTurnosEnCarcel());
        }
    }

    private void notificarActualizacion() {
        for (ObservadorExtendido obs : observadores)
            obs.onActualizacionJuego();
    }

    private void notificarCambioTurno(IJugador jugador) {
        for (ObservadorExtendido obs : observadores)
            obs.onCambioTurno(jugador);
    }

    private void notificarDadosLanzados(int d1, int d2) {
        for (ObservadorExtendido obs : observadores)
            obs.onDadosLanzados(d1, d2);
    }

    private void notificarPropiedadComprada(Propiedad prop, IJugador jug) {
        for (ObservadorExtendido obs : observadores)
            obs.onPropiedadComprada(prop, jug);
    }

    private void notificarMensaje(String msg) {
        for (ObservadorExtendido obs : observadores)
            obs.onMensaje(msg);
    }

    private void notificarOfertaPropiedad(Propiedad prop, IJugador jug) {
        for (ObservadorExtendido obs : observadores)
            obs.onOfertaPropiedad(prop, jug);
    }

    private void notificarCartaEvento(String tipo, String desc, String efecto) {
        for (ObservadorExtendido obs : observadores)
            obs.onCartaEvento(tipo, desc, efecto);
    }

    private void notificarPagoImpuesto(String nombre, int monto, IJugador jug) {
        for (ObservadorExtendido obs : observadores)
            obs.onPagoImpuesto(nombre, monto, jug);
    }
}
