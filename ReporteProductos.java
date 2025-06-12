import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class ReporteProductos extends JFrame {
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JButton btnGenerarPDF;

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
        btnGenerarPDF = new JButton("Generar PDF");

        cargarDatos();

        btnGenerarPDF.addActionListener(e -> generarPDF());

        add(scrollPane, BorderLayout.CENTER);
        add(btnGenerarPDF, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void cargarDatos() {
        try (BufferedReader reader = new BufferedReader(new FileReader("productos.txt"))) {
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
            JOptionPane.showMessageDialog(this, "Error al leer productos.txt");
        }
    }

    private void generarPDF() {
        Document documento = new Document();
        try {
            PdfWriter.getInstance(documento, new FileOutputStream("ReporteProductos.pdf"));
            documento.open();
            documento.add(new Paragraph("Reporte de Productos y Medicamentos\n\n"));

            PdfPTable tablaPDF = new PdfPTable(5);
            tablaPDF.addCell("Nombre");
            tablaPDF.addCell("Tipo");
            tablaPDF.addCell("Cantidad");
            tablaPDF.addCell("Stock Mínimo");
            tablaPDF.addCell("Fecha Vencimiento");

            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                for (int j = 0; j < modeloTabla.getColumnCount(); j++) {
                    tablaPDF.addCell(modeloTabla.getValueAt(i, j).toString());
                }
            }

            documento.add(tablaPDF);
            documento.close();
            JOptionPane.showMessageDialog(this, "PDF generado exitosamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al generar PDF: " + e.getMessage());
        }
    }

    // Método principal para ejecutar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReporteProductos());
    }
}
