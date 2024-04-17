package alan.prod.projectworkshopjavafx.gui;

import alan.prod.projectworkshopjavafx.db.DbException;
import alan.prod.projectworkshopjavafx.gui.listeners.DataChangeListener;
import alan.prod.projectworkshopjavafx.gui.util.Alerts;
import alan.prod.projectworkshopjavafx.gui.util.Constraints;
import alan.prod.projectworkshopjavafx.gui.util.Utils;
import alan.prod.projectworkshopjavafx.model.entities.Department;
import alan.prod.projectworkshopjavafx.model.entities.Seller;
import alan.prod.projectworkshopjavafx.model.exceptions.ValidationException;
import alan.prod.projectworkshopjavafx.model.services.DepartmentService;
import alan.prod.projectworkshopjavafx.model.services.SellerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SellerFormController implements Initializable {
    private Seller entity;
    private SellerService service;
    private DepartmentService departmentService;
    private List<DataChangeListener> dataChangeListenerListeners = new ArrayList<>();
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private DatePicker dpBirthDate;
    @FXML
    private TextField txtBaseSalary;
    @FXML
    private ComboBox<Department> comboBoxDepartment;
    @FXML
    private Label labelErrorName;
    @FXML
    private Label labelErrorEmail;
    @FXML
    private Label labelErrorBirthDate;
    @FXML
    private Label labelErrorBaseSalary;
    @FXML
    private Button btSave;
    @FXML
    private Button btCancel;
    private ObservableList<Department> obsList;

    public void setSeller(Seller entity) {
        this.entity = entity;
    }

    public void setServices(SellerService service,DepartmentService departmentService) {
        this.service = service;
        this.departmentService = departmentService;
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
        Constraints.setTextFieldMaxLength(txtName,70);
        Constraints.setTextFieldDouble(txtBaseSalary);
        Constraints.setTextFieldMaxLength(txtEmail,60);
        Utils.formatDatePicker(dpBirthDate,"dd/MM/yyyy");
        initializeComboBoxDepartment();
    }

    public void updateFormData(){
        if( entity == null){
            throw new IllegalStateException("Entity is null");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
        txtEmail.setText(entity.getEmail());
        Locale.setDefault(Locale.US);
        txtBaseSalary.setText(String.format("%.2f",entity.getBaseSalary()));
        if(entity.getBirthDate() != null){
            dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
        }
        if(entity.getDepartment() == null){
            comboBoxDepartment.getSelectionModel().selectFirst();
        } else{
            comboBoxDepartment.setValue(entity.getDepartment());
        }
    }

    public void loadAssociatedObjects(){
        if(departmentService == null){
            throw new IllegalStateException("DepartmentService is null");
        }
        List<Department> list = departmentService.findAll();
        obsList = FXCollections.observableArrayList(list);
        comboBoxDepartment.setItems(obsList);
    }

    private void setErrorMessage(Map<String,String> errors){
        Set<String> fields = errors.keySet();
        if(fields.contains("name")){
            labelErrorName.setText(errors.get("name"));
        }
    }
    private void initializeComboBoxDepartment() {
        Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        comboBoxDepartment.setCellFactory(factory);
        comboBoxDepartment.setButtonCell(factory.call(null));
    }
}
