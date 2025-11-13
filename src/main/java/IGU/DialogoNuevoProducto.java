/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IGU;

import logica.*;
import exception.ProductoInvalidoException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 *
 * @author Nataly
 */
public class DialogoNuevoProducto extends JDialog {
    private GestionarInventario gestorInventario;
    private JTextField txtNombre, txtPrecio, txtCodigo, txtStock, txtUbicacion;
    private JComboBox<String> cmbCategoria, cmbTipo;
    
    public DialogoNuevoProducto(Frame parent, GestionarInventario gestor) {
        super(parent, "Nuevo Producto", true);
        this.gestorInventario = gestor;
        initComponents();
    }
    
    private void initComponents() {
        setSize(450, 400);
        setLocationRelativeTo(getParent());
        
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("Tipo:"));
        cmbTipo = new JComboBox<>(new String[]{"Producto Físico", "Servicio Digital"});
        panel.add(cmbTipo);
        
        panel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);
        
        panel.add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        panel.add(txtPrecio);
        
        panel.add(new JLabel("Categoría:"));
        cmbCategoria = new JComboBox<>(new String[]{
            "COMPUTADORAS", "SMARTPHONES", "ACCESORIOS", 
            "PERIFERICOS", "COMPONENTES", "SERVICIOS_TECNICOS"
        });
        panel.add(cmbCategoria);
        
        panel.add(new JLabel("Código de Barras:"));
        txtCodigo = new JTextField();
        panel.add(txtCodigo);
        
        panel.add(new JLabel("Stock:"));
        txtStock = new JTextField();
        panel.add(txtStock);
        
        panel.add(new JLabel("Ubicación:"));
        txtUbicacion = new JTextField();
        panel.add(txtUbicacion);
        
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnGuardar.addActionListener((ActionEvent e) -> {
            guardarProducto();
        });
        
        btnCancelar.addActionListener((ActionEvent e) -> {
            dispose();
        });
        
        panel.add(btnGuardar);
        panel.add(btnCancelar);
        
        add(panel);
    }
    
    private void guardarProducto() {
        try {
            String nombre = txtNombre.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            String categoria = (String) cmbCategoria.getSelectedItem();
            
            if (cmbTipo.getSelectedIndex() == 0) {
                // Producto Físico
                String codigo = txtCodigo.getText().trim();
                int stock = Integer.parseInt(txtStock.getText().trim());
                String ubicacion = txtUbicacion.getText().trim();
                
                ProductoFisico producto = new ProductoFisico(nombre, precio, categoria, codigo, stock, ubicacion);
                gestorInventario.agregarProducto(producto);
                
                JOptionPane.showMessageDialog(this, "Producto agregado exitosamente");
                dispose();
            } else {
                // Servicio Digital
                int duracion = Integer.parseInt(JOptionPane.showInputDialog(this, "Duración (minutos):"));
                String descripcion = JOptionPane.showInputDialog(this, "Descripción:");
                
                ServicioDigital servicio = new ServicioDigital(nombre, precio, categoria, duracion, descripcion);
                gestorInventario.agregarStock(servicio);
                
                JOptionPane.showMessageDialog(this, "Servicio agregado exitosamente");
                dispose();
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error en formato de números", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ProductoInvalidoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
