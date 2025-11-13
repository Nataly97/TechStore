/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exception;

/**
 *
 * @author Nataly
 */
public class CedulaUnicaException extends Exception {
    public CedulaUnicaException(String cedula) {
        super("La cédula/DNI " + cedula + " ya se encuentra registrada en el sistema.");
    }
}