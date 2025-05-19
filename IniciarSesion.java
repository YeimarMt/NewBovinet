import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;


public class IniciarSesion extends JFrame {
    public IniciarSesion(JFrame parent) {
        parent.dispose(); // Cierra ventana anterior

        setTitle("Iniciar Sesión");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel izquierdo con el formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(242, 242, 242)); // Fondo gris claro

        JPanel card = new JPanel(new GridLayout(6, 2, 10, 10)); // Aumentamos a 6 filas
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        card.setPreferredSize(new Dimension(300, 280));

        // Campos del formulario
        JTextField correo = new JTextField();
        JPasswordField clave = new JPasswordField();

        JButton btnIniciar = new JButton("Iniciar Sesión");
        btnIniciar.setBackground(new Color(46, 139, 87));
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.setFocusPainted(false);
        btnIniciar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnIniciar.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String correoIngresado = correo.getText().trim();
        String claveIngresada = new String(clave.getPassword()).trim();

        boolean encontrado = false;
        String rolUsuario = "";

        try (BufferedReader reader = new BufferedReader(new FileReader("usuarios.txt"))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length == 4) {
                    String correoArchivo = datos[1];
                    String claveArchivo = datos[2];

                    if (correoArchivo.equals(correoIngresado) && claveArchivo.equals(claveIngresada)) {
                        encontrado = true;
                        rolUsuario = datos[3]; // Guardamos el rol (Usuario o Administrador)
                        break;
                    }
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error al leer el archivo de usuarios.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return;
        }

        if (encontrado) {
            JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso como " + rolUsuario, "Bienvenido", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // cerrar esta ventana
            new PanelAnimales(rolUsuario); // Abrir panel según rol
        } else {
            JOptionPane.showMessageDialog(null, "Correo o contraseña incorrectos.", "Acceso denegado", JOptionPane.ERROR_MESSAGE);
        }
    }
});



        JButton btnVolver = new JButton("Volver");
        btnVolver.setBackground(new Color(46, 139, 87));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Acción del botón Volver
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra esta ventana
                Main.main(null); // Reabre la ventana principal
            }
        });

        // Agregar campos al "card"
        card.add(new JLabel("Correo:"));
        card.add(correo);
        card.add(new JLabel("Contraseña:"));
        card.add(clave);
        card.add(new JLabel()); // Espacio vacío
        card.add(btnIniciar);
        card.add(new JLabel()); // Espacio vacío
        card.add(btnVolver);    // Agrega el botón Volver

        formPanel.add(card, new GridBagConstraints());

        // Imagen a la derecha
        ImageIcon icon = new ImageIcon("img/vaquita.jpg");
        Image scaled = icon.getImage().getScaledInstance(300, 400, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaled));

        // Agregar a la ventana
        add(formPanel, BorderLayout.WEST);
        add(imageLabel, BorderLayout.EAST);

        setVisible(true);
    }
}
