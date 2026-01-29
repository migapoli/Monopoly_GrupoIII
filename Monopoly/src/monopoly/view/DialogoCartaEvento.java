package monopoly.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Diálogo para mostrar cartas de Suerte/Comunidad con diseño moderno oscuro.
 */
public class DialogoCartaEvento extends JDialog {

    public DialogoCartaEvento(JFrame parent, String tipoCarta, String descripcion, String efecto) {
        super(parent, "Carta/Evento", true);

        setUndecorated(true);
        inicializarComponentes(tipoCarta, descripcion, efecto);
        setSize(420, 380);
        setLocationRelativeTo(parent);
    }

    private void inicializarComponentes(String tipoCarta, String descripcion, String efecto) {
        setLayout(new BorderLayout());

        // Determinar color según tipo de carta
        Color colorCarta = tipoCarta.toUpperCase().contains("SUERTE")
                ? MonopolyTheme.ACENTO_DORADO
                : new Color(100, 181, 246);

        // Panel principal con borde redondeado
        JPanel panelPrincipal = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MonopolyTheme.FONDO_PANEL);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        panelPrincipal.setBackground(MonopolyTheme.FONDO_PANEL);
        panelPrincipal.setBorder(BorderFactory.createLineBorder(MonopolyTheme.BORDE_SUTIL, 1, true));

        // Header
        JPanel panelHeader = crearPanelHeader(colorCarta);

        // Panel de contenido
        JPanel panelContenido = crearPanelContenido(tipoCarta, descripcion, efecto, colorCarta);

        // Panel de botones
        JPanel panelBotones = crearPanelBotones();

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private JPanel crearPanelHeader(Color colorCarta) {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MonopolyTheme.HEADER_OSCURO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + 10, 15, 15);
                g2.fillRect(0, 10, getWidth(), getHeight());
            }
        };
        panel.setPreferredSize(new Dimension(420, 50));
        panel.setOpaque(false);

        JLabel lblTitulo = new JLabel("🎴 CARTA / EVENTO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(colorCarta);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(new EmptyBorder(15, 0, 15, 0));

        panel.add(lblTitulo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelContenido(String tipoCarta, String descripcion, String efecto, Color colorCarta) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(MonopolyTheme.FONDO_PANEL);
        panel.setBorder(new EmptyBorder(20, 25, 15, 25));

        // Panel de la carta
        JPanel panelCarta = new JPanel();
        panelCarta.setLayout(new BoxLayout(panelCarta, BoxLayout.Y_AXIS));
        panelCarta.setBackground(MonopolyTheme.FONDO_PANEL_CLARO);
        panelCarta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MonopolyTheme.BORDE_SUTIL, 1, true),
                new EmptyBorder(0, 0, 0, 0)));
        panelCarta.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCarta.setMaximumSize(new Dimension(370, 180));

        // Barra de color
        JPanel barraCarta = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(colorCarta);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        barraCarta.setPreferredSize(new Dimension(370, 5));
        barraCarta.setMaximumSize(new Dimension(370, 5));
        barraCarta.setOpaque(false);

        // Tipo de carta con emoji
        String emoji = tipoCarta.toUpperCase().contains("SUERTE") ? "🍀" : "📋";
        JLabel lblTipo = new JLabel(emoji + " " + tipoCarta.toUpperCase() + " " + emoji);
        lblTipo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTipo.setForeground(colorCarta);
        lblTipo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTipo.setBorder(new EmptyBorder(15, 0, 10, 0));

        // Línea decorativa
        JPanel lineaDecorativa = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(colorCarta);
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(20, getHeight() / 2, getWidth() - 20, getHeight() / 2);
                g2.dispose();
            }
        };
        lineaDecorativa.setPreferredSize(new Dimension(300, 10));
        lineaDecorativa.setMaximumSize(new Dimension(300, 10));
        lineaDecorativa.setOpaque(false);
        lineaDecorativa.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Descripción
        JLabel lblDesc = new JLabel("<html><center><i>\"" + descripcion + "\"</i></center></html>");
        lblDesc.setFont(new Font("Georgia", Font.ITALIC, 12));
        lblDesc.setForeground(MonopolyTheme.TEXTO_SECUNDARIO);
        lblDesc.setHorizontalAlignment(SwingConstants.CENTER);
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDesc.setBorder(new EmptyBorder(10, 20, 15, 20));

        panelCarta.add(barraCarta);
        panelCarta.add(lblTipo);
        panelCarta.add(lineaDecorativa);
        panelCarta.add(lblDesc);

        panel.add(panelCarta);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Efecto de la carta
        JPanel panelEfecto = new JPanel();
        panelEfecto.setLayout(new BoxLayout(panelEfecto, BoxLayout.Y_AXIS));
        panelEfecto.setOpaque(false);
        panelEfecto.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblEfectoTitulo = new JLabel("Efecto sobre el jugador:");
        lblEfectoTitulo.setFont(new Font("Arial", Font.PLAIN, 11));
        lblEfectoTitulo.setForeground(MonopolyTheme.TEXTO_SECUNDARIO);
        lblEfectoTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblEfecto = new JLabel(efecto);
        lblEfecto.setFont(new Font("Arial", Font.BOLD, 15));
        lblEfecto.setForeground(colorCarta);
        lblEfecto.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelEfecto.add(lblEfectoTitulo);
        panelEfecto.add(Box.createRigidArea(new Dimension(0, 5)));
        panelEfecto.add(lblEfecto);

        panel.add(panelEfecto);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(MonopolyTheme.FONDO_PANEL);
        panel.setBorder(new EmptyBorder(5, 0, 20, 0));

        JButton btnAceptar = MonopolyTheme.crearBotonPrimario("✓ ACEPTAR");
        btnAceptar.setPreferredSize(new Dimension(160, 42));
        btnAceptar.addActionListener(e -> dispose());

        panel.add(btnAceptar);

        return panel;
    }
}
