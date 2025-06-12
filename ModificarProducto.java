import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class ModificarProducto extends JFrame {

    private JTextField txtNombreBuscar, txtCantidad, txtUnidad, txtStockMinimo, txtFechaVencimiento;
    private JLabel lblTipo;
    private String tipoEncontrado;
    private TablaActualizable tablaActualizable; // NUEVO

    public ModificarProducto(TablaActualizable tablaActualizable) {
        this.tablaActualizable = tablaActualizable; // NUEVO

        setTitle("Modificar Producto");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 2, 5, 5));

        add(new JLabel("Nombre a buscar:"));
        txtNombreBuscar = new JTextField();
        add(txtNombreBuscar);

        JButton btnBuscar = new JButton("Buscar");
        add(btnBuscar);
        add(new JLabel()); // vacío

        add(new JLabel("Tipo:"));
        lblTipo = new JLabel("");
        add(lblTipo);

        add(new JLabel("Cantidad:"));
        txtCantidad = new JTextField();
        add(txtCantidad);

        add(new JLabel("Unidad:"));
        txtUnidad = new JTextField();
        add(txtUnidad);

        add(new JLabel("Stock Mínimo:"));
        txtStockMinimo = new JTextField();
        add(txtStockMinimo);

        add(new JLabel("Fecha de Vencimiento (yyyy-MM-dd):"));
        txtFechaVencimiento = new JTextField();
        add(txtFechaVencimiento);

        JButton btnActualizar = new JButton("Actualizar");
        add(btnActualizar);
        add(new JLabel()); // vacío

        btnBuscar.addActionListener(e -> buscarProducto());
        btnActualizar.addActionListener(e -> actualizarProducto());

        setVisible(true);
    }

    private void buscarProducto() {
        String nombreBuscar = txtNombreBuscar.getText().trim();
        if (nombreBuscar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre para buscar.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader("productos.txt"))) {
            String linea;
            boolean encontrado = false;
            while ((linea = reader.readLine()) != null) {
                if (linea.contains("Nombre: " + nombreBuscar)) {
                    encontrado = true;
                    lblTipo.setText(linea.contains("Tipo: Alimento") ? "Alimento" : "Medicamento");
                    tipoEncontrado = lblTipo.getText();

                    String cantidadStr = extraerCampo(linea, "Cantidad:");
                    String[] partes = cantidadStr.split(" ");
                    txtCantidad.setText(partes[0]);
                    txtUnidad.setText(partes.length > 1 ? partes[1] : "");

                    txtStockMinimo.setText(extraerCampo(linea, "Stock Mínimo:"));
                    txtFechaVencimiento.setText(extraerCampo(linea, "Vence:"));
                    break;
                }
            }
            if (!encontrado) {
                JOptionPane.showMessageDialog(this, "Producto no encontrado.");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer archivo: " + ex.getMessage());
        }
    }

    private void actualizarProducto() {
        String nombre = txtNombreBuscar.getText().trim();
        String cantidad = txtCantidad.getText().trim();
        String unidad = txtUnidad.getText().trim();
        String stockMinimo = txtStockMinimo.getText().trim();
        String fecha = txtFechaVencimiento.getText().trim();

        if (nombre.isEmpty() || cantidad.isEmpty() || stockMinimo.isEmpty() || fecha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos obligatorios.");
            return;
        }

        try {
            LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fecha inválida. Usa el formato yyyy-MM-dd.");
            return;
        }

        File archivoOriginal = new File("productos.txt");
        File archivoTemporal = new File("productos_temp.txt");

        // ...existing code...
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoOriginal));
             BufferedWriter writer = new BufferedWriter(new FileWriter(archivoTemporal))) {

            String linea;
            boolean modificado = false;
            while ((linea = reader.readLine()) != null) {
                if (linea.contains("Nombre: " + nombre)) {
                    // Reemplazar esta línea
                    String nuevaLinea = "Nombre: " + nombre + " | Tipo: " + tipoEncontrado +
                            " | Cantidad: " + cantidad + " " + unidad +
                            " | Stock Mínimo: " + stockMinimo +
                            " | Vence: " + fecha;
                    writer.write(nuevaLinea);
                    modificado = true;
                } else {
                    writer.write(linea);
                }
                writer.newLine();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el archivo: " + ex.getMessage());
            return;
        }

        // Solo reemplaza el archivo si se modificó algún producto
        if (archivoOriginal.delete() && archivoTemporal.renameTo(archivoOriginal)) {
            if (tablaActualizable != null) {
                tablaActualizable.actualizarTabla(); // ACTUALIZA TABLA
            }
            JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el producto.");
        }

    }

    private String extraerCampo(String linea, String campo) {
        int inicio = linea.indexOf(campo);
        if (inicio == -1) return "";
        int fin = linea.indexOf("|", inicio);
        if (fin == -1) fin = linea.length();
        return linea.substring(inicio + campo.length(), fin).trim();
    }
}
