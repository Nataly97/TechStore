/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import java.io.Serializable;

/**
 *
 * @author Nataly
 */
//Agregación con Procto Base 
//Composición con Venta
public class DetalleVenta implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idVenta;
    private ProductoBase producto; // Agregación con ProductoBase
    private int cantidad;

    public DetalleVenta(int idVenta, ProductoBase producto, int cantidad) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }

        this.idVenta = idVenta;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    // Calcula el subtotal de este ítem (precio con descuento * cantidad)
    public double getSubtotalItem() {
        return producto.calcularPrecioConDescuento() * cantidad;
    }

    // Getters
    public int getIdVenta() {
        return idVenta;
    }

    public ProductoBase getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    // Setters
    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }
        this.cantidad = cantidad;
    }
}
