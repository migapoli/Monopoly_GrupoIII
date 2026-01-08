package monopoly.view;

import monopoly.view.dto.CasillaDTO;
import monopoly.view.dto.JugadorDTO;
import monopoly.view.dto.TableroDTO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * PANEL TABLERO PURO - Dibuja el tablero usando SOLO DTOs.
 * 
 * ═══════════════════════════════════════════════════════════════════
 * 🎯 EJEMPLO DE VISTA PURA
 * ═══════════════════════════════════════════════════════════════════
 * 
 * Esta clase demuestra cómo renderizar el tablero SIN conocer el Modelo.
 * Toda la información viene del TableroDTO que proporciona el Controller.
 * 
 * IMPORTS PERMITIDOS:
 * ✅ monopoly.view.dto.*
 * ✅ javax.swing.*
 * ✅ java.awt.*
 * 
 * IMPORTS PROHIBIDOS:
 * ❌ monopoly.model.*
 * ❌ monopoly.controller.* (el panel no necesita el controller)
 * 
 * ═══════════════════════════════════════════════════════════════════
 */
public class PanelTableroPuro extends JPanel {

    private static final int CASILLAS_POR_LADO = 11;

    // Estado actual del tablero (recibido como DTO)
    private TableroDTO tableroDTO;

    public PanelTableroPuro() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * Actualiza el tablero con datos del DTO.
     * ¡Este es el método que el Controller llama!
     */
    public void actualizarConDTO(TableroDTO tableroDTO) {
        this.tableroDTO = tableroDTO;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (tableroDTO == null) {
            // Si no hay datos, mostrar mensaje
            g.drawString("Esperando datos del juego...", 50, 50);
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int anchoPanel = getWidth();
        int altoPanel = getHeight();
        int tamanoTablero = Math.min(anchoPanel, altoPanel) - 40;
        int offsetX = (anchoPanel - tamanoTablero) / 2;
        int offsetY = (altoPanel - tamanoTablero) / 2;

        dibujarTablero(g2d, offsetX, offsetY, tamanoTablero);
        dibujarCentro(g2d, offsetX, offsetY, tamanoTablero);
        dibujarJugadores(g2d, offsetX, offsetY, tamanoTablero);
    }

    /**
     * Dibuja las casillas del tablero usando CasillaDTO.
     */
    private void dibujarTablero(Graphics2D g2d, int offsetX, int offsetY, int tamanoTablero) {
        List<CasillaDTO> casillas = tableroDTO.getCasillas();
        int tamanoCasilla = tamanoTablero / CASILLAS_POR_LADO;

        for (int i = 0; i < 40; i++) {
            Point pos = obtenerPosicionCasilla(i, offsetX, offsetY, tamanoCasilla);
            CasillaDTO casillaDTO = casillas.get(i);

            dibujarCasilla(g2d, casillaDTO, pos.x, pos.y, tamanoCasilla, i);
        }
    }

    /**
     * Dibuja una casilla individual usando SOLO el DTO.
     * ¡NO accede a Casilla, Propiedad, ni ninguna clase del Modelo!
     */
    private void dibujarCasilla(Graphics2D g2d, CasillaDTO casilla, int x, int y, int tamano, int posicion) {
        // Fondo de la casilla
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x, y, tamano, tamano);

        // ✅ Usamos el DTO para verificar si es propiedad
        if (casilla.esPropiedad() && casilla.getColorGrupo() != null) {
            g2d.setColor(casilla.getColorGrupo());
            g2d.fillRect(x, y, tamano, tamano / 5);

            // ✅ Usamos el DTO para verificar propietario
            if (casilla.tienePropietario() && casilla.getColorPropietario() != null) {
                g2d.setColor(casilla.getColorPropietario());
                g2d.fillRect(x + 2, y + 2, 8, 8);
            }
        }

        // Borde de la casilla
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(x, y, tamano, tamano);

        // ✅ Usamos getNombre() del DTO
        g2d.setFont(new Font("Arial", Font.PLAIN, 8));
        String nombre = casilla.getNombre();
        if (nombre.length() > 12) {
            nombre = nombre.substring(0, 10) + "...";
        }

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(nombre);
        g2d.drawString(nombre, x + (tamano - textWidth) / 2, y + tamano - 5);
    }

    private Point obtenerPosicionCasilla(int index, int offsetX, int offsetY, int tamanoCasilla) {
        int x = 0, y = 0;

        if (index <= 10) {
            x = offsetX + (10 - index) * tamanoCasilla;
            y = offsetY + 10 * tamanoCasilla;
        } else if (index <= 20) {
            x = offsetX;
            y = offsetY + (20 - index) * tamanoCasilla;
        } else if (index <= 30) {
            x = offsetX + (index - 20) * tamanoCasilla;
            y = offsetY;
        } else {
            x = offsetX + 10 * tamanoCasilla;
            y = offsetY + (index - 30) * tamanoCasilla;
        }

        return new Point(x, y);
    }

    private void dibujarCentro(Graphics2D g2d, int offsetX, int offsetY, int tamanoTablero) {
        int tamanoCasilla = tamanoTablero / CASILLAS_POR_LADO;
        int xCentro = offsetX + tamanoCasilla;
        int yCentro = offsetY + tamanoCasilla;
        int anchoCentro = tamanoCasilla * 9;
        int altoCentro = tamanoCasilla * 9;

        g2d.setColor(Color.WHITE);
        g2d.fillRect(xCentro, yCentro, anchoCentro, altoCentro);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(xCentro, yCentro, anchoCentro, altoCentro);

        // Título
        g2d.setFont(new Font("Arial", Font.BOLD, 28));
        String titulo = "MEGAPOLY";
        FontMetrics fm = g2d.getFontMetrics();
        int tituloX = xCentro + (anchoCentro - fm.stringWidth(titulo)) / 2;
        g2d.drawString(titulo, tituloX, yCentro + 50);

        // Info del turno usando DTO
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        String turnoInfo = "Turno: " + tableroDTO.getNombreJugadorActual();
        fm = g2d.getFontMetrics();
        g2d.drawString(turnoInfo, xCentro + (anchoCentro - fm.stringWidth(turnoInfo)) / 2, yCentro + 100);

        // Fondo común usando DTO
        String fondoComun = "Fondo Común: " + tableroDTO.getFondoComun() + " EUR";
        g2d.drawString(fondoComun, xCentro + (anchoCentro - fm.stringWidth(fondoComun)) / 2, yCentro + altoCentro - 40);
    }

    /**
     * Dibuja los jugadores en el tablero usando SOLO JugadorDTO.
     */
    private void dibujarJugadores(Graphics2D g2d, int offsetX, int offsetY, int tamanoTablero) {
        List<JugadorDTO> jugadores = tableroDTO.getJugadores();
        int tamanoCasilla = tamanoTablero / CASILLAS_POR_LADO;

        for (int i = 0; i < jugadores.size(); i++) {
            JugadorDTO jugador = jugadores.get(i);

            // ✅ Usamos getPosicion() del DTO
            Point posCasilla = obtenerPosicionCasilla(jugador.getPosicion(), offsetX, offsetY, tamanoCasilla);

            int fichaSize = 15;
            int offsetFicha = (i % 2) * 20;

            // ✅ Usamos getColor() del DTO
            g2d.setColor(jugador.getColor());
            g2d.fillOval(
                    posCasilla.x + 10 + offsetFicha,
                    posCasilla.y + 30 + (i / 2) * 20,
                    fichaSize,
                    fichaSize);

            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(
                    posCasilla.x + 10 + offsetFicha,
                    posCasilla.y + 30 + (i / 2) * 20,
                    fichaSize,
                    fichaSize);
        }
    }
}
