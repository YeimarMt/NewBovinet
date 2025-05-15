import java.awt.*;
import javax.swing.*;

public class IniciarSesion extends JFrame {
    public IniciarSesion(JFrame parent) {
        parent.dispose(); // cerrar la ventana principal

        setTitle("Inicio de Sesión");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        JTextField usuario = new JTextField();
        JPasswordField clave = new JPasswordField();

        add(new JLabel("Usuario:"));
        add(usuario);
        add(new JLabel("Contraseña:"));
        add(clave);

        JButton btnIngresar = new JButton("Ingresar");
        JButton btnCancelar = new JButton("Cancelar");
        add(btnIngresar);
        add(btnCancelar);

        btnCancelar.addActionListener(e -> dispose());

        setVisible(true);
    }
}
