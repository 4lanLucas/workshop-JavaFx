package alan.prod.projectworkshopjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage){
       try{
           FXMLLoader loader = new FXMLLoader(getClass().getResource("gui/MainView.fxml"));
           ScrollPane scrollPane = loader.load();

           scrollPane.setFitToHeight(true);
           scrollPane.setFitToWidth(true);

           Scene mainScene = new Scene(scrollPane);
           stage.setScene(mainScene);
           stage.setTitle("Sample JavaFx application");
           Image i = new Image(getClass().getResourceAsStream("imgs/javaIcon.png"));
           stage.getIcons().add(i);
           stage.show();
       } catch (IOException e){
           e.printStackTrace();
       }
    }

    public static void main(String[] args) {
        launch();
    }
}