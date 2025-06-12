import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("BoviNet - Estilo Elegante");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 450);
        frame.setLocationRelativeTo(null);

        // Barra de navegación con BorderLayout
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(Color.DARK_GRAY);

        // Cargar el logo desde carpeta "img"
        ImageIcon icon = new ImageIcon("img/logo.png");
        Image scaledImage = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0)); // margen izquierdo

        // Crear panel para los botones alineados a la derecha
        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        rightButtons.setOpaque(false); // fondo transparente

        // Crear botones con bordes redondeados y estilo verde
        RoundedButton btnInventario = createElegantButton("Inventario");
        RoundedButton btnAnimales = createElegantButton("Animales");
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
        rightButtons.add(btnInventario);
        rightButtons.add(btnAnimales);
        rightButtons.add(btnInicioSesion);
        rightButtons.add(btnCrearCuenta);
        rightButtons.add(btnSalir);

        // Agregar logo y botones al navBar
        navBar.add(logoLabel, BorderLayout.WEST);
        navBar.add(rightButtons, BorderLayout.EAST);

        // Panel de contenido principal
        JPanel content = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Bienvenido a BoviNet", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        label.setForeground(Color.black);
        content.setBackground(Color.WHITE);
        content.add(label, BorderLayout.CENTER);

        // Layout general
        frame.setLayout(new BorderLayout());
        frame.add(navBar, BorderLayout.NORTH);
        frame.add(content, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // Crea botones elegantes con efecto hover y color verde
    private static RoundedButton createElegantButton(String text) {
        RoundedButton button = new RoundedButton(text, 30); // radio 30 píxeles
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0, 127, 0)); // verde oscuro (hover)
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(46, 139, 87)); // verde
            }
        });
        return button;
    }
}

// Botón personalizado con bordes redondeados
class RoundedButton extends JButton {
    private int radius;

    public RoundedButton(String text, int radius) {
        super(text);
        this.radius = radius;
        setOpaque(false);
        setFocusPainted(false);
        setFont(new Font("Segoe UI", Font.BOLD, 16));
        setBackground(new Color(46, 139, 87)); // verde
        setForeground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(44, 44, 44)); // borde gris oscuro
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, radius, radius);
    }

    @Override
    public boolean contains(int x, int y) {
        return new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius).contains(x, y);
    }
}