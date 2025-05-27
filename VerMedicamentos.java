import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;

public class VerMedicamentos extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;  // Para filtro y orden
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public VerMedicamentos() {
        setTitle("Ver Medicamentos");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        String[] columnas = {"Nombre", "Tipo", "Cantidad", "Unidad", "Stock Mínimo", "Fecha Vencimiento"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No editable directamente
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Configurar sorter para ordenar y filtrar
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);

        // Renderizado para colorear filas según estado (igual que antes)
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                    return c;
                }

                try {
                    int modelRow = table.convertRowIndexToModel(row);
                    String fechaStr = (String) modelo.getValueAt(modelRow, 5);
                    int cantidad = Integer.parseInt(modelo.getValueAt(modelRow, 2).toString());
                    int stockMin = Integer.parseInt(modelo.getValueAt(modelRow, 4).toString());

                    LocalDate hoy = LocalDate.now();
                    LocalDate fecha = LocalDate.parse(fechaStr, formatter);
                    long diasParaVencer = java.time.temporal.ChronoUnit.DAYS.between(hoy, fecha);

                    if (fecha.isBefore(hoy)) {
                        c.setBackground(new Color(255, 102, 102));
                        c.setForeground(Color.BLACK);
                    } else if (diasParaVencer <= 7 && cantidad <= stockMin) {
                        c.setBackground(new Color(255, 255, 204));
                        c.setForeground(Color.RED);
                    } else if (diasParaVencer <= 7) {
                        c.setBackground(new Color(255, 178, 102));
                        c.setForeground(Color.BLACK);
                    } else if (cantidad <= stockMin) {
                        c.setBackground(new Color(255, 255, 204));
                        c.setForeground(Color.BLACK);
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                } catch (Exception e) {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        });

        // Campo filtro arriba
        JPanel panelFiltro = new JPanel(new BorderLayout());
        JLabel etiquetaFiltro = new JLabel("Buscar: ");
        JTextField campoFiltro = new JTextField();
        panelFiltro.add(etiquetaFiltro, BorderLayout.WEST);
        panelFiltro.add(campoFiltro, BorderLayout.CENTER);

        // Añadir listener para filtrar según texto
        campoFiltro.getDocument().addDocumentListener(new DocumentListener() {
            private void filtrar() {
                String texto = campoFiltro.getText();
                if (texto.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    // Filtra en todas las columnas que quieras, aquí en 0(nombre),1(tipo),5(fecha)
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 0, 1, 5));
                }
            }
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            public void changedUpdate(DocumentEvent e) { filtrar(); }
        });

        JScrollPane scroll = new JScrollPane(tabla);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelFiltro, BorderLayout.NORTH);
        getContentPane().add(scroll, BorderLayout.CENTER);

        cargarMedicamentos();

        setVisible(true);
    }

    private void cargarMedicamentos() {
        modelo.setRowCount(0); // Limpiar

        ArrayList<String> vencidos = new ArrayList<>();
        ArrayList<String> proximosAVencer = new ArrayList<>();
        ArrayList<String> stockBajo = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("productos.txt"))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.contains("Tipo: Medicamento")) continue;

                String nombre = extraerCampo(linea, "Nombre:");
                String tipo = "Medicamento";
                String cantidadStr = extraerCampo(linea, "Cantidad:");
                String[] partes = cantidadStr.split(" ");
                int cantidad = 0;
                String unidad = "";
                if (partes.length >= 1) cantidad = Integer.parseInt(partes[0]);
                if (partes.length >= 2) unidad = partes[1];

                int stockMin = Integer.parseInt(extraerCampo(linea, "Stock Mínimo:"));
                String vence = extraerCampo(linea, "Vence:");

                modelo.addRow(new Object[]{nombre, tipo, cantidad, unidad, stockMin, vence});

                LocalDate hoy = LocalDate.now();
                LocalDate fechaVence = LocalDate.parse(vence, formatter);
                long diasParaVencer = java.time.temporal.ChronoUnit.DAYS.between(hoy, fechaVence);

                if (fechaVence.isBefore(hoy)) {
                    vencidos.add(nombre);
                } else if (diasParaVencer <= 7) {
                    proximosAVencer.add(nombre + " (vence en " + diasParaVencer + " días)");
                }

                if (cantidad <= stockMin) {
                    stockBajo.add(nombre + " (stock: " + cantidad + ")");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error leyendo productos.txt: " + e.getMessage());
        }

        StringBuilder alerta = new StringBuilder();
        if (!vencidos.isEmpty()) {
            alerta.append("Medicamentos vencidos:\n");
            for (String p : vencidos) alerta.append(" - ").append(p).append("\n");
            alerta.append("\n");
        }
        if (!proximosAVencer.isEmpty()) {
            alerta.append("Medicamentos próximos a vencer (menos de 7 días):\n");
            for (String p : proximosAVencer) alerta.append(" - ").append(p).append("\n");
            alerta.append("\n");
        }
        if (!stockBajo.isEmpty()) {
            alerta.append("Medicamentos con stock bajo:\n");
            for (String p : stockBajo) alerta.append(" - ").append(p).append("\n");
            alerta.append("\n");
        }

        if (alerta.length() > 0) {
            JOptionPane.showMessageDialog(this, alerta.toString(), "Alertas importantes", JOptionPane.WARNING_MESSAGE);
        }
    }

    private String extraerCampo(String linea, String campo) {
        int inicio = linea.indexOf(campo);
        if (inicio == -1) return "";
        int fin = linea.indexOf("|", inicio);
        if (fin == -1) fin = linea.length();
        return linea.substring(inicio + campo.length(), fin).trim();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VerMedicamentos::new);
    }
}
