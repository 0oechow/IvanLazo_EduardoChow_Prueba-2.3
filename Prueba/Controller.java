import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Scanner;

class Producto {
    private String nombre;
    private int stock;

    public Producto(String nombre, int stock) {
        this.nombre = nombre;
        this.stock = stock;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void agregarStock(int cantidad) {
        this.stock += cantidad;
    }

    public boolean restarStock(int cantidad) {
        if (cantidad <= this.stock) {
            this.stock -= cantidad;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return nombre + " - Stock: " + stock;
    }
}

class Inventario {
    private ArrayList<Producto> productos;

    public Inventario() {
        this.productos = new ArrayList<>();
    }

    public boolean agregarProducto(String nombre, int stockInicial) {
        // Verificar si el producto ya existe
        for (Producto p : productos) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return false; // Producto ya existe
            }
        }
        
        productos.add(new Producto(nombre, stockInicial));
        return true;
    }

    public Producto buscarProducto(String nombre) {
        for (Producto p : productos) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }

    public boolean agregarStock(String nombre, int cantidad) {
        Producto producto = buscarProducto(nombre);
        if (producto != null && cantidad > 0) {
            producto.agregarStock(cantidad);
            return true;
        }
        return false;
    }

    public int restarStock(String nombre, int cantidad) {
        Producto producto = buscarProducto(nombre);
        if (producto != null && cantidad > 0) {
            if (producto.restarStock(cantidad)) {
                return producto.getStock(); // Retorna el nuevo stock
            } else {
                return -1; // No hay suficiente stock
            }
        }
        return -2; // Producto no encontrado o cantidad inválida
    }

    public boolean eliminarProducto(String nombre) {
        Producto producto = buscarProducto(nombre);
        if (producto != null) {
            productos.remove(producto);
            return true;
        }
        return false;
    }

    public ArrayList<Producto> getProductos() {
        return new ArrayList<>(productos);
    }

    public void limpiar() {
        productos.clear();
    }

    public boolean estaVacio() {
        return productos.isEmpty();
    }

    public ArrayList<Producto> getStockCritico(int limite) {
        ArrayList<Producto> criticos = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getStock() < limite) {
                criticos.add(p);
            }
        }
        return criticos;
    }
}

class MainController {
    private Inventario inventario;
    private MainWindow ventana;

    public MainController(MainWindow ventana) {
        this.inventario = new Inventario();
        this.ventana = ventana;
    }

    // Métodos de negocio
    public boolean crearProducto(String nombre, int stockInicial) {
        return inventario.agregarProducto(nombre, stockInicial);
    }

    public boolean agregarStock(String nombre, int cantidad) {
        return inventario.agregarStock(nombre, cantidad);
    }

    public int restarStock(String nombre, int cantidad) {
        return inventario.restarStock(nombre, cantidad);
    }

    public boolean eliminarProducto(String nombre) {
        return inventario.eliminarProducto(nombre);
    }

    public void limpiarInventario() {
        inventario.limpiar();
    }

    public ArrayList<Producto> getInventarioTotal() {
        return inventario.getProductos();
    }

    public ArrayList<Producto> getStockCritico(int limite) {
        return inventario.getStockCritico(limite);
    }

    public boolean estaVacio() {
        return inventario.estaVacio();
    }

    // Métodos de interfaz movidos desde Ventana
    public JButton crearBoton(String texto, ActionListener listener) {
        JButton btn = new JButton(texto);
        btn.addActionListener(listener);
        return btn;
    }

    // Caso de Uso para Gestionar productos (Boton crear)
    public void crearProducto() {
        Scanner texto = new Scanner(System.in);
        System.out.println("Nombre del nuevo producto: ");
        String item = texto.nextLine();

        if (item != null && !item.trim().isEmpty()) {
            Scanner cantidad = new Scanner(System.in);
            while (true) {
                System.out.println("Stock inicial: ");
                String stock = cantidad.nextLine();
                try {
                    int valor = Integer.parseInt(stock);
                    if (valor >= 0) {
                        boolean exito = crearProducto(item.trim(), valor);
                        if (exito) {
                            System.out.println("Producto creado: " + item + " con stock: " + stock);
                            ventana.actualizarVista();
                        } else {
                            System.err.println("El producto ya existe: " + item);
                        }
                        break;
                    } else {
                        System.err.println("El stock no puede ser negativo");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Cantidad no válida");
                }
            }
        } else {
            System.err.println("Nombre de producto vacío");
        }
    }

    // Caso de Uso para registrar entradas (Boton Agregar)
    public void agregarStock() {
        if (estaVacio()) {
            System.err.println("No hay productos en el inventario");
            return;
        }
        
        Scanner texto = new Scanner(System.in);
        System.out.println("Producto al que agregar stock: ");
        String item = texto.nextLine().trim();
        
        if (item.isEmpty()) {
            System.err.println("Nombre de producto vacío");
            return;
        }
        
        Scanner cantidad = new Scanner(System.in);
        while (true) {
            System.out.println("Cantidad a agregar: ");
            String stock = cantidad.nextLine();
            try {
                int valor = Integer.parseInt(stock);
                if (valor > 0) {
                    boolean exito = agregarStock(item, valor);
                    if (exito) {
                        System.out.println("Stock agregado exitosamente a: " + item);
                        ventana.actualizarVista();
                    } else {
                        System.err.println("Producto no encontrado: " + item);
                    }
                    break;
                } else {
                    System.err.println("La cantidad debe ser mayor a 0");
                }
            } catch (NumberFormatException e) {
                System.err.println("Cantidad no válida");
            }
        }
    }

    // Caso de Uso para Restar Stock (Boton Restar)
    public void restarStock() {
        if (estaVacio()) {
            System.err.println("No hay productos en el inventario");
            return;
        }
        
        Scanner texto = new Scanner(System.in);
        System.out.println("Producto al que restar stock: ");
        String item = texto.nextLine().trim();
        
        if (item.isEmpty()) {
            System.err.println("Nombre de producto vacío");
            return;
        }
        
        Scanner cantidad = new Scanner(System.in);
        while (true) {
            System.out.println("Cantidad a restar: ");
            String stock = cantidad.nextLine();
            try {
                int valor = Integer.parseInt(stock);
                if (valor > 0) {
                    int resultado = restarStock(item, valor);
                    if (resultado >= 0) {
                        System.out.println("Stock restado exitosamente. Nuevo stock: " + resultado);
                        ventana.actualizarVista();
                        
                        // Si el stock llega a 0, preguntar si eliminar el producto
                        if (resultado == 0) {
                            System.out.println("¿Eliminar producto " + item + "? (s/n): ");
                            String respuesta = cantidad.nextLine();
                            if (respuesta.equalsIgnoreCase("s")) {
                                eliminarProducto(item);
                                ventana.actualizarVista();
                                System.out.println("Producto eliminado: " + item);
                            }
                        }
                    } else if (resultado == -1) {
                        System.err.println("No hay suficiente stock para " + item);
                    } else {
                        System.err.println("Producto no encontrado: " + item);
                    }
                    break;
                } else {
                    System.err.println("La cantidad debe ser mayor a 0");
                }
            } catch (NumberFormatException e) {
                System.err.println("Cantidad no válida");
            }
        }
    }

    // Caso de Uso para Generar Reportes (Boton Reporte)
    public void generarReportes() {
        if (estaVacio()) {
            ventana.mostrarMensaje("La lista está vacía");
            return;
        }
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== GENERAR REPORTES ===");
        System.out.println("1. Reporte de Inventario Total");
        System.out.println("2. Reporte de Stock Crítico (stock < 5)");
        System.out.println("Seleccione opción: ");
        
        String opcion = scanner.nextLine();
        
        switch (opcion) {
            case "1":
                generarReporteInventarioTotal();
                break;
            case "2":
                generarReporteStockCritico();
                break;
            default:
                System.err.println("Opción no válida");
        }
    }
    
    // Generar Reporte con For each 
    public void generarReporteInventarioTotal() {
        System.out.println("\n=== INVENTARIO TOTAL ===");
        for (Producto producto : getInventarioTotal()) {
            System.out.println("Producto: " + producto.getNombre() + " - Stock: " + producto.getStock());
        }
        System.out.println("Total de productos: " + getInventarioTotal().size());
    }
    
    public void generarReporteStockCritico() {
        System.out.println("\n=== STOCK CRÍTICO (stock < 5) ===");
        ArrayList<Producto> criticos = getStockCritico(5);
        
        if (criticos.isEmpty()) {
            System.out.println("No hay productos con stock crítico");
        } else {
            for (Producto producto : criticos) {
                System.out.println("Producto: " + producto.getNombre() + " - Stock: " + producto.getStock() + " (CRÍTICO)");
            }
            System.out.println("Total de productos con stock crítico: " + criticos.size());
        }
    }

    public void limpiar() {
        limpiarInventario();
        ventana.actualizarVista();
        System.out.println("Inventario limpiado completamente");
    }
}