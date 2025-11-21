/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 *
 * @author Nataly
 */
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;              // Ej: CLI-001
    private String nombre;
    private String cedula;          // DNI / cédula
    private String telefono;
    private String direccion;       // Opcional
    private int puntosFidelidad;
    private List<Venta> historialCompras;

    // Constructor
    public Cliente(String id, String nombre, String cedula, String telefono, String direccion) {
        // El ID se asume ya generado correctamente (CLI-001, CLI-002, etc.)
        this.id = id;

        // Validaciones básicas
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacío.");
        }
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula/DNI del cliente no puede estar vacía.");
        }

        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.direccion = direccion;
        this.puntosFidelidad = 0;
        this.historialCompras = new ArrayList<>();
    }

    // Regla de Negocio: Cálculo de descuento por fidelidad 
    // 0-2 compras: 0% descuento
    // 3-5 compras: 5% descuento
    // 6+ compras: 10% descuento
    public double calcularDescuentoFidelidad() {
        int numCompras = historialCompras.size();
        if (numCompras >= 6) {
            return 0.10; // 10% descuento
        } else if (numCompras >= 3) {
            return 0.05; // 5% descuento
        } else {
            return 0.0;  // 0% descuento
        }
    }

    // Regla de Negocio: Acumular puntos 
    // Por cada $10 de compra = 1 punto 
    public void acumularPuntos(double montoCompra) {
        if (montoCompra <= 0) {
            return; // No acumula puntos por valores no positivos
        }
        int nuevosPuntos = (int) (montoCompra / 10.0);
        this.puntosFidelidad += nuevosPuntos;
    }

    // Regla de Negocio: Canjear puntos (100 puntos = $10 de descuento) 
    // Devuelve el valor en dinero del descuento aplicado
    public double canjearPuntos(int puntosACanjear) {
        if (puntosACanjear <= 0) {
            return 0.0;
        }

        // No se puede canjear más de lo que se tiene
        if (puntosACanjear > this.puntosFidelidad) {
            puntosACanjear = this.puntosFidelidad;
        }

        int bloquesDeCien = puntosACanjear / 100;

        this.puntosFidelidad -= (bloquesDeCien * 100);
        return bloquesDeCien * 10.0; // Descuento en dinero
    }

    // Agrega una venta al historial del cliente
    public void agregarCompra(Venta venta) {
        if (venta != null) {
            this.historialCompras.add(venta);
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getCedula() {
        return cedula;
    }

    // Si necesitas mantener el nombre getDni por compatibilidad:
    public String getDni() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public int getPuntosFidelidad() {
        return puntosFidelidad;
    }

    public List<Venta> getHistorialCompras() {
        return historialCompras;
    }

    // Setters básicos (sin permitir cambiar la cédula desde aquí para mantener unicidad)
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacío.");
        }
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
