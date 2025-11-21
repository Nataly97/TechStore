/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import logica.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;
import logica.Cliente;
import logica.ProductoBase;
import logica.ProductoFisico;
import logica.ServicioDigital;
import logica.Venta;

/**
 * Sistema de persistencia usando archivos Excel
 *
 * @author Nataly
 */
public class Persistencia {

    private static final String DIRECTORIO_DATOS = "datos";
    private static final String RUTA_CLIENTES = DIRECTORIO_DATOS + File.separator + "clientes.xlsx";
    private static final String RUTA_INVENTARIO = DIRECTORIO_DATOS + File.separator + "inventario.xlsx";
    private static final String RUTA_VENTAS = DIRECTORIO_DATOS + File.separator + "ventas.xlsx";
    private static final String RUTA_REPORTES = DIRECTORIO_DATOS + File.separator + "reportes.xlsx";

    public Persistencia() {
        inicializarSistemaPersistencia();
    }

    /**
     * Inicializa el sistema de persistencia creando el directorio y archivos necesarios.
     */
    private void inicializarSistemaPersistencia() {
        File directorio = new File(DIRECTORIO_DATOS);
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                System.out.println("✓ Directorio 'datos' creado");
            }
        }

        crearExcelSiNoExiste(RUTA_CLIENTES, new String[]{"ID", "Nombre", "DNI", "Teléfono", "Dirección", "Puntos"});
        crearExcelSiNoExiste(RUTA_INVENTARIO, new String[]{"Código", "Nombre", "Tipo", "Precio", "Stock", "Categoría", "Ubicación", "Descuento%", "Duración"});
        crearExcelSiNoExiste(RUTA_VENTAS, new String[]{"ID", "Factura", "Fecha", "Cliente", "Cajero", "Subtotal", "Descuentos", "IVA", "Total", "Estado"});
    }

    /**
     * Crea un archivo Excel con encabezados si no existe.
     */
    private void crearExcelSiNoExiste(String ruta, String[] encabezados) {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Datos");
                Row headerRow = sheet.createRow(0);

                CellStyle headerStyle = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                headerStyle.setFont(font);

                for (int i = 0; i < encabezados.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(encabezados[i]);
                    cell.setCellStyle(headerStyle);
                }

                try (FileOutputStream fileOut = new FileOutputStream(archivo)) {
                    workbook.write(fileOut);
                }
                System.out.println("✓ Archivo Excel creado: " + archivo.getName());
            } catch (IOException e) {
                System.err.println("✗ Error al crear Excel: " + e.getMessage());
            }
        }
    }

    // ==================== CLIENTES ====================

    /**
     * Guarda la lista de clientes en un archivo Excel.
     */
    public void guardarClientes(List<Cliente> clientes) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Clientes");

            // Encabezados
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Nombre", "DNI", "Teléfono", "Dirección", "Puntos"};
            
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Datos
            int rowNum = 1;
            for (Cliente cliente : clientes) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(cliente.getId());
                row.createCell(1).setCellValue(cliente.getNombre());
                row.createCell(2).setCellValue(cliente.getCedula());
                row.createCell(3).setCellValue(cliente.getTelefono() != null ? cliente.getTelefono() : "");
                row.createCell(4).setCellValue(cliente.getDireccion() != null ? cliente.getDireccion() : "");
                row.createCell(5).setCellValue(cliente.getPuntosFidelidad());
            }

            // Autoajustar columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream(RUTA_CLIENTES)) {
                workbook.write(fileOut);
            }
            System.out.println("✓ " + clientes.size() + " cliente(s) guardado(s) en Excel");
        } catch (IOException e) {
            System.err.println("✗ Error al guardar clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carga la lista de clientes desde un archivo Excel.
     */
    public List<Cliente> cargarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        File archivo = new File(RUTA_CLIENTES);

        if (!archivo.exists()) {
            System.out.println("⚠ Archivo de clientes no existe, se creará al guardar");
            return clientes;
        }

        try (FileInputStream fis = new FileInputStream(archivo);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    try {
                        String id = getCellValueAsString(row.getCell(0));
                        String nombre = getCellValueAsString(row.getCell(1));
                        String dni = getCellValueAsString(row.getCell(2));
                        String telefono = getCellValueAsString(row.getCell(3));
                        String direccion = getCellValueAsString(row.getCell(4));
                        int puntos = (int) getCellValueAsNumber(row.getCell(5));

                        if (!id.isEmpty() && !nombre.isEmpty() && !dni.isEmpty()) {
                            Cliente cliente = new Cliente(id, nombre, dni, telefono, direccion);
                            // Los puntos se acumulan mediante el método acumularPuntos
                            // pero para cargar desde Excel, los asignamos directamente
                            for (int p = 0; p < puntos; p += 10) {
                                cliente.acumularPuntos(10);
                            }
                            clientes.add(cliente);
                        }
                    } catch (Exception e) {
                        System.err.println("⚠ Error al cargar cliente en fila " + i + ": " + e.getMessage());
                    }
                }
            }
            System.out.println("✓ " + clientes.size() + " cliente(s) cargado(s) desde Excel");
        } catch (IOException e) {
            System.err.println("✗ Error al cargar clientes: " + e.getMessage());
        }

        return clientes;
    }

    // ==================== INVENTARIO ====================

    /**
     * Guarda la lista de productos en un archivo Excel.
     */
    public void guardarInventario(List<ProductoBase> productos) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Inventario");

            // Encabezados
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Código", "Nombre", "Tipo", "Precio", "Stock", "Categoría", "Ubicación", "Descuento%", "Duración"};
            
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Datos
            int rowNum = 1;
            for (ProductoBase producto : productos) {
                Row row = sheet.createRow(rowNum++);
                
                if (producto instanceof ProductoFisico) {
                    ProductoFisico pf = (ProductoFisico) producto;
                    row.createCell(0).setCellValue(pf.getCodigoDeBarras());
                    row.createCell(1).setCellValue(pf.getNombre());
                    row.createCell(2).setCellValue("FÍSICO");
                    row.createCell(3).setCellValue(pf.getPrecio());
                    row.createCell(4).setCellValue(pf.getStockEnTienda());
                    row.createCell(5).setCellValue(pf.getCategoria());
                    row.createCell(6).setCellValue(pf.getUbicacion() != null ? pf.getUbicacion() : "");
                    row.createCell(7).setCellValue(pf.getDescuentoPorcentaje() * 100); // Convertir a porcentaje
                    row.createCell(8).setCellValue("N/A");
                } else if (producto instanceof ServicioDigital) {
                    ServicioDigital sd = (ServicioDigital) producto;
                    row.createCell(0).setCellValue("SRV-" + sd.getNombre().hashCode()); // Generar código único
                    row.createCell(1).setCellValue(sd.getNombre());
                    row.createCell(2).setCellValue("SERVICIO");
                    row.createCell(3).setCellValue(sd.getPrecio());
                    row.createCell(4).setCellValue(0); // Los servicios no tienen stock
                    row.createCell(5).setCellValue(sd.getCategoria());
                    row.createCell(6).setCellValue("N/A");
                    row.createCell(7).setCellValue(0);
                    row.createCell(8).setCellValue(sd.getDuracionEstimadaMinutos());
                }
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream(RUTA_INVENTARIO)) {
                workbook.write(fileOut);
            }
            System.out.println("✓ " + productos.size() + " producto(s) guardado(s) en Excel");
        } catch (IOException e) {
            System.err.println("✗ Error al guardar inventario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carga la lista de productos desde un archivo Excel.
     */
    public List<ProductoBase> cargarInventario() {
        List<ProductoBase> productos = new ArrayList<>();
        File archivo = new File(RUTA_INVENTARIO);

        if (!archivo.exists()) {
            System.out.println("⚠ Archivo de inventario no existe, se creará al guardar");
            return productos;
        }

        try (FileInputStream fis = new FileInputStream(archivo);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    try {
                        String codigo = getCellValueAsString(row.getCell(0));
                        String nombre = getCellValueAsString(row.getCell(1));
                        String tipo = getCellValueAsString(row.getCell(2));
                        double precio = getCellValueAsNumber(row.getCell(3));
                        String categoria = getCellValueAsString(row.getCell(5));

                        if (nombre.isEmpty() || categoria.isEmpty()) {
                            continue;
                        }

                        if ("FÍSICO".equalsIgnoreCase(tipo)) {
                            int stock = (int) getCellValueAsNumber(row.getCell(4));
                            String ubicacion = getCellValueAsString(row.getCell(6));
                            double descuentoPorcentaje = getCellValueAsNumber(row.getCell(7)) / 100.0; // Convertir de porcentaje

                            ProductoFisico pf = new ProductoFisico(nombre, precio, categoria, codigo, stock, ubicacion);
                            if (descuentoPorcentaje > 0) {
                                pf.setEnOferta(true, descuentoPorcentaje);
                            }
                            productos.add(pf);
                        } else if ("SERVICIO".equalsIgnoreCase(tipo)) {
                            int duracion = (int) getCellValueAsNumber(row.getCell(8));
                            if (duracion <= 0) duracion = 60; // Valor por defecto

                            ServicioDigital sd = new ServicioDigital(nombre, precio, categoria, duracion, "Servicio técnico");
                            productos.add(sd);
                        }
                    } catch (Exception e) {
                        System.err.println("⚠ Error al cargar producto en fila " + i + ": " + e.getMessage());
                    }
                }
            }
            System.out.println("✓ " + productos.size() + " producto(s) cargado(s) desde Excel");
        } catch (IOException e) {
            System.err.println("✗ Error al cargar inventario: " + e.getMessage());
        }

        return productos;
    }

    // ==================== VENTAS ====================

    /**
     * Guarda la lista de ventas en un archivo Excel.
     */
    public void guardarVentas(List<Venta> ventas) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Ventas");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Factura", "Fecha", "Cliente", "Cajero", "Subtotal", "Descuentos", "IVA", "Total", "Estado"};
            
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            for (Venta venta : ventas) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(venta.getIdVenta());
                row.createCell(1).setCellValue(venta.getNumeroFactura() != null ? venta.getNumeroFactura() : "");
                row.createCell(2).setCellValue(venta.getFechaHora().format(formatter));
                row.createCell(3).setCellValue(venta.getCliente() != null ? venta.getCliente().getNombre() : "Sin cliente");
                row.createCell(4).setCellValue(venta.getCajero().getNombre());
                row.createCell(5).setCellValue(venta.calcularSubtotal());
                row.createCell(6).setCellValue(venta.calcularDescuentoTotal());
                
                double subtotalNeto = venta.calcularSubtotal() - venta.calcularDescuentoTotal();
                double iva = subtotalNeto * venta.getIvaPorcentaje();
                row.createCell(7).setCellValue(iva);
                row.createCell(8).setCellValue(venta.calcularTotal());
                row.createCell(9).setCellValue(venta.getEstado().toString());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream(RUTA_VENTAS)) {
                workbook.write(fileOut);
            }
            System.out.println("✓ " + ventas.size() + " venta(s) guardada(s) en Excel");
        } catch (IOException e) {
            System.err.println("✗ Error al guardar ventas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carga la lista de ventas desde un archivo Excel.
     * NOTA: Por simplicidad, solo carga información básica.
     * Los detalles de productos no se cargan (requeriría estructura más compleja).
     */
    public List<Venta> cargarVentas() {
        List<Venta> ventas = new ArrayList<>();
        File archivo = new File(RUTA_VENTAS);

        if (!archivo.exists()) {
            System.out.println("⚠ Archivo de ventas no existe, se creará al guardar");
            return ventas;
        }

        // Por simplicidad, las ventas se cargan vacías al inicio
        // Implementar carga completa requeriría guardar también los detalles de venta
        System.out.println("⚠ Carga de ventas simplificada (sin detalles de productos)");
        return ventas;
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Obtiene el valor de una celda como String.
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    /**
     * Obtiene el valor de una celda como número.
     */
    private double getCellValueAsNumber(Cell cell) {
        if (cell == null) {
            return 0.0;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            default:
                return 0.0;
        }
    }

    /**
     * Verifica el estado del sistema de persistencia.
     */
    public void verificarSistema() {
        System.out.println("\n========================================");
        System.out.println("  VERIFICACIÓN DEL SISTEMA DE PERSISTENCIA");
        System.out.println("========================================");
        System.out.println("Directorio de datos: " + new File(DIRECTORIO_DATOS).getAbsolutePath());
        System.out.println("Archivo clientes: " + (new File(RUTA_CLIENTES).exists() ? "✓ Existe" : "✗ No existe"));
        System.out.println("Archivo inventario: " + (new File(RUTA_INVENTARIO).exists() ? "✓ Existe" : "✗ No existe"));
        System.out.println("Archivo ventas: " + (new File(RUTA_VENTAS).exists() ? "✓ Existe" : "✗ No existe"));
        System.out.println("========================================\n");
    }
}
