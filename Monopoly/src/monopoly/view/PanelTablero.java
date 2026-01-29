package monopoly.view;

import monopoly.controller.ControladorJuego;
import monopoly.model.casilla.Casilla;
import monopoly.model.casilla.Propiedad;
import monopoly.model.jugador.IJugador;
import monopoly.model.tablero.Tablero;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelTablero extends JPanel {
    private ControladorJuego controlador;
    private static final int CASILLAS_POR_LADO = 11;

    // Colores vibrantes para el tablero
    private static final Color COLOR_TABLERO_FONDO = new Color(200, 230, 201);
    private static final Color COLOR_TABLERO_CENTRO = new Color(232, 245, 233);
    private static final Color COLOR_CASILLA_FONDO = new Color(255, 255, 255);
    private static final Color COLOR_BORDE_CASILLA = new Color(50, 60, 80);

    public PanelTablero(ControladorJuego controlador) {
        this.controlador = controlador;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int anchoPanel = getWidth();
        int altoPanel = getHeight();
        int tamanoTablero = Math.min(anchoPanel, altoPanel) - 20;
        int offsetX = (anchoPanel - tamanoTablero) / 2;
        int offsetY = (altoPanel - tamanoTablero) / 2;

        // Dibujar sombra del tablero
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillRoundRect(offsetX + 8, offsetY + 8, tamanoTablero, tamanoTablero, 15, 15);

        // Dibujar fondo del tablero
        g2d.setColor(COLOR_TABLERO_FONDO);
        g2d.fillRoundRect(offsetX, offsetY, tamanoTablero, tamanoTablero, 15, 15);

        dibujarTablero(g2d, offsetX, offsetY, tamanoTablero);
        dibujarCentro(g2d, offsetX, offsetY, tamanoTablero);
        dibujarJugadores(g2d, offsetX, offsetY, tamanoTablero);
    }

    private void dibujarTablero(Graphics2D g2d, int offsetX, int offsetY, int tamanoTablero) {
        Tablero tablero = controlador.getPartida().getTablero();
        List<Casilla> casillas = tablero.getCasillas();

        int tamanoCasilla = tamanoTablero / CASILLAS_POR_LADO;

        for (int i = 0; i < 40; i++) {
            Point pos = obtenerPosicionCasilla(i, offsetX, offsetY, tamanoCasilla);
            Casilla casilla = casillas.get(i);

            dibujarCasilla(g2d, casilla, pos.x, pos.y, tamanoCasilla, i);
        }
    }

    private void dibujarCasilla(Graphics2D g2d, Casilla casilla, int x, int y, int tamano, int posicion) {
        // Determinar orientación
        int lado = posicion / 10; // 0=abajo, 1=izq, 2=arriba, 3=derecha
        boolean esEsquina = posicion % 10 == 0;

        // Fondo de la casilla
        g2d.setColor(COLOR_CASILLA_FONDO);
        g2d.fillRect(x, y, tamano, tamano);

        // Color del grupo si es propiedad
        if (casilla instanceof Propiedad) {
            Propiedad prop = (Propiedad) casilla;
            Color grupoColor = prop.getGrupoColor();

            // Barra de color según orientación
            int barraAltura = tamano / 4;
            g2d.setColor(grupoColor);

            switch (lado) {
                case 0: // Inferior
                    g2d.fillRect(x, y, tamano, barraAltura);
                    break;
                case 1: // Izquierdo
                    g2d.fillRect(x + tamano - barraAltura, y, barraAltura, tamano);
                    break;
                case 2: // Superior
                    g2d.fillRect(x, y + tamano - barraAltura, tamano, barraAltura);
                    break;
                case 3: // Derecho
                    g2d.fillRect(x, y, barraAltura, tamano);
                    break;
            }

            // Indicador de propietario
            if (prop.tienePropietario()) {
                g2d.setColor(prop.getPropietario().getColor());
                g2d.fillOval(x + tamano - 14, y + tamano - 14, 10, 10);
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawOval(x + tamano - 14, y + tamano - 14, 10, 10);
            }

            // Indicador de casas
            int numCasas = prop.getCasas();
            if (numCasas > 0 || prop.tieneHotel()) {
                g2d.setColor(prop.tieneHotel() ? new Color(220, 20, 60) : new Color(0, 150, 0));
                int numIndicadores = prop.tieneHotel() ? 1 : numCasas;
                for (int i = 0; i < numIndicadores; i++) {
                    int casaX = x + 4 + (i * 8);
                    int casaY = y + 4;
                    g2d.fillRect(casaX, casaY, 6, 6);
                }
            }
        }

        // Borde de la casilla
        g2d.setColor(COLOR_BORDE_CASILLA);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRect(x, y, tamano, tamano);

        // Nombre de la casilla
        String nombre = casilla.getNombre();
        if (nombre.length() > 10 && !esEsquina) {
            nombre = nombre.substring(0, 8) + "..";
        }

        g2d.setFont(new Font("Arial", Font.BOLD, 8));
        g2d.setColor(new Color(40, 50, 70));
        FontMetrics fm = g2d.getFontMetrics();

        // Dibujar texto según orientación
        if (esEsquina) {
            // Esquinas: texto horizontal centrado
            int textWidth = fm.stringWidth(nombre);
            g2d.drawString(nombre, x + (tamano - textWidth) / 2, y + tamano / 2 + fm.getAscent() / 2);
        } else {
            switch (lado) {
                case 0: // Inferior
                    dibujarTextoVertical(g2d, nombre, x + tamano / 2, y + tamano - 8, true);
                    break;
                case 1: // Izquierdo
                    dibujarTextoHorizontal(g2d, nombre, x + 4, y + tamano / 2);
                    break;
                case 2: // Superior
                    dibujarTextoVertical(g2d, nombre, x + tamano / 2, y + 12, false);
                    break;
                case 3: // Derecho
                    dibujarTextoHorizontal(g2d, nombre, x + tamano - fm.stringWidth(nombre) - 4, y + tamano / 2);
                    break;
            }
        }
    }

    private void dibujarTextoVertical(Graphics2D g2d, String texto, int x, int y, boolean arribaAbajo) {
        Graphics2D g2dRotated = (Graphics2D) g2d.create();
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(texto);

        g2dRotated.translate(x, y);
        g2dRotated.rotate(arribaAbajo ? Math.PI / 2 : -Math.PI / 2);
        g2dRotated.drawString(texto, -textWidth / 2, fm.getAscent() / 2);
        g2dRotated.dispose();
    }

    private void dibujarTextoHorizontal(Graphics2D g2d, String texto, int x, int y) {
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(texto, x, y + fm.getAscent() / 2);
    }

    private Point obtenerPosicionCasilla(int index, int offsetX, int offsetY, int tamanoCasilla) {
        int x = 0, y = 0;

        if (index <= 10) {
            // Lado inferior (derecha a izquierda)
            x = offsetX + (10 - index) * tamanoCasilla;
            y = offsetY + 10 * tamanoCasilla;
        } else if (index <= 20) {
            // Lado izquierdo (abajo a arriba)
            x = offsetX;
            y = offsetY + (20 - index) * tamanoCasilla;
        } else if (index <= 30) {
            // Lado superior (izquierda a derecha)
            x = offsetX + (index - 20) * tamanoCasilla;
            y = offsetY;
        } else {
            // Lado derecho (arriba a abajo)
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

        // Fondo del centro con gradiente
        GradientPaint gradient = new GradientPaint(
                xCentro, yCentro, COLOR_TABLERO_CENTRO,
                xCentro + anchoCentro, yCentro + altoCentro, new Color(220, 237, 222));
        g2d.setPaint(gradient);
        g2d.fillRect(xCentro, yCentro, anchoCentro, altoCentro);

        // Título "MEGAPOLY"
        g2d.setFont(new Font("Georgia", Font.BOLD, 36));
        g2d.setColor(new Color(40, 55, 70));
        String titulo = "MEGAPOLY";
        FontMetrics fm = g2d.getFontMetrics();
        int tituloX = xCentro + (anchoCentro - fm.stringWidth(titulo)) / 2;
        int tituloY = yCentro + 60;
        g2d.drawString(titulo, tituloX, tituloY);

        // Subtítulo
        g2d.setFont(new Font("Georgia", Font.ITALIC, 16));
        g2d.setColor(new Color(80, 90, 100));
        String subtitulo = "The Game";
        fm = g2d.getFontMetrics();
        int subX = xCentro + (anchoCentro - fm.stringWidth(subtitulo)) / 2;
        g2d.drawString(subtitulo, subX, tituloY + 25);

        // Línea decorativa dorada
        g2d.setColor(MonopolyTheme.ACENTO_DORADO);
        g2d.setStroke(new BasicStroke(3));
        int lineaY = tituloY + 40;
        g2d.drawLine(xCentro + anchoCentro / 4, lineaY, xCentro + 3 * anchoCentro / 4, lineaY);

        // Dados
        int dadoSize = 55;
        int dadoY = yCentro + 110;
        int dado1X = xCentro + (anchoCentro / 2) - dadoSize - 15;
        int dado2X = xCentro + (anchoCentro / 2) + 15;

        dibujarDado(g2d, dado1X, dadoY, dadoSize, 1);
        dibujarDado(g2d, dado2X, dadoY, dadoSize, 1);

        // Mazos de cartas
        int mazoWidth = 90;
        int mazoHeight = 55;
        int mazoY = yCentro + 200;
        int mazo1X = xCentro + (anchoCentro / 2) - mazoWidth - 15;
        int mazo2X = xCentro + (anchoCentro / 2) + 15;

        // Mazo Comunidad
        dibujarMazo(g2d, mazo1X, mazoY, mazoWidth, mazoHeight, "CARTAS", "COMUNIDAD", new Color(135, 206, 250));

        // Mazo Suerte
        dibujarMazo(g2d, mazo2X, mazoY, mazoWidth, mazoHeight, "CARTAS", "SUERTE", new Color(255, 165, 0));

        // Fondo Común
        g2d.setFont(new Font("Arial", Font.BOLD, 13));
        g2d.setColor(new Color(60, 70, 85));
        String fondoComun = "💰 Fondo Común: 2.000 EUR";
        fm = g2d.getFontMetrics();
        int fondoComunX = xCentro + (anchoCentro - fm.stringWidth(fondoComun)) / 2;
        int fondoComunY = yCentro + altoCentro - 50;
        g2d.drawString(fondoComun, fondoComunX, fondoComunY);
    }

    private void dibujarDado(Graphics2D g2d, int x, int y, int size, int valor) {
        // Sombra
        g2d.setColor(new Color(0, 0, 0, 40));
        g2d.fillRoundRect(x + 3, y + 3, size, size, 10, 10);

        // Fondo del dado
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(x, y, size, size, 10, 10);

        // Borde
        g2d.setColor(new Color(60, 70, 85));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, size, size, 10, 10);

        // Puntos del dado
        g2d.setColor(new Color(40, 50, 70));
        int puntoSize = size / 6;
        int centroX = x + size / 2;
        int centroY = y + size / 2;
        int offset = size / 4;

        // Punto central (para valores impares)
        if (valor == 1 || valor == 3 || valor == 5) {
            g2d.fillOval(centroX - puntoSize / 2, centroY - puntoSize / 2, puntoSize, puntoSize);
        }

        // Esquinas superiores
        if (valor >= 4) {
            g2d.fillOval(x + offset - puntoSize / 2, y + offset - puntoSize / 2, puntoSize, puntoSize);
            g2d.fillOval(x + size - offset - puntoSize / 2, y + offset - puntoSize / 2, puntoSize, puntoSize);
        }

        // Esquinas inferiores
        if (valor >= 4) {
            g2d.fillOval(x + offset - puntoSize / 2, y + size - offset - puntoSize / 2, puntoSize, puntoSize);
            g2d.fillOval(x + size - offset - puntoSize / 2, y + size - offset - puntoSize / 2, puntoSize, puntoSize);
        }

        // Diagonal para 2 y 3
        if (valor == 2 || valor == 3) {
            g2d.fillOval(x + offset - puntoSize / 2, y + offset - puntoSize / 2, puntoSize, puntoSize);
            g2d.fillOval(x + size - offset - puntoSize / 2, y + size - offset - puntoSize / 2, puntoSize, puntoSize);
        }

        // Laterales para 6
        if (valor == 6) {
            g2d.fillOval(x + offset - puntoSize / 2, centroY - puntoSize / 2, puntoSize, puntoSize);
            g2d.fillOval(x + size - offset - puntoSize / 2, centroY - puntoSize / 2, puntoSize, puntoSize);
        }

        // Laterales para 5
        if (valor == 5) {
            g2d.fillOval(x + offset - puntoSize / 2, y + offset - puntoSize / 2, puntoSize, puntoSize);
            g2d.fillOval(x + size - offset - puntoSize / 2, y + offset - puntoSize / 2, puntoSize, puntoSize);
            g2d.fillOval(x + offset - puntoSize / 2, y + size - offset - puntoSize / 2, puntoSize, puntoSize);
            g2d.fillOval(x + size - offset - puntoSize / 2, y + size - offset - puntoSize / 2, puntoSize, puntoSize);
        }
    }

    private void dibujarMazo(Graphics2D g2d, int x, int y, int width, int height, String linea1, String linea2,
            Color color) {
        // Efecto 3D de cartas apiladas
        for (int i = 2; i >= 0; i--) {
            g2d.setColor(new Color(0, 0, 0, 20));
            g2d.fillRoundRect(x + i * 2, y + i * 2, width, height, 8, 8);
        }

        // Carta principal
        g2d.setColor(color);
        g2d.fillRoundRect(x, y, width, height, 8, 8);

        // Borde
        g2d.setColor(color.darker());
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, width, height, 8, 8);

        // Texto
        g2d.setFont(new Font("Arial", Font.BOLD, 9));
        g2d.setColor(new Color(40, 50, 65));
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(linea1, x + (width - fm.stringWidth(linea1)) / 2, y + height / 2 - 3);
        g2d.drawString(linea2, x + (width - fm.stringWidth(linea2)) / 2, y + height / 2 + 12);
    }

    private void dibujarJugadores(Graphics2D g2d, int offsetX, int offsetY, int tamanoTablero) {
        List<IJugador> jugadores = controlador.getPartida().getJugadores();
        int tamanoCasilla = tamanoTablero / CASILLAS_POR_LADO;

        for (int i = 0; i < jugadores.size(); i++) {
            IJugador jugador = jugadores.get(i);
            Point posCasilla = obtenerPosicionCasilla(jugador.getPosicion(), offsetX, offsetY, tamanoCasilla);

            // Calcular offset para evitar superposición
            int fichaSize = 18;
            int offsetFichaX = 8 + (i % 2) * 22;
            int offsetFichaY = tamanoCasilla / 2 + (i / 2) * 20;

            int fichaX = posCasilla.x + offsetFichaX;
            int fichaY = posCasilla.y + offsetFichaY;

            // Sombra de la ficha
            g2d.setColor(new Color(0, 0, 0, 60));
            g2d.fillOval(fichaX + 2, fichaY + 2, fichaSize, fichaSize);

            // Ficha del jugador
            g2d.setColor(jugador.getColor());
            g2d.fillOval(fichaX, fichaY, fichaSize, fichaSize);

            // Brillo
            g2d.setColor(new Color(255, 255, 255, 80));
            g2d.fillOval(fichaX + 3, fichaY + 3, fichaSize / 3, fichaSize / 3);

            // Borde
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(fichaX, fichaY, fichaSize, fichaSize);
        }
    }

    public void actualizar() {
        repaint();
    }
}
