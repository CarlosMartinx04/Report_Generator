module org.example.report_generator {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.desktop;
    requires org.jfree.jfreechart;
    requires javafx.swing;
    requires org.apache.pdfbox;

    opens org.example.report_generator to javafx.fxml;
    exports org.example.report_generator;
}