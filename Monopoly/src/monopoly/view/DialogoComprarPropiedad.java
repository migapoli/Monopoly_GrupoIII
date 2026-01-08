package monopoly.view;

import monopoly.model.casilla.Propiedad;
import monopoly.model.jugador.IJugador;

import javax.swing.*;
import java.awt.*;

public class DialogoComprarPropiedad extends JDialog {
    private boolean comprar = false;
    private Propiedad propiedad;
    private IJugador jugador;
    
    public DialogoComprarPropiedad(JFrame parent, Propiedad propiedad, IJugador jugador) {
        super(parent, "Compra de Propiedad", true);
        this.propiedad = propiedad;
        this.jugador = jugador;
        
        inicializarComponentes();
        setSize(400, 300);
        setLocationRelativeTo(parent);
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        
        // Panel principal con fondo gris
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBackground(new Color(200, 200, 200));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel lblTitulo = new JLabel("COMPRA DE PROPIEDAD");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Panel de información
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(Color.WHITE);
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblUbicacion = new JLabel("Ubicación: " + propiedad.getNombre());
        lblUbicacion.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 13));
        lblUbicacion.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelInfo.add(lblUbicacion);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JLabel lblPrecio = new JLabel("Precio de compra: " + propiedad.getPrecio() + " EUR");
        lblPrecio.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblAlquiler = new JLabel("Alquiler: " + propiedad.getAlquiler() + " EUR");
        lblAlquiler.setFont(new Font("Arial", Font.PLAIN, 12));
        lblAlquiler.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelInfo.add(lblPrecio);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        panelInfo.add(lblAlquiler);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JLabel lblDinero = new JLabel("Dinero acumulado: " + jugador.getDinero() + " EUR");
        lblDinero.setFont(new Font("Arial", Font.BOLD, 12));
        lblDinero.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelInfo.add(lblDinero);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(200, 200, 200));
        
        JButton btnComprar = crearBoton("COMPRAR");
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
        
        JButton btnRechazar = crearBoton("RECHAZAR");
        btnRechazar.addActionListener(e -> {
            comprar = false;
            dispose();
        });
        
        panelBotones.add(btnComprar);
        panelBotones.add(btnRechazar);
        
        // Agregar componentes al panel principal
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelInfo, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setBackground(new Color(45, 50, 70));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setOpaque(true);
        boton.setBorderPainted(false);
        boton.setPreferredSize(new Dimension(120, 40));
        return boton;
    }
    
    public boolean isComprar() {
        return comprar;
    }
}
