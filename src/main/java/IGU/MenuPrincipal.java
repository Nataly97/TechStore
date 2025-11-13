/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IGU;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 *
 * @author Nataly
 */



public class MenuPrincipal extends JFrame{
    public MenuPrincipal() {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("TechStore - Sistema de Gestión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBackground(new Color(240, 240, 240));
        
        // Título
        JLabel lblTitulo = new JLabel("TECHSTORE", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setForeground(new Color(0, 102, 204));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);
        
        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(4, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        panelBotones.setBackground(new Color(240, 240, 240));
        
        JButton btnClientes = crearBoton("Gestionar Clientes");
        JButton btnInventario = crearBoton("Gestionar Inventario");
        JButton btnVentas = crearBoton("Realizar Venta");
        JButton btnSalir = crearBoton("Salir");
        
        btnClientes.addActionListener((ActionEvent e) -> {
            new GestionarClientesVista().setVisible(true);
        });
        
        btnInventario.addActionListener((ActionEvent e) -> {
            new GestionInventario().setVisible(true);
        });
        
        btnVentas.addActionListener((ActionEvent e) -> {
            new PanelVentas().setVisible(true);
        });
        
        btnSalir.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        
        panelBotones.add(btnClientes);
        panelBotones.add(btnInventario);
        panelBotones.add(btnVentas);
        panelBotones.add(btnSalir);
        
        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
        
        add(panelPrincipal);
    }
    
    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setBackground(new Color(0, 102, 204));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MenuPrincipal().setVisible(true);
        });
    }
    
}
