package monopoly.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Clase centralizada con el tema visual del juego Megapoly.
 * Define colores, fuentes y métodos de utilidad para crear componentes
 * estilizados.
 */
public class MonopolyTheme {

    // ==================== COLORES PRINCIPALES ====================

    /** Fondo principal oscuro */
    public static final Color FONDO_PRINCIPAL = new Color(26, 31, 46);

    /** Fondo de paneles */
    public static final Color FONDO_PANEL = new Color(37, 43, 61);

    /** Fondo de paneles más claro */
    public static final Color FONDO_PANEL_CLARO = new Color(45, 52, 75);

    /** Color de acento dorado */
    public static final Color ACENTO_DORADO = new Color(255, 215, 0);

    /** Color de acento dorado oscuro */
    public static final Color ACENTO_DORADO_OSCURO = new Color(204, 172, 0);

    /** Texto principal claro (para fondos oscuros) */
    public static final Color TEXTO_PRINCIPAL = new Color(230, 230, 230);

    /** Texto oscuro (para fondos claros) */
    public static final Color TEXTO_OSCURO = new Color(30, 30, 30);

    /** Texto secundario gris */
    public static final Color TEXTO_SECUNDARIO = new Color(160, 168, 184);

    /** Color de éxito verde */
    public static final Color COLOR_EXITO = new Color(46, 204, 113);

    /** Color de error rojo */
    public static final Color COLOR_ERROR = new Color(231, 76, 60);

    /** Color de advertencia amarillo */
    public static final Color COLOR_ADVERTENCIA = new Color(241, 196, 15);

    /** Bordes sutiles */
    public static final Color BORDE_SUTIL = new Color(58, 66, 89);

    /** Header oscuro */
    public static final Color HEADER_OSCURO = new Color(20, 24, 35);

    // ==================== COLORES DEL TABLERO ====================

    /** Fondo del tablero */
    public static final Color TABLERO_FONDO = new Color(200, 230, 201);

    /** Centro del tablero */
    public static final Color TABLERO_CENTRO = new Color(232, 245, 233);

    // ==================== FUENTES ====================

    /** Fuente para títulos grandes */
    public static final Font FUENTE_TITULO_GRANDE = new Font("Georgia", Font.BOLD, 42);

    /** Fuente para títulos */
    public static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 18);

    /** Fuente para subtítulos */
    public static final Font FUENTE_SUBTITULO = new Font("Arial", Font.BOLD, 14);

    /** Fuente para texto normal */
    public static final Font FUENTE_NORMAL = new Font("Arial", Font.PLAIN, 13);

    /** Fuente para texto pequeño */
    public static final Font FUENTE_PEQUENA = new Font("Arial", Font.PLAIN, 11);

    /** Fuente para botones */
    public static final Font FUENTE_BOTON = new Font("Arial", Font.BOLD, 14);

    // ==================== MÉTODOS DE UTILIDAD ====================

    /**
     * Crea un borde redondeado con el color de borde sutil.
     */
    public static Border crearBordePanel() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE_SUTIL, 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }

    /**
     * Crea un botón estilizado con el tema del juego.
     */
    public static JButton crearBotonPrimario(String texto) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradiente de fondo
                GradientPaint gradient;
                if (getModel().isPressed()) {
                    gradient = new GradientPaint(0, 0, ACENTO_DORADO_OSCURO, 0, getHeight(), ACENTO_DORADO.darker());
                } else if (getModel().isRollover()) {
                    gradient = new GradientPaint(0, 0, ACENTO_DORADO.brighter(), 0, getHeight(), ACENTO_DORADO);
                } else {
                    gradient = new GradientPaint(0, 0, ACENTO_DORADO, 0, getHeight(), ACENTO_DORADO_OSCURO);
                }

                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                // Texto
                g2.setColor(HEADER_OSCURO);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };
        boton.setFont(FUENTE_BOTON);
        boton.setForeground(HEADER_OSCURO);
        boton.setPreferredSize(new Dimension(180, 50));
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    /**
     * Crea un botón secundario (oscuro) estilizado.
     */
    public static JButton crearBotonSecundario(String texto) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color colorFondo;
                if (getModel().isPressed()) {
                    colorFondo = FONDO_PANEL.darker();
                } else if (getModel().isRollover()) {
                    colorFondo = FONDO_PANEL_CLARO;
                } else {
                    colorFondo = FONDO_PANEL;
                }

                g2.setColor(colorFondo);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                // Borde
                g2.setColor(BORDE_SUTIL);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

                // Texto
                g2.setColor(TEXTO_PRINCIPAL);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };
        boton.setFont(FUENTE_BOTON);
        boton.setForeground(TEXTO_PRINCIPAL);
        boton.setPreferredSize(new Dimension(180, 50));
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    /**
     * Crea un campo de texto estilizado.
     */
    public static JTextField crearCampoTexto(String placeholder) {
        JTextField campo = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(FONDO_PANEL_CLARO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        campo.setFont(FUENTE_NORMAL);
        campo.setForeground(TEXTO_PRINCIPAL);
        campo.setCaretColor(ACENTO_DORADO);
        campo.setOpaque(false);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE_SUTIL, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        campo.setPreferredSize(new Dimension(200, 40));

        // Placeholder
        campo.setText(placeholder);
        campo.setForeground(TEXTO_SECUNDARIO);
        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(TEXTO_PRINCIPAL);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (campo.getText().isEmpty()) {
                    campo.setText(placeholder);
                    campo.setForeground(TEXTO_SECUNDARIO);
                }
            }
        });

        return campo;
    }

    /**
     * Crea un panel con fondo de glassmorphism.
     */
    public static JPanel crearPanelGlass() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo semi-transparente
                g2.setColor(new Color(FONDO_PANEL.getRed(), FONDO_PANEL.getGreen(), FONDO_PANEL.getBlue(), 230));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Borde sutil
                g2.setColor(BORDE_SUTIL);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

                g2.dispose();
            }
        };
        panel.setOpaque(false);
        return panel;
    }

    /**
     * Aplica el tema oscuro a los componentes Swing por defecto.
     */
    public static void aplicarTemaGlobal() {
        try {
            UIManager.put("Panel.background", FONDO_PRINCIPAL);
            UIManager.put("OptionPane.background", FONDO_PANEL);
            UIManager.put("OptionPane.messageForeground", TEXTO_PRINCIPAL);
            UIManager.put("Button.background", FONDO_PANEL);
            UIManager.put("Button.foreground", TEXTO_PRINCIPAL);
            UIManager.put("ComboBox.background", FONDO_PANEL_CLARO);
            UIManager.put("ComboBox.foreground", TEXTO_PRINCIPAL);
            UIManager.put("TextField.background", FONDO_PANEL_CLARO);
            UIManager.put("TextField.foreground", TEXTO_PRINCIPAL);
            UIManager.put("TextField.caretForeground", ACENTO_DORADO);
            UIManager.put("ScrollPane.background", FONDO_PANEL);
            UIManager.put("ScrollBar.background", FONDO_PANEL);
            UIManager.put("ScrollBar.thumb", BORDE_SUTIL);
            UIManager.put("TextArea.background", FONDO_PANEL);
            UIManager.put("TextArea.foreground", TEXTO_PRINCIPAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
