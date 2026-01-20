package monopoly.model.cartas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa el mazo de cartas de Suerte y Comunidad.
 * Implementa la funcionalidad de barajar y sacar cartas.
 */
public class MazoSuerte {

    private List<CartaSuerte> cartasSuerte;
    private List<CartaSuerte> cartasComunidad;
    private int indiceSuerte;
    private int indiceComunidad;

    public MazoSuerte() {
        this.cartasSuerte = new ArrayList<>();
        this.cartasComunidad = new ArrayList<>();
        this.indiceSuerte = 0;
        this.indiceComunidad = 0;

        inicializarCartas();
        barajar();
    }

    /**
     * Inicializa las cartas de Suerte y Comunidad con sus efectos.
     */
    private void inicializarCartas() {
        // ===== CARTAS DE SUERTE =====

        // Cartas de cobrar dinero
        cartasSuerte.add(new CartaSuerte(
                "El banco te paga dividendos de 50 EUR",
                CartaSuerte.TipoCarta.SUERTE,
                CartaSuerte.TipoEfecto.COBRAR,
                50));

        cartasSuerte.add(new CartaSuerte(
                "Has ganado un concurso de belleza. Cobra 100 EUR",
                CartaSuerte.TipoCarta.SUERTE,
                CartaSuerte.TipoEfecto.COBRAR,
                100));

        cartasSuerte.add(new CartaSuerte(
                "Vencimiento de tu seguro de vida. Cobra 150 EUR",
                CartaSuerte.TipoCarta.SUERTE,
                CartaSuerte.TipoEfecto.COBRAR,
                150));

        // Cartas de pagar dinero
        cartasSuerte.add(new CartaSuerte(
                "Multa por exceso de velocidad. Paga 50 EUR",
                CartaSuerte.TipoCarta.SUERTE,
                CartaSuerte.TipoEfecto.PAGAR,
                50));

        cartasSuerte.add(new CartaSuerte(
                "Reparaciones en tus propiedades. Paga 100 EUR",
                CartaSuerte.TipoCarta.SUERTE,
                CartaSuerte.TipoEfecto.PAGAR,
                100));

        // Cartas de movimiento
        cartasSuerte.add(new CartaSuerte(
                "Avanza hasta la Salida (Cobra 200 EUR)",
                CartaSuerte.TipoCarta.SUERTE,
                CartaSuerte.TipoEfecto.IR_A_CASILLA,
                0));

        cartasSuerte.add(new CartaSuerte(
                "Avanza 3 casillas",
                CartaSuerte.TipoCarta.SUERTE,
                CartaSuerte.TipoEfecto.MOVER_CASILLAS,
                3));

        cartasSuerte.add(new CartaSuerte(
                "Retrocede 3 casillas",
                CartaSuerte.TipoCarta.SUERTE,
                CartaSuerte.TipoEfecto.MOVER_CASILLAS,
                -3));

        // Carta de ir a la cárcel
        cartasSuerte.add(new CartaSuerte(
                "Ve directamente a la Cárcel. No pases por la Salida",
                CartaSuerte.TipoCarta.SUERTE,
                CartaSuerte.TipoEfecto.IR_CARCEL,
                0));

        // Carta de salir de la cárcel
        cartasSuerte.add(new CartaSuerte(
                "Quedas libre de la cárcel. Guarda esta carta",
                CartaSuerte.TipoCarta.SUERTE,
                CartaSuerte.TipoEfecto.SALIR_CARCEL,
                0));

        // ===== CARTAS DE COMUNIDAD =====

        // Cartas de cobrar dinero
        cartasComunidad.add(new CartaSuerte(
                "Error del banco a tu favor. Cobra 200 EUR",
                CartaSuerte.TipoCarta.COMUNIDAD,
                CartaSuerte.TipoEfecto.COBRAR,
                200));

        cartasComunidad.add(new CartaSuerte(
                "Es tu cumpleaños. Cobra 25 EUR de cada jugador",
                CartaSuerte.TipoCarta.COMUNIDAD,
                CartaSuerte.TipoEfecto.COBRAR,
                75 // Aproximación para 3 jugadores
        ));

        cartasComunidad.add(new CartaSuerte(
                "Herencia de un familiar lejano. Cobra 100 EUR",
                CartaSuerte.TipoCarta.COMUNIDAD,
                CartaSuerte.TipoEfecto.COBRAR,
                100));

        cartasComunidad.add(new CartaSuerte(
                "Devolución del impuesto sobre la renta. Cobra 20 EUR",
                CartaSuerte.TipoCarta.COMUNIDAD,
                CartaSuerte.TipoEfecto.COBRAR,
                20));

        // Cartas de pagar dinero
        cartasComunidad.add(new CartaSuerte(
                "Paga la factura del médico. 50 EUR",
                CartaSuerte.TipoCarta.COMUNIDAD,
                CartaSuerte.TipoEfecto.PAGAR,
                50));

        cartasComunidad.add(new CartaSuerte(
                "Paga la prima del seguro. 50 EUR",
                CartaSuerte.TipoCarta.COMUNIDAD,
                CartaSuerte.TipoEfecto.PAGAR,
                50));

        cartasComunidad.add(new CartaSuerte(
                "Paga la matrícula escolar. 150 EUR",
                CartaSuerte.TipoCarta.COMUNIDAD,
                CartaSuerte.TipoEfecto.PAGAR,
                150));

        // Cartas de movimiento
        cartasComunidad.add(new CartaSuerte(
                "Avanza hasta la Salida (Cobra 200 EUR)",
                CartaSuerte.TipoCarta.COMUNIDAD,
                CartaSuerte.TipoEfecto.IR_A_CASILLA,
                0));

        // Carta de ir a la cárcel
        cartasComunidad.add(new CartaSuerte(
                "Ve directamente a la Cárcel",
                CartaSuerte.TipoCarta.COMUNIDAD,
                CartaSuerte.TipoEfecto.IR_CARCEL,
                0));

        // Carta de salir de la cárcel
        cartasComunidad.add(new CartaSuerte(
                "Quedas libre de la cárcel. Guarda esta carta",
                CartaSuerte.TipoCarta.COMUNIDAD,
                CartaSuerte.TipoEfecto.SALIR_CARCEL,
                0));
    }

    /**
     * Baraja ambos mazos de cartas.
     */
    public void barajar() {
        Collections.shuffle(cartasSuerte);
        Collections.shuffle(cartasComunidad);
        indiceSuerte = 0;
        indiceComunidad = 0;
    }

    /**
     * Saca la siguiente carta del mazo de Suerte.
     * 
     * @return La carta de Suerte
     */
    public CartaSuerte sacarCartaSuerte() {
        if (indiceSuerte >= cartasSuerte.size()) {
            // Rebarajar si se acabaron las cartas
            Collections.shuffle(cartasSuerte);
            indiceSuerte = 0;
        }
        return cartasSuerte.get(indiceSuerte++);
    }

    /**
     * Saca la siguiente carta del mazo de Comunidad.
     * 
     * @return La carta de Comunidad
     */
    public CartaSuerte sacarCartaComunidad() {
        if (indiceComunidad >= cartasComunidad.size()) {
            // Rebarajar si se acabaron las cartas
            Collections.shuffle(cartasComunidad);
            indiceComunidad = 0;
        }
        return cartasComunidad.get(indiceComunidad++);
    }

    /**
     * Obtiene el número de cartas de Suerte.
     */
    public int getNumeroCartasSuerte() {
        return cartasSuerte.size();
    }

    /**
     * Obtiene el número de cartas de Comunidad.
     */
    public int getNumeroCartasComunidad() {
        return cartasComunidad.size();
    }
}
