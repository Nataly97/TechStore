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
    private double ivaPorcentaje = 0.19; //IVA configurable en sistema 

    public Venta(int idVenta, Cajero cajero, Cliente cliente, LocalDateTime fechaHora) {
        this.idVenta = idVenta;
        this.cajero = cajero;
        this.cliente = cliente;
        this.detalles = new ArrayList<>();
        this.estado = Estado.EN_PROCESO;
        this.descuentoPuntosCanjeados = 0.0;
        this.fechaHora = fechaHora;

    }

    // Regla de Negocio: Agregar productos/servicios al ticket 
    public void agregarItem(ProductoBase producto, int cantidad, int idVenta) throws StockInsuficienteException {
        if (cantidad <= 0) {
            return;
        }

        // Regla de Negocio: Validación de stock para ProductoFísico
        if (producto instanceof ProductoFisico) {
            ProductoFisico pf = (ProductoFisico) producto;
            if (pf.getStockEnTienda() < cantidad) {
                throw new StockInsuficienteException(pf.getNombre(), cantidad, pf.getStockEnTienda());
            }
        }

        // Buscar si el producto ya está en el detalle para consolidar un solo detalle
        for (DetalleVenta detalle : detalles) {
            if (detalle.getProducto().equals(producto)) {
                detalle.setCantidad(detalle.getCantidad() + cantidad);
            } else {
                detalles.add(new DetalleVenta(idVenta, producto, cantidad));
            }
        }
    }

    //El ticket calcula automáticamente
    // Cálculo de subtotales, descuentos y total 
    public double calcularSubtotal() {
        double subtotal = 0;
        for (DetalleVenta detalle : detalles) {
            // Usa el precio con descuento de oferta del producto 
            subtotal += detalle.getProducto().calcularPrecioConDescuento() * detalle.getCantidad();
        }
        return subtotal;
    }

    //Calular descuento total
    public double calcularDescuentoTotal() {
        double descuentoFidelidad = 0.0;
        if (cliente != null) {
            // Descuento por fidelidad del cliente
            descuentoFidelidad = calcularSubtotal() * cliente.calcularDescuentoFidelidad();
        }

        // Descuento por puntos canjeados
        return descuentoFidelidad + this.descuentoPuntosCanjeados;
    }

    //Calcular precio con impuestos 
    public double calcularTotal() {
        //Regla de Negocio: Validar que el ticket no esté vacío
        if (detalles.isEmpty()) {
            return 0.0;
        }
        double subtotalNeto = calcularSubtotal() - calcularDescuentoTotal();
        // IVA/Impuesto 
        double totalConImpuesto = subtotalNeto * (1 + ivaPorcentaje);

        return totalConImpuesto;
    }

    // Proceso de Venta: Confirmación
    public void confirmarVenta() {
        if (this.estado != Estado.EN_PROCESO) {
            return;
        }

        // Reducir stock de productos físicos 
        for (DetalleVenta detalle : detalles) {
            if (detalle.getProducto() instanceof ProductoFisico) {
                ((ProductoFisico) detalle.getProducto()).reducirStock(detalle.getCantidad());
            }
        }

        // Generar número de factura único con la fecha
        this.fechaHora = LocalDateTime.now();
        String fecha = String.format("%tY%tm%td", fechaHora, fechaHora, fechaHora);
        this.numeroFactura = "FACT-" + fecha + "-001";

        //Actualizar historial de cliente y puntos
        if (cliente != null) {
            cliente.agregarCompra(this);
            cliente.acumularPuntos(calcularSubtotal());
        }

        //Cambiar estado
        this.estado = Estado.PAGADA;
    }

    // Getters
    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Estado getEstado() {
        return estado;
    }

    public Cajero getCajero() {
        return cajero;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public int getIdVenta() {
        return idVenta;
    }
}
