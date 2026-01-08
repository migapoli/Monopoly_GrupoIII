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
    private static final int TAMANO_CASILLA = 70;
    
    public PanelTablero(ControladorJuego controlador) {
        this.controlador = controlador;
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
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
        // Fondo de la casilla
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x, y, tamano, tamano);
        
        // Color del grupo si es propiedad
        if (casilla instanceof Propiedad) {
            Propiedad prop = (Propiedad) casilla;
            g2d.setColor(prop.getGrupoColor());
            g2d.fillRect(x, y, tamano, tamano / 5);
            
            // Indicador de propietario
            if (prop.tienePropietario()) {
                g2d.setColor(prop.getPropietario().getColor());
                g2d.fillRect(x + 2, y + 2, 8, 8);
            }
        }
        
        // Borde de la casilla
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(x, y, tamano, tamano);
        
        // Nombre de la casilla (versión corta)
        g2d.setFont(new Font("Arial", Font.PLAIN, 8));
        String nombre = casilla.getNombre();
        if (nombre.length() > 12) {
            nombre = nombre.substring(0, 10) + "...";
        }
        
        // Dibujar texto rotado según el lado del tablero
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(nombre);
        int textHeight = fm.getHeight();
        
        if (posicion <= 10) { // Lado inferior
            g2d.drawString(nombre, x + (tamano - textWidth) / 2, y + tamano - 5);
        } else if (posicion <= 20) { // Lado izquierdo
            Graphics2D g2dRotated = (Graphics2D) g2d.create();
            g2dRotated.rotate(-Math.PI / 2, x + tamano / 2, y + tamano / 2);
            g2dRotated.drawString(nombre, x + (tamano - textWidth) / 2, y + tamano / 2 + textHeight / 3);
            g2dRotated.dispose();
        } else if (posicion <= 30) { // Lado superior
            g2d.drawString(nombre, x + (tamano - textWidth) / 2, y + 15);
        } else { // Lado derecho
            Graphics2D g2dRotated = (Graphics2D) g2d.create();
            g2dRotated.rotate(Math.PI / 2, x + tamano / 2, y + tamano / 2);
            g2dRotated.drawString(nombre, x + (tamano - textWidth) / 2, y + tamano / 2 + textHeight / 3);
            g2dRotated.dispose();
        }
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
        
        // Fondo del centro
        g2d.setColor(Color.WHITE);
        g2d.fillRect(xCentro, yCentro, anchoCentro, altoCentro);
        
        // Borde
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(xCentro, yCentro, anchoCentro, altoCentro);
        
        // Título "TABLERO"
        g2d.setFont(new Font("Arial", Font.BOLD, 28));
        g2d.setColor(Color.BLACK);
        String titulo = "TABLERO";
        FontMetrics fm = g2d.getFontMetrics();
        int tituloX = xCentro + (anchoCentro - fm.stringWidth(titulo)) / 2;
        int tituloY = yCentro + 50;
        g2d.drawString(titulo, tituloX, tituloY);
        
        // Dados
        int dadoSize = 60;
        int dadoY = yCentro + 80;
        int dado1X = xCentro + (anchoCentro / 2) - dadoSize - 10;
        int dado2X = xCentro + (anchoCentro / 2) + 10;
        
        // Dado 1
        g2d.setColor(Color.WHITE);
        g2d.fillRect(dado1X, dadoY, dadoSize, dadoSize);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(dado1X, dadoY, dadoSize, dadoSize);
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        fm = g2d.getFontMetrics();
        String lblDado1 = "Dado 1";
        g2d.drawString(lblDado1, dado1X + (dadoSize - fm.stringWidth(lblDado1)) / 2, dadoY + dadoSize + 15);
        
        // Dado 2
        g2d.setColor(Color.WHITE);
        g2d.fillRect(dado2X, dadoY, dadoSize, dadoSize);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(dado2X, dadoY, dadoSize, dadoSize);
        String lblDado2 = "Dado 2";
        g2d.drawString(lblDado2, dado2X + (dadoSize - fm.stringWidth(lblDado2)) / 2, dadoY + dadoSize + 15);
        
        // Mazos de cartas
        int mazoWidth = 100;
        int mazoHeight = 60;
        int mazoY = yCentro + 200;
        int mazo1X = xCentro + (anchoCentro / 2) - mazoWidth - 10;
        int mazo2X = xCentro + (anchoCentro / 2) + 10;
        
        // Mazo Comunidad
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(mazo1X, mazoY, mazoWidth, mazoHeight);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(mazo1X, mazoY, mazoWidth, mazoHeight);
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        String lblMazo1 = "Mazo Cartas";
        String lblMazo1_2 = "Comunidad";
        fm = g2d.getFontMetrics();
        g2d.drawString(lblMazo1, mazo1X + (mazoWidth - fm.stringWidth(lblMazo1)) / 2, mazoY + mazoHeight / 2 - 3);
        g2d.drawString(lblMazo1_2, mazo1X + (mazoWidth - fm.stringWidth(lblMazo1_2)) / 2, mazoY + mazoHeight / 2 + 12);
        
        // Mazo Suerte
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(mazo2X, mazoY, mazoWidth, mazoHeight);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(mazo2X, mazoY, mazoWidth, mazoHeight);
        String lblMazo2 = "Mazo Cartas";
        String lblMazo2_2 = "Suerte";
        g2d.drawString(lblMazo2, mazo2X + (mazoWidth - fm.stringWidth(lblMazo2)) / 2, mazoY + mazoHeight / 2 - 3);
        g2d.drawString(lblMazo2_2, mazo2X + (mazoWidth - fm.stringWidth(lblMazo2_2)) / 2, mazoY + mazoHeight / 2 + 12);
        
        // Fondo Común
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        String fondoComun = "Fondo Común: 2.000 EUR";
        fm = g2d.getFontMetrics();
        int fondoComunX = xCentro + (anchoCentro - fm.stringWidth(fondoComun)) / 2;
        int fondoComunY = yCentro + altoCentro - 40;
        g2d.drawString(fondoComun, fondoComunX, fondoComunY);
    }
    
    private void dibujarJugadores(Graphics2D g2d, int offsetX, int offsetY, int tamanoTablero) {
        List<IJugador> jugadores = controlador.getPartida().getJugadores();
        int tamanoCasilla = tamanoTablero / CASILLAS_POR_LADO;
        
        for (int i = 0; i < jugadores.size(); i++) {
            IJugador jugador = jugadores.get(i);
            Point posCasilla = obtenerPosicionCasilla(jugador.getPosicion(), offsetX, offsetY, tamanoCasilla);
            
            // Dibujar ficha del jugador
            int fichaSize = 15;
            int offsetFicha = (i % 2) * 20; // Desplazamiento para evitar superposición
            
            g2d.setColor(jugador.getColor());
            g2d.fillOval(
                posCasilla.x + 10 + offsetFicha,
                posCasilla.y + 30 + (i / 2) * 20,
                fichaSize,
                fichaSize
            );
            
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(
                posCasilla.x + 10 + offsetFicha,
                posCasilla.y + 30 + (i / 2) * 20,
                fichaSize,
                fichaSize
            );
        }
    }
    
    public void actualizar() {
        repaint();
    }
}
