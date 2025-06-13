import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

public class VerAlimentos extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public VerAlimentos() {
        setTitle("Ver Alimentos");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        String[] columnas = {"Nombre", "Tipo", "Cantidad", "Unidad", "Stock Mínimo", "Fecha Vencimiento", "↕️ Movimiento"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);

        tabla.getColumn("↕️ Movimiento").setCellRenderer(new ButtonRenderer());
        tabla.getColumn("↕️ Movimiento").setCellEditor(new ButtonEditor(new JTextField()));

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                    return c;
                }

                if (column != 6) {
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
                }

                return c;
            }
        });

        JPanel panelFiltro = new JPanel(new BorderLayout());
        JLabel etiquetaFiltro = new JLabel("Buscar: ");
        JTextField campoFiltro = new JTextField();
        panelFiltro.add(etiquetaFiltro, BorderLayout.WEST);
        panelFiltro.add(campoFiltro, BorderLayout.CENTER);

        campoFiltro.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            public void changedUpdate(DocumentEvent e) { filtrar(); }

            private void filtrar() {
                String texto = campoFiltro.getText();
                sorter.setRowFilter(texto.trim().isEmpty() ? null : RowFilter.regexFilter("(?i)" + texto));
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        getContentPane().add(panelFiltro, BorderLayout.NORTH);
        getContentPane().add(scroll, BorderLayout.CENTER);

        cargarAlimentos();
        setVisible(true);
    }

    private void cargarAlimentos() {
        modelo.setRowCount(0);

        try (BufferedReader reader = new BufferedReader(new FileReader("productos.txt"))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 5) {
                    String nombre = partes[0].trim();
                    String tipo = partes[1].trim();
                    String[] cantidadYUnidad = partes[2].trim().split(" ");
                    int cantidad = Integer.parseInt(cantidadYUnidad[0]);
                    String unidad = cantidadYUnidad.length > 1 ? cantidadYUnidad[1] : "";
                    int stockMin = Integer.parseInt(partes[3].trim());
                    String vence = partes[4].trim();

                    if (tipo.equalsIgnoreCase("Alimento")) {
                        modelo.addRow(new Object[]{nombre, tipo, cantidad, unidad, stockMin, vence, "Mover"});
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error leyendo productos.txt: " + e.getMessage());
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("Mover");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean clicked;
        private int row;

        public ButtonEditor(JTextField checkBox) {
            super(checkBox);
            button = new JButton("Mover");
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.row = row;
            button.setText("Mover");
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                String nombre = modelo.getValueAt(row, 0).toString();
                int actual = Integer.parseInt(modelo.getValueAt(row, 2).toString());
                String unidad = modelo.getValueAt(row, 3).toString();

                String[] opciones = {"Entrada", "Salida"};
                int opcion = JOptionPane.showOptionDialog(null, "¿Qué deseas hacer con " + nombre + "?",
                        "Control de inventario", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, opciones, opciones[0]);

                if (opcion != -1) {
                    String input = JOptionPane.showInputDialog(null, "Cantidad a " + opciones[opcion] + ":");
                    if (input != null && !input.trim().isEmpty()) {
                        try {
                            int cantidad = Integer.parseInt(input);
                            if (cantidad <= 0) {
                                JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor que cero.");
                            } else if (opcion == 1 && cantidad > actual) {
                                JOptionPane.showMessageDialog(null, "No puedes retirar más de lo disponible.");
                            } else {
                                int nuevo = opcion == 0 ? actual + cantidad : actual - cantidad;
                                modelo.setValueAt(nuevo, row, 2);
                                actualizarArchivo(nombre, nuevo, unidad);
                                modelo.fireTableRowsUpdated(row, row);
                                JOptionPane.showMessageDialog(null, "Cantidad actualizada correctamente.");
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Cantidad inválida.");
                        }
                    }
                }
            }
            clicked = false;
            return "Mover";
        }

        private void actualizarArchivo(String nombre, int nuevaCantidad, String unidad) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader("productos.txt"));
                StringBuilder contenido = new StringBuilder();
                String linea;
                while ((linea = reader.readLine()) != null) {
                    if (linea.startsWith(nombre + ",")) {
                        String[] partes = linea.split(",");
                        partes[2] = nuevaCantidad + " " + unidad;
                        linea = String.join(",", partes);
                    }
                    contenido.append(linea).append("\n");
                }
                reader.close();

                BufferedWriter writer = new BufferedWriter(new FileWriter("productos.txt"));
                writer.write(contenido.toString());
                writer.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error actualizando archivo: " + e.getMessage());
            }
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VerAlimentos::new);
    }
}
