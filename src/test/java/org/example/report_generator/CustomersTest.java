package org.example.report_generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CustomersTest {
    @Test
    public void CustomerConstructor(){
        //1ª Preparar las variables.

        int id = 7;
        String nombre = "Carlos";
        String email = "carlos@email.com";
        String ciudad = "Calahorra";


        // 2º Probar la función o funciones
        Customers c = new Customers(id, nombre, email, ciudad);

        // 3º Probar el resultado:

        Assertions.assertEquals(7, c.getId());
        Assertions.assertEquals("Carlos", c.getNombre());
        Assertions.assertEquals("carlos@email.com", c.getEmail());
        Assertions.assertEquals("Calahorra", c.getCiudad());
    }
}
