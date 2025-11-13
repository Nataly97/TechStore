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
//Hereda de ProductoBase
public class ServicioDigital extends ProductoBase implements Serializable {

    private static final long serialVersionUID = 1L;
    private int duracionEstimadaMinutos;
    private String descripcion;

    public ServicioDigital(String nombre, double precio, String categoria, int duracion, String descripcion)
            throws ProductoInvalidoException {
        super(nombre, precio, categoria);
        this.duracionEstimadaMinutos = duracion;
        this.descripcion = descripcion;
    }

    // Implementación de método abstracto calcularPrecioConDescuento
    @Override
    public double calcularPrecioConDescuento() {
        // Los servicios digitales NO aplican descuentos de ofertas
        return this.precio;
    }

    // Getters
    public int getDuracionEstimadaMinutos() {
        return duracionEstimadaMinutos;
    }
}
