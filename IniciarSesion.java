import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class IniciarSesion extends JFrame {
    public IniciarSesion(JFrame parent) {
        parent.dispose();

        setTitle("Iniciar Sesión");
        setSize(1024, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel principal con fondo degradado
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

        // Panel del formulario con efecto de cristal
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Panel interno con efecto de cristal
        JPanel glassPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 200));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
            }
        };
        glassPanel.setLayout(new GridBagLayout());
        glassPanel.setOpaque(false);
        glassPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        glassPanel.setPreferredSize(new Dimension(350, 400));

        // Título
        JLabel titleLabel = new JLabel("Iniciar Sesión");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(46, 139, 87));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campos del formulario
        JTextField correo = createStyledTextField("Correo electrónico");
        JPasswordField clave = createStyledPasswordField("Contraseña");

        // Botones
        JButton btnIniciar = createStyledButton("Iniciar Sesión");
        JButton btnVolver = createStyledButton("Volver");

        // Acción del botón Iniciar Sesión
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
                                rolUsuario = datos[3];
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
                    dispose();
                    new PanelAnimales(rolUsuario);
                } else {
                    JOptionPane.showMessageDialog(null, "Correo o contraseña incorrectos.", "Acceso denegado", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción del botón Volver
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Main.main(null);
            }
        });

        // Configuración del GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel correoLabel = new JLabel("Correo electrónico:");
        correoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        correoLabel.setForeground(new Color(0, 100, 0));
        correoLabel.setHorizontalAlignment(SwingConstants.LEFT);
        correoLabel.setBorder(new EmptyBorder(0, 5, 0, 0));

        JLabel claveLabel = new JLabel("Contraseña:");
        claveLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        claveLabel.setForeground(new Color(0, 100, 0));
        claveLabel.setHorizontalAlignment(SwingConstants.LEFT);
        claveLabel.setBorder(new EmptyBorder(0, 5, 0, 0));

        // Agregar componentes al panel de cristal
        glassPanel.add(titleLabel, gbc);
        glassPanel.add(Box.createVerticalStrut(10)); // Espacio después del título
        glassPanel.add(correoLabel, gbc);
        glassPanel.add(correo, gbc);
        glassPanel.add(claveLabel, gbc);
        glassPanel.add(clave, gbc);
        glassPanel.add(btnIniciar, gbc);
        glassPanel.add(btnVolver, gbc);

        // Agregar el panel de cristal al panel del formulario
        formPanel.add(glassPanel);
        mainPanel.add(formPanel);

        // Agregar el panel principal a la ventana
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField textField = new JTextField(20);
        textField.setPreferredSize(new Dimension(300, 35));
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(46, 139, 87), 1),
            BorderFactory.createEmptyBorder(3, 8, 3, 8)
        ));
        textField.setBackground(new Color(255, 255, 255, 230));
        return textField;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(300, 35));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(46, 139, 87), 1),
            BorderFactory.createEmptyBorder(3, 8, 3, 8)
        ));
        passwordField.setBackground(new Color(255, 255, 255, 230));
        return passwordField;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(300, 35));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(46, 139, 87));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover
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
}