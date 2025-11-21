/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IGU;

import exception.CedulaUnicaException;
import controlador.GestionarClientes;
import logica.Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 *
 * @author Nataly
 */
public class DialogoNuevoCliente extends JDialog {

   private GestionarClientes gestorClientes;
    private JTextField txtNombre, txtCedula, txtTelefono, txtDireccion;
    private JButton btnGuardar, btnCancelar;

    public DialogoNuevoCliente(Frame parent, boolean modal, GestionarClientes gestor) {
        super(parent, "Registrar Nuevo Cliente", modal);
        this.gestorClientes = gestor;
        initComponents();
    }

    private void initComponents() {
        // No usar setSize fijo: mejor pack() al final
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Título
        JLabel lblTitulo = new JLabel("Nuevo Cliente", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(0, 51, 153));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Panel de formulario: uso GridBag para controlar mejor anchuras
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Helper to configure text fields consistently
        Font campoFont = new Font("Segoe UI", Font.PLAIN, 14);
        Dimension campoDim = new Dimension(260, 30);

        // Nombre
        JLabel lblNombre = new JLabel("Nombre completo: *");
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panelFormulario.add(lblNombre, gbc);

        txtNombre = new JTextField();
        txtNombre.setFont(campoFont);
        txtNombre.setColumns(20);
        txtNombre.setPreferredSize(campoDim);
        txtNombre.setForeground(Color.BLACK);
        txtNombre.setBackground(Color.WHITE);
        txtNombre.setCaretColor(Color.BLACK);
        gbc.gridx = 1;
        gbc.gridy = row++;
        gbc.weightx = 1;
        panelFormulario.add(txtNombre, gbc);

        // Cédula
        JLabel lblCedula = new JLabel("Cédula/DNI: *");
        lblCedula.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panelFormulario.add(lblCedula, gbc);

        txtCedula = new JTextField();
        txtCedula.setFont(campoFont);
        txtCedula.setColumns(20);
        txtCedula.setPreferredSize(campoDim);
        txtCedula.setForeground(Color.BLACK);
        txtCedula.setBackground(Color.WHITE);
        txtCedula.setCaretColor(Color.BLACK);
        gbc.gridx = 1;
        gbc.gridy = row++;
        gbc.weightx = 1;
        panelFormulario.add(txtCedula, gbc);

        // Teléfono
        JLabel lblTelefono = new JLabel("Teléfono: *");
        lblTelefono.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panelFormulario.add(lblTelefono, gbc);

        txtTelefono = new JTextField();
        txtTelefono.setFont(campoFont);
        txtTelefono.setColumns(20);
        txtTelefono.setPreferredSize(campoDim);
        txtTelefono.setForeground(Color.BLACK);
        txtTelefono.setBackground(Color.WHITE);
        txtTelefono.setCaretColor(Color.BLACK);
        gbc.gridx = 1;
        gbc.gridy = row++;
        gbc.weightx = 1;
        panelFormulario.add(txtTelefono, gbc);

        // Dirección
        JLabel lblDireccion = new JLabel("Dirección:");
        lblDireccion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panelFormulario.add(lblDireccion, gbc);

        txtDireccion = new JTextField();
        txtDireccion.setFont(campoFont);
        txtDireccion.setColumns(20);
        txtDireccion.setPreferredSize(campoDim);
        txtDireccion.setForeground(Color.BLACK);
        txtDireccion.setBackground(Color.WHITE);
        txtDireccion.setCaretColor(Color.BLACK);
        gbc.gridx = 1;
        gbc.gridy = row++;
        gbc.weightx = 1;
        panelFormulario.add(txtDireccion, gbc);

        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 8));

        btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGuardar.setBackground(new Color(204, 255, 204));
        btnGuardar.setPreferredSize(new Dimension(120, 35));
        btnGuardar.addActionListener((ActionEvent e) -> guardarCliente());

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancelar.setBackground(new Color(255, 204, 204));
        btnCancelar.setPreferredSize(new Dimension(120, 35));
        btnCancelar.addActionListener((ActionEvent e) -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        // Nota de campos obligatorios (alineada a la izquierda)
        JLabel lblNota = new JLabel("* Campos obligatorios");
        lblNota.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblNota.setForeground(Color.GRAY);
        JPanel panelNota = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelNota.add(lblNota);

        // Panel sur que contiene botones y nota
        JPanel southPanel = new JPanel(new BorderLayout(0, 6));
        southPanel.add(panelBotones, BorderLayout.CENTER);
        southPanel.add(panelNota, BorderLayout.SOUTH);

        panelPrincipal.add(southPanel, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);

        // Ajustar tamaño automáticamente
        pack();

        // Asegurarse de que no quede demasiado pequeño
        setMinimumSize(new Dimension(460, getHeight()));
        setLocationRelativeTo(getParent());
    }

    private void guardarCliente() {
        String nombre = txtNombre.getText().trim();
        String cedula = txtCedula.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();

        // Validaciones
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El nombre es obligatorio.",
                    "Campo Requerido",
                    JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus();
            return;
        }

        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "La cédula/DNI es obligatoria.",
                    "Campo Requerido",
                    JOptionPane.WARNING_MESSAGE);
            txtCedula.requestFocus();
            return;
        }

        if (telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El teléfono es obligatorio.",
                    "Campo Requerido",
                    JOptionPane.WARNING_MESSAGE);
            txtTelefono.requestFocus();
            return;
        }

        if (!cedula.matches("\\d+")) {
            JOptionPane.showMessageDialog(this,
                    "La cédula debe contener solo números.",
                    "Formato Inválido",
                    JOptionPane.WARNING_MESSAGE);
            txtCedula.requestFocus();
            return;
        }

        if (cedula.length() < 6 || cedula.length() > 15) {
            JOptionPane.showMessageDialog(this,
                    "La cédula debe tener entre 6 y 15 dígitos.",
                    "Formato Inválido",
                    JOptionPane.WARNING_MESSAGE);
            txtCedula.requestFocus();
            return;
        }

        if (!telefono.matches("[0-9\\s\\-()]+")) {
            JOptionPane.showMessageDialog(this,
                    "El teléfono contiene caracteres inválidos.",
                    "Formato Inválido",
                    JOptionPane.WARNING_MESSAGE);
            txtTelefono.requestFocus();
            return;
        }

        if (direccion.isEmpty()) {
            direccion = "No especificada";
        }

        try {
            Cliente nuevoCliente = gestorClientes.registrarNuevoCliente(nombre, cedula, telefono, direccion);

            JOptionPane.showMessageDialog(this,
                    "Cliente registrado exitosamente.\n\n"
                            + "ID: " + nuevoCliente.getId() + "\n"
                            + "Nombre: " + nuevoCliente.getNombre() + "\n"
                            + "Cédula: " + nuevoCliente.getCedula() + "\n"
                            + "Puntos iniciales: " + nuevoCliente.getPuntosFidelidad(),
                    "Registro Exitoso",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();

        } catch (CedulaUnicaException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error - Cédula Duplicada",
                    JOptionPane.ERROR_MESSAGE);
            txtCedula.requestFocus();
            txtCedula.selectAll();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al registrar el cliente:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Para probar el diálogo
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GestionarClientes gestor = new GestionarClientes();
            DialogoNuevoCliente dialogo = new DialogoNuevoCliente(null, true, gestor);
            dialogo.setVisible(true);
        });
    }
}
