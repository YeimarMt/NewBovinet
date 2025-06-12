import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class RegistrarEvento extends JFrame {
    private JTextField idField;
    private JComboBox<String> tipoEventoCombo;
    private JDateChooser fechaChooser;

    public RegistrarEvento() {
        setTitle("Registrar Evento");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        Font font = new Font("Segoe UI", Font.PLAIN, 14);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("ID Animal:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(15);
        idField.setFont(font);
        add(idField, gbc);

        // Tipo de evento
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Tipo Evento:"), gbc);
        gbc.gridx = 1;
        tipoEventoCombo = new JComboBox<>(new String[]{"Vacunación", "Preñez", "Parto", "Control Sanitario"});
        tipoEventoCombo.setFont(font);
        add(tipoEventoCombo, gbc);

        // Fecha
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Fecha Evento:"), gbc);
        gbc.gridx = 1;
        fechaChooser = new JDateChooser();
        fechaChooser.setDateFormatString("dd/MM/yyyy");
        fechaChooser.setPreferredSize(new Dimension(200, 25));
        add(fechaChooser, gbc);

        // Botón guardar
        gbc.gridx = 1; gbc.gridy = 3;
        JButton guardarBtn = new JButton("Registrar Evento");
        guardarBtn.setBackground(new Color(70, 130, 180));
        guardarBtn.setForeground(Color.WHITE);
        guardarBtn.setFont(font);
        add(guardarBtn, gbc);

        guardarBtn.addActionListener(e -> registrarEvento());

        setVisible(true);
    }

    private void registrarEvento() {
        String id = idField.getText().trim();
        String tipoEvento = (String) tipoEventoCombo.getSelectedItem();
        Date fecha = fechaChooser.getDate();

        if (id.isEmpty() || fecha == null) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!existeAnimal(id)) {
            JOptionPane.showMessageDialog(this, "El ID no existe en animales.txt", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String fechaStr = new SimpleDateFormat("dd/MM/yyyy").format(fecha);
        String linea = id + ";" + tipoEvento + ";" + fechaStr;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("eventos.txt", true))) {
            writer.write(linea);
            writer.newLine();
            JOptionPane.showMessageDialog(this, "Evento registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el evento.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean existeAnimal(String idBuscado) {
        File archivo = new File("animales.txt");
        if (!archivo.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length > 0 && partes[0].equals(idBuscado)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void main(String[] args) {
        new RegistrarEvento();
    }
}
