import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Scanner;

class LoginWindow extends JFrame {

    private JTextField userField;
    private JPasswordField passField;

    public LoginWindow() {
        setTitle("Login");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Usuario:"));
        userField = new JTextField();
        panel.add(userField);

        panel.add(new JLabel("Contraseña:"));
        passField = new JPasswordField();
        panel.add(passField);

        JButton loginBtn = new JButton("Ingresar");
        loginBtn.addActionListener(e -> login());
        panel.add(loginBtn);

        add(panel);
        setVisible(true);
    }

    private void login() {
        String user = userField.getText().trim();
        if (!user.isEmpty()) {
            new MainWindow(user);
        } else {
            JOptionPane.showMessageDialog(this, "Ingrese un usuario");
        }
    }
}

class MainWindow extends JFrame {

    private DefaultListModel<String> model;
    private JList<String> ProductoJList;
    private MainController controller;

    public MainWindow(String usuario) {
        setTitle("Gestor - Usuario: " + usuario);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Inicializar controlador
        controller = new MainController(this);
        
        model = new DefaultListModel<>();
        ProductoJList = new JList<>(model);

        JPanel panelBotones = new JPanel(new GridLayout(1, 5, 5, 5));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        panelBotones.add(controller.crearBoton("Crear Producto", e -> controller.crearProducto()));
        panelBotones.add(controller.crearBoton("Agregar Stock", e -> controller.agregarStock()));
        panelBotones.add(controller.crearBoton("Restar Stock", e -> controller.restarStock()));
        panelBotones.add(controller.crearBoton("Reportes", e -> controller.generarReportes()));
        panelBotones.add(controller.crearBoton("Limpiar", e -> controller.limpiar()));

        add(new JScrollPane(ProductoJList), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void actualizarVista() {
        model.clear();
        for (Producto producto : controller.getInventarioTotal()) {
            model.addElement(producto.toString());
        }
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

}
