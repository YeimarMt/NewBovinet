import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class AgregarProducto extends JFrame {

    private JTextField nombreField, cantidadField, stockMinField, fechaField;
    private JComboBox<String> tipoCombo, unidadCombo;

    public AgregarProducto() {
        setTitle("Agregar Producto");
        setSize(500, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(250, 250, 250));

        JLabel titulo = new JLabel("🧾 Agregar Producto al Inventario", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(new Color(34, 139, 34));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        mainPanel.add(titulo, BorderLayout.NORTH);

        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 40, 20, 40),
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1)
        ));
        cardPanel.setLayout(new GridLayout(6, 2, 15, 20));

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Campos del formulario
        cardPanel.add(createLabel("Nombre:", labelFont));
        nombreField = createTextField();
        cardPanel.add(nombreField);

        cardPanel.add(createLabel("Tipo:", labelFont));
        tipoCombo = new JComboBox<>(new String[]{"Alimento", "Medicamento"});
        tipoCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tipoCombo.setBackground(Color.WHITE);
        tipoCombo.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        cardPanel.add(tipoCombo);

        cardPanel.add(createLabel("Cantidad:", labelFont));
        cantidadField = createTextField();
        cardPanel.add(cantidadField);

        cardPanel.add(createLabel("Unidad:", labelFont));
        unidadCombo = new JComboBox<>(new String[]{"kg", "g", "L", "ml", "unidad"});
        unidadCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        unidadCombo.setBackground(Color.WHITE);
        unidadCombo.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        cardPanel.add(unidadCombo);

        cardPanel.add(createLabel("Stock mínimo:", labelFont));
        stockMinField = createTextField();
        cardPanel.add(stockMinField);

        cardPanel.add(createLabel("Fecha vencimiento (AAAA-MM-DD):", labelFont));
        fechaField = createTextField();
        agregarFormatoFecha(fechaField);
        cardPanel.add(fechaField);

        mainPanel.add(cardPanel, BorderLayout.CENTER);

        JButton guardarBtn = new JButton("✅ Guardar");
        guardarBtn.setBackground(new Color(46, 139, 87));
        guardarBtn.setForeground(Color.WHITE);
        guardarBtn.setFocusPainted(false);
        guardarBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        guardarBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        guardarBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        guardarBtn.addActionListener(e -> guardarProducto());

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(250, 250, 250));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        btnPanel.add(guardarBtn);

        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(new Color(60, 60, 60));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        return field;
    }
    
    private void agregarFormatoFecha(JTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            private boolean isUpdating = false;

            private void formatFecha() {
                if (isUpdating) return;
                isUpdating = true;

                String texto = field.getText().replaceAll("[^\\d]", "");
                StringBuilder formateado = new StringBuilder();

                for (int i = 0; i < texto.length() && i < 8; i++) {
                    formateado.append(texto.charAt(i));
                    if ((i == 3 || i == 5) && i != texto.length() - 1) {
                        formateado.append("-");
                    }
                }

                field.setText(formateado.toString());
                isUpdating = false;
            }

            @Override
            public void insertUpdate(DocumentEvent e) { formatFecha(); }
            @Override
            public void removeUpdate(DocumentEvent e) { formatFecha(); }
            @Override
            public void changedUpdate(DocumentEvent e) { formatFecha(); }
        });
    }

    private void guardarProducto() {
        String nombre = nombreField.getText().trim();
        String tipo = (String) tipoCombo.getSelectedItem();
        String cantidadStr = cantidadField.getText().trim();
        String unidad = (String) unidadCombo.getSelectedItem();
        String stockStr = stockMinField.getText().trim();
        String fechaStr = fechaField.getText().trim();

        if (nombre.isEmpty() || cantidadStr.isEmpty() || stockStr.isEmpty() || fechaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❗ Por favor, complete todos los campos.");
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            int stockMin = Integer.parseInt(stockStr);
            LocalDate fecha = LocalDate.parse(fechaStr);

            // Guardar en archivo
            FileWriter writer = new FileWriter("productos.txt", true);
            writer.write("Nombre: " + nombre + " | Tipo: " + tipo + " | Cantidad: " + cantidad + " " + unidad +
                        " | Stock Mínimo: " + stockMin + " | Vence: " + fecha + "\n");
            writer.close();

            // Alerta si la cantidad es menor o igual al stock mínimo
            if (cantidad <= stockMin) {
                JOptionPane.showMessageDialog(this, "⚠️ Atención: Este producto ha alcanzado el stock mínimo.\n"
                        + "Considere reponerlo pronto.");
            } else {
                JOptionPane.showMessageDialog(this, "✅ Producto guardado con éxito.");
            }

            dispose();

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "❌ Cantidad y stock deben ser números enteros.");
        } catch (DateTimeParseException dte) {
            JOptionPane.showMessageDialog(this, "❌ La fecha debe tener formato AAAA-MM-DD.");
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this, "❌ Error al guardar el producto.");
        }
    }

}
