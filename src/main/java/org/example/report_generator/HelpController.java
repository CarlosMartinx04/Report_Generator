package org.example.report_generator;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

import java.awt.*;
import java.net.URI;
import java.util.ResourceBundle;

public class HelpController {
    @FXML
    Label labelTitleHelp, labelAutor, labelProyect, labelNameProyect;
    @FXML
    Hyperlink hyperlinkText;

    private ResourceBundle bundle;

    /**
     * Recoge el bundle(informacion de lenguajes) de la pantalla principal y la convierte cuando se cree el stage
     * @param bundle
     */
    public void setLenguage(ResourceBundle bundle){
        this.bundle = bundle;

        labelTitleHelp.setText(bundle.getString("lbl.labelTitleHelp"));
        labelAutor.setText(bundle.getString("lbl.labelAutor"));
        labelProyect.setText(bundle.getString("lbl.labelProyect"));
        labelNameProyect.setText(bundle.getString("lbl.labelNameProyect"));
        hyperlinkText.setText(bundle.getString("hpl.hyperlinkText"));
    }

    /**
     * Nos redirige al hipervinculo que esta asociado en esta funcion
     * @param event
     */
    @FXML
    private void onOpenLink(ActionEvent event){
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/CarlosMartinx04/Report_Generator.git"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
