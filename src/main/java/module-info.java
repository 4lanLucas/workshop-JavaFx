module alan.prod.projectworkshopjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens alan.prod.projectworkshopjavafx to javafx.fxml;
    exports alan.prod.projectworkshopjavafx;
    exports alan.prod.projectworkshopjavafx.gui;
    opens alan.prod.projectworkshopjavafx.gui to javafx.fxml;
    opens alan.prod.projectworkshopjavafx.model.services to javafx.fxml;
    opens alan.prod.projectworkshopjavafx.model.entities to javafx.fxml;
    exports alan.prod.projectworkshopjavafx.model.entities;
    exports alan.prod.projectworkshopjavafx.model.services;
    exports alan.prod.projectworkshopjavafx.model.dao;
    exports alan.prod.projectworkshopjavafx.db;


}