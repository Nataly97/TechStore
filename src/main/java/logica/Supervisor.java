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

    private static final long serialVersionUID = 1L;

    public Supervisor(int idEmpleado, String nombre, String cedula) {
        super(idEmpleado, nombre, cedula, "Supervisor");
    }

    @Override
    public String tareaPrincipal() {
        return "Autorizar descuentos especiales y supervisar ventas mayores.";
    }

    /**
     * Autoriza un descuento especial sobre una venta.
     * Regla simple: solo se permiten descuentos de hasta el 30% (0.30).
     */
    public boolean autorizarDescuentoEspecial(Venta venta, double porcentaje) {
        if (venta == null) {
            return false;
        }
        if (porcentaje < 0) {
            return false;
        }
        return porcentaje <= 0.30;
    }
}
