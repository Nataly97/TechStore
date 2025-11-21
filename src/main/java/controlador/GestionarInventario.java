/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import exception.ProductoInvalidoException;
import exception.StockInsuficienteException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Nataly
 */
public class GestionarInventario {

   private List<ProductoBase> productos;
    private static final String ARCHIVO_INVENTARIO = "inventario.dat";

    public GestionarInventario() {
        this.productos = new ArrayList<>();
        cargarInventario();
    }

    /**
     * Agregar un producto físico al inventario
     */
    public void agregarProducto(ProductoFisico producto) throws ProductoInvalidoException {
        if (producto == null) {
            throw new ProductoInvalidoException("El producto no puede ser nulo");
        }
        
        // Validar que no exista un producto con el mismo código de barras
        boolean existe = productos.stream()
                .filter(p -> p instanceof ProductoFisico)
                .map(p -> (ProductoFisico) p)
                .anyMatch(p -> p.getCodigoDeBarras().equals(producto.getCodigoDeBarras()));
        
        if (existe) {
            throw new ProductoInvalidoException("Ya existe un producto con ese código de barras");
        }
        
        productos.add(producto);
        guardarInventario();
    }

    /**
     * Agregar un servicio digital al inventario
     */
    public void agregarStock(ServicioDigital servicio) throws ProductoInvalidoException {
        if (servicio == null) {
            throw new ProductoInvalidoException("El servicio no puede ser nulo");
        }
        
        productos.add(servicio);
        guardarInventario();
    }

    /**
     * Buscar producto por código de barras
     */
    public ProductoFisico buscarPorCodigoBarras(String codigoBarras) {
        return productos.stream()
                .filter(p -> p instanceof ProductoFisico)
                .map(p -> (ProductoFisico) p)
                .filter(p -> p.getCodigoDeBarras().equals(codigoBarras))
                .findFirst()
                .orElse(null);
    }

    /**
     * Buscar productos por nombre
     */
    public List<ProductoBase> buscarPorNombre(String nombre) {
        String nombreLower = nombre.toLowerCase();
        return productos.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(nombreLower))
                .collect(Collectors.toList());
    }

    /**
     * Buscar productos por categoría
     */
    public List<ProductoBase> buscarPorCategoria(String categoria) {
        return productos.stream()
                .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }

    /**
     * Actualizar stock de un producto físico
     */
    public boolean actualizarStock(String codigoBarras, int nuevoStock) {
        ProductoFisico producto = buscarPorCodigoBarras(codigoBarras);
        if (producto != null) {
            producto.setStockEnTienda(nuevoStock);
            guardarInventario();
            return true;
        }
        return false;
    }

    /**
     * Reducir stock de un producto (usado en ventas)
     */
    public void reducirStock(String codigoBarras, int cantidad) throws StockInsuficienteException {
        ProductoFisico producto = buscarPorCodigoBarras(codigoBarras);
        if (producto != null) {
            producto.reducirStock(cantidad);
            guardarInventario();
        }
    }

    /**
     * Obtener todos los productos
     */
    public List<ProductoBase> obtenerTodosLosProductos() {
        return new ArrayList<>(productos);
    }

    /**
     * Obtener productos con stock bajo (menos de 5 unidades)
     */
    public List<ProductoFisico> obtenerProductosStockBajo() {
        return productos.stream()
                .filter(p -> p instanceof ProductoFisico)
                .map(p -> (ProductoFisico) p)
                .filter(p -> p.getStockEnTienda() < 5)
                .collect(Collectors.toList());
    }

    /**
     * Eliminar producto del inventario
     */
    public boolean eliminarProducto(String codigoBarras) {
        ProductoFisico producto = buscarPorCodigoBarras(codigoBarras);
        if (producto != null) {
            productos.remove(producto);
            guardarInventario();
            return true;
        }
        return false;
    }

    /**
     * Obtener cantidad total de productos
     */
    public int getCantidadProductos() {
        return productos.size();
    }

    /**
     * Guardar inventario en archivo
     */
    private void guardarInventario() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_INVENTARIO))) {
            oos.writeObject(productos);
        } catch (IOException e) {
            System.err.println("Error al guardar inventario: " + e.getMessage());
        }
    }

    /**
     * Cargar inventario desde archivo
     */
    @SuppressWarnings("unchecked")
    private void cargarInventario() {
        File archivo = new File(ARCHIVO_INVENTARIO);
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_INVENTARIO))) {
                productos = (List<ProductoBase>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al cargar inventario: " + e.getMessage());
                productos = new ArrayList<>();
            }
        }
    }
}
