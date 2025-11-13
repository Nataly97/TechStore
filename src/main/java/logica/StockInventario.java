/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package logica;

/**
 *
 * @author Nataly
 */
public interface StockInventario {

    // Define el comportamiento para la gestión de inventario
    void agregarStock(int cantidad);

    void reducirStock(int cantidad);

    int getStockEnTienda();

    boolean estaAgotado();
}
