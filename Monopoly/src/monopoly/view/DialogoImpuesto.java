package monopoly.view;

import monopoly.model.jugador.IJugador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Diálogo para mostrar el pago de impuestos con diseño moderno.
 */
public class DialogoImpuesto extends JDialog {

    // Colores del diseño
    private static final Color COLOR_HEADER = new Color(180, 60, 60);
    private static final Color COLOR_FONDO = new Color(240, 240, 245);
    private static final Color COLOR_PANEL = Color.WHITE;
    private static final Color COLOR_ROJO = new Color(220, 53, 69);
    private static final Color COLOR_TEXTO = new Color(35, 45, 65);
    private static final Color COLOR_BOTON = new Color(55, 65, 85);

    public DialogoImpuesto(JFrame parent, String nombreCasilla, int monto, IJugador jugador) {
        super(parent, "Pago de Impuesto", true);

        setUndecorated(true);
        inicializarComponentes(nombreCasilla, monto, jugador);
        setSize(380, 280);
        setLocationRelativeTo(parent);
    }

    private void inicializarComponentes(String nombreCasilla, int monto, IJugador jugador) {
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

        // Header rojo
        JPanel panelHeader = crearPanelHeader();

        // Contenido
        JPanel panelContenido = crearPanelContenido(nombreCasilla, monto, jugador);

        // Botones
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
        panel.setPreferredSize(new Dimension(380, 50));
        panel.setOpaque(false);

        JLabel lblTitulo = new JLabel("💰 PAGO DE IMPUESTO 💰");
        lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(new EmptyBorder(15, 0, 15, 0));

        panel.add(lblTitulo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelContenido(String nombreCasilla, int monto, IJugador jugador) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(25, 30, 15, 30));

        // Nombre de la casilla
        JLabel lblCasilla = new JLabel(nombreCasilla);
        lblCasilla.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 18));
        lblCasilla.setForeground(COLOR_TEXTO);
        lblCasilla.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblCasilla);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Monto a pagar
        JPanel panelMonto = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelMonto.setBackground(COLOR_FONDO);

        JLabel lblDebes = new JLabel("Debes pagar: ");
        lblDebes.setFont(new Font("Arial", Font.PLAIN, 14));
        lblDebes.setForeground(COLOR_TEXTO);

        JLabel lblMonto = new JLabel(String.format("%,d EUR", monto).replace(",", "."));
        lblMonto.setFont(new Font("Arial", Font.BOLD, 20));
        lblMonto.setForeground(COLOR_ROJO);

        panelMonto.add(lblDebes);
        panelMonto.add(lblMonto);

        panel.add(panelMonto);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Dinero actual
        JPanel panelDinero = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelDinero.setBackground(COLOR_FONDO);

        JLabel lblDineroActual = new JLabel("Tu dinero actual: ");
        lblDineroActual.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDineroActual.setForeground(COLOR_TEXTO);

        int dineroRestante = jugador.getDinero() - monto;
        JLabel lblDineroCantidad = new JLabel(String.format("%,d EUR", jugador.getDinero()).replace(",", "."));
        lblDineroCantidad.setFont(new Font("Arial", Font.BOLD, 12));
        lblDineroCantidad.setForeground(new Color(40, 167, 69));

        panelDinero.add(lblDineroActual);
        panelDinero.add(lblDineroCantidad);

        panel.add(panelDinero);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Dinero restante
        JPanel panelRestante = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelRestante.setBackground(COLOR_FONDO);

        JLabel lblRestante = new JLabel("Después del pago: ");
        lblRestante.setFont(new Font("Arial", Font.ITALIC, 11));
        lblRestante.setForeground(new Color(100, 100, 105));

        JLabel lblRestanteCantidad = new JLabel(String.format("%,d EUR", dineroRestante).replace(",", "."));
        lblRestanteCantidad.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 11));
        lblRestanteCantidad.setForeground(dineroRestante >= 0 ? new Color(100, 100, 105) : COLOR_ROJO);

        panelRestante.add(lblRestante);
        panelRestante.add(lblRestanteCantidad);

        panel.add(panelRestante);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(5, 0, 20, 0));

        JButton btnPagar = crearBotonEstilizado("💳 PAGAR");
        btnPagar.addActionListener(e -> dispose());

        panel.add(btnPagar);

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
        boton.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        boton.setForeground(Color.WHITE);
        boton.setPreferredSize(new Dimension(150, 42));
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
}
