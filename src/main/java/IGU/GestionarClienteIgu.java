/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IGU;

import logica.GestionarClientes;
import logica.Cliente;
import exception.CedulaUnicaException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
/**
 *
 * @author Nataly
 */
public class GestionarClienteIgu extends javax.swing.JFrame{
    private GestionarClientes gestionClientes;
    public GestionarClienteIgu() {
        initComponents();
        this.gestionClientes = new GestionarClientes();
        setLocationRelativeTo(null);
        setTitle("Gestión de Clientes - TechStore");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204), 2));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 18));
        jLabel1.setForeground(new java.awt.Color(0, 0, 204));
        jLabel1.setText("Gestión de Clientes");

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12));
        jButton1.setForeground(new java.awt.Color(0, 0, 204));
        jButton1.setText("Buscar Cliente");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12));
        jButton2.setForeground(new java.awt.Color(0, 0, 204));
        jButton2.setText("Nuevo Cliente");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12));
        jButton3.setForeground(new java.awt.Color(0, 0, 204));
        jButton3.setText("Actualizar Cliente");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12));
        jButton4.setForeground(new java.awt.Color(0, 0, 204));
        jButton4.setText("Perfil Cliente");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(135, 135, 135)
                        .addComponent(jButton4)))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(52, 52, 52)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(100, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        

    // BOTÓN: BUSCAR CLIENTE
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        String criterio = JOptionPane.showInputDialog(this, 
                "Ingrese ID, DNI o nombre del cliente:", 
                "Buscar Cliente", 
                JOptionPane.QUESTION_MESSAGE);
        
        if (criterio != null && !criterio.trim().isEmpty()) {
            List<Cliente> resultados = gestionClientes.buscarClientes(criterio);
            
            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                        "No se encontraron clientes con ese criterio.", 
                        "Sin resultados", 
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                mostrarResultadosBusqueda(resultados);
            }
        }
    }                                        

    // BOTÓN: NUEVO CLIENTE
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField txtNombre = new JTextField();
        JTextField txtDNI = new JTextField();
        JTextField txtTelefono = new JTextField();
        JTextField txtDireccion = new JTextField();
        
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("DNI/Cédula:"));
        panel.add(txtDNI);
        panel.add(new JLabel("Teléfono:"));
        panel.add(txtTelefono);
        panel.add(new JLabel("Dirección:"));
        panel.add(txtDireccion);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
                "Registrar Nuevo Cliente", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();
            String dni = txtDNI.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String direccion = txtDireccion.getText().trim();
            
            if (nombre.isEmpty() || dni.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                        "Nombre y DNI son obligatorios.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                Cliente nuevoCliente = gestionClientes.registrarNuevoCliente(nombre, dni, telefono, direccion);
                JOptionPane.showMessageDialog(this, 
                        "Cliente registrado exitosamente.\nID: " + nuevoCliente.getId(), 
                        "Éxito", 
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (CedulaUnicaException e) {
                JOptionPane.showMessageDialog(this, 
                        e.getMessage(), 
                        "Error - DNI Duplicado", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }                                        

    // BOTÓN: ACTUALIZAR CLIENTE
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        String id = JOptionPane.showInputDialog(this, 
                "Ingrese el ID del cliente a actualizar:", 
                "Actualizar Cliente", 
                JOptionPane.QUESTION_MESSAGE);
        
        if (id != null && !id.trim().isEmpty()) {
            Cliente cliente = gestionClientes.buscarClientePorID(id.trim());
            
            if (cliente == null) {
                JOptionPane.showMessageDialog(this, 
                        "No se encontró un cliente con ese ID.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
            JTextField txtNombre = new JTextField(cliente.getNombre());
            JTextField txtTelefono = new JTextField(cliente.getTelefono());
            JTextField txtDireccion = new JTextField(cliente.getDireccion());
            
            panel.add(new JLabel("Nombre:"));
            panel.add(txtNombre);
            panel.add(new JLabel("Teléfono:"));
            panel.add(txtTelefono);
            panel.add(new JLabel("Dirección:"));
            panel.add(txtDireccion);
            
            int result = JOptionPane.showConfirmDialog(this, panel, 
                    "Actualizar Cliente - " + id, 
                    JOptionPane.OK_CANCEL_OPTION, 
                    JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                boolean actualizado = gestionClientes.actualizarCliente(
                        id, 
                        txtNombre.getText().trim(), 
                        txtTelefono.getText().trim(), 
                        txtDireccion.getText().trim()
                );
                
                if (actualizado) {
                    JOptionPane.showMessageDialog(this, 
                            "Cliente actualizado exitosamente.", 
                            "Éxito", 
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }                                        

    // BOTÓN: PERFIL CLIENTE
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        String id = JOptionPane.showInputDialog(this, 
                "Ingrese el ID del cliente:", 
                "Ver Perfil de Cliente", 
                JOptionPane.QUESTION_MESSAGE);
        
        if (id != null && !id.trim().isEmpty()) {
            Cliente cliente = gestionClientes.buscarClientePorID(id.trim());
            
            if (cliente == null) {
                JOptionPane.showMessageDialog(this, 
                        "No se encontró un cliente con ese ID.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String perfil = String.format(
                    "═══════════════════════════════\n" +
                    "       PERFIL DE CLIENTE\n" +
                    "═══════════════════════════════\n\n" +
                    "ID:              %s\n" +
                    "Nombre:          %s\n" +
                    "DNI/Cédula:      %s\n" +
                    "Teléfono:        %s\n" +
                    "Dirección:       %s\n" +
                    "Puntos:          %d\n\n" +
                    "═══════════════════════════════",
                    cliente.getId(),
                    cliente.getNombre(),
                    cliente.getDni(),
                    cliente.getTelefono(),
                    cliente.getDireccion(),
                    cliente.getPuntosFidelidad()
            );
            
            JTextArea textArea = new JTextArea(perfil);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JOptionPane.showMessageDialog(this, 
                    new JScrollPane(textArea), 
                    "Perfil de Cliente", 
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }                                        

    /**
     * Método auxiliar para mostrar resultados de búsqueda en tabla
     */
    private void mostrarResultadosBusqueda(List<Cliente> clientes) {
        String[] columnas = {"ID", "Nombre", "DNI", "Teléfono", "Puntos"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        
        for (Cliente c : clientes) {
            modelo.addRow(new Object[]{
                c.getId(),
                c.getNombre(),
                c.getDni(),
                c.getTelefono(),
                c.getPuntosFidelidad()
            });
        }
        
        JTable tabla = new JTable(modelo);
        tabla.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        
        JOptionPane.showMessageDialog(this, 
                scrollPane, 
                "Resultados de Búsqueda (" + clientes.size() + " encontrados)", 
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(GestionarClienteIgu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GestionarClienteIgu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration
}
