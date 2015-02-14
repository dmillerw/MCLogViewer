package dmillerw.log.gui;

import com.sun.javafx.application.LauncherImpl;
import dmillerw.log.parse.Log;
import dmillerw.log.parse.LogParser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Viewer extends Application {

    public static Log log;

    private static Stage ownerStage;

    private static File lastFile;

    public static void load(boolean reload) {
        if (reload && lastFile != null) {
            Viewer.log = LogParser.parseLog(lastFile);
        } else {
//            FileChooser fileChooser = new FileChooser();
//            fileChooser.setTitle("Open Resource File");
//            fileChooser.getExtensionFilters().addAll(
//                    new FileChooser.ExtensionFilter("Logs", "*.log")
//            );
//            File selectedFile = fileChooser.showOpenDialog(ownerStage);
            File selectedFile = new File("C:/Users/dylan/Documents/MultiMC/instances/1.7.10/minecraft/logs", "fml-client-latest.log");
            if (selectedFile != null) {
                lastFile = selectedFile;
                Viewer.log = LogParser.parseLog(selectedFile);
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Viewer.ownerStage = primaryStage;

        load(false);

        Parent root = FXMLLoader.load(getClass().getResource("viewer.fxml"));
        primaryStage.setTitle("LogViewer");
        primaryStage.setScene(new Scene(root, 640, 480));
        primaryStage.show();
    }

    public static void main(String[] args) {
        LauncherImpl.launchApplication(Viewer.class, args);
    }
}
