import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;


public class CrearCuenta extends JFrame {
    public CrearCuenta(JFrame parent) {
        parent.dispose(); // Cierra ventana anterior

        setTitle("Crear Cuenta");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel izquierdo con el formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(242, 242, 242)); // Fondo gris claro

        JPanel card = new JPanel(new GridLayout(6, 2, 10, 10)); // Aumentado para incluir botón Volver
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        card.setPreferredSize(new Dimension(300, 300));

        // Campos del formulario
        JTextField nombre = new JTextField();
        JTextField correo = new JTextField();
        JPasswordField clave = new JPasswordField();
        String[] roles = {"Usuario", "Administrador"};
        JComboBox<String> rol = new JComboBox<>(roles);

        JButton btnRegistrar = new JButton("Crear Cuenta");
        btnRegistrar.setBackground(new Color(46, 139, 87));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnRegistrar.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String nombreUsuario = nombre.getText().trim();
            String correoUsuario = correo.getText().trim();
            String claveUsuario = new String(clave.getPassword()).trim();
            String rolSeleccionado = (String) rol.getSelectedItem();

            // Validación básica
            if (nombreUsuario.isEmpty() || correoUsuario.isEmpty() || claveUsuario.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Guardar en el archivo
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("usuarios.txt", true))) {
                writer.write(nombreUsuario + ";" + correoUsuario + ";" + claveUsuario + ";" + rolSeleccionado);
                writer.newLine();
                JOptionPane.showMessageDialog(null, "Usuario registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                // Opcional: Limpiar los campos
                nombre.setText("");
                correo.setText("");
                clave.setText("");
                rol.setSelectedIndex(0);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error al guardar los datos.", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    });


        JButton btnVolver = new JButton("Volver");
        btnVolver.setBackground(new Color(46, 139, 87)); // verde
        btnVolver.setForeground(Color.white);
        btnVolver.setFocusPainted(false);
        btnVolver.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Acción del botón Volver
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra esta ventana
                Main.main(null); // Vuelve a abrir la ventana principal
            }
        });

        // Agregar campos al "card"
        card.add(new JLabel("Nombre de Usuario:"));
        card.add(nombre);
        card.add(new JLabel("Correo:"));
        card.add(correo);
        card.add(new JLabel("Contraseña:"));
        card.add(clave);
        card.add(new JLabel("Rol:"));
        card.add(rol);
        card.add(new JLabel());        // Espacio vacío
        card.add(btnRegistrar);
        card.add(new JLabel());        // Espacio vacío
        card.add(btnVolver);           // Botón Volver

        formPanel.add(card, new GridBagConstraints());

        // Imagen a la derecha
        ImageIcon icon = new ImageIcon("img/vaca.jpeg"); // Asegúrate de tener esta imagen
        Image scaled = icon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaled));

        // Agregar a la ventana
        add(formPanel, BorderLayout.WEST);
        add(imageLabel, BorderLayout.EAST);

        setVisible(true);
    }
}
