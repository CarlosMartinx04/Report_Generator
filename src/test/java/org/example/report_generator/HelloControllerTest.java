package org.example.report_generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testng.FileAssert;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class HelloControllerTest {

    File archivo = new File("/home/alumno/IdeaProjects/Report_Generator/clientes.csv");

    @Test
    public void testLoadCSV() {
        HelloController h = new HelloController();
        h.loadCSV(archivo);
        h.getData();
        //Aquí compruebo el contenido de archivo//
    }

    @Test
    void testCrearGrafico() {
    }
}