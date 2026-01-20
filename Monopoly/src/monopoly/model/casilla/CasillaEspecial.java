package monopoly.model.casilla;

import monopoly.model.jugador.IJugador;

/**
 * Representa una casilla especial en el tablero de Monopoly.
 * Maneja la lógica de Cárcel, Impuestos, Parking, etc.
 */
public class CasillaEspecial extends Casilla {

    private int montoImpuesto; // Para casillas de impuesto
    private int montoBote; // Para parking (acumulador)

    // Constantes de la Cárcel
    public static final int FIANZA_CARCEL = 50;
    public static final int MAX_TURNOS_CARCEL = 3;

    public CasillaEspecial(String nombre, int posicion, TipoCasilla tipo) {
        super(nombre, posicion, tipo);
        this.montoImpuesto = 0;
        this.montoBote = 0;
    }

    public CasillaEspecial(String nombre, int posicion, TipoCasilla tipo, int montoImpuesto) {
        super(nombre, posicion, tipo);
        this.montoImpuesto = montoImpuesto;
        this.montoBote = 0;
    }

    // ===== LÓGICA DE IMPUESTOS =====

    /**
     * Cobra el impuesto al jugador.
     * 
     * @param jugador El jugador que paga
     * @return Mensaje describiendo la acción
     */
    public String cobrarImpuesto(IJugador jugador) {
        if (getTipo() != TipoCasilla.IMPUESTO) {
            return "";
        }

        jugador.restarDinero(montoImpuesto);
        montoBote += montoImpuesto; // El dinero va al bote del parking

        return jugador.getNombre() + " paga " + montoImpuesto + " EUR de impuestos.";
    }

    public int getMontoImpuesto() {
        return montoImpuesto;
    }

    public void setMontoImpuesto(int monto) {
        this.montoImpuesto = monto;
    }

    // ===== LÓGICA DE PARKING =====

    /**
     * El jugador recoge el bote acumulado en Parking.
     * 
     * @param jugador El jugador que recoge
     * @return Mensaje describiendo la acción
     */
    public String recogerBote(IJugador jugador) {
        if (getTipo() != TipoCasilla.PARKING || montoBote == 0) {
            return jugador.getNombre() + " descansa en el Parking Gratuito.";
        }

        int boteRecogido = montoBote;
        jugador.sumarDinero(montoBote);
        montoBote = 0;

        return jugador.getNombre() + " recoge " + boteRecogido + " EUR del bote del Parking!";
    }

    public int getMontoBote() {
        return montoBote;
    }

    public void agregarAlBote(int cantidad) {
        this.montoBote += cantidad;
    }

    // ===== LÓGICA DE CÁRCEL =====

    /**
     * Envía un jugador a la cárcel.
     * 
     * @param jugador El jugador a encarcelar
     * @return Mensaje describiendo la acción
     */
    public static String enviarACarcel(IJugador jugador) {
        jugador.setPosicion(10); // Posición de la cárcel
        jugador.setEnCarcel(true);
        return jugador.getNombre() + " va directamente a la Cárcel!";
    }

    /**
     * Intenta que el jugador salga de la cárcel pagando fianza.
     * 
     * @param jugador El jugador encarcelado
     * @return Mensaje describiendo el resultado
     */
    public static String pagarFianza(IJugador jugador) {
        if (!jugador.isEnCarcel()) {
            return "";
        }

        if (jugador.puedePermitirse(FIANZA_CARCEL)) {
            jugador.restarDinero(FIANZA_CARCEL);
            jugador.setEnCarcel(false);
            jugador.resetearTurnosEnCarcel();
            return jugador.getNombre() + " paga " + FIANZA_CARCEL + " EUR de fianza y sale de la Cárcel.";
        } else {
            return jugador.getNombre() + " no tiene suficiente dinero para pagar la fianza.";
        }
    }

    /**
     * Procesa un turno en la cárcel.
     * 
     * @param jugador   El jugador encarcelado
     * @param sacoDoles Si el jugador sacó dobles en el dado
     * @return Mensaje describiendo el resultado
     */
    public static String procesarTurnoCarcel(IJugador jugador, boolean sacoDoles) {
        if (!jugador.isEnCarcel()) {
            return "";
        }

        // Si saca dobles, sale de la cárcel
        if (sacoDoles) {
            jugador.setEnCarcel(false);
            jugador.resetearTurnosEnCarcel();
            return jugador.getNombre() + " saca dobles y sale de la Cárcel!";
        }

        // Incrementar turnos en cárcel
        jugador.incrementarTurnosEnCarcel();

        // Si lleva 3 turnos, debe pagar obligatoriamente
        if (jugador.getTurnosEnCarcel() >= MAX_TURNOS_CARCEL) {
            if (jugador.puedePermitirse(FIANZA_CARCEL)) {
                jugador.restarDinero(FIANZA_CARCEL);
                jugador.setEnCarcel(false);
                jugador.resetearTurnosEnCarcel();
                return jugador.getNombre() + " lleva 3 turnos en la Cárcel. Paga " + FIANZA_CARCEL
                        + " EUR obligatoriamente y sale.";
            } else {
                // Si no puede pagar, queda en bancarrota
                return jugador.getNombre() + " no puede pagar la fianza obligatoria. ¡Está en bancarrota!";
            }
        }

        return jugador.getNombre() + " sigue en la Cárcel (Turno " + jugador.getTurnosEnCarcel() + "/"
                + MAX_TURNOS_CARCEL + ").";
    }

    // ===== LÓGICA DE SALIDA =====

    /**
     * Procesa cuando un jugador pasa o cae en la Salida.
     * 
     * @param jugador El jugador
     * @param pasoPor true si pasó por encima, false si cayó exactamente
     * @return Mensaje describiendo la acción
     */
    public static String procesarSalida(IJugador jugador, boolean pasoPor) {
        // El cobro de 200 EUR ya se hace en el método mover() del Jugador
        if (pasoPor) {
            return jugador.getNombre() + " pasa por la Salida y cobra 200 EUR.";
        } else {
            return jugador.getNombre() + " cae en la Salida.";
        }
    }

    // ===== LÓGICA DE IR A CÁRCEL =====

    /**
     * Procesa cuando un jugador cae en "Ir a la Cárcel".
     * 
     * @param jugador El jugador
     * @return Mensaje describiendo la acción
     */
    public static String procesarIrACarcel(IJugador jugador) {
        return enviarACarcel(jugador);
    }

    @Override
    public void ejecutarAccion() {
        // La lógica se ejecuta desde el controlador
    }

    @Override
    public String toString() {
        String info = getNombre() + " [" + getTipo() + "]";
        if (getTipo() == TipoCasilla.IMPUESTO && montoImpuesto > 0) {
            info += " - " + montoImpuesto + " EUR";
        }
        if (getTipo() == TipoCasilla.PARKING && montoBote > 0) {
            info += " - Bote: " + montoBote + " EUR";
        }
        return info;
    }
}
