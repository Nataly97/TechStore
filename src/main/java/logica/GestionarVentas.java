/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import exception.StockInsuficienteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nataly
 */
public class GestionarVentas {

    private List<Venta> ventas;
    private Venta ventaActual;
    private int contadorFacturas;

    public GestionarVentas() {
        this.ventas = new ArrayList<>();
        this.ventaActual = null;
        this.contadorFacturas = 1;
    }

    /**
     * Funcionalidad: Iniciar nueva venta (crear ticket)
     */
    public void iniciarVenta(Cajero cajero, Cliente cliente) {
        int nuevoId = contadorFacturas;
        LocalDateTime ahora = LocalDateTime.now();
        this.ventaActual = new Venta(nuevoId, cajero, cliente, ahora);
    }

    /**
     * Funcionalidad: Agregar productos/servicios al ticket
     */
    public void agregarProductoAVenta(ProductoBase producto, int cantidad) throws StockInsuficienteException {
        if (ventaActual == null) {
            throw new IllegalStateException("No hay venta activa. Debe iniciar una venta primero.");
        }
        ventaActual.agregarItem(producto, cantidad, ventaActual.getIdVenta());
    }

    /**
     * Funcionalidad: Eliminar productos del ticket
     */
    public boolean eliminarProductoDeVenta(ProductoBase producto) {
        if (ventaActual == null) {
            return false;
        }
        return ventaActual.getDetalles().removeIf(d -> d.getProducto().equals(producto));
    }

    /**
     * Funcionalidad: Aplicar descuentos especiales (canjear puntos)
     */
    public void aplicarDescuentoPuntos(int puntos) {
        if (ventaActual != null && ventaActual.getCliente() != null) {
            double descuento = ventaActual.getCliente().canjearPuntos(puntos);
            // El descuento ya se aplica en el cálculo de la venta
        }
    }

    /**
     * Funcionalidad: Ver resumen del ticket
     */
    public String verResumenTicket() {
        if (ventaActual == null) {
            return "No hay venta activa";
        }

        StringBuilder resumen = new StringBuilder();
        resumen.append("=== RESUMEN DEL TICKET ===\n");
        resumen.append("Estado: ").append(ventaActual.getEstado()).append("\n\n");

        for (DetalleVenta detalle : ventaActual.getDetalles()) {
            resumen.append(detalle.getProducto().getNombre())
                    .append(" x").append(detalle.getCantidad())
                    .append(" = $").append(String.format("%.2f", detalle.getSubtotalItem()))
                    .append("\n");
        }

        resumen.append("\nSubtotal: $").append(String.format("%.2f", ventaActual.calcularSubtotal()));
        resumen.append("\nDescuentos: -$").append(String.format("%.2f", ventaActual.calcularDescuentoTotal()));
        resumen.append("\nTOTAL: $").append(String.format("%.2f", ventaActual.calcularTotal()));

        return resumen.toString();
    }

    /**
     * Funcionalidad: Calcular total (con impuestos y descuentos)
     */
    public double calcularTotalVenta() {
        if (ventaActual == null) {
            return 0.0;
        }
        return ventaActual.calcularTotal();
    }

    /**
     * Funcionalidad: Procesar pago y confirmar venta
     */
    public String confirmarVenta() {
        if (ventaActual == null) {
            return "No hay venta activa";
        }

        ventaActual.confirmarVenta();
        ventas.add(ventaActual);

        String factura = generarFactura();

        // Limpiar venta actual
        ventaActual = null;
        contadorFacturas++;

        return factura;
    }

    /**
     * Funcionalidad: Generar e imprimir factura
     */
    public String generarFactura() {
        if (ventaActual == null) {
            return "No hay venta para generar factura";
        }

        StringBuilder factura = new StringBuilder();
        factura.append("========================================\n");
        factura.append("           TECHSTORE                    \n");
        factura.append("   Tienda de Productos Tecnológicos    \n");
        factura.append("========================================\n");

        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        String fecha = ahora.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String numeroFactura = String.format("FACT-%s-%03d", fecha, contadorFacturas);

        factura.append("Factura: ").append(numeroFactura).append("\n");
        factura.append("Fecha: ").append(ahora.format(formatter)).append("\n");
        factura.append("Cajero: ").append(ventaActual.getCajero().getNombre()).append("\n");

        if (ventaActual.getCliente() != null) {
            Cliente cliente = ventaActual.getCliente();
            factura.append("Cliente: ").append(cliente.getNombre()).append("\n");
            factura.append("ID: ").append(cliente.getId()).append("\n");
            factura.append("Puntos: ").append(cliente.getPuntosFidelidad()).append("\n");
        }

        factura.append("========================================\n");
        factura.append("PRODUCTOS:\n");

        for (DetalleVenta detalle : ventaActual.getDetalles()) {
            factura.append(String.format("%-20s x%d\n",
                    detalle.getProducto().getNombre(),
                    detalle.getCantidad()));
            factura.append(String.format("  $%.2f c/u = $%.2f\n",
                    detalle.getProducto().calcularPrecioConDescuento(),
                    detalle.getSubtotalItem()));
        }

        factura.append("========================================\n");
        factura.append(String.format("Subtotal:        $%.2f\n", ventaActual.calcularSubtotal()));

        double descuento = ventaActual.calcularDescuentoTotal();
        if (descuento > 0) {
            factura.append(String.format("Descuentos:     -$%.2f\n", descuento));
        }

        double iva = ventaActual.calcularSubtotal() * 0.19;
        factura.append(String.format("IVA (19%%):       $%.2f\n", iva));
        factura.append("========================================\n");
        factura.append(String.format("TOTAL:           $%.2f\n", ventaActual.calcularTotal()));
        factura.append("========================================\n");
        factura.append("    ¡Gracias por su compra!            \n");
        factura.append("========================================\n");

        return factura.toString();
    }

    /**
     * Funcionalidad: Cancelar venta
     */
    public void cancelarVenta() {
        if (ventaActual != null) {
            ventaActual = null;
        }
    }

    /**
     * Reportes: Ventas del día por cajero
     */
    public String generarReporteVentasPorCajero(String nombreCajero) {
        LocalDateTime hoy = LocalDateTime.now();
        List<Venta> ventasDelDia = new ArrayList<>();
        double totalVentas = 0;

        for (Venta v : ventas) {
            if (v.getCajero().getNombre().equals(nombreCajero)
                    && v.getFechaHora().toLocalDate().equals(hoy.toLocalDate())
                    && v.getEstado() == Venta.Estado.PAGADA) {
                ventasDelDia.add(v);
                totalVentas += v.calcularTotal();
            }
        }

        StringBuilder reporte = new StringBuilder();
        reporte.append("=== REPORTE DE VENTAS DEL DÍA ===\n");
        reporte.append("Cajero: ").append(nombreCajero).append("\n");
        reporte.append("Fecha: ").append(hoy.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
        reporte.append("Cantidad de ventas: ").append(ventasDelDia.size()).append("\n");
        reporte.append("Total vendido: $").append(String.format("%.2f", totalVentas)).append("\n");

        return reporte.toString();
    }

    public Venta getVentaActual() {
        return ventaActual;
    }

    public List<Venta> getVentas() {
        return new ArrayList<>(ventas);
    }
}
