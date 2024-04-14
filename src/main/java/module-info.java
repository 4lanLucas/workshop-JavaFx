module alan.prod.projectworkshopjavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens alan.prod.projectworkshopjavafx to javafx.fxml;
    exports alan.prod.projectworkshopjavafx;
    exports alan.prod.projectworkshopjavafx.gui;
    opens alan.prod.projectworkshopjavafx.gui to javafx.fxml;
}