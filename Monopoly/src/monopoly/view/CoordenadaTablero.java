package monopoly.view;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * Sistema de coordenadas para el tablero de Monopoly.
 * 
 * Esta clase mapea coordenadas de píxeles a casillas del tablero y viceversa.
 * Las coordenadas están calibradas para la imagen de fondo del tablero
 * MEGAPOLY.
 * 
 * El tablero tiene 40 casillas organizadas así:
 * - Casilla 0: Salida (esquina inferior derecha)
 * - Casillas 1-9: Lado inferior (derecha a izquierda)
 * - Casilla 10: Cárcel (esquina inferior izquierda)
 * - Casillas 11-19: Lado izquierdo (abajo a arriba)
 * - Casilla 20: Parking (esquina superior izquierda)
 * - Casillas 21-29: Lado superior (izquierda a derecha)
 * - Casilla 30: Ve a la Cárcel (esquina superior derecha)
 * - Casillas 31-39: Lado derecho (arriba a abajo)
 */
public class CoordenadaTablero {

    // Dimensiones de referencia de la imagen del tablero MEGAPOLY
    // Estas son las dimensiones base sobre las cuales se calculan las coordenadas
    private static final int IMAGEN_ANCHO_REFERENCIA = 817;
    private static final int IMAGEN_ALTO_REFERENCIA = 816;

    // Coordenadas de las esquinas del tablero en la imagen de referencia
    // Esquina superior izquierda (Parking Gratuito)
    private static final int TABLERO_X_INICIO = 0;
    private static final int TABLERO_Y_INICIO = 0;

    // Dimensiones del tablero dentro de la imagen
    private static final int TABLERO_ANCHO = 817;
    private static final int TABLERO_ALTO = 816;

    // Número de casillas por lado (incluyendo esquinas)
    private static final int CASILLAS_POR_LADO = 11;

    // Tamaño de las casillas de esquina (más grandes)
    private static final double ESQUINA_ANCHO = 72.0;
    private static final double ESQUINA_ALTO = 72.0;

    // Tamaño de las casillas normales
    private static final double CASILLA_ANCHO_BASE = (TABLERO_ANCHO - 2 * ESQUINA_ANCHO) / 9.0;
    private static final double CASILLA_ALTO_BASE = (TABLERO_ALTO - 2 * ESQUINA_ALTO) / 9.0;

    // El factor de escala actual
    private double escalaX = 1.0;
    private double escalaY = 1.0;

    // Offset del tablero dentro del panel
    private int offsetX = 0;
    private int offsetY = 0;

    // Rectángulos que representan cada casilla (calculados al ajustar escala)
    private Rectangle[] rectangulosCasillas = new Rectangle[40];

    /**
     * Constructor por defecto.
     */
    public CoordenadaTablero() {
        calcularRectangulosCasillas();
    }

    /**
     * Ajusta las coordenadas según el tamaño actual del panel.
     * 
     * @param anchoPanelTablero Ancho actual del área donde se dibuja el tablero
     * @param altoPanelTablero  Alto actual del área donde se dibuja el tablero
     * @param offsetXActual     Desplazamiento X del tablero dentro del panel
     * @param offsetYActual     Desplazamiento Y del tablero dentro del panel
     */
    public void ajustarEscala(int anchoPanelTablero, int altoPanelTablero,
            int offsetXActual, int offsetYActual) {
        // Calcular la escala basada en el tamaño de la imagen mostrada
        this.escalaX = (double) anchoPanelTablero / IMAGEN_ANCHO_REFERENCIA;
        this.escalaY = (double) altoPanelTablero / IMAGEN_ALTO_REFERENCIA;
        this.offsetX = offsetXActual;
        this.offsetY = offsetYActual;

        calcularRectangulosCasillas();
    }

    /**
     * Calcula los rectángulos de cada casilla basándose en la escala actual.
     */
    private void calcularRectangulosCasillas() {
        for (int i = 0; i < 40; i++) {
            rectangulosCasillas[i] = calcularRectanguloCasilla(i);
        }
    }

    /**
     * Calcula el rectángulo de una casilla específica.
     */
    private Rectangle calcularRectanguloCasilla(int numeroCasilla) {
        // Calcular la posición y tamaño de la casilla
        int x, y, ancho, alto;

        double casillaAncho = CASILLA_ANCHO_BASE * escalaX;
        double casillaAlto = CASILLA_ALTO_BASE * escalaY;

        // Las esquinas son más grandes
        double esquinaAncho = ESQUINA_ANCHO * escalaX;
        double esquinaAlto = ESQUINA_ALTO * escalaY;

        int lado = numeroCasilla / 10; // 0=inferior, 1=izquierdo, 2=superior, 3=derecho
        int posicionEnLado = numeroCasilla % 10;

        double tableroX = TABLERO_X_INICIO * escalaX + offsetX;
        double tableroY = TABLERO_Y_INICIO * escalaY + offsetY;
        double tableroAncho = TABLERO_ANCHO * escalaX;
        double tableroAlto = TABLERO_ALTO * escalaY;

        switch (lado) {
            case 0: // Lado inferior (casillas 0-10, derecha a izquierda)
                if (posicionEnLado == 0) {
                    // Casilla 0 - Salida (esquina inferior derecha)
                    x = (int) (tableroX + tableroAncho - esquinaAncho);
                    y = (int) (tableroY + tableroAlto - esquinaAlto);
                    ancho = (int) esquinaAncho;
                    alto = (int) esquinaAlto;
                } else {
                    // Casillas 1-9 (del lado inferior, derecha a izquierda)
                    // Las casillas horizontales son más anchas que altas
                    x = (int) (tableroX + tableroAncho - esquinaAncho - posicionEnLado * casillaAncho);
                    y = (int) (tableroY + tableroAlto - esquinaAlto); // Corregido: usar alto de esquina
                    ancho = (int) casillaAncho;
                    alto = (int) esquinaAlto; // Corregido: usar alto de esquina para casillas horizontales
                }
                break;

            case 1: // Lado izquierdo (casillas 10-20, abajo a arriba)
                if (posicionEnLado == 0) {
                    // Casilla 10 - Cárcel (esquina inferior izquierda)
                    x = (int) tableroX;
                    y = (int) (tableroY + tableroAlto - esquinaAlto);
                    ancho = (int) esquinaAncho;
                    alto = (int) esquinaAlto;
                } else {
                    // Casillas 11-19 (del lado izquierdo, abajo a arriba)
                    // Las casillas laterales son más estrechas (usan ancho de esquina)
                    x = (int) tableroX;
                    y = (int) (tableroY + tableroAlto - esquinaAlto - posicionEnLado * casillaAlto);
                    ancho = (int) esquinaAncho; // Corregido: usar ancho de esquina para casillas laterales
                    alto = (int) casillaAlto;
                }
                break;

            case 2: // Lado superior (casillas 20-30, izquierda a derecha)
                if (posicionEnLado == 0) {
                    // Casilla 20 - Parking Gratuito (esquina superior izquierda)
                    x = (int) tableroX;
                    y = (int) tableroY;
                    ancho = (int) esquinaAncho;
                    alto = (int) esquinaAlto;
                } else {
                    // Casillas 21-29 (del lado superior, izquierda a derecha)
                    // Las casillas horizontales son más anchas que altas
                    x = (int) (tableroX + esquinaAncho + (posicionEnLado - 1) * casillaAncho);
                    y = (int) tableroY;
                    ancho = (int) casillaAncho;
                    alto = (int) esquinaAlto; // Corregido: usar alto de esquina para casillas horizontales
                }
                break;

            case 3: // Lado derecho (casillas 30-39, arriba a abajo)
                if (posicionEnLado == 0) {
                    // Casilla 30 - Ve a la Cárcel (esquina superior derecha)
                    x = (int) (tableroX + tableroAncho - esquinaAncho);
                    y = (int) tableroY;
                    ancho = (int) esquinaAncho;
                    alto = (int) esquinaAlto;
                } else {
                    // Casillas 31-39 (del lado derecho, arriba a abajo)
                    // Las casillas laterales son más estrechas (usan ancho de esquina)
                    x = (int) (tableroX + tableroAncho - esquinaAncho); // Corregido: usar ancho de esquina
                    y = (int) (tableroY + esquinaAlto + (posicionEnLado - 1) * casillaAlto);
                    ancho = (int) esquinaAncho; // Corregido: usar ancho de esquina para casillas laterales
                    alto = (int) casillaAlto;
                }
                break;

            default:
                // No debería llegar aquí
                x = 0;
                y = 0;
                ancho = 0;
                alto = 0;
        }

        return new Rectangle(x, y, ancho, alto);
    }

    /**
     * Obtiene el número de casilla en la que se encuentra un punto dado.
     * 
     * @param x Coordenada X del punto (en píxeles del panel)
     * @param y Coordenada Y del punto (en píxeles del panel)
     * @return Número de casilla (0-39) o -1 si el punto no está en ninguna casilla
     */
    public int obtenerCasillaEnCoordenada(int x, int y) {
        Point punto = new Point(x, y);

        for (int i = 0; i < 40; i++) {
            if (rectangulosCasillas[i].contains(punto)) {
                return i;
            }
        }

        return -1; // No está en ninguna casilla (está en el centro del tablero o fuera)
    }

    /**
     * Obtiene el centro de una casilla específica.
     * 
     * @param numeroCasilla El número de la casilla (0-39)
     * @return El punto central de la casilla
     */
    public Point obtenerCentroCasilla(int numeroCasilla) {
        if (numeroCasilla < 0 || numeroCasilla >= 40) {
            throw new IllegalArgumentException("Número de casilla inválido: " + numeroCasilla);
        }

        Rectangle rect = rectangulosCasillas[numeroCasilla];
        return new Point(
                rect.x + rect.width / 2,
                rect.y + rect.height / 2);
    }

    /**
     * Obtiene el rectángulo que representa una casilla específica.
     * 
     * @param numeroCasilla El número de la casilla (0-39)
     * @return El rectángulo de la casilla
     */
    public Rectangle obtenerRectanguloCasilla(int numeroCasilla) {
        if (numeroCasilla < 0 || numeroCasilla >= 40) {
            throw new IllegalArgumentException("Número de casilla inválido: " + numeroCasilla);
        }

        return new Rectangle(rectangulosCasillas[numeroCasilla]);
    }

    /**
     * Obtiene una posición ajustada dentro de una casilla para evitar superposición
     * de múltiples fichas de jugadores.
     * 
     * @param numeroCasilla           El número de la casilla (0-39)
     * @param indiceJugador           El índice del jugador (0, 1, 2, 3...)
     * @param totalJugadoresEnCasilla El número total de jugadores en esta casilla
     * @return La posición ajustada para la ficha del jugador
     */
    public Point obtenerPosicionFichaJugador(int numeroCasilla, int indiceJugador,
            int totalJugadoresEnCasilla) {
        Rectangle rect = rectangulosCasillas[numeroCasilla];

        // Calcular offset para distribuir las fichas dentro de la casilla
        int fichaSize = Math.min(rect.width, rect.height) / 3;
        int espacioDisponible = Math.min(rect.width, rect.height) - fichaSize;

        // Distribuir fichas en una cuadrícula 2x2
        int fila = indiceJugador / 2;
        int columna = indiceJugador % 2;

        int offsetX = columna * (espacioDisponible / 2) + fichaSize / 4;
        int offsetY = fila * (espacioDisponible / 2) + rect.height / 2;

        return new Point(
                rect.x + offsetX,
                rect.y + offsetY);
    }

    /**
     * Obtiene información de una casilla dado su número.
     * 
     * @param numeroCasilla El número de casilla (0-39)
     * @return Un objeto con información sobre la casilla
     */
    public InfoCasilla obtenerInfoCasilla(int numeroCasilla) {
        if (numeroCasilla < 0 || numeroCasilla >= 40) {
            return null;
        }

        String lado;
        boolean esEsquina = (numeroCasilla % 10 == 0);

        switch (numeroCasilla / 10) {
            case 0:
                lado = "inferior";
                break;
            case 1:
                lado = "izquierdo";
                break;
            case 2:
                lado = "superior";
                break;
            case 3:
                lado = "derecho";
                break;
            default:
                lado = "desconocido";
        }

        return new InfoCasilla(numeroCasilla, lado, esEsquina,
                rectangulosCasillas[numeroCasilla]);
    }

    /**
     * Clase interna para representar información de una casilla.
     */
    public static class InfoCasilla {
        private final int numero;
        private final String lado;
        private final boolean esEsquina;
        private final Rectangle rectangulo;

        public InfoCasilla(int numero, String lado, boolean esEsquina, Rectangle rectangulo) {
            this.numero = numero;
            this.lado = lado;
            this.esEsquina = esEsquina;
            this.rectangulo = new Rectangle(rectangulo);
        }

        public int getNumero() {
            return numero;
        }

        public String getLado() {
            return lado;
        }

        public boolean esEsquina() {
            return esEsquina;
        }

        public Rectangle getRectangulo() {
            return rectangulo;
        }

        @Override
        public String toString() {
            return String.format("Casilla %d [%s%s] en (%d, %d) - %dx%d",
                    numero, lado, esEsquina ? ", esquina" : "",
                    rectangulo.x, rectangulo.y, rectangulo.width, rectangulo.height);
        }
    }

    /**
     * Imprime información de debug sobre todas las casillas.
     */
    public void imprimirDebugCasillas() {
        System.out.println("=== DEBUG COORDENADAS TABLERO ===");
        System.out.println("Escala: " + escalaX + " x " + escalaY);
        System.out.println("Offset: " + offsetX + ", " + offsetY);
        System.out.println();

        for (int i = 0; i < 40; i++) {
            System.out.println(obtenerInfoCasilla(i));
        }
    }
}
