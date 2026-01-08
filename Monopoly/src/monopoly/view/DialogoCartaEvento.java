package monopoly.view;

import javax.swing.*;
import java.awt.*;

public class DialogoCartaEvento extends JDialog {
    
    public DialogoCartaEvento(JFrame parent, String tipoCarta, String descripcion, String efecto) {
        super(parent, "Carta/Evento", true);
        
        inicializarComponentes(tipoCarta, descripcion, efecto);
        setSize(400, 350);
        setLocationRelativeTo(parent);
    }
    
    private void inicializarComponentes(String tipoCarta, String descripcion, String efecto) {
        setLayout(new BorderLayout(10, 10));
        
        // Panel principal con fondo gris
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBackground(new Color(200, 200, 200));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel lblTitulo = new JLabel("CARTA/EVENTO");
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
        
        // Encabezado de la carta
        JLabel lblEncabezado = new JLabel("Encabezado de la carta/evento");
        lblEncabezado.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 13));
        lblEncabezado.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelInfo.add(lblEncabezado);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Tipo de carta
        JLabel lblTipo = new JLabel(tipoCarta);
        lblTipo.setFont(new Font("Arial", Font.BOLD, 12));
        lblTipo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelInfo.add(lblTipo);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Descripción
        JTextArea areaDescripcion = new JTextArea(descripcion);
        areaDescripcion.setEditable(false);
        areaDescripcion.setFont(new Font("Arial", Font.ITALIC, 11));
        areaDescripcion.setLineWrap(true);
        areaDescripcion.setWrapStyleWord(true);
        areaDescripcion.setBackground(Color.WHITE);
        areaDescripcion.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        areaDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblDescTitulo = new JLabel("*Descripción de la carta/evento*");
        lblDescTitulo.setFont(new Font("Arial", Font.ITALIC, 11));
        lblDescTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelInfo.add(lblDescTitulo);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        panelInfo.add(areaDescripcion);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel lblEfectoTexto = new JLabel("(...)");
        lblEfectoTexto.setFont(new Font("Arial", Font.PLAIN, 11));
        lblEfectoTexto.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelInfo.add(lblEfectoTexto);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Efecto
        JLabel lblEfectoTitulo = new JLabel("*Efecto de la carta sobre el jugador*");
        lblEfectoTitulo.setFont(new Font("Arial", Font.ITALIC, 11));
        lblEfectoTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblEfecto = new JLabel(efecto);
        lblEfecto.setFont(new Font("Arial", Font.BOLD, 11));
        lblEfecto.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelInfo.add(lblEfectoTitulo);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        panelInfo.add(lblEfecto);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(200, 200, 200));
        
        JButton btnAceptar = crearBoton("ACEPTAR");
        btnAceptar.addActionListener(e -> dispose());
        
        panelBotones.add(btnAceptar);
        
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
}
