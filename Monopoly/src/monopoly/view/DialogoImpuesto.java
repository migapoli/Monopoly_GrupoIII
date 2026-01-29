package monopoly.view;

import monopoly.model.jugador.IJugador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Diálogo para mostrar el pago de impuestos con diseño moderno oscuro.
 */
public class DialogoImpuesto extends JDialog {

    public DialogoImpuesto(JFrame parent, String nombreCasilla, int monto, IJugador jugador) {
        super(parent, "Pago de Impuesto", true);

        setUndecorated(true);
        inicializarComponentes(nombreCasilla, monto, jugador);
        setSize(400, 300);
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
                g2.setColor(MonopolyTheme.FONDO_PANEL);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        panelPrincipal.setBackground(MonopolyTheme.FONDO_PANEL);
        panelPrincipal.setBorder(BorderFactory.createLineBorder(MonopolyTheme.BORDE_SUTIL, 1, true));

        // Header
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
                // Header con tono rojizo
                g2.setColor(new Color(80, 30, 35));
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + 10, 15, 15);
                g2.fillRect(0, 10, getWidth(), getHeight());
            }
        };
        panel.setPreferredSize(new Dimension(400, 50));
        panel.setOpaque(false);

        JLabel lblTitulo = new JLabel("💸 PAGO DE IMPUESTO 💸");
        lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        lblTitulo.setForeground(MonopolyTheme.COLOR_ERROR);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(new EmptyBorder(15, 0, 15, 0));

        panel.add(lblTitulo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelContenido(String nombreCasilla, int monto, IJugador jugador) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(MonopolyTheme.FONDO_PANEL);
        panel.setBorder(new EmptyBorder(25, 30, 15, 30));

        // Nombre de la casilla
        JLabel lblCasilla = new JLabel(nombreCasilla);
        lblCasilla.setFont(new Font("Georgia", Font.BOLD, 18));
        lblCasilla.setForeground(MonopolyTheme.TEXTO_PRINCIPAL);
        lblCasilla.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblCasilla);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Monto a pagar
        JPanel panelMonto = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelMonto.setOpaque(false);

        JLabel lblDebes = new JLabel("Debes pagar: ");
        lblDebes.setFont(MonopolyTheme.FUENTE_NORMAL);
        lblDebes.setForeground(MonopolyTheme.TEXTO_SECUNDARIO);

        JLabel lblMonto = new JLabel(String.format("%,d EUR", monto).replace(",", "."));
        lblMonto.setFont(new Font("Arial", Font.BOLD, 22));
        lblMonto.setForeground(MonopolyTheme.COLOR_ERROR);

        panelMonto.add(lblDebes);
        panelMonto.add(lblMonto);

        panel.add(panelMonto);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Dinero actual
        JPanel panelDinero = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelDinero.setOpaque(false);

        JLabel lblDineroActual = new JLabel("Tu dinero actual: ");
        lblDineroActual.setFont(MonopolyTheme.FUENTE_PEQUENA);
        lblDineroActual.setForeground(MonopolyTheme.TEXTO_SECUNDARIO);

        JLabel lblDineroCantidad = new JLabel(String.format("%,d EUR", jugador.getDinero()).replace(",", "."));
        lblDineroCantidad.setFont(new Font("Arial", Font.BOLD, 12));
        lblDineroCantidad.setForeground(MonopolyTheme.COLOR_EXITO);

        panelDinero.add(lblDineroActual);
        panelDinero.add(lblDineroCantidad);

        panel.add(panelDinero);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Dinero restante
        int dineroRestante = jugador.getDinero() - monto;
        JPanel panelRestante = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelRestante.setOpaque(false);

        JLabel lblRestante = new JLabel("Después del pago: ");
        lblRestante.setFont(new Font("Arial", Font.ITALIC, 11));
        lblRestante.setForeground(MonopolyTheme.TEXTO_SECUNDARIO);

        JLabel lblRestanteCantidad = new JLabel(String.format("%,d EUR", dineroRestante).replace(",", "."));
        lblRestanteCantidad.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 11));
        lblRestanteCantidad
                .setForeground(dineroRestante >= 0 ? MonopolyTheme.TEXTO_SECUNDARIO : MonopolyTheme.COLOR_ERROR);

        panelRestante.add(lblRestante);
        panelRestante.add(lblRestanteCantidad);

        panel.add(panelRestante);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(MonopolyTheme.FONDO_PANEL);
        panel.setBorder(new EmptyBorder(5, 0, 20, 0));

        JButton btnPagar = MonopolyTheme.crearBotonSecundario("💳 PAGAR");
        btnPagar.setPreferredSize(new Dimension(160, 42));
        btnPagar.addActionListener(e -> dispose());

        panel.add(btnPagar);

        return panel;
    }
}
