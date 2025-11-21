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
// Herencia de Empleado
public class Cajero extends Empleado implements Serializable {

    private static final long serialVersionUID = 1L;

    public Cajero(int idEmpleado,String nombre, String cedula, String cajero_Principal) {
        super(idEmpleado, nombre, cedula, "Cajero");
    }

    @Override
    public String tareaPrincipal() {
        return "Procesar venta y emitiendo factura.";
    }

}
