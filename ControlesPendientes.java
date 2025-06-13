import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ControlesPendientes extends JFrame {
    private DefaultTableModel modelo;

    public ControlesPendientes(String rol) {
        setTitle("Controles Sanitarios Pendientes");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Último Control", "Estado"}, 0);
        JTable tabla = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        cargarDatos();

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
        add(bottomPanel, BorderLayout.SOUTH);

        // Para administrador, doble clic abre hoja de vida
        if (rol.equals("Administrador")) {
            tabla.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        int fila = tabla.getSelectedRow();
                        if (fila != -1) {
                            String id = modelo.getValueAt(fila, 0).toString();
                            dispose(); // Cierra esta ventana
                            new VerHojasDeVida(); // Abre hoja de vida
                        }
                    }
                }
            });
        }

        setVisible(true);
    }

    private void cargarDatos() {
        Map<String, String[]> animales = cargarAnimales();
        Map<String, List<String[]>> eventos = cargarEventos();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date ahora = new Date();

        for (String id : animales.keySet()) {
            String nombre = animales.get(id)[0];
            List<String[]> eventosAnimal = eventos.getOrDefault(id, new ArrayList<>());

            Date ultimoControl = null;
            for (String[] ev : eventosAnimal) {
                String tipo = ev[0];
                String fechaStr = ev[1];
                if (tipo.equalsIgnoreCase("Control Sanitario")) {
                    try {
                        Date fechaEv = sdf.parse(fechaStr);
                        if (ultimoControl == null || fechaEv.after(ultimoControl)) {
                            ultimoControl = fechaEv;
                        }
                    } catch (ParseException ignored) {}
                }
            }

            String estado;
            if (ultimoControl == null) {
                estado = "Pendiente";
            } else {
                long diffMs = ahora.getTime() - ultimoControl.getTime();
                long seisMesesMs = 6L * 30 * 24 * 60 * 60 * 1000; // 6 meses en ms
                if (diffMs > seisMesesMs) {
                    estado = "Pendiente";
                } else {
                    estado = "Al día";
                }
            }

            if (estado.equals("Pendiente")) {
                String fechaUltControl = (ultimoControl == null) ? "Ninguno" : sdf.format(ultimoControl);
                modelo.addRow(new Object[]{id, nombre, fechaUltControl, estado});
            }
        }
    }

    private Map<String, String[]> cargarAnimales() {
        Map<String, String[]> map = new HashMap<>();
        File archivo = new File("animales.txt");
        if (!archivo.exists()) return map;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 3) {
                    String id = partes[0];
                    String nombre = partes[1];
                    String raza = partes[2];
                    map.put(id, new String[]{nombre, raza});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private Map<String, List<String[]>> cargarEventos() {
        Map<String, List<String[]>> map = new HashMap<>();
        File archivo = new File("eventos.txt");
        if (!archivo.exists()) return map;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 3) {
                    String id = partes[0];
                    String tipoEvento = partes[1];
                    String fecha = partes[2];
                    String descripcion = (partes.length > 3) ? partes[3] : "";
                    map.computeIfAbsent(id, k -> new ArrayList<>()).add(new String[]{tipoEvento, fecha, descripcion});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}
