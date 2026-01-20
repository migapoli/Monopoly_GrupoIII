package monopoly.view;

import monopoly.model.jugador.IJugador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Diálogo para gestionar las opciones de la cárcel con diseño moderno.
 */
public class DialogoCarcel extends JDialog {

    private boolean pagarFianza = false;
    private boolean intentarDobles = false;

    // Colores del diseño
    private static final Color COLOR_HEADER = new Color(70, 70, 85);
    private static final Color COLOR_FONDO = new Color(240, 240, 245);
    private static final Color COLOR_PANEL = Color.WHITE;
    private static final Color COLOR_NARANJA = new Color(255, 152, 0);
    private static final Color COLOR_TEXTO = new Color(35, 45, 65);
    private static final Color COLOR_BOTON = new Color(55, 65, 85);
    private static final Color COLOR_BOTON_VERDE = new Color(40, 167, 69);

    private static final int FIANZA = 50;

    public DialogoCarcel(JFrame parent, IJugador jugador, int turnosEnCarcel) {
        super(parent, "Estás en la Cárcel", true);

        setUndecorated(true);
        inicializarComponentes(jugador, turnosEnCarcel);
        setSize(400, 340);
        setLocationRelativeTo(parent);
    }

    private void inicializarComponentes(IJugador jugador, int turnosEnCarcel) {
        setLayout(new BorderLayout());

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

        JPanel panelHeader = crearPanelHeader();
        JPanel panelContenido = crearPanelContenido(jugador, turnosEnCarcel);
        JPanel panelBotones = crearPanelBotones(jugador);

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

        JLabel lblTitulo = new JLabel("🔒 ESTÁS EN LA CÁRCEL 🔒");
        lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(new EmptyBorder(15, 0, 15, 0));

        panel.add(lblTitulo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelContenido(IJugador jugador, int turnosEnCarcel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(20, 30, 15, 30));

        // Panel de información
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(COLOR_PANEL);
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(180, 180, 185), 1),
                new EmptyBorder(15, 20, 15, 20)));
        panelInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelInfo.setMaximumSize(new Dimension(340, 120));

        // Turnos en cárcel
        JLabel lblTurnos = new JLabel("Turno " + turnosEnCarcel + " de 3 en la cárcel");
        lblTurnos.setFont(new Font("Arial", Font.BOLD, 14));
        lblTurnos.setForeground(COLOR_NARANJA);
        lblTurnos.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelInfo.add(lblTurnos);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 15)));

        // Opciones disponibles
        JLabel lblOpciones = new JLabel("<html><center>Puedes:<br>" +
                "• Pagar " + FIANZA + " EUR de fianza<br>" +
                "• Intentar sacar dobles en los dados</center></html>");
        lblOpciones.setFont(new Font("Arial", Font.PLAIN, 12));
        lblOpciones.setForeground(COLOR_TEXTO);
        lblOpciones.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelInfo.add(lblOpciones);

        panel.add(panelInfo);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Dinero disponible
        JPanel panelDinero = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelDinero.setBackground(COLOR_FONDO);

        JLabel lblDinero = new JLabel("Tu dinero: ");
        lblDinero.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDinero.setForeground(COLOR_TEXTO);

        JLabel lblCantidad = new JLabel(String.format("%,d EUR", jugador.getDinero()).replace(",", "."));
        lblCantidad.setFont(new Font("Arial", Font.BOLD, 12));
        lblCantidad.setForeground(jugador.getDinero() >= FIANZA ? COLOR_BOTON_VERDE : new Color(220, 53, 69));

        panelDinero.add(lblDinero);
        panelDinero.add(lblCantidad);

        panel.add(panelDinero);

        if (turnosEnCarcel >= 3) {
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
            JLabel lblObligatorio = new JLabel("⚠️ Debes pagar la fianza obligatoriamente");
            lblObligatorio.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));
            lblObligatorio.setForeground(new Color(220, 53, 69));
            lblObligatorio.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(lblObligatorio);
        }

        return panel;
    }

    private JPanel crearPanelBotones(IJugador jugador) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(5, 0, 20, 0));

        boolean puedepagarFianza = jugador.puedePermitirse(FIANZA);

        JButton btnPagarFianza = crearBotonEstilizado("💰 PAGAR FIANZA", COLOR_BOTON_VERDE);
        btnPagarFianza.setEnabled(puedepagarFianza);
        btnPagarFianza.addActionListener(e -> {
            pagarFianza = true;
            dispose();
        });

        JButton btnTirarDados = crearBotonEstilizado("🎲 TIRAR DADOS", COLOR_BOTON);
        btnTirarDados.addActionListener(e -> {
            intentarDobles = true;
            dispose();
        });

        panel.add(btnPagarFianza);
        panel.add(btnTirarDados);

        return panel;
    }

    private JButton crearBotonEstilizado(String texto, Color colorBase) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color color = isEnabled() ? colorBase : Color.GRAY;

                if (getModel().isPressed() && isEnabled()) {
                    g2.setColor(color.darker());
                } else if (getModel().isRollover() && isEnabled()) {
                    g2.setColor(color.brighter());
                } else {
                    g2.setColor(color);
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
        boton.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));
        boton.setForeground(Color.WHITE);
        boton.setPreferredSize(new Dimension(150, 40));
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    public boolean isPagarFianza() {
        return pagarFianza;
    }

    public boolean isIntentarDobles() {
        return intentarDobles;
    }
}
