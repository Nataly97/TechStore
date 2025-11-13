/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.techstore;

import IGU.*;
import logica.*;
import javax.swing.*;
import persistencia.Persistencia;

/**
 *
 * @author Nataly
 */
public class TechStore {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║   TECHSTORE - Sistema de Gestión   ║");
        System.out.println("║   Tienda de Productos Tecnológicos ║");
        System.out.println("╚════════════════════════════════════╝\n");

        // Inicializar sistema de persistencia
        Persistencia persistencia = new Persistencia();
        persistencia.verificarSistema();

        // Configurar Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo establecer el Look and Feel");
        }

        // Iniciar interfaz gráfica
        java.awt.EventQueue.invokeLater(() -> {
            mostrarMenuPrincipal();
        });
    }

    private static void mostrarMenuPrincipal() {
        String[] opciones = {
            "Gestión de Clientes",
            "Gestión de Inventario",
            "Realizar Venta",
            "Salir"
        };

        int seleccion = JOptionPane.showOptionDialog(
                null,
                "Seleccione el módulo a abrir:",
                "TechStore - Menú Principal",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        switch (seleccion) {
            case 0:
                new GestionarClienteIgu().setVisible(true);
                break;
            case 1:
                new IGU.GestionInventario().setVisible(true);
                break;
            case 2:
                new IGU.PanelVentas().setVisible(true);
                break;
            case 3:
            default:
                System.exit(0);
                break;
        }
    }
}
