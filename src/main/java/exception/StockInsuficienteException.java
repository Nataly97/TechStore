/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exception;

/**
 *
 * @author Nataly
 */
public class StockInsuficienteException extends Exception {
    public StockInsuficienteException(String producto, int stockSolicitado, int stockDisponible) {
        super("Stock insuficiente para " + producto + ". Solicitado: " + stockSolicitado + ", Disponible: " + stockDisponible);
    }
}
