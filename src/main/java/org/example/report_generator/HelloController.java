package org.example.report_generator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class HelloController implements Initializable {
    private ResourceBundle bundle;

    @FXML
    private BorderPane chartPane;
    @FXML
    private Button buttonSelectCSV, buttonExportPDF, buttonApplyFilters;
    @FXML
    private TextField textFieldName;
    @FXML
    private ComboBox<String> comboBoxLanguage, comboBoxCity;

    @FXML
    private TableView<Customers> tableView;
    @FXML
    private TableColumn<Customers, String> tableColumnName, tableColumnEmail, tableColumnCity;

    private ObservableList<Customers> data = FXCollections.observableArrayList();


    @FXML
    private Label labelCustomers, labelTotalCustomers;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setLanguage(Locale.getDefault());
        SetLanguageNames();

        comboBoxLanguage.setValue(bundle.getString("cb.language.spanish"));
        comboBoxLanguage.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(bundle.getString("cb.language.spanish"))) {
                setLanguage(Locale.forLanguageTag("es"));
                comboBoxLanguage.setValue(bundle.getString("cb.language.spanish"));
            } else if (newValue.equals(bundle.getString("cb.language.english"))) {
                setLanguage(Locale.forLanguageTag("en"));
                comboBoxLanguage.setValue(bundle.getString("cb.language.english"));
            }
            SetLanguageNames();
        });
    }

    private void SetLanguageNames() {
        comboBoxLanguage.getItems().clear();
        comboBoxLanguage.getItems().addAll(
                bundle.getString("cb.language.spanish"),
                bundle.getString("cb.language.english")
        );
    }

    private void setLanguage(Locale locale){
        bundle = ResourceBundle.getBundle("i18n/Messages", locale);
        comboBoxCity.setPromptText(bundle.getString("cb.comboBoxCity"));

        buttonSelectCSV.setText(bundle.getString("btn.buttonSelectCSV"));
        buttonExportPDF.setText(bundle.getString("btn.buttonExportPDF"));
        buttonApplyFilters.setText(bundle.getString("btn.buttonApplyFilters"));

        textFieldName.setPromptText(bundle.getString("tf.textFieldName"));

        tableColumnName.setText(bundle.getString("tb.tableColumnName"));
        tableColumnEmail.setText(bundle.getString("tb.tableColumnEmail"));
        tableColumnCity.setText(bundle.getString("tb.tableColumnCity"));

        labelCustomers.setText(bundle.getString("lbl.labelCustomers"));

    }

    
    @FXML
    public void onActionChooser(ActionEvent event) throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo CSV");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV (*.csv)", "*.csv")
        );

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        File archivo = fileChooser.showOpenDialog(stage);

        if (archivo != null) {
            System.out.println("Seleccionado: " + archivo.getAbsolutePath());
            loadCSV(archivo);
            int total = tableView.getItems().size();
            labelTotalCustomers.setText(String.valueOf(total));
            crearGrafico();
            llenarComboBoxCiudades();
        }
    }

    public void loadCSV (File archivo) throws IOException {
        data.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] campos = line.split(",");
                if (campos.length >= 4) {
                    int id = Integer.parseInt(campos[0].trim());
                    String nombre = campos[1].trim();
                    String email = campos[2].trim();
                    String ciudad = campos[3].trim();
                    data.add(new Customers(id, nombre, email, ciudad));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnCity.setCellValueFactory(new PropertyValueFactory<>("ciudad"));


        tableView.setItems(data);
    }

    @FXML
    public void crearGrafico() {
        if (data.isEmpty()) return;

        DefaultPieDataset dataset = new DefaultPieDataset();
        data.stream()
                .collect(Collectors.groupingBy(Customers::getCiudad, Collectors.counting()))
                .forEach((ciudad, count) -> dataset.setValue(ciudad, count));

        JFreeChart pieChart = ChartFactory.createPieChart(
                "",
                dataset,
                true,
                true,
                false
        );

        ChartPanel chartPanel = new ChartPanel(pieChart);
        SwingNode swingNode = new SwingNode();
        swingNode.setContent(chartPanel);

        chartPane.setCenter(swingNode);
    }


    @FXML
    private void onExportPDF(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            exportarAPDF(file);
        }
    }

    @FXML
    private void exportarAPDF(File archivo) {
        try (PDDocument document = new PDDocument()) {

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);


            WritableImage fxImage = chartPane.snapshot(new SnapshotParameters(), null);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(fxImage, null);


            org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject pdImage =
                    LosslessFactory.createFromImage(document, bufferedImage);


            float scale = 0.5f;
            contentStream.drawImage(pdImage, 50, 400, pdImage.getWidth() * scale, pdImage.getHeight() * scale);

            WritableImage tableImage = tableView.snapshot(new SnapshotParameters(), null);
            BufferedImage bufferedTable = SwingFXUtils.fromFXImage(tableImage, null);
            org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject pdTable =
                    LosslessFactory.createFromImage(document, bufferedTable);

            contentStream.drawImage(pdTable, 50, 100, pdTable.getWidth() * scale, pdTable.getHeight() * scale);

            contentStream.close();

            document.save(archivo);
            System.out.println("PDF exportado a: " + archivo.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void llenarComboBoxCiudades() {
        Set<String> ciudades = data.stream()
                .map(Customers::getCiudad)
                .collect(Collectors.toSet());

        comboBoxCity.getItems().clear();
        comboBoxCity.getItems().add("Todas");
        comboBoxCity.getItems().addAll(ciudades);

        comboBoxCity.setValue("Todas");
    }

    @FXML
    private void applyFilter() {
        String ciudadSeleccionada = comboBoxCity.getValue();
        String nombreBusqueda = textFieldName.getText().trim().toLowerCase();

        ObservableList<Customers> filtrados = data.stream()
                .filter(c -> {
                    boolean coincideCiudad = ciudadSeleccionada.equals("Todas") || c.getCiudad().equals(ciudadSeleccionada);
                    boolean coincideNombre = nombreBusqueda.isEmpty() || c.getNombre().toLowerCase().contains(nombreBusqueda);
                    return coincideCiudad && coincideNombre;
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        tableView.setItems(filtrados);
        labelTotalCustomers.setText(String.valueOf(filtrados.size()));
    }

}
