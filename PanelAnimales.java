import java.awt.*;
import javax.swing.*;

public class PanelAnimales extends JFrame {
    public PanelAnimales(String rol) {
        setTitle("Gestión de Animales");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(rol.equals("Administrador") ? 6 : 1, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panel.setBackground(new Color(245, 245, 245));

        // Botón común
        JButton btnVer = createStyledButton("Ver Animales");
        btnVer.addActionListener(e -> new VerAnimales(rol));
        panel.add(btnVer);

        // Botones exclusivos para administrador
        if (rol.equals("Administrador")) {
            JButton btnAgregar = createStyledButton("Agregar Animal");
            btnAgregar.addActionListener(e -> new AgregarAnimal());
            panel.add(btnAgregar);

            JButton btnModificar = createStyledButton("Modificar Animal");
            btnModificar.addActionListener(e -> new ActualizarAnimal());
            panel.add(btnModificar);

            JButton btnActualizar = createStyledButton("Actualizar Lista");
            panel.add(btnActualizar);

            // NUEVO BOTÓN DE INVENTARIO
            JButton btnInventario = createStyledButton("Inventario");
            btnInventario.addActionListener(e -> new InventarioPanel());
            panel.add(btnInventario);
        }

        add(panel);
        setVisible(true);
    }

    // Método para aplicar estilos a cada botón
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(46, 139, 87));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return button;
    }
}
