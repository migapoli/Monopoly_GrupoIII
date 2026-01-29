package monopoly.view;

import monopoly.model.casilla.Propiedad;
import monopoly.model.jugador.IJugador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Diálogo para comprar una propiedad con diseño moderno oscuro.
 */
public class DialogoComprarPropiedad extends JDialog {
    private boolean comprar = false;
    private Propiedad propiedad;
    private IJugador jugador;

    public DialogoComprarPropiedad(JFrame parent, Propiedad propiedad, IJugador jugador) {
        super(parent, "Compra de Propiedad", true);
        this.propiedad = propiedad;
        this.jugador = jugador;

        setUndecorated(true);
        inicializarComponentes();
        setSize(400, 340);
        setLocationRelativeTo(parent);
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

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
        JPanel panelHeader = crearPanelHeader();

        // Panel de contenido
        JPanel panelContenido = crearPanelContenido();

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
                g2.setColor(MonopolyTheme.HEADER_OSCURO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + 10, 15, 15);
                g2.fillRect(0, 10, getWidth(), getHeight());
            }
        };
        panel.setPreferredSize(new Dimension(400, 50));
        panel.setOpaque(false);

        JLabel lblTitulo = new JLabel("🏠 COMPRA DE PROPIEDAD");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(MonopolyTheme.ACENTO_DORADO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(new EmptyBorder(15, 0, 15, 0));

        panel.add(lblTitulo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelContenido() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(MonopolyTheme.FONDO_PANEL);
        panel.setBorder(new EmptyBorder(20, 25, 15, 25));

        // Panel de información de la propiedad
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(MonopolyTheme.FONDO_PANEL_CLARO);
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MonopolyTheme.BORDE_SUTIL, 1, true),
                new EmptyBorder(15, 15, 15, 15)));
        panelInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelInfo.setMaximumSize(new Dimension(350, 120));

        // Barra de color del grupo
        JPanel barraColor = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(propiedad.getGrupoColor());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);
                g2.dispose();
            }
        };
        barraColor.setPreferredSize(new Dimension(300, 6));
        barraColor.setMaximumSize(new Dimension(300, 6));
        barraColor.setAlignmentX(Component.CENTER_ALIGNMENT);
        barraColor.setOpaque(false);

        // Nombre de la propiedad
        JLabel lblNombre = new JLabel(propiedad.getNombre());
        lblNombre.setFont(new Font("Georgia", Font.BOLD, 18));
        lblNombre.setForeground(MonopolyTheme.TEXTO_PRINCIPAL);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Etiqueta de propiedad
        JLabel lblEtiqueta = new JLabel("TÍTULO DE PROPIEDAD");
        lblEtiqueta.setFont(new Font("Arial", Font.PLAIN, 10));
        lblEtiqueta.setForeground(MonopolyTheme.TEXTO_SECUNDARIO);
        lblEtiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelInfo.add(barraColor);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 10)));
        panelInfo.add(lblNombre);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        panelInfo.add(lblEtiqueta);

        panel.add(panelInfo);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Información de precios
        JPanel panelPrecios = new JPanel();
        panelPrecios.setLayout(new BoxLayout(panelPrecios, BoxLayout.Y_AXIS));
        panelPrecios.setOpaque(false);
        panelPrecios.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblPrecio = new JLabel(
                "💵 Precio: " + String.format("%,d", propiedad.getPrecio()).replace(",", ".") + " EUR");
        lblPrecio.setFont(MonopolyTheme.FUENTE_NORMAL);
        lblPrecio.setForeground(MonopolyTheme.TEXTO_PRINCIPAL);
        lblPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblAlquiler = new JLabel(
                "🏷 Alquiler: " + String.format("%,d", propiedad.getAlquiler()).replace(",", ".") + " EUR");
        lblAlquiler.setFont(MonopolyTheme.FUENTE_NORMAL);
        lblAlquiler.setForeground(MonopolyTheme.TEXTO_SECUNDARIO);
        lblAlquiler.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelPrecios.add(lblPrecio);
        panelPrecios.add(Box.createRigidArea(new Dimension(0, 5)));
        panelPrecios.add(lblAlquiler);

        panel.add(panelPrecios);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Dinero del jugador
        JPanel panelDinero = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelDinero.setOpaque(false);

        JLabel lblDineroTexto = new JLabel("Tu dinero: ");
        lblDineroTexto.setFont(new Font("Arial", Font.BOLD, 13));
        lblDineroTexto.setForeground(MonopolyTheme.TEXTO_SECUNDARIO);

        JLabel lblDineroCantidad = new JLabel(String.format("%,d", jugador.getDinero()).replace(",", ".") + " EUR");
        lblDineroCantidad.setFont(new Font("Arial", Font.BOLD, 13));
        lblDineroCantidad.setForeground(MonopolyTheme.COLOR_EXITO);

        panelDinero.add(lblDineroTexto);
        panelDinero.add(lblDineroCantidad);

        panel.add(panelDinero);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(MonopolyTheme.FONDO_PANEL);
        panel.setBorder(new EmptyBorder(5, 0, 20, 0));

        JButton btnComprar = MonopolyTheme.crearBotonPrimario("✓ COMPRAR");
        btnComprar.setPreferredSize(new Dimension(140, 42));
        btnComprar.addActionListener(e -> {
            if (jugador.getDinero() >= propiedad.getPrecio()) {
                comprar = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No tienes suficiente dinero para comprar esta propiedad",
                        "Dinero insuficiente",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton btnRechazar = MonopolyTheme.crearBotonSecundario("✗ RECHAZAR");
        btnRechazar.setPreferredSize(new Dimension(140, 42));
        btnRechazar.addActionListener(e -> {
            comprar = false;
            dispose();
        });

        panel.add(btnComprar);
        panel.add(btnRechazar);

        return panel;
    }

    public boolean isComprar() {
        return comprar;
    }
}
