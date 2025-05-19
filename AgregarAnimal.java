import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import javax.swing.*;

public class AgregarAnimal extends JFrame {
    private JTextField nombreField, razaField;
    private JLabel idLabel;
    private JDateChooser fechaChooser;

    public AgregarAnimal() {
        setTitle("Agregar Animal");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String nuevoId = generarNuevoId();

        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        // ID
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        idLabel = new JLabel(nuevoId);
        idLabel.setFont(font);
        add(idLabel, gbc);

        // Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        nombreField = new JTextField(20);
        nombreField.setFont(font);
        add(nombreField, gbc);

        // Raza
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Raza:"), gbc);
        gbc.gridx = 1;
        razaField = new JTextField(20);
        razaField.setFont(font);
        add(razaField, gbc);

        // Fecha con JDateChooser
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Fecha Nacimiento:"), gbc);
        gbc.gridx = 1;
        fechaChooser = new JDateChooser();
        fechaChooser.setDateFormatString("dd/MM/yyyy");
        fechaChooser.setPreferredSize(new Dimension(200, 25));
        add(fechaChooser, gbc);

        // Botón Guardar
        gbc.gridx = 1; gbc.gridy = 4;
        JButton guardarBtn = new JButton("Guardar");
        guardarBtn.setBackground(new Color(46, 139, 87));
        guardarBtn.setForeground(Color.WHITE);
        guardarBtn.setFocusPainted(false);
        guardarBtn.setFont(font);
        add(guardarBtn, gbc);

        guardarBtn.addActionListener(e -> guardarAnimal(nuevoId));

        setVisible(true);
    }

    private void guardarAnimal(String id) {
        String nombre = nombreField.getText().trim();
        String raza = razaField.getText().trim();

        if (nombre.isEmpty() || raza.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (fechaChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una fecha válida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        java.util.Date fechaSeleccionada = fechaChooser.getDate();
        java.util.Date fechaActual = new java.util.Date();

        if (fechaSeleccionada.after(fechaActual)) {
            JOptionPane.showMessageDialog(this, "La fecha no puede ser futura.", "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }


        String fecha = new SimpleDateFormat("dd/MM/yyyy").format(fechaChooser.getDate());
        String linea = id + ";" + nombre + ";" + raza + ";" + fecha;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("animales.txt", true))) {
            writer.write(linea);
            writer.newLine();
            JOptionPane.showMessageDialog(this, "Animal agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generarNuevoId() {
        int maxId = 0;
        File archivo = new File("animales.txt");

        if (archivo.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    String[] datos = linea.split(";");
                    try {
                        int id = Integer.parseInt(datos[0]);
                        if (id > maxId) maxId = id;
                    } catch (NumberFormatException ignored) {}
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return String.format("%02d", maxId + 1);
    }

    public static void main(String[] args) {
        new AgregarAnimal();
    }
}
