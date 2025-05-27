import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.table.*;

public class VerProductosVencidos extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;

    public VerProductosVencidos() {
        setTitle("⚠️ Productos Vencidos");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        String[] columnas = {"Nombre", "Tipo", "Cantidad", "Unidad", "Stock Mínimo", "Fecha Vencimiento"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Render rojo solo si producto vencido
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate hoy = LocalDate.now();

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                String fechaStr = (String) table.getValueAt(row, 5);
                LocalDate fechaProducto;
                try {
                    fechaProducto = LocalDate.parse(fechaStr, formatter);
                } catch (Exception e) {
                    fechaProducto = null;
                }

                if (fechaProducto != null && fechaProducto.isBefore(hoy)) {
                    c.setBackground(new Color(255, 102, 102));
                    c.setForeground(Color.BLACK);
                } else {
                    if (isSelected) {
                        c.setBackground(table.getSelectionBackground());
                        c.setForeground(table.getSelectionForeground());
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        cargarProductosVencidos();
        setVisible(true);
    }

    private void cargarProductosVencidos() {
        modelo.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try (BufferedReader reader = new BufferedReader(new FileReader("productos.txt"))) {
            String linea;
            LocalDate hoy = LocalDate.now();
            boolean hayVencidos = false;
            while ((linea = reader.readLine()) != null) {
                String nombre = extraerCampo(linea, "Nombre:");
                String tipo = extraerCampo(linea, "Tipo:");
                String cantidadStr = extraerCampo(linea, "Cantidad:");
                String[] parts = cantidadStr.split(" ");
                int cantidad = 0;
                String unidad = "";
                if (parts.length >= 1) {
                    try {
                        cantidad = Integer.parseInt(parts[0]);
                    } catch (NumberFormatException e) {
                        cantidad = 0;
                    }
                }
                if (parts.length > 1) unidad = parts[1];
                int stockMin = 0;
                try {
                    stockMin = Integer.parseInt(extraerCampo(linea, "Stock Mínimo:"));
                } catch (NumberFormatException e) {
                    stockMin = 0;
                }
                String fechaStr = extraerCampo(linea, "Vence:");
                if (fechaStr.isEmpty()) continue;

                LocalDate fecha;
                try {
                    fecha = LocalDate.parse(fechaStr, formatter);
                } catch (Exception e) {
                    continue;
                }

                if (fecha.isBefore(hoy)) {
                    modelo.addRow(new Object[]{nombre, tipo, cantidad, unidad, stockMin, fechaStr});
                    hayVencidos = true;
                }
            }
            if (!hayVencidos) {
                JOptionPane.showMessageDialog(this, "✅ No hay productos vencidos.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "❌ Error al leer el archivo de productos.");
        }
    }

    private String extraerCampo(String linea, String campo) {
        int inicio = linea.indexOf(campo);
        if (inicio == -1) return "";
        int fin = linea.indexOf("|", inicio);
        if (fin == -1) fin = linea.length();
        return linea.substring(inicio + campo.length(), fin).trim();
    }
}
