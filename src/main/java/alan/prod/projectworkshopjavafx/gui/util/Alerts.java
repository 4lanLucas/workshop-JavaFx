package alan.prod.projectworkshopjavafx.gui.util;

import javafx.scene.control.Alert;

public class Alerts {
    public static void showAlert(String tittle,String header, String content, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(tittle);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }
}
