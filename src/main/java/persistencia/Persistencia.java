/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import logica.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nataly
 */
public class Persistencia {

    private static final String DIRECTORIO_DATOS = "datos";
    private static final String RUTA_CLIENTES = DIRECTORIO_DATOS + File.separator + "clientes.dat";
    private static final String RUTA_PRODUCTOS = DIRECTORIO_DATOS + File.separator + "productos.dat";
    private static final String RUTA_VENTAS = DIRECTORIO_DATOS + File.separator + "ventas.dat";
    private static final String RUTA_FACTURAS = DIRECTORIO_DATOS + File.separator + "facturas.txt";

    public Persistencia() {
        inicializarSistemaPersistencia();
    }

    /**
     * Inicializa el sistema de persistencia creando directorios y archivos
     * necesarios
     */
    private void inicializarSistemaPersistencia() {
        // Crear directorio de datos
        File directorio = new File(DIRECTORIO_DATOS);
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                System.out.println("✓ Directorio 'datos' creado en: " + directorio.getAbsolutePath());
            } else {
                System.err.println("✗ No se pudo crear el directorio 'datos'");
                return;
            }
        } else {
            System.out.println("✓ Directorio 'datos' ya existe en: " + directorio.getAbsolutePath());
        }

        // Crear archivos si no existen
        crearArchivoSiNoExiste(RUTA_CLIENTES);
        crearArchivoSiNoExiste(RUTA_PRODUCTOS);
        crearArchivoSiNoExiste(RUTA_VENTAS);
        crearArchivoTextoSiNoExiste(RUTA_FACTURAS);
    }

    /**
     * Crea un archivo binario vacío si no existe
     */
    private void crearArchivoSiNoExiste(String ruta) {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            try {
                if (archivo.createNewFile()) {
                    System.out.println("✓ Archivo creado: " + archivo.getAbsolutePath());
                    // Inicializar con una lista vacía
                    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
                        oos.writeObject(new ArrayList<>());
                    }
                }
            } catch (IOException e) {
                System.err.println("✗ Error al crear archivo " + ruta + ": " + e.getMessage());
            }
        }
    }

    /**
     * Crea un archivo de texto vacío si no existe
     */
    private void crearArchivoTextoSiNoExiste(String ruta) {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            try {
                if (archivo.createNewFile()) {
                    System.out.println("✓ Archivo de facturas creado: " + archivo.getAbsolutePath());
                }
            } catch (IOException e) {
                System.err.println("✗ Error al crear archivo de facturas: " + e.getMessage());
            }
        }
    }

    // ==== CLIENTES ====
    public void guardarClientes(List<Cliente> clientes) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RUTA_CLIENTES))) {
            oos.writeObject(clientes);
            System.out.println("✓ " + clientes.size() + " cliente(s) guardado(s) exitosamente");
        } catch (IOException e) {
            System.err.println("✗ Error al guardar clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Cliente> cargarClientes() {
        File archivo = new File(RUTA_CLIENTES);
        if (!archivo.exists() || archivo.length() == 0) {
            System.out.println("ℹ No hay clientes guardados, retornando lista vacía");
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(RUTA_CLIENTES))) {
            List<Cliente> clientes = (List<Cliente>) ois.readObject();
            System.out.println("✓ " + clientes.size() + " cliente(s) cargado(s) exitosamente");
            return clientes;
        } catch (EOFException e) {
            System.out.println("ℹ Archivo de clientes vacío, retornando lista vacía");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("✗ Error al cargar clientes: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ==== PRODUCTOS ====
    public void guardarProductos(List<ProductoBase> productos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RUTA_PRODUCTOS))) {
            oos.writeObject(productos);
            System.out.println("✓ " + productos.size() + " producto(s) guardado(s) exitosamente");
        } catch (IOException e) {
            System.err.println("✗ Error al guardar productos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public List<ProductoBase> cargarProductos() {
        File archivo = new File(RUTA_PRODUCTOS);
        if (!archivo.exists() || archivo.length() == 0) {
            System.out.println("ℹ No hay productos guardados, retornando lista vacía");
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(RUTA_PRODUCTOS))) {
            List<ProductoBase> productos = (List<ProductoBase>) ois.readObject();
            System.out.println("✓ " + productos.size() + " producto(s) cargado(s) exitosamente");
            return productos;
        } catch (EOFException e) {
            System.out.println("ℹ Archivo de productos vacío, retornando lista vacía");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("✗ Error al cargar productos: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ==== VENTAS ====
    public void guardarVentas(List<Venta> ventas) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RUTA_VENTAS))) {
            oos.writeObject(ventas);
            System.out.println("✓ " + ventas.size() + " venta(s) guardada(s) exitosamente");
        } catch (IOException e) {
            System.err.println("✗ Error al guardar ventas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Venta> cargarVentas() {
        File archivo = new File(RUTA_VENTAS);
        if (!archivo.exists() || archivo.length() == 0) {
            System.out.println("ℹ No hay ventas guardadas, retornando lista vacía");
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(RUTA_VENTAS))) {
            List<Venta> ventas = (List<Venta>) ois.readObject();
            System.out.println("✓ " + ventas.size() + " venta(s) cargada(s) exitosamente");
            return ventas;
        } catch (EOFException e) {
            System.out.println("ℹ Archivo de ventas vacío, retornando lista vacía");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("✗ Error al cargar ventas: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ==== FACTURAS (TEXTO) ====
    public void guardarFactura(String factura) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA_FACTURAS, true))) {
            writer.write(factura);
            writer.newLine();
            writer.write("=====================================\n\n");
            System.out.println("✓ Factura guardada exitosamente");
        } catch (IOException e) {
            System.err.println("✗ Error al guardar factura: " + e.getMessage());
        }
    }

    /**
     * Método de prueba para verificar que el sistema de persistencia funciona
     */
    public void verificarSistema() {
        System.out.println("\n=== VERIFICACIÓN DEL SISTEMA DE PERSISTENCIA ===");
        System.out.println("Directorio de datos: " + new File(DIRECTORIO_DATOS).getAbsolutePath());
        System.out.println("Archivo clientes: " + new File(RUTA_CLIENTES).exists());
        System.out.println("Archivo productos: " + new File(RUTA_PRODUCTOS).exists());
        System.out.println("Archivo ventas: " + new File(RUTA_VENTAS).exists());
        System.out.println("Archivo facturas: " + new File(RUTA_FACTURAS).exists());
        System.out.println("================================================\n");
    }
}
