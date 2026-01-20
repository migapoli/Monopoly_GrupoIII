package monopoly.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Diálogo para mostrar cartas de Suerte/Comunidad con diseño moderno.
 */
public class DialogoCartaEvento extends JDialog {

    // Colores del diseño
    private static final Color COLOR_HEADER = new Color(45, 55, 75);
    private static final Color COLOR_FONDO = new Color(240, 240, 245);
    private static final Color COLOR_PANEL = Color.WHITE;
    private static final Color COLOR_AMARILLO = new Color(255, 193, 7);
    private static final Color COLOR_TEXTO = new Color(35, 45, 65);
    private static final Color COLOR_BOTON = new Color(55, 65, 85);

    public DialogoCartaEvento(JFrame parent, String tipoCarta, String descripcion, String efecto) {
        super(parent, "Carta/Evento", true);

        setUndecorated(true);
        inicializarComponentes(tipoCarta, descripcion, efecto);
        setSize(400, 380);
        setLocationRelativeTo(parent);
    }

    private void inicializarComponentes(String tipoCarta, String descripcion, String efecto) {
        setLayout(new BorderLayout());

        // Panel principal con borde redondeado
        JPanel panelPrincipal = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(new LineBorder(new Color(200, 200, 205), 1, true));

        // Header oscuro
        JPanel panelHeader = crearPanelHeader();

        // Panel de contenido
        JPanel panelContenido = crearPanelContenido(tipoCarta, descripcion, efecto);

        // Panel de botones
        JPanel panelBotones = crearPanelBotones();

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private JPanel crearPanelHeader() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_HEADER);
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + 10, 15, 15);
                g2.fillRect(0, 10, getWidth(), getHeight());
            }
        };
        panel.setPreferredSize(new Dimension(400, 50));
        panel.setOpaque(false);

        JLabel lblTitulo = new JLabel("CARTA / EVENTO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(new EmptyBorder(15, 0, 15, 0));

        panel.add(lblTitulo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelContenido(String tipoCarta, String descripcion, String efecto) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(20, 25, 15, 25));

        // Panel de la carta (con borde y línea amarilla)
        JPanel panelCarta = new JPanel();
        panelCarta.setLayout(new BoxLayout(panelCarta, BoxLayout.Y_AXIS));
        panelCarta.setBackground(COLOR_PANEL);
        panelCarta.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(180, 180, 185), 1),
                new EmptyBorder(0, 0, 0, 0)));
        panelCarta.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCarta.setMaximumSize(new Dimension(350, 180));

        // Encabezado de la carta
        JPanel panelEncabezado = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelEncabezado.setBackground(COLOR_PANEL);
        panelEncabezado.setBorder(new EmptyBorder(12, 0, 5, 0));

        JLabel lblEncabezado = new JLabel("Encabezado de la carta/evento");
        lblEncabezado.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 14));
        lblEncabezado.setForeground(COLOR_TEXTO);

        panelEncabezado.add(lblEncabezado);

        // Línea amarilla decorativa
        JPanel lineaAmarilla = new JPanel();
        lineaAmarilla.setBackground(COLOR_AMARILLO);
        lineaAmarilla.setPreferredSize(new Dimension(280, 4));
        lineaAmarilla.setMaximumSize(new Dimension(280, 4));

        JPanel panelLineaAmarilla = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelLineaAmarilla.setBackground(COLOR_PANEL);
        panelLineaAmarilla.setBorder(new EmptyBorder(0, 0, 10, 0));
        panelLineaAmarilla.add(lineaAmarilla);

        // Tipo de carta
        JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTipo.setBackground(COLOR_PANEL);

        String emoji = tipoCarta.contains("SUERTE") ? "🍀" : "📋";
        JLabel lblTipo = new JLabel(emoji + " " + tipoCarta + " " + emoji);
        lblTipo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        lblTipo.setForeground(COLOR_TEXTO);

        panelTipo.add(lblTipo);

        // Descripción
        JPanel panelDesc = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelDesc.setBackground(COLOR_PANEL);
        panelDesc.setBorder(new EmptyBorder(5, 10, 10, 10));

        JLabel lblDesc = new JLabel("<html><center><i>\"" + descripcion + "\"</i></center></html>");
        lblDesc.setFont(new Font("Arial", Font.ITALIC, 11));
        lblDesc.setForeground(new Color(100, 100, 105));
        lblDesc.setHorizontalAlignment(SwingConstants.CENTER);

        panelDesc.add(lblDesc);

        panelCarta.add(panelEncabezado);
        panelCarta.add(panelLineaAmarilla);
        panelCarta.add(panelTipo);
        panelCarta.add(panelDesc);

        panel.add(panelCarta);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Efecto de la carta
        JPanel panelEfecto = new JPanel();
        panelEfecto.setLayout(new BoxLayout(panelEfecto, BoxLayout.Y_AXIS));
        panelEfecto.setBackground(COLOR_FONDO);
        panelEfecto.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblEfectoTitulo = new JLabel("Efecto sobre el jugador:");
        lblEfectoTitulo.setFont(new Font("Arial", Font.ITALIC, 11));
        lblEfectoTitulo.setForeground(new Color(100, 100, 105));
        lblEfectoTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblEfecto = new JLabel(efecto);
        lblEfecto.setFont(new Font("Arial", Font.BOLD, 14));
        lblEfecto.setForeground(COLOR_AMARILLO.darker());
        lblEfecto.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelEfecto.add(lblEfectoTitulo);
        panelEfecto.add(Box.createRigidArea(new Dimension(0, 5)));
        panelEfecto.add(lblEfecto);

        panel.add(panelEfecto);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(5, 0, 20, 0));

        JButton btnAceptar = crearBotonEstilizado("✓ ACEPTAR");
        btnAceptar.addActionListener(e -> dispose());

        panel.add(btnAceptar);

        return panel;
    }

    private JButton crearBotonEstilizado(String texto) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(COLOR_BOTON.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(COLOR_BOTON.brighter());
                } else {
                    g2.setColor(COLOR_BOTON);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setForeground(Color.WHITE);
        boton.setPreferredSize(new Dimension(150, 42));
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
}
