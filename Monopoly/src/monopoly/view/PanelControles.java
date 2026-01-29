package monopoly.view;

import monopoly.controller.ControladorJuego;

import javax.swing.*;
import java.awt.*;

public class PanelControles extends JPanel {
    private ControladorJuego controlador;
    private JButton btnTirarDados;
    private JButton btnFinalizarTurno;
    private JPanel panelDados;
    private int valorDado1 = 1;
    private int valorDado2 = 1;

    public PanelControles(ControladorJuego controlador) {
        this.controlador = controlador;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 15));
        setOpaque(false);

        // Panel contenedor con efecto glass
        JPanel panelContenedor = new JPanel(new BorderLayout(10, 15)) {
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
        panelContenedor.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título del panel
        JLabel lblTitulo = new JLabel("PANEL DE JUEGO");
        lblTitulo.setFont(MonopolyTheme.FUENTE_TITULO);
        lblTitulo.setForeground(MonopolyTheme.ACENTO_DORADO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        // Panel de dados visual
        panelDados = crearPanelDados();

        // Panel de botones
        JPanel panelBotones = crearPanelBotones();

        panelContenedor.add(lblTitulo, BorderLayout.NORTH);
        panelContenedor.add(panelDados, BorderLayout.CENTER);
        panelContenedor.add(panelBotones, BorderLayout.SOUTH);

        add(panelContenedor, BorderLayout.CENTER);
    }

    private JPanel crearPanelDados() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int dadoSize = 70;
                int gap = 25;
                int totalWidth = dadoSize * 2 + gap;
                int startX = (getWidth() - totalWidth) / 2;
                int startY = (getHeight() - dadoSize) / 2;

                // Dado 1
                dibujarDadoVisual(g2, startX, startY, dadoSize, valorDado1);

                // Signo +
                g2.setFont(new Font("Arial", Font.BOLD, 24));
                g2.setColor(MonopolyTheme.TEXTO_SECUNDARIO);
                g2.drawString("+", startX + dadoSize + 5, startY + dadoSize / 2 + 8);

                // Dado 2
                dibujarDadoVisual(g2, startX + dadoSize + gap, startY, dadoSize, valorDado2);

                // Total
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                g2.setColor(MonopolyTheme.ACENTO_DORADO);
                String total = "= " + (valorDado1 + valorDado2);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(total, (getWidth() - fm.stringWidth(total)) / 2, startY + dadoSize + 25);
            }
        };
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(0, 130));
        return panel;
    }

    private void dibujarDadoVisual(Graphics2D g2d, int x, int y, int size, int valor) {
        // Sombra
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillRoundRect(x + 4, y + 4, size, size, 12, 12);

        // Fondo del dado con gradiente
        GradientPaint gradient = new GradientPaint(
                x, y, new Color(255, 255, 255),
                x + size, y + size, new Color(235, 235, 240));
        g2d.setPaint(gradient);
        g2d.fillRoundRect(x, y, size, size, 12, 12);

        // Borde
        g2d.setColor(new Color(80, 90, 110));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, size, size, 12, 12);

        // Puntos del dado
        g2d.setColor(new Color(50, 60, 80));
        int puntoSize = size / 6;
        int centroX = x + size / 2;
        int centroY = y + size / 2;
        int offset = size / 4;

        // Posiciones de los puntos según el valor
        switch (valor) {
            case 1:
                dibujarPunto(g2d, centroX, centroY, puntoSize);
                break;
            case 2:
                dibujarPunto(g2d, x + offset, y + offset, puntoSize);
                dibujarPunto(g2d, x + size - offset, y + size - offset, puntoSize);
                break;
            case 3:
                dibujarPunto(g2d, x + offset, y + offset, puntoSize);
                dibujarPunto(g2d, centroX, centroY, puntoSize);
                dibujarPunto(g2d, x + size - offset, y + size - offset, puntoSize);
                break;
            case 4:
                dibujarPunto(g2d, x + offset, y + offset, puntoSize);
                dibujarPunto(g2d, x + size - offset, y + offset, puntoSize);
                dibujarPunto(g2d, x + offset, y + size - offset, puntoSize);
                dibujarPunto(g2d, x + size - offset, y + size - offset, puntoSize);
                break;
            case 5:
                dibujarPunto(g2d, x + offset, y + offset, puntoSize);
                dibujarPunto(g2d, x + size - offset, y + offset, puntoSize);
                dibujarPunto(g2d, centroX, centroY, puntoSize);
                dibujarPunto(g2d, x + offset, y + size - offset, puntoSize);
                dibujarPunto(g2d, x + size - offset, y + size - offset, puntoSize);
                break;
            case 6:
                dibujarPunto(g2d, x + offset, y + offset, puntoSize);
                dibujarPunto(g2d, x + size - offset, y + offset, puntoSize);
                dibujarPunto(g2d, x + offset, centroY, puntoSize);
                dibujarPunto(g2d, x + size - offset, centroY, puntoSize);
                dibujarPunto(g2d, x + offset, y + size - offset, puntoSize);
                dibujarPunto(g2d, x + size - offset, y + size - offset, puntoSize);
                break;
        }
    }

    private void dibujarPunto(Graphics2D g2d, int x, int y, int size) {
        g2d.fillOval(x - size / 2, y - size / 2, size, size);
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Botón Lanzar Dado
        btnTirarDados = MonopolyTheme.crearBotonPrimario("LANZAR DADOS");
        btnTirarDados.setPreferredSize(new Dimension(200, 45));
        btnTirarDados.setMaximumSize(new Dimension(200, 45));
        btnTirarDados.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTirarDados.addActionListener(e -> {
            controlador.tirarDados();
        });

        // Botón Pasar Turno
        btnFinalizarTurno = MonopolyTheme.crearBotonPrimario("PASAR TURNO");
        btnFinalizarTurno.setPreferredSize(new Dimension(200, 45));
        btnFinalizarTurno.setMaximumSize(new Dimension(200, 45));
        btnFinalizarTurno.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnFinalizarTurno.addActionListener(e -> {
            controlador.finalizarTurno();
        });

        panel.add(btnTirarDados);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnFinalizarTurno);

        return panel;
    }

    public void mostrarDados(int dado1, int dado2) {
        valorDado1 = dado1;
        valorDado2 = dado2;

        // Animación de los dados
        Timer timer = new Timer(80, null);
        final int[] contador = { 0 };
        final int[] tempValor1 = { dado1 };
        final int[] tempValor2 = { dado2 };

        timer.addActionListener(e -> {
            if (contador[0] < 5) {
                // Valores aleatorios durante la animación
                tempValor1[0] = (int) (Math.random() * 6) + 1;
                tempValor2[0] = (int) (Math.random() * 6) + 1;
                valorDado1 = tempValor1[0];
                valorDado2 = tempValor2[0];
                panelDados.repaint();
                contador[0]++;
            } else {
                // Valor final
                valorDado1 = dado1;
                valorDado2 = dado2;
                panelDados.repaint();
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    public void actualizar() {
        repaint();
    }
}
