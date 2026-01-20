package monopoly.view;

import monopoly.model.casilla.Propiedad;
import monopoly.model.jugador.IJugador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Diálogo para comprar una propiedad con diseño moderno.
 */
public class DialogoComprarPropiedad extends JDialog {
    private boolean comprar = false;
    private Propiedad propiedad;
    private IJugador jugador;

    // Colores del diseño
    private static final Color COLOR_HEADER = new Color(45, 55, 75);
    private static final Color COLOR_FONDO = new Color(240, 240, 245);
    private static final Color COLOR_PANEL = Color.WHITE;
    private static final Color COLOR_VERDE = new Color(76, 175, 80);
    private static final Color COLOR_TEXTO = new Color(35, 45, 65);
    private static final Color COLOR_BOTON = new Color(55, 65, 85);

    public DialogoComprarPropiedad(JFrame parent, Propiedad propiedad, IJugador jugador) {
        super(parent, "Compra de Propiedad", true);
        this.propiedad = propiedad;
        this.jugador = jugador;

        setUndecorated(true);
        inicializarComponentes();
        setSize(380, 320);
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
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(new LineBorder(new Color(200, 200, 205), 1, true));

        // Header oscuro
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
                g2.setColor(COLOR_HEADER);
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + 10, 15, 15);
                g2.fillRect(0, 10, getWidth(), getHeight());
            }
        };
        panel.setPreferredSize(new Dimension(380, 50));
        panel.setOpaque(false);

        JLabel lblTitulo = new JLabel("COMPRA DE PROPIEDAD");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(new EmptyBorder(15, 0, 15, 0));

        panel.add(lblTitulo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelContenido() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(20, 25, 15, 25));

        // Panel de información de la propiedad (con borde y línea verde)
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(COLOR_PANEL);
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(180, 180, 185), 1),
                new EmptyBorder(0, 0, 0, 0)));
        panelInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelInfo.setMaximumSize(new Dimension(330, 100));

        // Título de la propiedad (pequeño, gris)
        JPanel panelTituloPropiedad = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTituloPropiedad.setBackground(COLOR_PANEL);
        panelTituloPropiedad.setBorder(new EmptyBorder(8, 0, 5, 0));

        JLabel lblTituloPropiedad = new JLabel("TÍTULO DE PROPIEDAD");
        lblTituloPropiedad.setFont(new Font("Arial", Font.PLAIN, 9));
        lblTituloPropiedad.setForeground(new Color(130, 130, 135));
        lblTituloPropiedad.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(180, 180, 185), 1),
                new EmptyBorder(2, 8, 2, 8)));

        panelTituloPropiedad.add(lblTituloPropiedad);

        // Nombre de la ubicación
        JPanel panelUbicacion = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelUbicacion.setBackground(COLOR_PANEL);

        JLabel lblUbicacion = new JLabel("Ubicación: " + propiedad.getNombre());
        lblUbicacion.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 16));
        lblUbicacion.setForeground(COLOR_TEXTO);

        panelUbicacion.add(lblUbicacion);

        // Línea verde decorativa
        JPanel lineaVerde = new JPanel();
        lineaVerde.setBackground(COLOR_VERDE);
        lineaVerde.setPreferredSize(new Dimension(280, 4));
        lineaVerde.setMaximumSize(new Dimension(280, 4));

        JPanel panelLineaVerde = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelLineaVerde.setBackground(COLOR_PANEL);
        panelLineaVerde.setBorder(new EmptyBorder(0, 0, 10, 0));
        panelLineaVerde.add(lineaVerde);

        panelInfo.add(panelTituloPropiedad);
        panelInfo.add(panelUbicacion);
        panelInfo.add(panelLineaVerde);

        panel.add(panelInfo);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Información de precios
        JPanel panelPrecios = new JPanel();
        panelPrecios.setLayout(new BoxLayout(panelPrecios, BoxLayout.Y_AXIS));
        panelPrecios.setBackground(COLOR_FONDO);
        panelPrecios.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblPrecio = new JLabel(
                "Precio de compra: " + String.format("%,d", propiedad.getPrecio()).replace(",", ".") + " EUR");
        lblPrecio.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPrecio.setForeground(COLOR_TEXTO);
        lblPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblAlquiler = new JLabel(
                "Alquiler: " + String.format("%,d", propiedad.getAlquiler()).replace(",", ".") + " EUR");
        lblAlquiler.setFont(new Font("Arial", Font.PLAIN, 12));
        lblAlquiler.setForeground(COLOR_TEXTO);
        lblAlquiler.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelPrecios.add(lblPrecio);
        panelPrecios.add(Box.createRigidArea(new Dimension(0, 3)));
        panelPrecios.add(lblAlquiler);

        panel.add(panelPrecios);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Dinero acumulado (con color verde)
        JPanel panelDinero = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelDinero.setBackground(COLOR_FONDO);

        JLabel lblDineroTexto = new JLabel("Dinero acumulado: ");
        lblDineroTexto.setFont(new Font("Arial", Font.BOLD, 12));
        lblDineroTexto.setForeground(COLOR_TEXTO);

        JLabel lblDineroCantidad = new JLabel(String.format("%,d", jugador.getDinero()).replace(",", ".") + " EUR");
        lblDineroCantidad.setFont(new Font("Arial", Font.BOLD, 12));
        lblDineroCantidad.setForeground(COLOR_VERDE);

        panelDinero.add(lblDineroTexto);
        panelDinero.add(lblDineroCantidad);

        panel.add(panelDinero);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(5, 0, 20, 0));

        JButton btnComprar = crearBotonEstilizado("✓ COMPRAR");
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

        JButton btnRechazar = crearBotonEstilizado("✗ RECHAZAR");
        btnRechazar.addActionListener(e -> {
            comprar = false;
            dispose();
        });

        panel.add(btnComprar);
        panel.add(btnRechazar);

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
        boton.setFont(new Font("Arial", Font.BOLD, 11));
        boton.setForeground(Color.WHITE);
        boton.setPreferredSize(new Dimension(130, 38));
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    public boolean isComprar() {
        return comprar;
    }
}
