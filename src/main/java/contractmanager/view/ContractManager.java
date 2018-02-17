package contractmanager.view;

import contractmanager.controller.Controller;
import contractmanager.model.DataModel;
import contractmanager.utility.ConsoleApplication;
import contractmanager.utility.ConsoleWriter;
import cz.zcu.kiv.contractparser.model.ContractType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;


public class ContractManager extends Application {

    final Logger logger = Logger.getLogger(String.valueOf(ContractManager.class));

    public static final ResourceBundle properties = ResourceBundle.getBundle("contractmanager");
    public static final ResourceBundle localization = ResourceBundle.getBundle("contractmanager",
            new Locale("en", "EN"));

    public static Stage stage;
    public static Scene scene;
    public static DataModel dataModel;
    public static ConsoleWriter consoleWriter;

    @Override
    public void start(Stage stage) {

        this.stage = stage;

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(properties.getString("sceneFileName")));
        loader.setResources(localization);
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setTitle(localization.getString("windowTitle"));
        stage.getIcons().add(new Image(properties.getString("icon")));

        scene = new Scene(root);
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        dataModel = new DataModel();

        Controller controller = loader.getController();
        controller.setMainStage(stage);

        stage.show();

        GridPane grid_details = (GridPane) scene.lookup("#grid_details");

        consoleWriter = new ConsoleWriter();


        int row = 3;

        for(Map.Entry<ContractType, Boolean> entry : dataModel.getContractTypes().entrySet()) {
            ContractType contractType = entry.getKey();
            boolean used = entry.getValue();

            if(used) {
                Label lbl_title = new Label();
                lbl_title.setText(contractType.name() + " " + localization.getString("labelContracts") + ": ");
                lbl_title.setFont(Font.font("System", FontWeight.BOLD, 12));

                Label lbl_value = new Label();
                lbl_value.setId("lbl_" + contractType.name());
                lbl_value.setText("0");

                grid_details.addRow(row, lbl_title);
                grid_details.add(lbl_value, 1, row);
                row++;
            }
        }
    }


    /**
     * Main application method. It decides whether to run standard application with 
     *
     * @param args
     */
    public static void main(String[] args) {

        // if there are no arguments run standard application with GUI
        if(args.length == 0){
            launch(args);
        }
        // otherwise run application in console
        else{
            ConsoleApplication.runConsoleApplication(args);
        }
    }
}