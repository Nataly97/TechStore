/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.techstore;

import IGU.MenuPrincipalApp;

/**
 *
 * @author Nataly
 */
public class TechStore {

    public static void main(String[] args) {
        // Opcional: configurar el look and feel de Swing
        try {
            //Bloque try con UIManager:
            //Intenta aplicar el tema “Nimbus” para que tu interfaz se vea más moderna. Si falla, usa el tema por defecto.
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Si falla Nimbus, sigue con el look and feel por defecto
            e.printStackTrace();
        }

        // Iniciar la aplicación en el hilo de eventos de Swing
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Aquí asumimos que tienes un JFrame llamado MenuPrincipalApp
                MenuPrincipalApp menu = new MenuPrincipalApp();
                menu.setLocationRelativeTo(null); // centrar la ventana
                menu.setVisible(true);
            }
        });
    }
}
