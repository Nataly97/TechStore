/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IGU;

import exception.CedulaUnicaException;
import logica.GestionarClientes;
import logica.Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 *
 * @author Nataly
 */
public class DialogoNuevoCliente extends JDialog{
    private GestionarClientes gestorClientes;
    private JTextField txtNombre, txtDNI, txtTelefono, txtDireccion;
    
    public DialogoNuevoCliente(Frame parent, GestionarClientes gestor) {
        super(parent, "Nuevo Cliente", true);
        this.gestorClientes = gestor;
        initComponents();
    }
    
    private void initComponents() {
        setSize(400, 300);
        setLocationRelativeTo(getParent());
        
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);
        
        panel.add(new JLabel("DNI/Cédula:"));
        txtDNI = new JTextField();
        panel.add(txtDNI);
        
        panel.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panel.add(txtTelefono);
        
        panel.add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        panel.add(txtDireccion);
        
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnGuardar.addActionListener((ActionEvent e) -> {
            guardarCliente();
        });
        
        btnCancelar.addActionListener((ActionEvent e) -> {
            dispose();
        });
        
        panel.add(btnGuardar);
        panel.add(btnCancelar);
        
        add(panel);
    }
    
    private void guardarCliente() {
        String nombre = txtNombre.getText().trim();
        String dni = txtDNI.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();
        
        if (nombre.isEmpty() || dni.isEmpty() || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete los campos obligatorios");
            return;
        }
        
        try {
            Cliente nuevoCliente = gestorClientes.registrarNuevoCliente(nombre, dni, telefono, direccion);
            JOptionPane.showMessageDialog(this, "Cliente registrado: " + nuevoCliente.getId());
            dispose();
        } catch (CedulaUnicaException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
