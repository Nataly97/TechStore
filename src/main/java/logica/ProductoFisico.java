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
    private double descuentoPorcentaje;

    public ProductoFisico(String nombre, double precio, String categoria, String codigoDeBarras, int stock, String ubicacion)
            throws ProductoInvalidoException {
        super(nombre, precio, categoria);
        // Validación específica 
        // El precio debe ser mayor a 0
        if (precio <= 0) {
            throw new ProductoInvalidoException("El precio de un producto físico debe ser mayor a 0.");
        }
        //El stock no puede ser negativo
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }

        this.codigoDeBarras = codigoDeBarras;
        this.stockEnTienda = stock;
        this.ubicacion = ubicacion;
        this.fechaIngreso = LocalDate.now(); // LocalDate.now() obtine la fecha actual del sistema (año, mes, día)
        this.enOferta = false;
        this.descuentoPorcentaje = 0.0;
    }

    // Implementación de método abstracto de ProductoBase 
    @Override
    public double calcularPrecioConDescuento() {
        //Los productos pueden estar en oferta (descuento porcentual)
        if (enOferta && descuentoPorcentaje > 0) {
            return this.precio * (1 - descuentoPorcentaje);
        }
        return this.precio;
    }

    // Implementación de la clase interz StockInventario
    //gestionar el inventario en tienda
    @Override
    public void agregarStock(int cantidad) {
        this.stockEnTienda += cantidad;
    }

    @Override
    public void reducirStock(int cantidad) {
        if (this.stockEnTienda - cantidad < 0) {
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

    //Setters
    public void setEnOferta(boolean enOferta, double descuento) {
        this.enOferta = enOferta;
        this.descuentoPorcentaje = descuento;
    }

    public void setStockEnTienda(int stockEnTienda) {
        this.stockEnTienda = stockEnTienda;
    }

}
