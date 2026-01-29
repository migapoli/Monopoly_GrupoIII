package monopoly.view;

import monopoly.controller.ControladorJuego;

import javax.swing.*;
import java.awt.*;

public class PanelHistorial extends JPanel {
    private ControladorJuego controlador;
    private JTextArea areaHistorial;

    public PanelHistorial(ControladorJuego controlador) {
        this.controlador = controlador;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(0, 220));

        // Panel contenedor con efecto glass
        JPanel panelContenedor = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(37, 43, 61, 230));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                g2.setColor(MonopolyTheme.BORDE_SUTIL);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

                g2.dispose();
            }
        };
        panelContenedor.setOpaque(false);
        panelContenedor.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Título del panel
        JLabel lblTitulo = new JLabel("📜 HISTORIAL DE EVENTOS");
        lblTitulo.setFont(MonopolyTheme.FUENTE_TITULO);
        lblTitulo.setForeground(MonopolyTheme.ACENTO_DORADO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Área de texto para el historial con estilo consola
        areaHistorial = new JTextArea() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(25, 30, 42));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        areaHistorial.setEditable(false);
        areaHistorial.setFont(new Font("Consolas", Font.PLAIN, 11));
        areaHistorial.setForeground(new Color(180, 190, 200));
        areaHistorial.setCaretColor(MonopolyTheme.ACENTO_DORADO);
        areaHistorial.setLineWrap(true);
        areaHistorial.setWrapStyleWord(true);
        areaHistorial.setOpaque(false);
        areaHistorial.setMargin(new Insets(10, 10, 10, 10));

        // ScrollPane con estilo personalizado
        JScrollPane scrollPane = new JScrollPane(areaHistorial);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createLineBorder(MonopolyTheme.BORDE_SUTIL, 1, true));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Personalizar scrollbar
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = MonopolyTheme.BORDE_SUTIL;
                this.trackColor = MonopolyTheme.FONDO_PANEL;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });
        verticalBar.setPreferredSize(new Dimension(8, 0));

        panelContenedor.add(lblTitulo, BorderLayout.NORTH);
        panelContenedor.add(scrollPane, BorderLayout.CENTER);

        add(panelContenedor, BorderLayout.CENTER);

        // Mensaje inicial
        agregarMensaje("Sistema iniciado - ¡Buena suerte!");
    }

    public void agregarMensaje(String mensaje) {
        String linea = "› " + mensaje + "\n";
        areaHistorial.append(linea);

        // Auto-scroll al final
        areaHistorial.setCaretPosition(areaHistorial.getDocument().getLength());
    }

    public void actualizar() {
        // Cargar todo el historial de la partida
        areaHistorial.setText("");
        for (String evento : controlador.getPartida().getHistorial()) {
            areaHistorial.append("› " + evento + "\n");
        }

        // Auto-scroll al final
        SwingUtilities.invokeLater(() -> {
            areaHistorial.setCaretPosition(areaHistorial.getDocument().getLength());
        });
    }
}
