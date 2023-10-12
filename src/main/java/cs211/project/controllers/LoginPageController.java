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

public class LoginPageController {
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;
    @FXML private Label errorLabel;

    private Datasource<UserList> datasource;
    private UserList userList;
    private User user;

    @FXML
    public void initialize() {
        datasource = new UserListFileDatasource("data", "user-list.csv");
        userList = datasource.readData();
        errorLabel.setText("");
    }

    @FXML
    protected void onOrganizerBtnClick(){
        try {
            FXRouter.goTo("organizer");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onLoginBtnClick(){
        String usernameText = usernameTextField.getText();
        String passwordText = passwordTextField.getText();
        if (!usernameText.equals("") && !passwordText.equals("")) {
            user = userList.findUserByUsername(usernameText);
            if (user == null || !passwordText.equals(user.getPassword())) {
                errorLabel.setText("invalid username or password");
            } else {
                if(user.getRank().equals("user")) {
                    try {
                        FXRouter.goTo("events", user.getUsername());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    user.setLoginTime();
                    datasource.writeData(userList);
                } else {
                    try {
                        FXRouter.goTo("admin-view-user-list", user.getUsername());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        } else {
            errorLabel.setText("please fill out the field");
        }
    }

    @FXML
    protected  void onRegisterBtnClick(){
        try {
            FXRouter.goTo("register");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected  void instructionBtn(){
        try {
            FXRouter.goTo("instruction");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
