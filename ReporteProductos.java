import java.awt.BorderLayout;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class ReporteProductos extends JFrame {
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JButton btnExportarTxt;

    public ReporteProductos() {
        setTitle("Reporte de Productos y Medicamentos");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Columnas
        String[] columnas = {"Nombre", "Tipo", "Cantidad", "Stock Mínimo", "Fecha Vencimiento"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaProductos = new JTable(modeloTabla);

        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        btnExportarTxt = new JButton("Exportar como TXT");

        cargarDatos();

        btnExportarTxt.addActionListener(e -> exportarComoTxt());

        add(scrollPane, BorderLayout.CENTER);
        add(btnExportarTxt, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0); // Limpia la tabla
        File archivo = new File("productos.txt");

        if (!archivo.exists()) {
            JOptionPane.showMessageDialog(this, "El archivo productos.txt no existe.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 5) {
                    for (int i = 0; i < partes.length; i++) {
                        partes[i] = partes[i].trim();
                    }
                    modeloTabla.addRow(partes);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer productos.txt", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportarComoTxt() {
        File archivo = new File("ReporteProductos.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            writer.write("=== REPORTE DE PRODUCTOS Y MEDICAMENTOS ===\n\n");

            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                writer.write("Nombre: " + modeloTabla.getValueAt(i, 0) + " | ");
                writer.write("Tipo: " + modeloTabla.getValueAt(i, 1) + " | ");
                writer.write("Cantidad: " + modeloTabla.getValueAt(i, 2) + " | ");
                writer.write("Stock Mínimo: " + modeloTabla.getValueAt(i, 3) + " | ");
                writer.write("Vence: " + modeloTabla.getValueAt(i, 4) + "\n");
            }

            writer.flush();
            JOptionPane.showMessageDialog(this, "Archivo exportado exitosamente como: " + archivo.getName());

            // Abre automáticamente el archivo
            Desktop.getDesktop().open(archivo);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al exportar archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método principal para ejecutar esta ventana por separado
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReporteProductos());
    }
}
