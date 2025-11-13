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
public class Cliente implements Serializable{
    private static final long serialVersionUID = 1L;
    private String id; 
    private String nombre;
    private String cedula; 
    private String telefono;
    private String direccion; 
    private int puntosFidelidad;
    private List<Venta> historialCompras;

    // Constructor
    public Cliente(String id, String nombre, String cedula, String telefono, String direccion) {
        // Validación de unicidad de DNI se haría en el GestorClientes
        this.id = id;
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.direccion = direccion;
        this.puntosFidelidad = 0;
        this.historialCompras = new ArrayList<>();
    }

    // Regla de Negocio: Cálculo de descuento por fidelidad 
    public double calcularDescuentoFidelidad() {
        int numCompras = historialCompras.size();
        if (numCompras >= 6) {
            return 0.10; // 10% descuento
        } else if (numCompras >= 3) {
            return 0.05; // 5% descuento
        } else {
            return 0.0; // 0% descuento
        }
    }

    // Regla de Negocio: Acumular puntos (Por cada $10 de compra = 1 punto) 
    public void acumularPuntos(double montoCompra) {
        int nuevosPuntos = (int) (montoCompra / 10.0);
        this.puntosFidelidad += nuevosPuntos;
    }

    // Regla de Negocio: Canjear puntos (100 puntos = $10 de descuento) 
    public double canjearPuntos(int puntosACanjear) {
        if (puntosACanjear > this.puntosFidelidad) {
            puntosACanjear = this.puntosFidelidad; // Solo canjea los puntos disponibles
        }
        int bloquesDeCien = puntosACanjear / 100;
        
        this.puntosFidelidad -= (bloquesDeCien * 100);
        return bloquesDeCien * 10.0; // Descuento en dinero
    }
    //Recibe la lista de ventas y le grega la nueva venta  
    public void agregarCompra(Venta venta) {
        this.historialCompras.add(venta);
    }

    // Getters
    public String getId() {
        return id;
    }

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

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
