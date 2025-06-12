import java.awt.*;
import java.io.*;
import javax.swing.*;

public class DarDeBajaAnimal extends JFrame {
    private JTextField idField, fechaField;
    private JComboBox<String> motivoCombo;

    public DarDeBajaAnimal() {
        setTitle("Dar de Baja Animal");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));

        JLabel idLabel = new JLabel("ID del Animal:");
        idField = new JTextField();

        JLabel motivoLabel = new JLabel("Motivo de la Baja:");
        motivoCombo = new JComboBox<>(new String[]{"Venta", "Fallecimiento"});

        JLabel fechaLabel = new JLabel("Fecha de Baja (YYYY-MM-DD):");
        fechaField = new JTextField();

        JButton darBajaBtn = new JButton("Confirmar Baja");
        darBajaBtn.addActionListener(e -> darDeBaja());

        add(idLabel); add(idField);
        add(motivoLabel); add(motivoCombo);
        add(fechaLabel); add(fechaField);
        add(new JLabel()); add(darBajaBtn);

        setVisible(true);
    }

    private void darDeBaja() {
        String id = idField.getText().trim();
        String motivo = motivoCombo.getSelectedItem().toString();
        String fecha = fechaField.getText().trim();

        if (id.isEmpty() || fecha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File archivo = new File("animales.txt");
        if (!archivo.exists()) {
            JOptionPane.showMessageDialog(this, "No se encontró el archivo animales.txt", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File tempFile = new File("animales_temp.txt");
        boolean encontrado = false;
        boolean yaDadoDeBaja = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 4 && datos[0].equals(id)) {
                    encontrado = true;
                    if (linea.contains(";Baja;")) {
                        yaDadoDeBaja = true;
                        writer.write(linea); // No modificar
                    } else {
                        linea += ";Baja;" + motivo + ";" + fecha;
                        writer.write(linea);
                    }
                } else {
                    writer.write(linea);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        archivo.delete();
        tempFile.renameTo(archivo);

        if (!encontrado) {
            JOptionPane.showMessageDialog(this, "No se encontró ningún animal con el ID especificado.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (yaDadoDeBaja) {
            JOptionPane.showMessageDialog(this, "El animal ya fue dado de baja anteriormente.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Animal dado de baja correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
}
