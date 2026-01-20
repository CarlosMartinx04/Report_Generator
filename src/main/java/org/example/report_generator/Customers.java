package org.example.report_generator;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Customers {
    private final SimpleIntegerProperty id;

    private final SimpleStringProperty nombre;
    private final SimpleStringProperty email;
    private final SimpleStringProperty ciudad;

    public Customers(int id, String nombre, String email, String ciudad) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.email = new SimpleStringProperty(email);
        this.ciudad = new SimpleStringProperty(ciudad);
    }

    public int getId() { return id.get(); }
    public String getNombre() { return nombre.get(); }
    public String getEmail() { return email.get(); }
    public String getCiudad() { return ciudad.get(); }

}
