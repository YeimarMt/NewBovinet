import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CrearCuenta extends JFrame {
    public CrearCuenta(JFrame parent) {
        parent.dispose();

        setTitle("Crear Cuenta");
        setSize(1024, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(46, 139, 87);
                Color color2 = new Color(34, 139, 34);
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new GridBagLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel glassPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 200));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        glassPanel.setLayout(new GridBagLayout());
        glassPanel.setOpaque(false);
        glassPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        glassPanel.setPreferredSize(new Dimension(500, 500));

        JLabel titleLabel = new JLabel("Crear Cuenta");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(46, 139, 87));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField nombre = createStyledTextField();
        JTextField correo = createStyledTextField();
        JPasswordField clave = createStyledPasswordField();

        String[] roles = {"Usuario", "Administrador"};
        JComboBox<String> rol = new JComboBox<>(roles);
        rol.setMaximumSize(new Dimension(300, 40));
        rol.setPreferredSize(new Dimension(300, 40));
        rol.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rol.setBackground(new Color(255, 255, 255, 230));
        rol.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(46, 139, 87), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JButton btnRegistrar = createStyledButton("Crear Cuenta");
        JButton btnVolver = createStyledButton("Volver");

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreUsuario = nombre.getText().trim();
                String correoUsuario = correo.getText().trim();
                String claveUsuario = new String(clave.getPassword()).trim();
                String rolSeleccionado = (String) rol.getSelectedItem();

                if (nombreUsuario.isEmpty() || correoUsuario.isEmpty() || claveUsuario.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter("usuarios.txt", true))) {
                    writer.write(nombreUsuario + ";" + correoUsuario + ";" + claveUsuario + ";" + rolSeleccionado);
                    writer.newLine();
                    JOptionPane.showMessageDialog(null, "Usuario registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
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

        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Main.main(null);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel nombreLabel = createStyledLabel("Nombre:");
        JLabel correoLabel = createStyledLabel("Correo electrónico:");
        JLabel claveLabel = createStyledLabel("Contraseña:");

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nombreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        correoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        correo.setAlignmentX(Component.CENTER_ALIGNMENT);
        claveLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        clave.setAlignmentX(Component.CENTER_ALIGNMENT);
        rol.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVolver.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(nombreLabel);
        centerPanel.add(nombre);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(correoLabel);
        centerPanel.add(correo);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(claveLabel);
        centerPanel.add(clave);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(rol);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(btnRegistrar);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(btnVolver);
        centerPanel.add(Box.createVerticalGlue());

        glassPanel.removeAll();
        glassPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcGlass = new GridBagConstraints();
        gbcGlass.gridx = 0;
        gbcGlass.gridy = 0;
        gbcGlass.anchor = GridBagConstraints.CENTER;
        gbcGlass.fill = GridBagConstraints.NONE;
        glassPanel.add(centerPanel, gbcGlass);

        formPanel.add(glassPanel);
        mainPanel.add(formPanel);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(20);
        textField.setMaximumSize(new Dimension(300, 40));
        textField.setPreferredSize(new Dimension(300, 40));
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(46, 139, 87), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        textField.setBackground(new Color(255, 255, 255, 230));
        return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(new Dimension(300, 40));
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(46, 139, 87), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        passwordField.setBackground(new Color(255, 255, 255, 230));
        return passwordField;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(300, 40));
        button.setPreferredSize(new Dimension(300, 40));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(46, 139, 87));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(34, 139, 34));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(46, 139, 87));
            }
        });
        return button;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(0, 100, 0));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setBorder(new EmptyBorder(0, 10, 0, 0));
        label.setMaximumSize(new Dimension(300, 20));
        label.setPreferredSize(new Dimension(300, 20));
        return label;
    }
}