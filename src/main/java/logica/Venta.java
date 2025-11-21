/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import exception.StockInsuficienteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 *
 * @author Nataly
 */
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;

    // Definición de estados de la venta
    public enum Estado {
        EN_PROCESO,
        PAGADA,
        ENTREGADA,
        CANCELADA
    }

    private int idVenta;
    private String numeroFactura;
    private Cliente cliente;
    private Cajero cajero;
    private List<DetalleVenta> detalles; // Composición
    private LocalDateTime fechaHora;
    private Estado estado;
    private double descuentoPuntosCanjeados;
    private double ivaPorcentaje = 0.19; // IVA configurable en sistema (19%)

    public Venta(int idVenta, Cajero cajero, Cliente cliente) {
        this.idVenta = idVenta;
        this.cajero = cajero;
        this.cliente = cliente;
        this.detalles = new ArrayList<>();
        this.estado = Estado.EN_PROCESO;
        this.descuentoPuntosCanjeados = 0.0;
        this.fechaHora = LocalDateTime.now();
    }

    // Regla de Negocio: Agregar productos/servicios al ticket 
    public void agregarItem(ProductoBase producto, int cantidad) throws StockInsuficienteException {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }

        // Regla de Negocio: Validación de stock para ProductoFísico
        if (producto instanceof ProductoFisico) {
            ProductoFisico pf = (ProductoFisico) producto;
            if (pf.getStockEnTienda() < cantidad) {
                throw new StockInsuficienteException(pf.getNombre(), cantidad, pf.getStockEnTienda());
            }
        }

        // Buscar si el producto ya está en el detalle para consolidar
        boolean encontrado = false;
        for (DetalleVenta detalle : detalles) {
            if (detalle.getProducto().equals(producto)) {
                // Validar stock nuevamente con la cantidad acumulada
                if (producto instanceof ProductoFisico) {
                    ProductoFisico pf = (ProductoFisico) producto;
                    int nuevaCantidad = detalle.getCantidad() + cantidad;
                    if (pf.getStockEnTienda() < nuevaCantidad) {
                        throw new StockInsuficienteException(pf.getNombre(), nuevaCantidad, pf.getStockEnTienda());
                    }
                }
                detalle.setCantidad(detalle.getCantidad() + cantidad);
                encontrado = true;
                break;
            }
        }

        // Si no se encontró, agregar nuevo detalle
        if (!encontrado) {
            detalles.add(new DetalleVenta(this.idVenta, producto, cantidad));
        }
    }

    // Eliminar un producto del ticket
    public void eliminarItem(ProductoBase producto) {
        detalles.removeIf(detalle -> detalle.getProducto().equals(producto));
    }

    // Aplicar descuento por puntos canjeados
    public void aplicarDescuentoPuntos(int puntosACanjear) {
        if (cliente != null) {
            this.descuentoPuntosCanjeados = cliente.canjearPuntos(puntosACanjear);
        }
    }

    // El ticket calcula automáticamente
    // Cálculo de subtotales, descuentos y total 
    public double calcularSubtotal() {
        double subtotal = 0;
        for (DetalleVenta detalle : detalles) {
            // Usa el precio con descuento de oferta del producto 
            subtotal += detalle.getSubtotalItem();
        }
        return subtotal;
    }

    // Calcular descuento total
    public double calcularDescuentoTotal() {
        double descuentoFidelidad = 0.0;
        if (cliente != null) {
            // Descuento por fidelidad del cliente
            descuentoFidelidad = calcularSubtotal() * cliente.calcularDescuentoFidelidad();
        }

        // Descuento por puntos canjeados
        return descuentoFidelidad + this.descuentoPuntosCanjeados;
    }

    // Calcular precio con impuestos 
    public double calcularTotal() {
        // Regla de Negocio: Validar que el ticket no esté vacío
        if (detalles.isEmpty()) {
            return 0.0;
        }
        double subtotalNeto = calcularSubtotal() - calcularDescuentoTotal();
        // IVA/Impuesto 
        double totalConImpuesto = subtotalNeto * (1 + ivaPorcentaje);

        return totalConImpuesto;
    }

    // Proceso de Venta: Confirmación
    // El número de factura debe ser asignado desde fuera (GestionarVentas)
    public void confirmarVenta(String numeroFactura) {
        if (this.estado != Estado.EN_PROCESO) {
            throw new IllegalStateException("Solo se pueden confirmar ventas en proceso.");
        }

        if (detalles.isEmpty()) {
            throw new IllegalStateException("No se puede confirmar una venta sin productos.");
        }

        // Reducir stock de productos físicos 
        for (DetalleVenta detalle : detalles) {
            if (detalle.getProducto() instanceof ProductoFisico) {
                ((ProductoFisico) detalle.getProducto()).reducirStock(detalle.getCantidad());
            }
        }

        // Asignar número de factura
        this.numeroFactura = numeroFactura;
        this.fechaHora = LocalDateTime.now();

        // Actualizar historial de cliente y puntos
        if (cliente != null) {
            cliente.agregarCompra(this);
            cliente.acumularPuntos(calcularSubtotal());
        }

        // Cambiar estado
        this.estado = Estado.PAGADA;
    }

    // Marcar venta como entregada
    public void marcarComoEntregada() {
        if (this.estado != Estado.PAGADA) {
            throw new IllegalStateException("Solo se pueden entregar ventas pagadas.");
        }
        this.estado = Estado.ENTREGADA;
    }

    // Cancelar venta (solo si está en proceso)
    public void cancelarVenta() {
        if (this.estado != Estado.EN_PROCESO) {
            throw new IllegalStateException("Solo se pueden cancelar ventas en proceso.");
        }
        this.estado = Estado.CANCELADA;
    }

    // Getters
    public int getIdVenta() {
        return idVenta;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Cajero getCajero() {
        return cajero;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public Estado getEstado() {
        return estado;
    }

    public double getDescuentoPuntosCanjeados() {
        return descuentoPuntosCanjeados;
    }

    public double getIvaPorcentaje() {
        return ivaPorcentaje;
    }

    // Setter para IVA (si necesitas configurarlo)
    public void setIvaPorcentaje(double ivaPorcentaje) {
        if (ivaPorcentaje < 0 || ivaPorcentaje > 1) {
            throw new IllegalArgumentException("El IVA debe estar entre 0 y 1 (por ejemplo 0.19 = 19%).");
        }
        this.ivaPorcentaje = ivaPorcentaje;
    }
}
