package monopoly.model.cartas;

import monopoly.model.jugador.IJugador;

/**
 * Representa una carta de Suerte o Comunidad en el juego Monopoly.
 * Cada carta tiene una descripción y un efecto sobre el jugador.
 */
public class CartaSuerte {

    public enum TipoCarta {
        SUERTE,
        COMUNIDAD
    }

    public enum TipoEfecto {
        COBRAR, // Cobrar dinero del banco
        PAGAR, // Pagar dinero al banco
        MOVER_CASILLAS, // Moverse X casillas
        IR_A_CASILLA, // Ir a una casilla específica
        IR_CARCEL, // Ir directamente a la cárcel
        SALIR_CARCEL // Carta para salir de la cárcel
    }

    private String descripcion;
    private TipoCarta tipoCarta;
    private TipoEfecto tipoEfecto;
    private int valor; // Cantidad de dinero o casillas según el efecto

    public CartaSuerte(String descripcion, TipoCarta tipoCarta, TipoEfecto tipoEfecto, int valor) {
        this.descripcion = descripcion;
        this.tipoCarta = tipoCarta;
        this.tipoEfecto = tipoEfecto;
        this.valor = valor;
    }

    /**
     * Aplica el efecto de la carta sobre un jugador.
     * 
     * @param jugador El jugador afectado
     * @return Mensaje describiendo el resultado
     */
    public String aplicarEfecto(IJugador jugador) {
        StringBuilder resultado = new StringBuilder();
        resultado.append(jugador.getNombre()).append(": ").append(descripcion).append(" → ");

        switch (tipoEfecto) {
            case COBRAR:
                jugador.sumarDinero(valor);
                resultado.append("Cobra ").append(valor).append(" EUR");
                break;

            case PAGAR:
                jugador.restarDinero(valor);
                resultado.append("Paga ").append(valor).append(" EUR");
                break;

            case MOVER_CASILLAS:
                int nuevaPosicion = (jugador.getPosicion() + valor) % 40;
                if (nuevaPosicion < 0)
                    nuevaPosicion += 40;
                jugador.setPosicion(nuevaPosicion);
                resultado.append("Se mueve ").append(valor).append(" casillas");
                break;

            case IR_A_CASILLA:
                // Si pasa por la salida, cobra 200
                if (valor < jugador.getPosicion() && valor != 10) { // 10 es la cárcel
                    jugador.sumarDinero(200);
                    resultado.append("Pasa por Salida y cobra 200 EUR. ");
                }
                jugador.setPosicion(valor);
                resultado.append("Va a la casilla ").append(valor);
                break;

            case IR_CARCEL:
                jugador.setPosicion(10); // Casilla de la cárcel
                jugador.setEnCarcel(true);
                resultado.append("¡Va a la cárcel!");
                break;

            case SALIR_CARCEL:
                // Esta carta se guarda para uso futuro
                resultado.append("Guarda carta de salir de la cárcel");
                break;
        }

        return resultado.toString();
    }

    // Getters
    public String getDescripcion() {
        return descripcion;
    }

    public TipoCarta getTipoCarta() {
        return tipoCarta;
    }

    public TipoEfecto getTipoEfecto() {
        return tipoEfecto;
    }

    public int getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "[" + tipoCarta + "] " + descripcion;
    }
}
