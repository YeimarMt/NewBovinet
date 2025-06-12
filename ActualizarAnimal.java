import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;


public class ActualizarAnimal extends JFrame {
    private JTextField idField, nombreField, razaField;
    private JDateChooser fechaChooser;
    private JComboBox<String> eventoBox;

    public ActualizarAnimal() {
        setTitle("Actualizar Animal");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        Dimension fieldSize = new Dimension(250, 30);

        // ID + Botón Buscar
        JLabel idLabel = new JLabel("ID del Animal:");
        idLabel.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 0;
        add(idLabel, gbc);

        idField = new JTextField();
        idField.setFont(fieldFont);
        idField.setPreferredSize(fieldSize);
        gbc.gridx = 1;
        add(idField, gbc);

        JButton buscarBtn = new JButton("Buscar");
        buscarBtn.setFont(fieldFont);
        gbc.gridx = 2;
        add(buscarBtn, gbc);

        // Nombre
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 1;
        add(nombreLabel, gbc);

        nombreField = new JTextField();
        nombreField.setFont(fieldFont);
        nombreField.setPreferredSize(fieldSize);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(nombreField, gbc);
        gbc.gridwidth = 1;

        // Raza
        JLabel razaLabel = new JLabel("Raza:");
        razaLabel.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 2;
        add(razaLabel, gbc);

        razaField = new JTextField();
        razaField.setFont(fieldFont);
        razaField.setPreferredSize(fieldSize);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(razaField, gbc);
        gbc.gridwidth = 1;

        // Fecha nacimiento
        JLabel fechaLabel = new JLabel("Fecha Nacimiento:");
        fechaLabel.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 3;
        add(fechaLabel, gbc);

        fechaChooser = new JDateChooser();
        fechaChooser.setDateFormatString("dd/MM/yyyy");
        fechaChooser.setPreferredSize(fieldSize);
        fechaChooser.setFont(fieldFont);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(fechaChooser, gbc);
        gbc.gridwidth = 1;

        // Evento
        JLabel eventoLabel = new JLabel("Evento:");
        eventoLabel.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 4;
        add(eventoLabel, gbc);

        String[] eventos = {"Vacunación", "Parto", "Embarazo", "Venta", "Fallecimiento"};
        eventoBox = new JComboBox<>(eventos);
        eventoBox.setFont(fieldFont);
        eventoBox.setPreferredSize(fieldSize);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(eventoBox, gbc);
        gbc.gridwidth = 1;

        // Botón Guardar Evento
        JButton guardarBtn = new JButton("Guardar Evento");
        guardarBtn.setFont(fieldFont);
        guardarBtn.setBackground(new Color(46, 139, 87));
        guardarBtn.setForeground(Color.WHITE);
        guardarBtn.setFocusPainted(false);
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 2;
        add(guardarBtn, gbc);

        // Acciones
        buscarBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            if (!id.isEmpty()) {
                buscarAnimalParaModificar(id);
            } else {
                JOptionPane.showMessageDialog(this, "Ingresa un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        guardarBtn.addActionListener(e -> guardarEvento());

        setVisible(true);
    }

    private void buscarAnimalParaModificar(String idBuscado) {
    File archivo = new File("animales.txt");
    if (!archivo.exists()) {
        JOptionPane.showMessageDialog(this, "Archivo animales.txt no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
        String linea;
        boolean encontrado = false;

        while ((linea = reader.readLine()) != null) {
            String[] datos = linea.split(";");
            if (datos.length >= 4 && datos[0].equals(idBuscado)) {
                encontrado = true;

                if (linea.contains(";Fallecimiento;") || linea.endsWith(";Fallecimiento")) {
                    JOptionPane.showMessageDialog(this,
                        "Este animal ya fue dado de baja.\nNo se permite su modificación.",
                        "Acceso restringido", JOptionPane.WARNING_MESSAGE);

                    nombreField.setEditable(false);
                    razaField.setEditable(false);
                    fechaChooser.setEnabled(false);
                    eventoBox.setEnabled(false);
                    return;
                }

                nombreField.setText(datos[1]);
                razaField.setText(datos[2]);

                try {
                    Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(datos[3]);
                    fechaChooser.setDate(fecha);
                } catch (ParseException ex) {
                    fechaChooser.setDate(null);
                }

                nombreField.setEditable(true);
                razaField.setEditable(true);
                fechaChooser.setEnabled(true);
                eventoBox.setEnabled(true);
                return;
            }
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(this, "No se encontró ningún animal con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    } catch (IOException e) {
        e.printStackTrace();
    }
}


    private void guardarEvento() {
        String id = idField.getText().trim();
        String evento = (String) eventoBox.getSelectedItem();
        String nombre = nombreField.getText().trim();
        String raza = razaField.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes ingresar un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (nombre.isEmpty() || raza.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y raza no pueden estar vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date fechaSeleccionada = fechaChooser.getDate();
        if (fechaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una fecha válida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date fechaActual = new Date();
        if (fechaSeleccionada.after(fechaActual)) {
            JOptionPane.showMessageDialog(this, "La fecha no puede ser futura.", "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String fechaFormateada = new SimpleDateFormat("dd/MM/yyyy").format(fechaSeleccionada);

        File archivo = new File("animales.txt");
        File archivoTemp = new File("animales_temp.txt");
        boolean actualizado = false;

        try (
            BufferedReader reader = new BufferedReader(new FileReader(archivo));
            BufferedWriter writer = new BufferedWriter(new FileWriter(archivoTemp))
        ) {
            String linea;

            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");

                if (datos[0].equals(id)) {
                    datos[1] = nombre;
                    datos[2] = raza;
                    datos[3] = fechaFormateada;

                    if (datos.length == 5) {
                        datos[4] = evento;
                    } else {
                        linea += ";" + evento;
                        datos = linea.split(";");
                    }

                    linea = String.join(";", datos);
                    actualizado = true;
                }

                writer.write(linea);
                writer.newLine();
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return;
        }

        if (actualizado) {
            if (archivo.delete()) {
                if (archivoTemp.renameTo(archivo)) {
                    JOptionPane.showMessageDialog(this, "Evento actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al renombrar el archivo temporal.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error al reemplazar el archivo original.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            archivoTemp.delete();
            JOptionPane.showMessageDialog(this, "ID no encontrado. No se actualizó ningún animal.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
}