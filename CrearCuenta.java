import java.awt.*;
import javax.swing.*;

public class CrearCuenta extends JFrame {
    public CrearCuenta(JFrame parent) {
        parent.dispose(); // Cierra ventana anterior

        setTitle("Crear Cuenta");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel izquierdo con el formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(new Color(242, 242, 242)); // Fondo gris claro

        JPanel card = new JPanel();
        card.setLayout(new GridLayout(5, 2, 10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        card.setPreferredSize(new Dimension(300, 250));

        // Campos del formulario
        JTextField nombre = new JTextField();
        JTextField correo = new JTextField();
        JPasswordField clave = new JPasswordField();
        String[] roles = {"Usuario", "Administrador"};
        JComboBox<String> rol = new JComboBox<>(roles);

        JButton btnRegistrar = new JButton("Crear Cuenta");
        btnRegistrar.setBackground(new Color(46, 139, 87)); // Verde
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Agregar campos al "card"
        card.add(new JLabel("Nombre de Usuario:"));
        card.add(nombre);
        card.add(new JLabel("Correo:"));
        card.add(correo);
        card.add(new JLabel("Contraseña:"));
        card.add(clave);
        card.add(new JLabel("Rol:"));
        card.add(rol);
        card.add(new JLabel()); // Espacio vacío
        card.add(btnRegistrar);

        formPanel.add(card, new GridBagConstraints());

        // Imagen a la derecha
        ImageIcon icon = new ImageIcon("img/fondo.jpg"); // cambia la imagen según tu archivo
        Image scaled = icon.getImage().getScaledInstance(350, 400, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaled));

        // Agregar a la ventana
        add(formPanel, BorderLayout.WEST);
        add(imageLabel, BorderLayout.EAST);

        setVisible(true);
    }
}
