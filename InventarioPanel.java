import java.awt.*;
import javax.swing.*;

public class InventarioPanel extends JFrame {
    public InventarioPanel() {
        setTitle("Gestión de Inventario - BoviNet");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));

        JLabel title = new JLabel("Módulo de Inventario", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(34, 139, 34));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 30, 10));
        cardsPanel.setBackground(new Color(240, 248, 255));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        cardsPanel.add(createCard("Agregar Producto", "img/agregar.png", () -> {
            new AgregarProducto();
        }));


        cardsPanel.add(createCard("ver Alimentos", "img/alimento.png", () -> {
            new VerAlimentos();
        }));

        cardsPanel.add(createCard("Ver Medicamentos", "img/medicamentos.jpg", () -> {
            new VerMedicamentos();
        }));

        cardsPanel.add(createCard("Ver Productos Vencidos", "img/vencidos.jpg", () -> {
            new VerProductosVencidos();
        }));

        cardsPanel.add(createCard("Modificar Productos", "img/modificar.png", () -> {
            new ModificarProducto(null); // null para usar el diálogo de selección
        }));

        mainPanel.add(cardsPanel, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);
    }

    private JPanel createCard(String title, String iconPath, Runnable onClick) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setOpaque(true);
        card.setMaximumSize(new Dimension(200, 250));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setToolTipText(title);

        // Icono
        ImageIcon icon = new ImageIcon(iconPath);
        Image scaledImage = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(iconLabel);

        // Texto
        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(new Color(34, 34, 34));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        card.add(label);

        // Botón
        JButton btn = new JButton("Abrir");
        btn.setBackground(new Color(46, 139, 87));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> onClick.run());
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(btn);

        return card;
    }
}
