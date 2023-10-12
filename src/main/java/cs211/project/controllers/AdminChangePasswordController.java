package cs211.project.controllers;

import cs211.project.models.User;
import cs211.project.models.collection.UserList;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.UserListFileDatasource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AdminChangePasswordController {
    @FXML private Label errorLabel;
    @FXML private TextField currentPasswordTextField;
    @FXML private TextField newPasswordTextField;
    @FXML private TextField confirmNewPasswordTextField;

    String admin;
    private Datasource<UserList> datasource;
    private UserList userList;
    private User user;

    @FXML
    public void initialize(){
        admin = (String) FXRouter.getData();
        datasource = new UserListFileDatasource("data", "user-list.csv");
        userList = datasource.readData();
        errorLabel.setText("");

        user = userList.findUserByUsername(admin);

    }
    @FXML
    private void onSaveBtnClick(){
        String currentPasswordText = currentPasswordTextField.getText();
        String newPasswordText = newPasswordTextField.getText();
        String confirmNewPasswordText = confirmNewPasswordTextField.getText();

        if (!currentPasswordText.equals("") && !newPasswordText.equals("") && !confirmNewPasswordText.equals("")){
            if (newPasswordText.equals(currentPasswordText)){
                errorLabel.setText("Password cannot be same");
                newPasswordTextField.setText("");
                confirmNewPasswordTextField.setText("");
            }
            if (!currentPasswordText.equals(user.getPassword())){
                errorLabel.setText("Wrong current password");
                currentPasswordTextField.setText("");
            }
            if (!confirmNewPasswordText.equals(newPasswordText)){
                errorLabel.setText("Password not match");
                newPasswordTextField.setText("");
                confirmNewPasswordTextField.setText("");
            }
            if (!newPasswordText.equals(currentPasswordText) && currentPasswordText.equals(user.getPassword()) && confirmNewPasswordText.equals(newPasswordText)){
                user.setPassword(newPasswordText);
                try {
                    datasource.writeData(userList);
                    FXRouter.goTo("admin-view-user-list");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    @FXML
    private void onBackBtnClick(){
        try {

            FXRouter.goTo("admin-view-user-list");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


