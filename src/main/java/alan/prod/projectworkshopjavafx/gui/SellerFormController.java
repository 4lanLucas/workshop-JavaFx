package alan.prod.projectworkshopjavafx.gui;

import alan.prod.projectworkshopjavafx.db.DbException;
import alan.prod.projectworkshopjavafx.gui.listeners.DataChangeListener;
import alan.prod.projectworkshopjavafx.gui.util.Alerts;
import alan.prod.projectworkshopjavafx.gui.util.Constraints;
import alan.prod.projectworkshopjavafx.gui.util.Utils;
import alan.prod.projectworkshopjavafx.model.entities.Seller;
import alan.prod.projectworkshopjavafx.model.exceptions.ValidationException;
import alan.prod.projectworkshopjavafx.model.services.SellerService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;

public class SellerFormController implements Initializable {
    private Seller entity;
    private SellerService service;
    private List<DataChangeListener> dataChangeListenerListeners = new ArrayList<>();
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private Label labelErrorId;
    @FXML
    private Label labelErrorName;
    @FXML
    private Button btSave;
    @FXML
    private Button btCancel;

    public void setSeller(Seller entity) {
        this.entity = entity;
    }

    public void setSellerService(SellerService service) {
        this.service = service;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListenerListeners.add(listener);
    }

    @FXML
    public void onBtSaveAction(ActionEvent event) {
        if(entity == null){
            throw new IllegalStateException("Entity is null");
        }
        if(service == null){
            throw new IllegalStateException("Service is null");
        }
        try{
            entity = getFormData();
            service.saveOrUpdate(entity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        } catch(DbException e){
            Alerts.showAlert("Error saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        } catch(ValidationException e){
            setErrorMessage(e.getErrors());
        }
    }

    private void notifyDataChangeListeners() {
        for(DataChangeListener listener : dataChangeListenerListeners){
            listener.onDataChanged();
        }
    }

    private Seller getFormData() {
        ValidationException exception = new ValidationException("Validation error");
        Seller obj = new Seller();
        obj.setId(Utils.tryParseToInt(txtId.getText()));
        if (txtName.getText() == null || txtName.getText().trim().equals("")){
            exception.addError("name","field cant be empty");
        }
        obj.setName(txtName.getText());
        if(!exception.getErrors().isEmpty()){
            throw exception;
        }
        return obj;
    }

    @FXML
    public void onBtCancelAction(ActionEvent event){
        Utils.currentStage(event).close();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName,30);
    }
    public void updateFormData(){
        if( entity == null){
            throw new IllegalStateException("Entity is null");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
    }
    private void setErrorMessage(Map<String,String> errors){
        Set<String> fields = errors.keySet();
        if(fields.contains("name")){
            labelErrorName.setText(errors.get("name"));
        }
    }
}
