/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IGU;

import controlador.GestionarInventario;
import controlador.GestionarVentas;
import controlador.GestionarClientes;
import logica.*;
import exception.StockInsuficienteException;
import persistencia.Persistencia;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 *
 * @author Nataly
 */
public class PanelVentas extends JFrame {

    private GestionarVentas gestorVentas;
    private GestionarClientes gestorClientes;
    private GestionarInventario gestorInventario;

    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotal;

    private Cajero cajeroActual;
    private Cliente clienteActual;

    /**
     * Constructor principal de la ventana de ventas.
     */
    public PanelVentas(GestionarVentas gestionVentas,
                       GestionarInventario gestionInventario,
                       GestionarClientes gestionClientes,
                       Cajero cajeroActual) {

        // Usar los gestores que vienen desde el menú principal
        this.gestorVentas = gestionVentas;
        this.gestorClientes = gestionClientes;
        this.gestorInventario = gestionInventario;
        this.cajeroActual = cajeroActual;

        initComponents();
    }

    private void initComponents() {
        setTitle("TechStore - Realizar Venta");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior - Cliente
        JPanel panelCliente = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblCliente = new JLabel("Cliente: Sin cliente");
        JButton btnBuscarCliente = new JButton("Buscar Cliente");

        btnBuscarCliente.addActionListener((ActionEvent e) -> buscarCliente(lblCliente));

        panelCliente.add(lblCliente);
        panelCliente.add(btnBuscarCliente);

        // Panel central - Productos
        String[] columnas = {"Producto", "Cantidad", "Precio Unit.", "Subtotal"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaProductos = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaProductos);

        // Panel de acciones (lado derecho)
        JPanel panelAcciones = new JPanel(new GridLayout(3, 1, 10, 10));
        JButton btnIniciarVenta = new JButton("Iniciar Venta");
        JButton btnAgregarProducto = new JButton("Agregar Producto");
        JButton btnEliminarProducto = new JButton("Eliminar Producto");

        btnIniciarVenta.addActionListener((ActionEvent e) -> iniciarVenta());
        btnAgregarProducto.addActionListener((ActionEvent e) -> agregarProducto());
        btnEliminarProducto.addActionListener((ActionEvent e) -> eliminarProducto());

        panelAcciones.add(btnIniciarVenta);
        panelAcciones.add(btnAgregarProducto);
        panelAcciones.add(btnEliminarProducto);

        // Panel inferior - Total y confirmar
        JPanel panelInferior = new JPanel(new BorderLayout());
        lblTotal = new JLabel("TOTAL: $0.00", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 20));

        JButton btnConfirmar = new JButton("CONFIRMAR VENTA");
        btnConfirmar.setBackground(new Color(0, 153, 0));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFont(new Font("Arial", Font.BOLD, 16));
        btnConfirmar.addActionListener((ActionEvent e) -> confirmarVenta());

        panelInferior.add(lblTotal, BorderLayout.NORTH);
        panelInferior.add(btnConfirmar, BorderLayout.SOUTH);

        // Ensamblar
        panelPrincipal.add(panelCliente, BorderLayout.NORTH);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelAcciones, BorderLayout.EAST);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private void iniciarVenta() {
        try {
            gestorVentas.iniciarVenta(cajeroActual, clienteActual);
            JOptionPane.showMessageDialog(this,
                    "Venta iniciada correctamente.",
                    "Venta",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al iniciar la venta:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Busca un cliente por cédula/DNI y actualiza la etiqueta.
     */
    private void buscarCliente(JLabel lblCliente) {
        String dni = JOptionPane.showInputDialog(this,
                "Ingrese cédula/DNI del cliente:",
                "Buscar Cliente",
                JOptionPane.QUESTION_MESSAGE);

        if (dni != null && !dni.trim().isEmpty()) {
            try {
                // AJUSTA ESTE MÉTODO al que realmente tengas en GestionarClientes
                // Por ejemplo: buscarClientePorCedula(String cedula)
                clienteActual = gestorClientes.buscarClientePorDNI(dni.trim());

                if (clienteActual != null) {
                    lblCliente.setText("Cliente: " + clienteActual.getNombre() +
                                       " (" + clienteActual.getCedula() + ")");
                    JOptionPane.showMessageDialog(this,
                            "Cliente encontrado: " + clienteActual.getNombre(),
                            "Cliente",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Cliente no encontrado.",
                            "Cliente",
                            JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error al buscar cliente:\n" + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void agregarProducto() {
        if (gestorVentas.getVentaActual() == null) {
            JOptionPane.showMessageDialog(this,
                    "Debe iniciar una venta primero.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String codigo = JOptionPane.showInputDialog(this,
                "Ingrese código de barras del producto:",
                "Agregar Producto",
                JOptionPane.QUESTION_MESSAGE);

        if (codigo != null && !codigo.trim().isEmpty()) {
            try {
                // AJUSTA ESTE MÉTODO al que realmente tengas en GestionarInventario
                // Ej: buscarProductoFisicoPorCodigoBarras(String codigo)
                ProductoFisico producto = gestorInventario.buscarPorCodigoBarras(codigo.trim());

                if (producto != null) {
                    String cantStr = JOptionPane.showInputDialog(this,
                            "Cantidad:",
                            "Agregar Producto",
                            JOptionPane.QUESTION_MESSAGE);

                    if (cantStr != null && !cantStr.trim().isEmpty()) {
                        int cantidad = Integer.parseInt(cantStr.trim());

                        // Ajusta a la firma real de tu método en GestionarVentas
                        gestorVentas.agregarProductoAVenta(producto, cantidad);

                        actualizarTabla();
                        actualizarTotal();
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Producto no encontrado.",
                            "Producto",
                            JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Cantidad inválida.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (StockInsuficienteException e) {
                JOptionPane.showMessageDialog(this,
                        e.getMessage(),
                        "Stock insuficiente",
                        JOptionPane.WARNING_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error al agregar producto:\n" + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarProducto() {
        int fila = tablaProductos.getSelectedRow();
        if (fila >= 0 && gestorVentas.getVentaActual() != null) {
            // Opcional: también podrías quitar del DetalleVenta en gestorVentas
            modeloTabla.removeRow(fila);
            actualizarTotal();
        }
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        if (gestorVentas.getVentaActual() != null) {
            for (DetalleVenta detalle : gestorVentas.getVentaActual().getDetalles()) {
                Object[] fila = {
                        detalle.getProducto().getNombre(),
                        detalle.getCantidad(),
                        String.format("$%.2f", detalle.getProducto().calcularPrecioConDescuento()),
                        String.format("$%.2f", detalle.getSubtotalItem())
                };
                modeloTabla.addRow(fila);
            }
        }
    }

    private void actualizarTotal() {
        if (gestorVentas.getVentaActual() != null) {
            double total = gestorVentas.calcularTotalVenta();
            lblTotal.setText(String.format("TOTAL: $%.2f", total));
        } else {
            lblTotal.setText("TOTAL: $0.00");
        }
    }

    private void confirmarVenta() {
        if (gestorVentas.getVentaActual() == null) {
            JOptionPane.showMessageDialog(this,
                    "No hay venta activa.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String factura = gestorVentas.confirmarVenta();

            JTextArea areaFactura = new JTextArea(factura);
            areaFactura.setEditable(false);
            JScrollPane scroll = new JScrollPane(areaFactura);
            scroll.setPreferredSize(new Dimension(400, 500));

            JOptionPane.showMessageDialog(this,
                    scroll,
                    "Factura",
                    JOptionPane.INFORMATION_MESSAGE);

            // Limpiar
            modeloTabla.setRowCount(0);
            lblTotal.setText("TOTAL: $0.00");
            clienteActual = null;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al confirmar la venta:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}
