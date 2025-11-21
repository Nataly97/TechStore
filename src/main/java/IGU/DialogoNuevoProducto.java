/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IGU;

import controlador.GestionarInventario;
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

    /**
     * Constructor del diálogo.
     *
     * @param parent Ventana padre
     * @param modal Si es modal
     * @param gestor Gestor de inventario
     */
    public DialogoNuevoProducto(Frame parent, boolean modal, GestionarInventario gestor) {
        super(parent, "Nuevo Producto", modal);
        this.gestorInventario = gestor;
        initComponents();
    }

    private void initComponents() {
        setSize(480, 420);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitulo = new JLabel("Registrar Nuevo Producto", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(0, 51, 153));
        panel.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridLayout(7, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        // Tipo de producto
        panelForm.add(new JLabel("Tipo de producto: *"));
        cmbTipo = new JComboBox<>(new String[]{"Producto Físico", "Servicio Digital"});
        panelForm.add(cmbTipo);

        // Nombre
        panelForm.add(new JLabel("Nombre: *"));
        txtNombre = new JTextField();
        panelForm.add(txtNombre);

        // Precio
        panelForm.add(new JLabel("Precio (COP): *"));
        txtPrecio = new JTextField();
        panelForm.add(txtPrecio);

        // Categoría (como String, NO enum)
        panelForm.add(new JLabel("Categoría: *"));
        cmbCategoria = new JComboBox<>(new String[]{
            "COMPUTADORAS",
            "SMARTPHONES",
            "ACCESORIOS",
            "PERIFERICOS",
            "COMPONENTES",
            "SERVICIOS_TECNICOS"
        });
        panelForm.add(cmbCategoria);

        // Código
        panelForm.add(new JLabel("Código de Barras / Interno: *"));
        txtCodigo = new JTextField();
        panelForm.add(txtCodigo);

        // Stock (solo para físicos)
        panelForm.add(new JLabel("Stock inicial (solo físico): *"));
        txtStock = new JTextField();
        panelForm.add(txtStock);

        // Ubicación (solo para físicos)
        panelForm.add(new JLabel("Ubicación en bodega/estantería:"));
        txtUbicacion = new JTextField();
        panelForm.add(txtUbicacion);

        panel.add(panelForm, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.setBackground(new Color(204, 255, 204));
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGuardar.setPreferredSize(new Dimension(120, 35));

        btnCancelar.setBackground(new Color(255, 204, 204));
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancelar.setPreferredSize(new Dimension(120, 35));

        btnGuardar.addActionListener((ActionEvent e) -> guardarProducto());
        btnCancelar.addActionListener((ActionEvent e) -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        panel.add(panelBotones, BorderLayout.SOUTH);

        add(panel);
    }

    /**
     * Valida los campos y registra el producto (físico o digital).
     */
    private void guardarProducto() {
        String nombre = txtNombre.getText().trim();
        String precioStr = txtPrecio.getText().trim();
        String codigo = txtCodigo.getText().trim();
        String stockStr = txtStock.getText().trim();
        String ubicacion = txtUbicacion.getText().trim();
        String categoriaStr = (String) cmbCategoria.getSelectedItem();
        boolean esFisico = (cmbTipo.getSelectedIndex() == 0);

        // Validaciones básicas
        if (nombre.isEmpty()) {
            mostrarAdvertencia("El nombre es obligatorio.");
            txtNombre.requestFocus();
            return;
        }

        if (precioStr.isEmpty()) {
            mostrarAdvertencia("El precio es obligatorio.");
            txtPrecio.requestFocus();
            return;
        }

        if (codigo.isEmpty()) {
            mostrarAdvertencia("El código es obligatorio.");
            txtCodigo.requestFocus();
            return;
        }

        if (esFisico && stockStr.isEmpty()) {
            mostrarAdvertencia("El stock inicial es obligatorio para productos físicos.");
            txtStock.requestFocus();
            return;
        }

        try {
            double precio = Double.parseDouble(precioStr);
            if (precio <= 0) {
                mostrarAdvertencia("El precio debe ser mayor que 0.");
                txtPrecio.requestFocus();
                return;
            }

            // En tu modelo, la categoría es String, NO enum
            String categoria = categoriaStr;

            if (esFisico) {
                // Producto físico
                int stock = Integer.parseInt(stockStr);
                if (stock < 0) {
                    mostrarAdvertencia("El stock no puede ser negativo.");
                    txtStock.requestFocus();
                    return;
                }

                if (ubicacion.isEmpty()) {
                    ubicacion = "No especificada";
                }

                ProductoFisico producto = new ProductoFisico(
                        nombre,
                        precio,
                        categoria,
                        codigo,
                        stock,
                        ubicacion
                );

                gestorInventario.agregarProductoFisico(producto);

                JOptionPane.showMessageDialog(this,
                        "Producto físico agregado exitosamente.\n\n"
                        + "Nombre: " + producto.getNombre() + "\n"
                        + "Código: " + producto.getCodigoDeBarras() + "\n"
                        + "Stock inicial: " + producto.getStockEnTienda(),
                        "Registro Exitoso",
                        JOptionPane.INFORMATION_MESSAGE);

                dispose();
            } else {
                // Servicio digital
                String duracionStr = JOptionPane.showInputDialog(
                        this,
                        "Duración estimada del servicio (minutos):",
                        "Duración del Servicio",
                        JOptionPane.QUESTION_MESSAGE
                );
                if (duracionStr == null) {
                    return;
                }
                duracionStr = duracionStr.trim();
                if (duracionStr.isEmpty()) {
                    mostrarAdvertencia("Debe especificar la duración estimada en minutos.");
                    return;
                }

                int duracionMin = Integer.parseInt(duracionStr);
                if (duracionMin <= 0) {
                    mostrarAdvertencia("La duración debe ser mayor que 0 minutos.");
                    return;
                }

                String descripcion = JOptionPane.showInputDialog(
                        this,
                        "Descripción del servicio digital:",
                        "Descripción",
                        JOptionPane.QUESTION_MESSAGE
                );
                if (descripcion == null || descripcion.trim().isEmpty()) {
                    descripcion = "Servicio digital sin descripción detallada.";
                }

                ServicioDigital servicio = new ServicioDigital(
                        nombre,
                        precio,
                        categoria,
                        duracionMin,
                        descripcion
                );

                gestorInventario.agregarServicioDigital(servicio);

                JOptionPane.showMessageDialog(this,
                        "Servicio digital agregado exitosamente.\n\n"
                        + "Nombre: " + servicio.getNombre() + "\n"
                        + "Categoría: " + servicio.getCategoria() + "\n"
                        + "Duración estimada: " + servicio.getDuracionEstimadaMinutos() + " minutos",
                        "Registro Exitoso",
                        JOptionPane.INFORMATION_MESSAGE);

                dispose();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Error en el formato de números. Verifique precio, stock y duración.\n\n"
                    + "Detalles: " + e.getMessage(),
                    "Error de Formato",
                    JOptionPane.ERROR_MESSAGE);
        } catch (ProductoInvalidoException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Producto Inválido",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error inesperado al registrar el producto:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this,
                mensaje,
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Método main opcional para probar el diálogo de forma independiente.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GestionarInventario gestor = new GestionarInventario();
            DialogoNuevoProducto dialogo = new DialogoNuevoProducto(null, true, gestor);
            dialogo.setVisible(true);
        });
    }
}
