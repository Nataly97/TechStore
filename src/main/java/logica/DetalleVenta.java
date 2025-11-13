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
        this.idVenta = idVenta;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public double getSubtotalItem() {
        return producto.calcularPrecioConDescuento() * cantidad;
    }

    // Getters
    public ProductoBase getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    //Setters
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

}
