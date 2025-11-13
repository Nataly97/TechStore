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
public abstract class Empleado implements Serializable {

    private static final long serialVersionUID = 1L;

    protected int idEmpleado;
    protected String nombre;
    protected String cedula;
    protected String rol;

    public Empleado(int idEmpleado, String nombre, String cedula, String rol) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.cedula = cedula;
        this.rol = rol;
    }

    // Metodo abstracto para las tareas del los empleados 
    public abstract String tareaPrincipal();

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getRol() {
        return rol;
    }
}
