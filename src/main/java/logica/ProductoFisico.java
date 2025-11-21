/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import exception.ProductoInvalidoException;
import java.time.LocalDate;
import java.io.Serializable;

/**
 *
 * @author Nataly
 */
// Herencia de ProductoBase e implementación de StockInventario
public class ProductoFisico extends ProductoBase implements StockInventario, Serializable {

    private static final long serialVersionUID = 1L;
    private String codigoDeBarras;
    private int stockEnTienda;
    private String ubicacion;
    private LocalDate fechaIngreso;
    private boolean enOferta;
    private double descuentoPorcentaje; // Ej: 0.10 = 10%

    public ProductoFisico(String nombre, double precio, String categoria,
                      String codigoDeBarras, int stock, String ubicacion)
            throws ProductoInvalidoException {

        super(nombre, precio, categoria);

        // El precio ya fue validado en ProductoBase (mín/máx), aquí solo reforzamos > 0
        if (precio <= 0) {
            throw new ProductoInvalidoException("El precio de un producto físico debe ser mayor a 0.");
        }

        // El stock no puede ser negativo
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }

        this.codigoDeBarras = codigoDeBarras;
        this.stockEnTienda = stock;
        this.ubicacion = ubicacion;
        this.fechaIngreso = LocalDate.now();
        this.enOferta = false;
        this.descuentoPorcentaje = 0.0;
    }

    // Implementación de método abstracto de ProductoBase 
    @Override
    public double calcularPrecioConDescuento() {
        // Los productos pueden estar en oferta (descuento porcentual)
        if (enOferta && descuentoPorcentaje > 0) {
            return this.precio * (1 - descuentoPorcentaje);
        }
        return this.precio;
    }

    // Implementación de la interfaz StockInventario
    @Override
    public void agregarStock(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad a agregar no puede ser negativa.");
        }
        this.stockEnTienda += cantidad;
    }

    @Override
    public void reducirStock(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad a reducir no puede ser negativa.");
        }
        if (this.stockEnTienda - cantidad < 0) {
            throw new IllegalArgumentException("No hay suficiente stock para reducir esa cantidad.");
        }
        this.stockEnTienda -= cantidad;
    }

    @Override
    public int getStockEnTienda() {
        return stockEnTienda;
    }

    @Override
    public boolean estaAgotado() {
        return stockEnTienda == 0;
    }

    // Getters 
    public String getCodigoDeBarras() {
        return codigoDeBarras;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public boolean isEnOferta() {
        return enOferta;
    }

    public double getDescuentoPorcentaje() {
        return descuentoPorcentaje;
    }

    // Setters
    public void setEnOferta(boolean enOferta, double descuento) {
        // descuento esperado entre 0 y 1 (0.10 = 10%)
        if (descuento < 0 || descuento > 1) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 1 (por ejemplo 0.10 = 10%).");
        }
        this.enOferta = enOferta;
        this.descuentoPorcentaje = descuento;
    }

    public void setDescuentoPorcentaje(double descuentoPorcentaje) {
        this.descuentoPorcentaje = descuentoPorcentaje;
    }

    public void setStockEnTienda(int stockEnTienda) {
        if (stockEnTienda < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        this.stockEnTienda = stockEnTienda;
    }

}
