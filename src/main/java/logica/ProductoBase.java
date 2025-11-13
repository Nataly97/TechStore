/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import exception.ProductoInvalidoException;
import java.io.Serializable;

/**
 *
 * @author Nataly
 */
public abstract class ProductoBase implements Serializable {

    private static final long serialVersionUID = 1L;
    protected String nombre;
    protected double precio;
    protected String categoria;

    // Constructor
    public ProductoBase(String nombre, double precio, String categoria) throws ProductoInvalidoException {
        // Validación de reglas de negocio Productos Físicos
        //Nombre: El nombre del producto no puede estar vacío
        //trim() Quita los espacios en blaco al final y al inicio del string
        //isEmpty() Valida si la longitud del string es cero
        if (nombre == null || nombre.trim().isEmpty()) {
            //IllegalArgumentException exception para valores nulos
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        //
        //El precio mínimo es $0.01
        //El precio máximo es $99,999.99
        if (precio < 0.01 || precio > 99999.99) {
            throw new ProductoInvalidoException("El precio debe estar entre $0.01 y $99,999.99.");
        }

        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }

    // Método abstracto 
    public abstract double calcularPrecioConDescuento();

    // Getters
    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public String getCategoria() {
        return categoria;
    }
}
