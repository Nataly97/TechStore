/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

/**
 *
 * @author Nataly
 */
// Herencia de Empleado
public class Supervisor extends Empleado {

    public Supervisor(int idEmpleado, String nombre, String cedula) {
        super(idEmpleado, nombre, cedula, "Supervisor"); // Rol 
    }

    @Override
    public String tareaPrincipal() {
        return "Autorizando descuentos especiales y ventas especiales.";
    }

    // Autorizar descuento especial
    public boolean autorizarDescuentoEspecial(Venta venta, double porcentaje) {
        return porcentaje <= 0.30;
    }
}
