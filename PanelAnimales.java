import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;

public class PanelAnimales extends JFrame {
    private static final Color COLOR_FONDO = new Color(240, 245, 240);
    private static final Color COLOR_BOTON = new Color(76, 175, 80);
    private static final Color COLOR_BOTON_HOVER = new Color(56, 142, 60);
    private static final Color COLOR_TEXTO = new Color(33, 33, 33);
    private static final Color COLOR_NAVBAR = new Color(108, 117, 125);
    private static final int NAVBAR_WIDTH = 250;
    private static final int MIN_WINDOW_WIDTH = 800;
    private static final int MIN_WINDOW_HEIGHT = 500;

    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;

        public RoundedBorder(int radius) {
            this.radius = radius;
            this.color = new Color(0, 0, 0, 30);
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius, radius, radius, radius);
        }
    }

    public PanelAnimales(String rol) {
        setTitle("Panel de control");
        setSize(1024, 600);
        setMinimumSize(new Dimension(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(COLOR_FONDO);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_FONDO);

        // Navbar
        JPanel navbar = new JPanel();
        navbar.setLayout(new BoxLayout(navbar, BoxLayout.Y_AXIS));
        navbar.setBackground(COLOR_NAVBAR);
        navbar.setPreferredSize(new Dimension(NAVBAR_WIDTH, getHeight()));
        navbar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(120, 129, 137)));

        JLabel logoLabel = new JLabel("Bovinet");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));
        navbar.add(logoLabel);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(COLOR_NAVBAR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 15, 10, 15);

        int row = 0;

        JButton btnVer = createStyledButton("Ver Animales");
        btnVer.addActionListener(e -> new VerAnimales(rol));
        gbc.gridy = row++;
        buttonPanel.add(btnVer, gbc);

        JButton btnControlesPendientes = createStyledButton("Controles Sanitarios Pendientes");
        btnControlesPendientes.addActionListener(e -> new ControlesPendientes(rol));
        gbc.gridy = row++;
        buttonPanel.add(btnControlesPendientes, gbc);

        if (rol.equals("Administrador")) {
            JButton btnAgregar = createStyledButton("Agregar Animal");
            btnAgregar.addActionListener(e -> new AgregarAnimal());
            gbc.gridy = row++;
            buttonPanel.add(btnAgregar, gbc);

            JButton btnModificar = createStyledButton("Modificar Animal");
            btnModificar.addActionListener(e -> new ActualizarAnimal());
            gbc.gridy = row++;
            buttonPanel.add(btnModificar, gbc);

            JButton btnRegistrarEvento = createStyledButton("Registrar Evento");
            btnRegistrarEvento.addActionListener(e -> new RegistrarEvento());
            gbc.gridy = row++;
            buttonPanel.add(btnRegistrarEvento, gbc);

            JButton btnVerHojas = createStyledButton("Ver Hojas de Vida");
            btnVerHojas.addActionListener(e -> new VerHojasDeVida());
            gbc.gridy = row++;
            buttonPanel.add(btnVerHojas, gbc);

            JButton btnDarBaja = createStyledButton("Dar de Baja Animal");
            btnDarBaja.addActionListener(e -> new DarDeBajaAnimal());
            gbc.gridy = row++;
            buttonPanel.add(btnDarBaja, gbc);

            JButton btnActualizar = createStyledButton("Actualizar Lista");
            gbc.gridy = row++;
            buttonPanel.add(btnActualizar, gbc);

            JButton btnInventario = createStyledButton("Inventario");
            btnInventario.addActionListener(e -> new InventarioPanel());
            gbc.gridy = row++;
            buttonPanel.add(btnInventario, gbc);

            JButton btnReportes = createStyledButton("Reportes");
            btnReportes.addActionListener(e -> new ReporteProductos());
            gbc.gridy = row++;
            buttonPanel.add(btnReportes, gbc);
        }

        navbar.add(Box.createVerticalGlue());
        navbar.add(buttonPanel);
        navbar.add(Box.createVerticalGlue());

        // Panel de bienvenida
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(COLOR_FONDO);
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setBackground(COLOR_FONDO);

        GridBagConstraints welcomeGbc = new GridBagConstraints();

        JLabel welcomeLabel = new JLabel("Bienvenido a Bovinet");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeLabel.setForeground(COLOR_TEXTO);

        JLabel subtitleLabel = new JLabel("Sistema de Gestión Ganadera");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(100, 100, 100));

        welcomeGbc.gridx = 0;
        welcomeGbc.gridy = 0;
        welcomeGbc.insets = new Insets(0, 0, 10, 0);
        welcomePanel.add(welcomeLabel, welcomeGbc);

        welcomeGbc.gridy = 1;
        welcomePanel.add(subtitleLabel, welcomeGbc);

        contentPanel.add(welcomePanel, BorderLayout.CENTER);

        // Ensamblar panel principal
        mainPanel.add(navbar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateLayout(getWidth(), getHeight());
            }
        });

        add(mainPanel);
        setVisible(true);
    }

    private void updateLayout(int width, int height) {
        float scale = Math.min(width / 1024f, height / 600f);
        int baseFontSize = 14;
        int newFontSize = Math.max(12, Math.round(baseFontSize * scale));

        for (Component comp : getComponents()) {
            if (comp instanceof JButton) {
                comp.setFont(new Font("Segoe UI", Font.BOLD, newFontSize));
            }
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed() || getModel().isRollover()) {
                    g2.setColor(COLOR_BOTON_HOVER);
                } else {
                    g2.setColor(COLOR_BOTON);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
                g2.dispose();
            }
        };

        button.setBackground(COLOR_BOTON);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(new RoundedBorder(15));
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(NAVBAR_WIDTH - 30, 45));
        button.setMaximumSize(new Dimension(NAVBAR_WIDTH - 30, 45));
        button.setMinimumSize(new Dimension(NAVBAR_WIDTH - 30, 45));

        return button;
    }
}
