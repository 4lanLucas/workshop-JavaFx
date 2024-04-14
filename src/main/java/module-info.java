module alan.prod.projectworkshopjavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens alan.prod.projectworkshopjavafx to javafx.fxml;
    exports alan.prod.projectworkshopjavafx;
}