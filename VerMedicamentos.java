import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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

public class VerMedicamentos extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public VerMedicamentos() {
        setTitle("Ver Medicamentos");
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

        tabla.getColumn("↕️ Movimiento").setCellRenderer(new ButtonRenderer());
        tabla.getColumn("↕️ Movimiento").setCellEditor(new ButtonEditor(new JCheckBox()));

        JPanel panelFiltro = new JPanel(new BorderLayout());
        JLabel etiquetaFiltro = new JLabel("Buscar: ");
        JTextField campoFiltro = new JTextField();
        panelFiltro.add(etiquetaFiltro, BorderLayout.WEST);
        panelFiltro.add(campoFiltro, BorderLayout.CENTER);

        campoFiltro.getDocument().addDocumentListener(new DocumentListener() {
            private void filtrar() {
                String texto = campoFiltro.getText();
                if (texto.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
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
    modelo.setRowCount(0);
    File archivo = new File("productos.txt");

    if (!archivo.exists()) {
        JOptionPane.showMessageDialog(this, "El archivo productos.txt no existe.");
        return;
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
        String linea;
        while ((linea = reader.readLine()) != null) {
            String[] partes = linea.split(",");

            if (partes.length < 5) continue;

            String nombre = partes[0].trim();
            String tipo = partes[1].trim();

            if (!tipo.equalsIgnoreCase("Medicamento")) continue;

            String cantidadUnidad = partes[2].trim();
            String[] cantidadPartes = cantidadUnidad.split(" ", 2);
            int cantidad = Integer.parseInt(cantidadPartes[0]);
            String unidad = cantidadPartes.length > 1 ? cantidadPartes[1] : "";

            int stockMin = Integer.parseInt(partes[3].trim());
            String vence = partes[4].trim();

            modelo.addRow(new Object[]{nombre, tipo, cantidad, unidad, stockMin, vence, "Mover"});
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error leyendo productos.txt: " + e.getMessage());
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

class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.table = table;
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    public Object getCellEditorValue() {
        if (isPushed) {
            int row = table.getSelectedRow();
            String nombre = table.getValueAt(row, 0).toString();
            int cantidadActual = Integer.parseInt(table.getValueAt(row, 2).toString());
            String unidad = table.getValueAt(row, 3).toString();

            String[] opciones = {"Entrada", "Salida"};
            int eleccion = JOptionPane.showOptionDialog(null,
                    "¿Qué deseas hacer con " + nombre + "?",
                    "Movimiento de stock",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, opciones, opciones[0]);

            if (eleccion != -1) {
                String tipoMovimiento = opciones[eleccion];
                String input = JOptionPane.showInputDialog("Ingrese la cantidad a " + tipoMovimiento.toLowerCase() + ":");

                if (input != null && input.matches("\\d+")) {
                    int cantidad = Integer.parseInt(input);
                    if (tipoMovimiento.equals("Salida") && cantidad > cantidadActual) {
                        JOptionPane.showMessageDialog(null, "No se puede retirar más de lo disponible.");
                    } else {
                        int nuevaCantidad = tipoMovimiento.equals("Entrada") ?
                                cantidadActual + cantidad : cantidadActual - cantidad;

                        table.setValueAt(nuevaCantidad, row, 2);

                        try {
                            File archivo = new File("productos.txt");
                            ArrayList<String> lineas = new ArrayList<>();
                            BufferedReader reader = new BufferedReader(new FileReader(archivo));
                            String linea;
                            while ((linea = reader.readLine()) != null) {
                                if (linea.contains("Nombre: " + nombre) && linea.contains("Tipo: Medicamento")) {
                                    String[] partes = linea.split("\\|");
                                    for (int i = 0; i < partes.length; i++) {
                                        if (partes[i].trim().startsWith("Cantidad:")) {
                                            partes[i] = " Cantidad: " + nuevaCantidad + " " + unidad;
                                            break;
                                        }
                                    }
                                    linea = String.join("|", partes);
                                }
                                lineas.add(linea);
                            }
                            reader.close();
                            PrintWriter writer = new PrintWriter(new FileWriter(archivo));
                            for (String l : lineas) {
                                writer.println(l);
                            }
                            writer.close();
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(null, "Error al actualizar archivo: " + e.getMessage());
                        }
                    }
                }
            }
        }
        isPushed = false;
        return label;
    }

    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
