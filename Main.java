import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("BoviNet - Lo mejor de la ganaderia");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1024, 600));
        frame.setLocationRelativeTo(null);

        // Panel principal con gradiente
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(240, 248, 255);
                Color color2 = new Color(230, 240, 230);
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };

        // Barra de navegación mejorada
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(new Color(34, 34, 34));
        navBar.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Logo mejorado
        ImageIcon icon = new ImageIcon("img/logo.png");
        Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        // Panel de botones mejorado
        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightButtons.setOpaque(false);

        // Crear botones con bordes redondeados y estilo verde
        RoundedButton btnInicioSesion = createElegantButton("Inicio de Sesión");
        RoundedButton btnCrearCuenta = createElegantButton("Crear Cuenta");
        RoundedButton btnSalir = createElegantButton("Salir");

        btnSalir.addActionListener(e -> System.exit(0));
        btnInicioSesion.addActionListener(e -> {
            new IniciarSesion(frame); // pasa la ventana actual
        });

        btnCrearCuenta.addActionListener(e -> {
            new CrearCuenta(frame); // pasa la ventana actual
        });

        // Agregar botones al panel derecho
        rightButtons.add(btnInicioSesion);
        rightButtons.add(btnCrearCuenta);
        rightButtons.add(btnSalir);

        // Contenido principal mejorado
        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        
        // Panel de bienvenida con diseño moderno
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel welcomeLabel = new JLabel("Bienvenido a BoviNet");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        welcomeLabel.setForeground(new Color(34, 34, 34));
        
        JLabel subtitleLabel = new JLabel("La mejor plataforma para la gestión ganadera");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(70, 70, 70));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        welcomePanel.add(welcomeLabel, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        welcomePanel.add(subtitleLabel, gbc);

        // Agregar componentes al frame
        navBar.add(logoLabel, BorderLayout.WEST);
        navBar.add(rightButtons, BorderLayout.EAST);
        content.add(welcomePanel, BorderLayout.CENTER);
        
        mainPanel.add(navBar, BorderLayout.NORTH);
        mainPanel.add(content, BorderLayout.CENTER);
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static RoundedButton createElegantButton(String text) {
        RoundedButton button = new RoundedButton(text, 20);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(46, 139, 87));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 100, 0), 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0, 100, 0));
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 80, 0), 2),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(46, 139, 87));
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 100, 0), 2),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(new Color(0, 80, 0));
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 60, 0), 2),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(new Color(0, 100, 0));
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 80, 0), 2),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
            }
        });
        return button;
    }
}

class RoundedButton extends JButton {
    private int radius;
    private Color hoverColor = new Color(0, 100, 0);
    private Color normalColor = new Color(46, 139, 87);
    private Color pressedColor = new Color(0, 80, 0);

    public RoundedButton(String text, int radius) {
        super(text);
        this.radius = radius;
        setOpaque(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        if (getModel().isPressed()) {
            g2.setColor(pressedColor);
        } else if (getModel().isRollover()) {
            g2.setColor(hoverColor);
        } else {
            g2.setColor(getBackground());
        }
        
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        
        // Efecto de brillo en la parte superior
        GradientPaint gp = new GradientPaint(
            0, 0, new Color(255, 255, 255, 50),
            0, getHeight()/2, new Color(255, 255, 255, 0)
        );
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, getWidth(), getHeight()/2, radius, radius);
        
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0, 100, 0));  
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, radius, radius);
    }

    @Override
    public boolean contains(int x, int y) {
        return new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius).contains(x, y);
    }
}