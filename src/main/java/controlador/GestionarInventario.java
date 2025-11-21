/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import exception.ProductoInvalidoException;
import exception.StockInsuficienteException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import persistencia.Persistencia;
import logica.ProductoBase;
import logica.ProductoFisico;
import logica.ServicioDigital;

/**
 *
 * @author Nataly
 */
public class GestionarInventario {

    private List<ProductoBase> productos;
    private Persistencia persistencia;

    public GestionarInventario() {
        // Inicializar sistema de persistencia
        this.persistencia = new Persistencia();

        // Cargar productos existentes desde el archivo
        this.productos = persistencia.cargarInventario();

        System.out.println("✓ GestionarInventario inicializado con " + productos.size() + " producto(s)");
    }

    /**
     * Agregar un producto físico al inventario. Valida que el código de barras
     * sea único.
     */
    public void agregarProductoFisico(ProductoFisico producto) throws ProductoInvalidoException {
        if (producto == null) {
            throw new ProductoInvalidoException("El producto no puede ser nulo.");
        }

        // Validar que no exista un producto con el mismo código de barras
        boolean existe = productos.stream()
                .filter(p -> p instanceof ProductoFisico)
                .map(p -> (ProductoFisico) p)
                .anyMatch(p -> p.getCodigoDeBarras().equals(producto.getCodigoDeBarras()));

        if (existe) {
            throw new ProductoInvalidoException("Ya existe un producto con el código de barras: " + producto.getCodigoDeBarras());
        }

        productos.add(producto);
        guardarInventario();
        System.out.println("✓ Producto físico agregado: " + producto.getNombre());
    }

    /**
     * Agregar un servicio digital al inventario.
     */
    public void agregarServicioDigital(ServicioDigital servicio) throws ProductoInvalidoException {
        if (servicio == null) {
            throw new ProductoInvalidoException("El servicio no puede ser nulo.");
        }

        productos.add(servicio);
        guardarInventario();
        System.out.println("✓ Servicio digital agregado: " + servicio.getNombre());
    }

    /**
     * Buscar producto físico por código de barras.
     */
    public ProductoFisico buscarPorCodigoBarras(String codigoBarras) {
        if (codigoBarras == null || codigoBarras.trim().isEmpty()) {
            return null;
        }
        return productos.stream()
                .filter(p -> p instanceof ProductoFisico)
                .map(p -> (ProductoFisico) p)
                .filter(p -> p.getCodigoDeBarras().equals(codigoBarras))
                .findFirst()
                .orElse(null);
    }

    /**
     * Buscar productos (físicos y servicios) por nombre.
     */
    public List<ProductoBase> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String nombreLower = nombre.toLowerCase();
        return productos.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(nombreLower))
                .collect(Collectors.toList());
    }

    /**
     * Buscar productos por categoría.
     */
    public List<ProductoBase> buscarPorCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return productos.stream()
                .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }

    /**
     * Actualizar stock de un producto físico (entrada de mercancía).
     */
    public boolean actualizarStock(String codigoBarras, int nuevoStock) {
        ProductoFisico producto = buscarPorCodigoBarras(codigoBarras);
        if (producto != null) {
            producto.setStockEnTienda(nuevoStock);
            guardarInventario();
            System.out.println("✓ Stock actualizado para: " + producto.getNombre() + " -> " + nuevoStock + " unidades");
            return true;
        }
        System.err.println("✗ Producto no encontrado: " + codigoBarras);
        return false;
    }

    /**
     * Agregar stock a un producto físico existente.
     */
    public boolean agregarStock(String codigoBarras, int cantidad) {
        ProductoFisico producto = buscarPorCodigoBarras(codigoBarras);
        if (producto != null) {
            producto.agregarStock(cantidad);
            guardarInventario();
            System.out.println("✓ Stock agregado para: " + producto.getNombre() + " (+" + cantidad + " unidades)");
            return true;
        }
        System.err.println("✗ Producto no encontrado: " + codigoBarras);
        return false;
    }

    /**
     * Reducir stock de un producto (usado en ventas).
     */
    public void reducirStock(String codigoBarras, int cantidad) throws StockInsuficienteException {
        ProductoFisico producto = buscarPorCodigoBarras(codigoBarras);
        if (producto != null) {
            producto.reducirStock(cantidad);
            guardarInventario();
            System.out.println("✓ Stock reducido para: " + producto.getNombre() + " (-" + cantidad + " unidades)");
        } else {
            throw new IllegalArgumentException("Producto no encontrado: " + codigoBarras);
        }
    }

    /**
     * Marcar un producto físico en oferta con descuento.
     */
    public boolean marcarEnOferta(String codigoBarras, boolean enOferta, double descuentoPorcentaje) {
        ProductoFisico producto = buscarPorCodigoBarras(codigoBarras);
        if (producto != null) {
            producto.setEnOferta(enOferta, descuentoPorcentaje);
            guardarInventario();
            System.out.println("✓ Oferta actualizada para: " + producto.getNombre() + " -> " + (enOferta ? (descuentoPorcentaje * 100) + "%" : "Sin oferta"));
            return true;
        }
        System.err.println("✗ Producto no encontrado: " + codigoBarras);
        return false;
    }

    /**
     * Obtener todos los productos (físicos y servicios).
     */
    public List<ProductoBase> obtenerTodosLosProductos() {
        return new ArrayList<>(productos);
    }

    /**
     * Obtener solo productos físicos.
     */
    public List<ProductoFisico> obtenerProductosFisicos() {
        return productos.stream()
                .filter(p -> p instanceof ProductoFisico)
                .map(p -> (ProductoFisico) p)
                .collect(Collectors.toList());
    }

    /**
     * Obtener solo servicios digitales.
     */
    public List<ServicioDigital> obtenerServiciosDigitales() {
        return productos.stream()
                .filter(p -> p instanceof ServicioDigital)
                .map(p -> (ServicioDigital) p)
                .collect(Collectors.toList());
    }

    /**
     * Obtener productos físicos con stock bajo (menos de 5 unidades).
     */
    public List<ProductoFisico> obtenerProductosStockBajo() {
        return productos.stream()
                .filter(p -> p instanceof ProductoFisico)
                .map(p -> (ProductoFisico) p)
                .filter(p -> p.getStockEnTienda() < 5 && p.getStockEnTienda() > 0)
                .collect(Collectors.toList());
    }

    /**
     * Obtener productos físicos agotados (stock = 0).
     */
    public List<ProductoFisico> obtenerProductosAgotados() {
        return productos.stream()
                .filter(p -> p instanceof ProductoFisico)
                .map(p -> (ProductoFisico) p)
                .filter(ProductoFisico::estaAgotado)
                .collect(Collectors.toList());
    }

    /**
     * Eliminar producto del inventario por código de barras.
     */
    public boolean eliminarProducto(String codigoBarras) {
        ProductoFisico producto = buscarPorCodigoBarras(codigoBarras);
        if (producto != null) {
            productos.remove(producto);
            guardarInventario();
            System.out.println("✓ Producto eliminado: " + producto.getNombre());
            return true;
        }
        System.err.println("✗ Producto no encontrado: " + codigoBarras);
        return false;
    }

    /**
     * Eliminar servicio digital por nombre.
     */
    public boolean eliminarServicio(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        ServicioDigital servicio = productos.stream()
                .filter(p -> p instanceof ServicioDigital)
                .map(p -> (ServicioDigital) p)
                .filter(s -> s.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);

        if (servicio != null) {
            productos.remove(servicio);
            guardarInventario();
            System.out.println("✓ Servicio eliminado: " + servicio.getNombre());
            return true;
        }
        System.err.println("✗ Servicio no encontrado: " + nombre);
        return false;
    }

    /**
     * Obtener cantidad total de productos.
     */
    public int getCantidadProductos() {
        return productos.size();
    }

    /**
     * Forzar guardado manual de todo el inventario.
     */
    public void guardarTodos() {
        guardarInventario();
        System.out.println("✓ Inventario guardado manualmente");
    }

    /**
     * Recargar inventario desde el archivo.
     */
    public void recargarInventario() {
        this.productos = persistencia.cargarInventario();
        System.out.println("✓ Inventario recargado desde el archivo: " + productos.size() + " producto(s)");
    }

    /**
     * Guardar inventario en archivo Excel.
     */
    private void guardarInventario() {
        persistencia.guardarInventario(productos);
    }
}
