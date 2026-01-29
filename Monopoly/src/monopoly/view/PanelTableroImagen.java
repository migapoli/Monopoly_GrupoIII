package monopoly.view;

import monopoly.controller.ControladorJuego;
import monopoly.model.jugador.IJugador;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Panel del tablero de Monopoly que usa una imagen de fondo y un sistema de
 * coordenadas.
 * 
 * Este panel:
 * - Dibuja la imagen del tablero MEGAPOLY como fondo
 * - Usa CoordenadaTablero para mapear posiciones de casillas
 * - Dibuja las fichas de los jugadores en las coordenadas correctas
 */
public class PanelTableroImagen extends JPanel {

    private ControladorJuego controlador;
    private CoordenadaTablero coordenadas;
    private BufferedImage imagenTablero;

    // Dimensiones actuales del tablero dibujado
    private int tableroAncho;
    private int tableroAlto;
    private int tableroOffsetX;
    private int tableroOffsetY;

    /**
     * Constructor del panel.
     * 
     * @param controlador El controlador del juego
     */
    public PanelTableroImagen(ControladorJuego controlador) {
        this.controlador = controlador;
        this.coordenadas = new CoordenadaTablero();

        setOpaque(false);
        cargarImagenTablero();

        // Agregar listener para debug (opcional - muestra casilla al hacer clic)
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int casilla = coordenadas.obtenerCasillaEnCoordenada(e.getX(), e.getY());
                if (casilla >= 0) {
                    System.out.println("Clic en casilla: " + casilla +
                            " - " + obtenerNombreCasilla(casilla));
                } else {
                    System.out.println("Clic fuera del tablero o en el centro");
                }
            }
        });
    }

    /**
     * Carga la imagen del tablero desde los recursos.
     */
    private void cargarImagenTablero() {
        try {
            // Intentar cargar desde recursos del classpath
            InputStream is = getClass().getResourceAsStream("/monopoly/view/resources/tablero_fondo.png");
            if (is != null) {
                imagenTablero = ImageIO.read(is);
                is.close();
            } else {
                // Si no se encuentra en recursos, intentar cargar desde archivo
                java.io.File file = new java.io.File("src/monopoly/view/resources/tablero_fondo.png");
                if (file.exists()) {
                    imagenTablero = ImageIO.read(file);
                } else {
                    System.err.println("No se pudo encontrar la imagen del tablero");
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen del tablero: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Activar antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int anchoPanel = getWidth();
        int altoPanel = getHeight();

        // Calcular tamaño y posición del tablero (mantener proporción cuadrada)
        int tamanoTablero = Math.min(anchoPanel, altoPanel) - 20;
        tableroOffsetX = (anchoPanel - tamanoTablero) / 2;
        tableroOffsetY = (altoPanel - tamanoTablero) / 2;
        tableroAncho = tamanoTablero;
        tableroAlto = tamanoTablero;

        // Actualizar el sistema de coordenadas con las dimensiones actuales
        coordenadas.ajustarEscala(tableroAncho, tableroAlto, tableroOffsetX, tableroOffsetY);

        // Dibujar la imagen de fondo del tablero
        dibujarImagenTablero(g2d);

        // Dibujar los jugadores usando el sistema de coordenadas
        dibujarJugadores(g2d);
    }

    /**
     * Dibuja la imagen del tablero escalada al tamaño del panel.
     */
    private void dibujarImagenTablero(Graphics2D g2d) {
        if (imagenTablero != null) {
            // Dibujar sombra
            g2d.setColor(new Color(0, 0, 0, 50));
            g2d.fillRoundRect(tableroOffsetX + 8, tableroOffsetY + 8, tableroAncho, tableroAlto, 15, 15);

            // Dibujar la imagen escalada
            g2d.drawImage(imagenTablero,
                    tableroOffsetX, tableroOffsetY,
                    tableroAncho, tableroAlto,
                    null);
        } else {
            // Si no hay imagen, dibujar un fondo de respaldo
            dibujarTableroRespaldo(g2d);
        }
    }

    /**
     * Dibuja un tablero de respaldo si la imagen no está disponible.
     */
    private void dibujarTableroRespaldo(Graphics2D g2d) {
        // Fondo verde del tablero
        g2d.setColor(new Color(200, 230, 201));
        g2d.fillRoundRect(tableroOffsetX, tableroOffsetY, tableroAncho, tableroAlto, 15, 15);

        // Borde
        g2d.setColor(new Color(50, 60, 80));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(tableroOffsetX, tableroOffsetY, tableroAncho, tableroAlto, 15, 15);

        // Texto indicando que falta la imagen
        g2d.setColor(new Color(100, 100, 100));
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        String mensaje = "Imagen del tablero no encontrada";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(mensaje,
                tableroOffsetX + (tableroAncho - fm.stringWidth(mensaje)) / 2,
                tableroOffsetY + tableroAlto / 2);
    }

    /**
     * Dibuja las fichas de todos los jugadores en sus posiciones actuales.
     */
    private void dibujarJugadores(Graphics2D g2d) {
        if (controlador == null || controlador.getPartida() == null) {
            return;
        }

        List<IJugador> jugadores = controlador.getPartida().getJugadores();

        // Contar cuántos jugadores hay en cada casilla
        int[] jugadoresPorCasilla = new int[40];
        int[] indiceEnCasilla = new int[jugadores.size()];

        for (int i = 0; i < jugadores.size(); i++) {
            int pos = jugadores.get(i).getPosicion();
            if (pos >= 0 && pos < 40) {
                indiceEnCasilla[i] = jugadoresPorCasilla[pos];
                jugadoresPorCasilla[pos]++;
            }
        }

        // Dibujar cada jugador
        for (int i = 0; i < jugadores.size(); i++) {
            IJugador jugador = jugadores.get(i);
            int posicionCasilla = jugador.getPosicion();

            if (posicionCasilla >= 0 && posicionCasilla < 40) {
                Point posFicha = coordenadas.obtenerPosicionFichaJugador(
                        posicionCasilla,
                        indiceEnCasilla[i],
                        jugadoresPorCasilla[posicionCasilla]);

                dibujarFichaJugador(g2d, jugador, posFicha.x, posFicha.y);
            }
        }
    }

    /**
     * Dibuja la ficha de un jugador en la posición especificada.
     */
    private void dibujarFichaJugador(Graphics2D g2d, IJugador jugador, int x, int y) {
        int fichaSize = 20;

        // Sombra de la ficha
        g2d.setColor(new Color(0, 0, 0, 60));
        g2d.fillOval(x + 2, y + 2, fichaSize, fichaSize);

        // Ficha del jugador con gradiente
        Color colorJugador = jugador.getColor();
        GradientPaint gradient = new GradientPaint(
                x, y, colorJugador.brighter(),
                x + fichaSize, y + fichaSize, colorJugador.darker());
        g2d.setPaint(gradient);
        g2d.fillOval(x, y, fichaSize, fichaSize);

        // Brillo
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.fillOval(x + 4, y + 4, fichaSize / 3, fichaSize / 3);

        // Borde blanco
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(x, y, fichaSize, fichaSize);
    }

    /**
     * Obtiene el nombre de una casilla por su número.
     * Este método es principalmente para debug.
     */
    private String obtenerNombreCasilla(int numeroCasilla) {
        if (controlador != null && controlador.getPartida() != null) {
            try {
                return controlador.getPartida().getTablero().getCasilla(numeroCasilla).getNombre();
            } catch (Exception e) {
                return "Casilla " + numeroCasilla;
            }
        }
        return "Casilla " + numeroCasilla;
    }

    /**
     * Obtiene el número de casilla en las coordenadas dadas.
     * 
     * @param x Coordenada X en píxeles
     * @param y Coordenada Y en píxeles
     * @return Número de casilla (0-39) o -1 si no está en ninguna
     */
    public int obtenerCasillaEnPunto(int x, int y) {
        return coordenadas.obtenerCasillaEnCoordenada(x, y);
    }

    /**
     * Obtiene el centro de una casilla específica.
     * 
     * @param numeroCasilla Número de la casilla (0-39)
     * @return Punto central de la casilla
     */
    public Point obtenerCentroCasilla(int numeroCasilla) {
        return coordenadas.obtenerCentroCasilla(numeroCasilla);
    }

    /**
     * Obtiene información detallada de una casilla.
     * 
     * @param numeroCasilla Número de la casilla (0-39)
     * @return Información de la casilla
     */
    public CoordenadaTablero.InfoCasilla obtenerInfoCasilla(int numeroCasilla) {
        return coordenadas.obtenerInfoCasilla(numeroCasilla);
    }

    /**
     * Actualiza el panel y redibuja.
     */
    public void actualizar() {
        repaint();
    }

    /**
     * Imprime información de debug sobre las coordenadas del tablero.
     */
    public void debugCoordenadas() {
        coordenadas.imprimirDebugCasillas();
    }
}
