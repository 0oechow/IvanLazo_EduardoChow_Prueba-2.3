import org.junit.Test;
import static org.junit.Assert.*;

public class BodegaTest {
    
    @Test
    public void testAgregarStock() {
        // Prueba que agregarStock suma correctamente
        Inventario inventario = new Inventario();
        inventario.agregarProducto("Arroz", 7);
        inventario.agregarStock("Arroz", 5);
        
        Producto p = inventario.buscarProducto("Arroz");
        assertNotNull(p);
        assertEquals(12, p.getStock());
    }
    
    @Test
    public void testRestarStockExitoso() {
        // Prueba que restarStock descuenta correctamente
        Inventario inventario = new Inventario();
        inventario.agregarProducto("Fideos", 10);
        int resultado = inventario.restarStock("Fideos", 3);
        
        assertEquals(7, resultado);
        
        Producto p = inventario.buscarProducto("Fideos");
        assertNotNull(p);
        assertEquals(7, p.getStock());
    }
    
    @Test
    public void testRestarStockSinNegativos() {
        // Prueba que restarStock no permite dejar el stock en negativo
        Inventario inventario = new Inventario();
        inventario.agregarProducto("Aceite", 10);
        int resultado = inventario.restarStock("Aceite", 11);
        
        assertEquals(-1, resultado); // Debe fallar porque no hay suficiente stock
        
        Producto p = inventario.buscarProducto("Aceite");
        assertNotNull(p);
        assertEquals(10, p.getStock()); // El stock debe permanecer igual
    }
    
    @Test
    public void testAgregarStockProductoNoExistente() {
        //  agregar stock a producto no existente
        Inventario inventario = new Inventario();
        boolean resultado = inventario.agregarStock("Leche", 5);
        
        assertFalse(resultado); // Debe fallar porque el producto no existe
    }
    
    @Test
    public void testProductoDuplicado() {
        //  no se pueden crear productos duplicados
        Inventario inventario = new Inventario();
        boolean primerProducto = inventario.agregarProducto("Azúcar", 10);
        boolean segundoProducto = inventario.agregarProducto("Azúcar", 5);
        
        assertTrue(primerProducto);
        assertFalse(segundoProducto); // El segundo debe fallar
    }

}
