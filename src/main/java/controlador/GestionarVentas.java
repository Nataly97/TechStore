/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import exception.StockInsuficienteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import logica.Cajero;
import logica.Cliente;
import logica.DetalleVenta;
import persistencia.Persistencia;
import logica.ProductoBase;
import logica.Venta;

/**
 *
 * @author Nataly
 */
public class GestionarVentas {

    private List<Venta> ventas;
    private Venta ventaActual;
    private int contadorVentas;
    private int contadorFacturasDia;
    private String fechaActual;
    private Persistencia persistencia;

    public GestionarVentas() {
        // Inicializar sistema de persistencia
        this.persistencia = new Persistencia();

        // Cargar ventas existentes desde el archivo
        this.ventas = persistencia.cargarVentas();

        // Calcular el siguiente ID de venta
        int maxId = ventas.stream()
                .map(Venta::getIdVenta)
                .max(Integer::compareTo)
                .orElse(0);
        this.contadorVentas = maxId + 1;

        // Inicializar contador de facturas del día
        this.fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        this.contadorFacturasDia = calcularContadorFacturasDia();

        this.ventaActual = null;

        System.out.println("✓ GestionarVentas inicializado con " + ventas.size() + " venta(s)");
    }

    /**
     * Calcula el contador de facturas del día actual.
     */
    private int calcularContadorFacturasDia() {
        String hoy = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int maxFactura = ventas.stream()
                .filter(v -> v.getNumeroFactura() != null && v.getNumeroFactura().contains(hoy))
                .map(v -> {
                    try {
                        String[] partes = v.getNumeroFactura().split("-");
                        if (partes.length == 3) {
                            return Integer.parseInt(partes[2]);
                        }
                    } catch (NumberFormatException e) {
                        // Ignorar
                    }
                    return 0;
                })
                .max(Integer::compareTo)
                .orElse(0);
        return maxFactura + 1;
    }

    /**
     * Funcionalidad: Iniciar nueva venta (crear ticket).
     */
    public void iniciarVenta(Cajero cajero, Cliente cliente) {
        if (cajero == null) {
            throw new IllegalArgumentException("El cajero no puede ser nulo.");
        }

        int nuevoId = contadorVentas;
        this.ventaActual = new Venta(nuevoId, cajero, cliente);
        System.out.println("✓ Nueva venta iniciada: ID " + nuevoId);
    }

    /**
     * Funcionalidad: Agregar productos/servicios al ticket.
     */
    public void agregarProductoAVenta(ProductoBase producto, int cantidad) throws StockInsuficienteException {
        if (ventaActual == null) {
            throw new IllegalStateException("No hay venta activa. Debe iniciar una venta primero.");
        }
        ventaActual.agregarItem(producto, cantidad);
        System.out.println("✓ Producto agregado a la venta: " + producto.getNombre() + " x" + cantidad);
    }

    /**
     * Funcionalidad: Eliminar productos del ticket.
     */
    public boolean eliminarProductoDeVenta(ProductoBase producto) {
        if (ventaActual == null) {
            return false;
        }
        ventaActual.eliminarItem(producto);
        System.out.println("✓ Producto eliminado de la venta: " + producto.getNombre());
        return true;
    }

    /**
     * Funcionalidad: Aplicar descuentos especiales (canjear puntos).
     */
    public void aplicarDescuentoPuntos(int puntos) {
        if (ventaActual == null) {
            throw new IllegalStateException("No hay venta activa.");
        }
        if (ventaActual.getCliente() == null) {
            throw new IllegalStateException("La venta no tiene cliente asociado.");
        }
        ventaActual.aplicarDescuentoPuntos(puntos);
        System.out.println("✓ Descuento por puntos aplicado: " + puntos + " puntos");
    }

    /**
     * Funcionalidad: Ver resumen del ticket.
     */
    public String verResumenTicket() {
        if (ventaActual == null) {
            return "No hay venta activa";
        }

        StringBuilder resumen = new StringBuilder();
        resumen.append("========================================\n");
        resumen.append("         RESUMEN DEL TICKET\n");
        resumen.append("========================================\n");
        resumen.append("Estado: ").append(ventaActual.getEstado()).append("\n");
        resumen.append("Cajero: ").append(ventaActual.getCajero().getNombre()).append("\n");
        
        if (ventaActual.getCliente() != null) {
            resumen.append("Cliente: ").append(ventaActual.getCliente().getNombre()).append("\n");
        }
        
        resumen.append("----------------------------------------\n");
        resumen.append("PRODUCTOS:\n");

        for (DetalleVenta detalle : ventaActual.getDetalles()) {
            resumen.append(String.format("%-25s x%-3d $%8.2f\n",
                    detalle.getProducto().getNombre(),
                    detalle.getCantidad(),
                    detalle.getSubtotalItem()));
        }

        resumen.append("----------------------------------------\n");
        resumen.append(String.format("Subtotal:           $%8.2f\n", ventaActual.calcularSubtotal()));
        resumen.append(String.format("Descuentos:        -$%8.2f\n", ventaActual.calcularDescuentoTotal()));
        resumen.append(String.format("IVA (%.0f%%):           $%8.2f\n", 
                ventaActual.getIvaPorcentaje() * 100,
                (ventaActual.calcularSubtotal() - ventaActual.calcularDescuentoTotal()) * ventaActual.getIvaPorcentaje()));
        resumen.append("========================================\n");
        resumen.append(String.format("TOTAL:              $%8.2f\n", ventaActual.calcularTotal()));
        resumen.append("========================================\n");

        return resumen.toString();
    }

    /**
     * Funcionalidad: Calcular total (con impuestos y descuentos).
     */
    public double calcularTotalVenta() {
        if (ventaActual == null) {
            return 0.0;
        }
        return ventaActual.calcularTotal();
    }

    /**
     * Funcionalidad: Procesar pago y confirmar venta.
     */
    public String confirmarVenta() {
        if (ventaActual == null) {
            throw new IllegalStateException("No hay venta activa");
        }

        // Verificar que la fecha actual sea la misma, si no, reiniciar contador
        String hoy = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        if (!hoy.equals(fechaActual)) {
            fechaActual = hoy;
            contadorFacturasDia = 1;
        }

        // Generar número de factura único
        String numeroFactura = String.format("FACT-%s-%03d", fechaActual, contadorFacturasDia);

        // Confirmar la venta (esto reduce stock, actualiza cliente, etc.)
        ventaActual.confirmarVenta(numeroFactura);

        // Agregar a la lista de ventas
        ventas.add(ventaActual);

        // Guardar en persistencia
        persistencia.guardarVentas(ventas);

        // Generar factura para imprimir
        String factura = generarFactura(ventaActual);

        System.out.println("✓ Venta confirmada: " + numeroFactura);

        // Incrementar contadores
        contadorFacturasDia++;
        contadorVentas++;

        // Limpiar venta actual
        ventaActual = null;

        return factura;
    }

    /**
     * Funcionalidad: Generar e imprimir factura.
     */
    private String generarFactura(Venta venta) {
        if (venta == null) {
            return "No hay venta para generar factura";
        }

        StringBuilder factura = new StringBuilder();
        factura.append("========================================\n");
        factura.append("            TECHSTORE\n");
        factura.append("   Tienda de Productos Tecnológicos\n");
        factura.append("========================================\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        factura.append("Factura: ").append(venta.getNumeroFactura()).append("\n");
        factura.append("Fecha: ").append(venta.getFechaHora().format(formatter)).append("\n");
        factura.append("Cajero: ").append(venta.getCajero().getNombre()).append("\n");

        if (venta.getCliente() != null) {
            Cliente cliente = venta.getCliente();
            factura.append("Cliente: ").append(cliente.getNombre()).append("\n");
            factura.append("ID: ").append(cliente.getId()).append("\n");
            factura.append("Puntos: ").append(cliente.getPuntosFidelidad()).append("\n");
        } else {
            factura.append("Cliente: Venta sin cliente\n");
        }

        factura.append("========================================\n");
        factura.append("PRODUCTOS:\n");

        for (DetalleVenta detalle : venta.getDetalles()) {
            factura.append(String.format("%-25s x%d\n",
                    detalle.getProducto().getNombre(),
                    detalle.getCantidad()));
            factura.append(String.format("  $%.2f c/u = $%.2f\n",
                    detalle.getProducto().calcularPrecioConDescuento(),
                    detalle.getSubtotalItem()));
        }

        factura.append("========================================\n");
        factura.append(String.format("Subtotal:           $%.2f\n", venta.calcularSubtotal()));

        double descuento = venta.calcularDescuentoTotal();
        if (descuento > 0) {
            factura.append(String.format("Descuentos:        -$%.2f\n", descuento));
        }

        double subtotalNeto = venta.calcularSubtotal() - descuento;
        double iva = subtotalNeto * venta.getIvaPorcentaje();
        factura.append(String.format("IVA (%.0f%%):           $%.2f\n", venta.getIvaPorcentaje() * 100, iva));
        factura.append("========================================\n");
        factura.append(String.format("TOTAL:              $%.2f\n", venta.calcularTotal()));
        factura.append("========================================\n");
        factura.append("      ¡Gracias por su compra!\n");
        factura.append("========================================\n");

        return factura.toString();
    }

    /**
     * Funcionalidad: Cancelar venta actual.
     */
    public void cancelarVenta() {
        if (ventaActual != null) {
            ventaActual.cancelarVenta();
            ventaActual = null;
            System.out.println("✓ Venta cancelada");
        }
    }

    /**
     * Reportes: Ventas del día por cajero.
     */
    public String generarReporteVentasPorCajero(String nombreCajero) {
        LocalDateTime hoy = LocalDateTime.now();
        List<Venta> ventasDelDia = ventas.stream()
                .filter(v -> v.getCajero().getNombre().equalsIgnoreCase(nombreCajero))
                .filter(v -> v.getFechaHora().toLocalDate().equals(hoy.toLocalDate()))
                .filter(v -> v.getEstado() == Venta.Estado.PAGADA || v.getEstado() == Venta.Estado.ENTREGADA)
                .collect(Collectors.toList());

        double totalVentas = ventasDelDia.stream()
                .mapToDouble(Venta::calcularTotal)
                .sum();

        StringBuilder reporte = new StringBuilder();
        reporte.append("========================================\n");
        reporte.append("    REPORTE DE VENTAS DEL DÍA\n");
        reporte.append("========================================\n");
        reporte.append("Cajero: ").append(nombreCajero).append("\n");
        reporte.append("Fecha: ").append(hoy.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
        reporte.append("Cantidad de ventas: ").append(ventasDelDia.size()).append("\n");
        reporte.append(String.format("Total vendido: $%.2f\n", totalVentas));
        reporte.append("========================================\n");

        return reporte.toString();
    }

    /**
     * Reportes: Total de ventas por período.
     */
    public String generarReporteTotalVentas() {
        double totalVentas = ventas.stream()
                .filter(v -> v.getEstado() == Venta.Estado.PAGADA || v.getEstado() == Venta.Estado.ENTREGADA)
                .mapToDouble(Venta::calcularTotal)
                .sum();

        StringBuilder reporte = new StringBuilder();
        reporte.append("========================================\n");
        reporte.append("    REPORTE TOTAL DE VENTAS\n");
        reporte.append("========================================\n");
        reporte.append("Total de ventas: ").append(ventas.size()).append("\n");
        reporte.append(String.format("Monto total: $%.2f\n", totalVentas));
        reporte.append("========================================\n");

        return reporte.toString();
    }

    /**
     * Reportes: Productos más vendidos.
     */
    public String generarReporteProductosMasVendidos() {
        // Aquí podrías implementar lógica más compleja
        // Por ahora, un reporte básico
        StringBuilder reporte = new StringBuilder();
        reporte.append("========================================\n");
        reporte.append("  PRODUCTOS MÁS VENDIDOS\n");
        reporte.append("========================================\n");
        reporte.append("(Funcionalidad pendiente de implementar)\n");
        reporte.append("========================================\n");
        return reporte.toString();
    }

    // Getters
    public Venta getVentaActual() {
        return ventaActual;
    }

    public List<Venta> getVentas() {
        return new ArrayList<>(ventas);
    }

    public List<Venta> getVentasDelDia() {
        LocalDateTime hoy = LocalDateTime.now();
        return ventas.stream()
                .filter(v -> v.getFechaHora().toLocalDate().equals(hoy.toLocalDate()))
                .collect(Collectors.toList());
    }

    /**
     * Forzar guardado manual de todas las ventas.
     */
    public void guardarTodas() {
        persistencia.guardarVentas(ventas);
        System.out.println("✓ Todas las ventas guardadas manualmente");
    }

    /**
     * Recargar ventas desde el archivo.
     */
    public void recargarVentas() {
        this.ventas = persistencia.cargarVentas();
        System.out.println("✓ Ventas recargadas desde el archivo: " + ventas.size() + " venta(s)");
    }
}
