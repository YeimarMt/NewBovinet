import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

public class VerAnimales extends JFrame {
    private JList<String> listaAnimales;
    private DefaultListModel<String> modeloLista;
    private JTextArea detalleArea;
    private ArrayList<String[]> animalesDatos;

    public VerAnimales(String rol) {
        setTitle("Ver Animales");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        modeloLista = new DefaultListModel<>();
        listaAnimales = new JList<>(modeloLista);
        listaAnimales.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollLista = new JScrollPane(listaAnimales);

        detalleArea = new JTextArea();
        detalleArea.setEditable(false);
        detalleArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detalleArea.setBorder(BorderFactory.createTitledBorder("Detalle del Animal"));

        animalesDatos = new ArrayList<>();
        cargarAnimales();

        listaAnimales.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int index = listaAnimales.getSelectedIndex();
                if (index != -1) {
                    String[] datos = animalesDatos.get(index);
                    StringBuilder detalle = new StringBuilder();
                    detalle.append("ID: ").append(datos[0]).append("\n");
                    detalle.append("Nombre: ").append(datos[1]).append("\n");
                    detalle.append("Raza: ").append(datos[2]).append("\n");
                    detalle.append("Fecha Nacimiento: ").append(datos[3]).append("\n");
                    detalle.append("Eventos: ").append(datos.length >= 5 ? datos[4] : "Ninguno");
                    detalleArea.setText(detalle.toString());
                }
            }
        });

        // Panel inferior con botón "Volver"
        JPanel bottomPanel = new JPanel();
        JButton btnVolver = new JButton("Volver");
        btnVolver.setBackground(new Color(46, 139, 87));
        btnVolver.setForeground(Color.BLACK);
        btnVolver.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnVolver.setFocusPainted(false);
        btnVolver.setPreferredSize(new Dimension(100, 35));

        btnVolver.addActionListener(e -> {
            dispose(); // Cierra esta ventana
            new PanelAnimales(rol); // Regresa al panel de animales
        });

        bottomPanel.add(btnVolver);

        // Agregar todo al frame
        add(scrollLista, BorderLayout.WEST);
        add(detalleArea, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void cargarAnimales() {
        File archivo = new File("animales.txt");
        if (!archivo.exists()) {
            JOptionPane.showMessageDialog(this, "No hay animales registrados.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 4) {
                    modeloLista.addElement(datos[0] + " - " + datos[1]); // ID - Nombre
                    animalesDatos.add(datos);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer animales.txt", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
