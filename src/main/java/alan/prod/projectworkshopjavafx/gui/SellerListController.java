package alan.prod.projectworkshopjavafx.gui;

import alan.prod.projectworkshopjavafx.Main;
import alan.prod.projectworkshopjavafx.db.DbException;
import alan.prod.projectworkshopjavafx.gui.listeners.DataChangeListener;
import alan.prod.projectworkshopjavafx.gui.util.Alerts;
import alan.prod.projectworkshopjavafx.gui.util.Utils;
import alan.prod.projectworkshopjavafx.model.entities.Seller;
import alan.prod.projectworkshopjavafx.model.services.DepartmentService;
import alan.prod.projectworkshopjavafx.model.services.SellerService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class SellerListController implements Initializable, DataChangeListener {
    private SellerService service;

    @FXML
    private TableView<Seller> tableViewSeller;
    @FXML
    private TableColumn<Seller,Integer> tableColumnId;
    @FXML
    private TableColumn<Seller,String> tableColumnName;
    @FXML
    private TableColumn<Seller, String> tableColumnEmail;
    @FXML
    private TableColumn<Seller, Date> tableColumnBirthDate;
    @FXML
    private TableColumn<Seller, Double> tableColumnBaseSalary;

    @FXML
    private TableColumn<Seller, Seller> tableColumnEdit;
    @FXML
    private TableColumn<Seller, Seller> tableColumnRemove;
    @FXML
    private Button btNew;
    private ObservableList<Seller> obsList;
    @FXML
    private void initRemoveButtons(){
        tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnRemove.setCellFactory(param -> new TableCell<Seller,Seller>(){
            private final Button button = new Button("remove");

            @Override
            protected void updateItem(Seller obj, boolean empty){
                super.updateItem(obj, empty);
                if(obj == null){
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> removeEntity(obj));
            }
        });
    }

    private void removeEntity(Seller obj) {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmation","are u sure to delete?");
        if(result.get() == ButtonType.OK){
            if (service == null) {
                throw new IllegalStateException("Service is null");
            }
            try{
                service.remove(obj);
                updateTableView();
            } catch (DbException e){
                Alerts.showAlert("Error removing object",null, e.getMessage(), Alert.AlertType.ERROR);
            }
        };
    }

    @FXML
    private void initEditButtons(){
         tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
         tableColumnEdit.setCellFactory(param -> new TableCell<Seller,Seller>(){
             private final Button button = new Button("edit");

             @Override
             protected void updateItem(Seller obj, boolean empty){
                 super.updateItem(obj, empty);
                 if(obj == null){
                     setGraphic(null);
                     return;
                 }
                 setGraphic(button);
                 button.setOnAction(event -> {
                     createDialogForm(obj,"SellerForm.fxml",Utils.currentStage(event));
                 });
             }
        });
    }
    @FXML
    public void onbtNewAction(ActionEvent event){
        Stage parentStage = Utils.currentStage(event);
        Seller obj = new Seller();
        createDialogForm(obj,"SellerForm.fxml", parentStage);
    }

    public void setSellerService(SellerService service){
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();

    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        Utils.formatTableColumnDate(tableColumnBirthDate,"dd/MM/yyyy");
        tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
        Utils.formatTableColumnDouble(tableColumnBaseSalary,2);

        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView(){
        if(service == null){
            throw new IllegalStateException("Service is null");
        }
        List<Seller> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewSeller.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }

    private void createDialogForm(Seller obj,String absoluteName, Stage parentStage){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();
            SellerFormController controller = loader.getController();
            controller.setSeller(obj);
            controller.setServices(new SellerService(), new DepartmentService());
            controller.loadAssociatedObjects();
            controller.updateFormData();
            controller.subscribeDataChangeListener(this);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Seller data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

        } catch (IOException e){
            e.printStackTrace();
            Alerts.showAlert("IOException",null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }
}
