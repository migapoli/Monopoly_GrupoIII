package monopoly.view;

import monopoly.controller.ControladorJuego;
import monopoly.model.casilla.Casilla;
import monopoly.model.casilla.Propiedad;
import monopoly.model.jugador.IJugador;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelInfoJugador extends JPanel {
    private ControladorJuego controlador;
    private JPanel panelContenido;

    public PanelInfoJugador(ControladorJuego controlador) {
        this.controlador = controlador;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(0, 280));

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
        JLabel lblTitulo = new JLabel("JUGADOR ACTUAL");
        lblTitulo.setFont(MonopolyTheme.FUENTE_TITULO);
        lblTitulo.setForeground(MonopolyTheme.ACENTO_DORADO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Panel con scroll para la información
        panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(panelContenido);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panelContenedor.add(lblTitulo, BorderLayout.NORTH);
        panelContenedor.add(scrollPane, BorderLayout.CENTER);

        add(panelContenedor, BorderLayout.CENTER);

        actualizar();
    }

    public void actualizar() {
        panelContenido.removeAll();

        IJugador jugadorActual = controlador.getPartida().getJugadorActual();

        if (jugadorActual != null) {
            JPanel infoActual = crearInfoJugadorActual(jugadorActual);
            panelContenido.add(infoActual);
        }

        panelContenido.add(Box.createVerticalGlue());

        revalidate();
        repaint();
    }

    private JPanel crearInfoJugadorActual(IJugador jugador) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Nombre del jugador con indicador de color
        JPanel panelNombre = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelNombre.setOpaque(false);
        panelNombre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        // Indicador de color
        JPanel indicadorColor = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(jugador.getColor());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                g2.dispose();
            }
        };
        indicadorColor.setPreferredSize(new Dimension(20, 20));
        indicadorColor.setOpaque(false);

        JLabel lblNombre = new JLabel(jugador.getNombre());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 16));
        lblNombre.setForeground(MonopolyTheme.TEXTO_PRINCIPAL);

        panelNombre.add(indicadorColor);
        panelNombre.add(lblNombre);

        panel.add(panelNombre);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Posición actual
        Casilla casillaActual = controlador.getPartida().getTablero().getCasilla(jugador.getPosicion());
        JPanel panelPosicion = crearLineaInfo("📍", "Posición:", casillaActual.getNombre());
        panel.add(panelPosicion);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Dinero
        String dineroFormateado = String.format("%,d", jugador.getDinero()).replace(",", ".") + " EUR";
        JPanel panelDinero = crearLineaInfo("💰", "Dinero:", dineroFormateado);
        panel.add(panelDinero);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Propiedades
        JLabel lblPropiedades = new JLabel("🏠 Propiedades: " + jugador.getPropiedades().size());
        lblPropiedades.setFont(MonopolyTheme.FUENTE_NORMAL);
        lblPropiedades.setForeground(MonopolyTheme.TEXTO_SECUNDARIO);
        lblPropiedades.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblPropiedades);

        // Lista de propiedades
        if (!jugador.getPropiedades().isEmpty()) {
            panel.add(Box.createRigidArea(new Dimension(0, 8)));
            JPanel panelListaPropiedades = crearListaPropiedades(jugador.getPropiedades());
            panel.add(panelListaPropiedades);
        }

        return panel;
    }

    private JPanel crearLineaInfo(String icono, String etiqueta, String valor) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        lblIcono.setForeground(MonopolyTheme.TEXTO_PRINCIPAL);

        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(MonopolyTheme.FUENTE_NORMAL);
        lblEtiqueta.setForeground(MonopolyTheme.TEXTO_SECUNDARIO);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.BOLD, 13));
        lblValor.setForeground(MonopolyTheme.TEXTO_PRINCIPAL);

        panel.add(lblIcono);
        panel.add(lblEtiqueta);
        panel.add(lblValor);

        return panel;
    }

    private JPanel crearListaPropiedades(List<Propiedad> propiedades) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        for (Propiedad prop : propiedades) {
            JPanel lineaPropiedad = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
            lineaPropiedad.setOpaque(false);
            lineaPropiedad.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));

            // Indicador de color del grupo
            JPanel indicadorGrupo = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(prop.getGrupoColor());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                    g2.dispose();
                }
            };
            indicadorGrupo.setPreferredSize(new Dimension(12, 12));
            indicadorGrupo.setOpaque(false);

            // Nombre de la propiedad
            String nombreProp = prop.getNombre();
            if (nombreProp.length() > 18) {
                nombreProp = nombreProp.substring(0, 16) + "..";
            }
            JLabel lblNombreProp = new JLabel(nombreProp);
            lblNombreProp.setFont(MonopolyTheme.FUENTE_PEQUENA);
            lblNombreProp.setForeground(MonopolyTheme.TEXTO_PRINCIPAL);

            // Casas/Hotel
            String casasInfo = "";
            if (prop.tieneHotel()) {
                casasInfo = "🏨";
            } else if (prop.getCasas() > 0) {
                casasInfo = "🏠 x" + prop.getCasas();
            }
            JLabel lblCasas = new JLabel(casasInfo);
            lblCasas.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 10));
            lblCasas.setForeground(MonopolyTheme.COLOR_EXITO);

            lineaPropiedad.add(indicadorGrupo);
            lineaPropiedad.add(lblNombreProp);
            if (!casasInfo.isEmpty()) {
                lineaPropiedad.add(lblCasas);
            }

            panel.add(lineaPropiedad);
        }

        return panel;
    }
}
