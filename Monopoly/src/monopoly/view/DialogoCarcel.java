package monopoly.view;

import monopoly.model.jugador.IJugador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Diálogo para gestionar las opciones de la cárcel con diseño moderno oscuro.
 */
public class DialogoCarcel extends JDialog {

    private boolean pagarFianza = false;
    private boolean intentarDobles = false;

    private static final int FIANZA = 50;

    public DialogoCarcel(JFrame parent, IJugador jugador, int turnosEnCarcel) {
        super(parent, "Estás en la Cárcel", true);

        setUndecorated(true);
        inicializarComponentes(jugador, turnosEnCarcel);
        setSize(420, 360);
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
                g2.setColor(MonopolyTheme.FONDO_PANEL);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        panelPrincipal.setBackground(MonopolyTheme.FONDO_PANEL);
        panelPrincipal.setBorder(BorderFactory.createLineBorder(MonopolyTheme.BORDE_SUTIL, 1, true));

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
                g2.setColor(MonopolyTheme.HEADER_OSCURO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + 10, 15, 15);
                g2.fillRect(0, 10, getWidth(), getHeight());
            }
        };
        panel.setPreferredSize(new Dimension(420, 50));
        panel.setOpaque(false);

        JLabel lblTitulo = new JLabel("🔒 ESTÁS EN LA CÁRCEL 🔒");
        lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        lblTitulo.setForeground(MonopolyTheme.COLOR_ERROR);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(new EmptyBorder(15, 0, 15, 0));

        panel.add(lblTitulo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelContenido(IJugador jugador, int turnosEnCarcel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(MonopolyTheme.FONDO_PANEL);
        panel.setBorder(new EmptyBorder(20, 30, 15, 30));

        // Panel de información
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(MonopolyTheme.FONDO_PANEL_CLARO);
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MonopolyTheme.BORDE_SUTIL, 1, true),
                new EmptyBorder(20, 25, 20, 25)));
        panelInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelInfo.setMaximumSize(new Dimension(360, 140));

        // Icono grande
        JLabel lblIcono = new JLabel("⛓️");
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Turnos en cárcel
        JLabel lblTurnos = new JLabel("Turno " + turnosEnCarcel + " de 3 en la cárcel");
        lblTurnos.setFont(new Font("Arial", Font.BOLD, 15));
        lblTurnos.setForeground(MonopolyTheme.COLOR_ADVERTENCIA);
        lblTurnos.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelInfo.add(lblIcono);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 10)));
        panelInfo.add(lblTurnos);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 12)));

        // Opciones disponibles
        JLabel lblOpciones = new JLabel("<html><center>Puedes:<br>" +
                "• Pagar " + FIANZA + " EUR de fianza<br>" +
                "• Intentar sacar dobles en los dados</center></html>");
        lblOpciones.setFont(MonopolyTheme.FUENTE_NORMAL);
        lblOpciones.setForeground(MonopolyTheme.TEXTO_SECUNDARIO);
        lblOpciones.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelInfo.add(lblOpciones);

        panel.add(panelInfo);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Dinero disponible
        JPanel panelDinero = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelDinero.setOpaque(false);

        JLabel lblDinero = new JLabel("Tu dinero: ");
        lblDinero.setFont(MonopolyTheme.FUENTE_NORMAL);
        lblDinero.setForeground(MonopolyTheme.TEXTO_SECUNDARIO);

        JLabel lblCantidad = new JLabel(String.format("%,d EUR", jugador.getDinero()).replace(",", "."));
        lblCantidad.setFont(new Font("Arial", Font.BOLD, 13));
        lblCantidad
                .setForeground(jugador.getDinero() >= FIANZA ? MonopolyTheme.COLOR_EXITO : MonopolyTheme.COLOR_ERROR);

        panelDinero.add(lblDinero);
        panelDinero.add(lblCantidad);

        panel.add(panelDinero);

        if (turnosEnCarcel >= 3) {
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
            JLabel lblObligatorio = new JLabel("⚠️ Debes pagar la fianza obligatoriamente");
            lblObligatorio.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));
            lblObligatorio.setForeground(MonopolyTheme.COLOR_ERROR);
            lblObligatorio.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(lblObligatorio);
        }

        return panel;
    }

    private JPanel crearPanelBotones(IJugador jugador) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(MonopolyTheme.FONDO_PANEL);
        panel.setBorder(new EmptyBorder(5, 0, 20, 0));

        boolean puedepagarFianza = jugador.puedePermitirse(FIANZA);

        JButton btnPagarFianza = MonopolyTheme.crearBotonPrimario("💰 PAGAR FIANZA");
        btnPagarFianza.setPreferredSize(new Dimension(160, 42));
        btnPagarFianza.setEnabled(puedepagarFianza);
        btnPagarFianza.addActionListener(e -> {
            pagarFianza = true;
            dispose();
        });

        JButton btnTirarDados = MonopolyTheme.crearBotonSecundario("🎲 TIRAR DADOS");
        btnTirarDados.setPreferredSize(new Dimension(160, 42));
        btnTirarDados.addActionListener(e -> {
            intentarDobles = true;
            dispose();
        });

        panel.add(btnPagarFianza);
        panel.add(btnTirarDados);

        return panel;
    }

    public boolean isPagarFianza() {
        return pagarFianza;
    }

    public boolean isIntentarDobles() {
        return intentarDobles;
    }
}
