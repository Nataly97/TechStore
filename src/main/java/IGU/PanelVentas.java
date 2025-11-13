/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IGU;

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
    private Persistencia persistencia;

    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotal;
    private JTextArea txtResumen;

    private Cajero cajeroActual;
    private Cliente clienteActual;

    public PanelVentas() {
        this.gestorVentas = new GestionarVentas();
        this.gestorClientes = new GestionarClientes();
        this.gestorInventario = new GestionarInventario();
        this.persistencia = new Persistencia();

        // Cajero por defecto
        this.cajeroActual = new Cajero(123, "Juan Pérez", "12345678");

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

        btnBuscarCliente.addActionListener((ActionEvent e) -> {
            buscarCliente();
        });

        panelCliente.add(lblCliente);
        panelCliente.add(btnBuscarCliente);

        // Panel central - Productos
        String[] columnas = {"Producto", "Cantidad", "Precio Unit.", "Subtotal"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaProductos = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaProductos);

        // Panel de acciones
        JPanel panelAcciones = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton btnAgregarProducto = new JButton("Agregar Producto");
        JButton btnEliminarProducto = new JButton("Eliminar Producto");
        JButton btnIniciarVenta = new JButton("Iniciar Venta");

        btnIniciarVenta.addActionListener((ActionEvent e) -> {
            iniciarVenta();
        });

        btnAgregarProducto.addActionListener((ActionEvent e) -> {
            agregarProducto();
        });

        btnEliminarProducto.addActionListener((ActionEvent e) -> {
            eliminarProducto();
        });

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

        btnConfirmar.addActionListener((ActionEvent e) -> {
            confirmarVenta();
        });

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
        gestorVentas.iniciarVenta(cajeroActual, clienteActual);
        JOptionPane.showMessageDialog(this, "Venta iniciada correctamente");
    }

    private void buscarCliente() {
        String dni = JOptionPane.showInputDialog(this, "Ingrese DNI del cliente:");
        if (dni != null && !dni.trim().isEmpty()) {
            clienteActual = gestorClientes.buscarClientePorDNI(dni);
            if (clienteActual != null) {
                JOptionPane.showMessageDialog(this, "Cliente encontrado: " + clienteActual.getNombre());
            } else {
                JOptionPane.showMessageDialog(this, "Cliente no encontrado");
            }
        }
    }

    private void agregarProducto() {
        if (gestorVentas.getVentaActual() == null) {
            JOptionPane.showMessageDialog(this, "Debe iniciar una venta primero");
            return;
        }

        String codigo = JOptionPane.showInputDialog(this, "Ingrese código de barras:");
        if (codigo != null && !codigo.trim().isEmpty()) {
            ProductoFisico producto = gestorInventario.buscarPorCodigoBarras(codigo);
            if (producto != null) {
                String cantStr = JOptionPane.showInputDialog(this, "Cantidad:");
                try {
                    int cantidad = Integer.parseInt(cantStr);
                    // CORRECCIÓN: Eliminar el parámetro idVenta
                    gestorVentas.agregarProductoAVenta(producto, cantidad);
                    actualizarTabla();
                    actualizarTotal();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Cantidad inválida");
                } catch (StockInsuficienteException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Producto no encontrado");
            }
        }
    }

    private void eliminarProducto() {
        int fila = tablaProductos.getSelectedRow();
        if (fila >= 0) {
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
        }
    }

    private void confirmarVenta() {
        if (gestorVentas.getVentaActual() == null) {
            JOptionPane.showMessageDialog(this, "No hay venta activa");
            return;
        }

        String factura = gestorVentas.confirmarVenta();
        persistencia.guardarFactura(factura);

        JTextArea areaFactura = new JTextArea(factura);
        areaFactura.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaFactura);
        scroll.setPreferredSize(new Dimension(400, 500));

        JOptionPane.showMessageDialog(this, scroll, "Factura", JOptionPane.INFORMATION_MESSAGE);

        // Limpiar
        modeloTabla.setRowCount(0);
        lblTotal.setText("TOTAL: $0.00");
        clienteActual = null;
    }

}
