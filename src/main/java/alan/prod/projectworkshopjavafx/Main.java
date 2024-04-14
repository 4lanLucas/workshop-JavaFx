package alan.prod.projectworkshopjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    private static Scene mainScene;

    @Override
    public void start(Stage stage){
       try{
           URL arquivo = getClass().getResource("gui/MainView.fxml");
           //FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
           ScrollPane scrollPane = FXMLLoader.load(arquivo);

           scrollPane.setFitToHeight(true);
           scrollPane.setFitToWidth(true);

           mainScene = new Scene(scrollPane);
           stage.setScene(mainScene);
           stage.setTitle("Sample JavaFx application");
           Image i = new Image(getClass().getResourceAsStream("imgs/javaIcon.png"));
           stage.getIcons().add(i);
           stage.show();
       } catch (IOException e){
           System.out.println(e.getMessage());;
       }
    }

    public static Scene getMainScene(){
        return mainScene;
    }

    public static void main(String[] args) {
        launch();
    }
}