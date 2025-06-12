import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

public class VerHojasDeVida extends JFrame {
    private JTextField idField;
    private JTextArea resultadoArea;
    private JButton exportarBtn;

    // Constructor sin parámetros (por defecto)
    public VerHojasDeVida() {
        setTitle("Hoja de Vida del Animal");
        setSize(500, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior con campo de ID y botones
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("Ingrese ID del Animal:");
        idField = new JTextField(10);
        JButton buscarBtn = new JButton("Buscar");
        exportarBtn = new JButton("Exportar como TXT");
        exportarBtn.setEnabled(false); // Solo se habilita si hay contenido

        topPanel.add(label);
        topPanel.add(idField);
        topPanel.add(buscarBtn);
        topPanel.add(exportarBtn);

        // Área de resultado
        resultadoArea = new JTextArea();
        resultadoArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultadoArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(resultadoArea);

        // Agregar componentes al frame
        add(topPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // Acciones
        buscarBtn.addActionListener(e -> buscarHojaDeVida());
        exportarBtn.addActionListener(e -> exportarComoTxt());

        setVisible(true);
    }

    private void buscarHojaDeVida() {
        String idBuscado = idField.getText().trim();
        if (idBuscado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String datosAnimal = obtenerDatosAnimal(idBuscado);
        if (datosAnimal == null) {
            resultadoArea.setText("No se encontró ningún animal con el ID: " + idBuscado);
            exportarBtn.setEnabled(false);
            return;
        }

        ArrayList<String> eventos = obtenerEventosAnimal(idBuscado);
        StringBuilder hoja = new StringBuilder();
        hoja.append("=== DATOS DEL ANIMAL ===\n");
        hoja.append(datosAnimal).append("\n\n");

        hoja.append("=== HISTORIAL CLÍNICO Y REPRODUCTIVO ===\n");
        if (eventos.isEmpty()) {
            hoja.append("Sin eventos registrados.\n");
        } else {
            for (String evento : eventos) {
                hoja.append("- ").append(evento).append("\n");
            }
        }

        resultadoArea.setText(hoja.toString());
        exportarBtn.setEnabled(true);
    }

    private String obtenerDatosAnimal(String idBuscado) {
        File archivo = new File("animales.txt");
        if (!archivo.exists()) return null;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 4 && datos[0].equals(idBuscado)) {
                    return "ID: " + datos[0] + "\nNombre: " + datos[1] + "\nRaza: " + datos[2] + "\nFecha Nacimiento: " + datos[3];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList<String> obtenerEventosAnimal(String idBuscado) {
        ArrayList<String> eventos = new ArrayList<>();
        File archivo = new File("eventos.txt");
        if (!archivo.exists()) return eventos;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 3 && partes[0].equals(idBuscado)) {
                    eventos.add("[" + partes[1] + "] - Fecha: " + partes[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return eventos;
    }

    private void exportarComoTxt() {
        String id = idField.getText().trim();
        String contenido = resultadoArea.getText();

        if (contenido.isEmpty() || id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay información para exportar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File archivo = new File("HojaDeVida_" + id + ".txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            writer.write(contenido);
            JOptionPane.showMessageDialog(this, "Hoja de vida exportada como: " + archivo.getName(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al exportar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
