module alan.prod.projectworkshopjavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens alan.prod.projectworkshopjavafx to javafx.fxml;
    exports alan.prod.projectworkshopjavafx;
    exports alan.prod.projectworkshopjavafx.gui;
    opens alan.prod.projectworkshopjavafx.gui to javafx.fxml;
    opens alan.prod.projectworkshopjavafx.models.services to javafx.fxml;
    opens alan.prod.projectworkshopjavafx.models.entities to javafx.fxml;
    exports alan.prod.projectworkshopjavafx.models.entities;
    exports alan.prod.projectworkshopjavafx.models.services;
}