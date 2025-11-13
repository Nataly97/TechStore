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
public class Tecnico extends Empleado {

    public Tecnico(int idEmpleado, String nombre, String cedula) {
        super(idEmpleado, nombre, cedula, "Técnico");
    }

    @Override
    public String tareaPrincipal() {
        return "Realizando servicio digital o instalación.";
    }
}
