/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import exception.CedulaUnicaException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import logica.Cliente;
import logica.Venta;
import persistencia.Persistencia;

/**
 *
 * @author Nataly
 */
public class GestionarClientes {

    private List<Cliente> clientes;
    // Contador atómico para asegurar IDs únicos de forma concurrente
    private AtomicInteger contadorID;
    // Sistema de persistencia
    private Persistencia persistencia;

    public GestionarClientes() {
        // Inicializar sistema de persistencia
        this.persistencia = new Persistencia();

        // Cargar clientes existentes desde el archivo
        this.clientes = persistencia.cargarClientes();

        // Calcular el siguiente ID basado en los clientes existentes
        int maxId = clientes.stream()
                .map(Cliente::getId)
                .filter(id -> id.startsWith("CLI-"))
                .map(id -> {
                    try {
                        return Integer.parseInt(id.substring(4));
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max(Integer::compareTo)
                .orElse(0);

        // Inicia el contador de ID's para el formato CLI-001 
        this.contadorID = new AtomicInteger(maxId + 1);

        System.out.println("✓ GestionarClientes inicializado con " + clientes.size() + " cliente(s)");
    }

    /**
     * Funcionalidad: Registrar nuevo cliente. Aplica la regla de negocio:
     * Cédula/DNI debe ser única y genera el ID automáticamente. Los datos se
     * guardan automáticamente en el archivo.
     */
    public Cliente registrarNuevoCliente(String nombre, String dni, String telefono, String direccion)
            throws CedulaUnicaException {

        // Validaciones básicas
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacío.");
        }
        if (dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula/DNI del cliente no puede estar vacía.");
        }

        // Regla de Negocio: La cédula/DNI debe ser única en el sistema 
        boolean dniExiste = clientes.stream()
                .anyMatch(c -> c.getDni().equals(dni));

        if (dniExiste) {
            throw new CedulaUnicaException(dni); // Lanza la excepción personalizada
        }

        // Regla de Negocio: Generación automática de ID (CLI-001, CLI-002...) 
        String nuevoID = String.format("CLI-%03d", contadorID.getAndIncrement());

        Cliente nuevoCliente = new Cliente(nuevoID, nombre, dni, telefono, direccion);
        this.clientes.add(nuevoCliente);

        // Guardar automáticamente en el archivo
        persistencia.guardarClientes(clientes);

        System.out.println("✓ Cliente registrado y guardado: " + nuevoID + " - " + nombre);
        return nuevoCliente;
    }

    /**
     * Funcionalidad: Buscar cliente por ID, cédula o nombre.
     */
    public List<Cliente> buscarClientes(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String criterioLower = criterio.toLowerCase();
        return clientes.stream()
                .filter(c -> c.getNombre().toLowerCase().contains(criterioLower)
                || c.getId().equalsIgnoreCase(criterio)
                || c.getDni().equals(criterio))
                .collect(Collectors.toList());
    }

    /**
     * Funcionalidad: Buscar cliente específico por DNI.
     */
    public Cliente buscarClientePorDNI(String dni) {
        if (dni == null || dni.trim().isEmpty()) {
            return null;
        }
        return clientes.stream()
                .filter(c -> c.getDni().equals(dni))
                .findFirst()
                .orElse(null);
    }

    /**
     * Funcionalidad: Buscar cliente específico por ID.
     */
    public Cliente buscarClientePorID(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        return clientes.stream()
                .filter(c -> c.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Funcionalidad: Actualizar datos de un cliente existente. Los cambios se
     * guardan automáticamente.
     */
    public boolean actualizarCliente(String dni, String nuevoNombre, String nuevoTelefono, String nuevaDireccion) {
        Cliente cliente = buscarClientePorDNI(dni);
        if (cliente != null) {
            if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                cliente.setNombre(nuevoNombre);
            }
            if (nuevoTelefono != null && !nuevoTelefono.trim().isEmpty()) {
                cliente.setTelefono(nuevoTelefono);
            }
            // La dirección puede ser vacía (es opcional)
            cliente.setDireccion(nuevaDireccion);

            // Guardar cambios automáticamente
            persistencia.guardarClientes(clientes);
            System.out.println("✓ Cliente actualizado y guardado: " + dni);
            return true;
        }
        System.err.println("✗ Cliente no encontrado: " + dni);
        return false;
    }

    /**
     * Funcionalidad: Eliminar un cliente del sistema. Los cambios se guardan
     * automáticamente.
     */
    public boolean eliminarCliente(String dni) {
        Cliente cliente = buscarClientePorDNI(dni);
        if (cliente != null) {
            clientes.remove(cliente);

            // Guardar cambios automáticamente
            persistencia.guardarClientes(clientes);
            System.out.println("✓ Cliente eliminado y guardado: " + dni);
            return true;
        }
        System.err.println("✗ Cliente no encontrado: " + dni);
        return false;
    }

    /**
     * Funcionalidad: Obtener todos los clientes registrados.
     */
    public List<Cliente> obtenerTodosLosClientes() {
        return new ArrayList<>(clientes);
    }

    /**
     * Funcionalidad: Ver historial de compras de un cliente.
     */
    public List<Venta> verHistorialCompras(String dni) {
        Cliente cliente = buscarClientePorDNI(dni);
        if (cliente != null) {
            return cliente.getHistorialCompras();
        }
        return new ArrayList<>();
    }

    /**
     * Funcionalidad: Consultar puntos de fidelidad de un cliente.
     */
    public int consultarPuntosFidelidad(String dni) {
        Cliente cliente = buscarClientePorDNI(dni);
        if (cliente != null) {
            return cliente.getPuntosFidelidad();
        }
        return 0;
    }

    /**
     * Funcionalidad: Canjear puntos de fidelidad de un cliente. Los cambios se
     * guardan automáticamente.
     *
     * @return El descuento en dinero obtenido por canjear los puntos
     */
    public double canjearPuntosCliente(String dni, int puntosACanjear) {
        Cliente cliente = buscarClientePorDNI(dni);
        if (cliente != null) {
            double descuento = cliente.canjearPuntos(puntosACanjear);

            // Guardar cambios automáticamente
            persistencia.guardarClientes(clientes);
            System.out.println("✓ Puntos canjeados del cliente: " + dni + " (-" + puntosACanjear + " puntos = $" + descuento + ")");
            return descuento;
        }
        return 0.0;
    }

    /**
     * Funcionalidad: Registrar una compra en el historial del cliente. Los
     * cambios se guardan automáticamente. NOTA: Esta función también acumula
     * puntos automáticamente.
     */
    public void registrarCompraCliente(String dni, Venta venta) {
        Cliente cliente = buscarClientePorDNI(dni);
        if (cliente != null) {
            cliente.agregarCompra(venta);
            // Los puntos ya se acumulan en Venta.confirmarVenta()
            // pero si necesitas hacerlo manualmente aquí, descomenta:
            // cliente.acumularPuntos(venta.calcularSubtotal());

            // Guardar cambios automáticamente
            persistencia.guardarClientes(clientes);
            System.out.println("✓ Compra registrada para el cliente: " + dni);
        }
    }

    /**
     * Funcionalidad: Obtener el descuento por fidelidad de un cliente.
     */
    public double obtenerDescuentoFidelidad(String dni) {
        Cliente cliente = buscarClientePorDNI(dni);
        if (cliente != null) {
            return cliente.calcularDescuentoFidelidad();
        }
        return 0.0;
    }

    /**
     * Funcionalidad: Forzar guardado manual de todos los clientes. Útil para
     * asegurar que los datos estén guardados.
     */
    public void guardarTodos() {
        persistencia.guardarClientes(clientes);
        System.out.println("✓ Todos los clientes guardados manualmente");
    }

    /**
     * Funcionalidad: Recargar clientes desde el archivo. Útil si se modificó el
     * archivo externamente.
     */
    public void recargarClientes() {
        this.clientes = persistencia.cargarClientes();

        // Recalcular el contador de IDs
        int maxId = clientes.stream()
                .map(Cliente::getId)
                .filter(id -> id.startsWith("CLI-"))
                .map(id -> {
                    try {
                        return Integer.parseInt(id.substring(4));
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max(Integer::compareTo)
                .orElse(0);

        this.contadorID = new AtomicInteger(maxId + 1);
        System.out.println("✓ Clientes recargados desde el archivo: " + clientes.size() + " cliente(s)");
    }
}
