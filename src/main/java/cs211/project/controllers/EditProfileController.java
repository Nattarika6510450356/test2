package cs211.project.controllers;

import cs211.project.models.User;
import cs211.project.models.collection.UserList;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.UserListFileDatasource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

public class EditProfileController {
    @FXML private Label errorLabel;
    @FXML private Label usernameLabel;
    @FXML private Label nameLabel;
    @FXML private TextField newNameTextField;
    @FXML private PasswordField currPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ImageView profilePicImageView;

    private Datasource<UserList> datasource;
    private UserList userList;
    private User user;

    @FXML
    public void initialize() {
        datasource = new UserListFileDatasource("data", "user-list.csv");
        userList = datasource.readData();
        errorLabel.setText("");

        String currentUser = (String) FXRouter.getData();
        user = userList.findUserByUsername(currentUser);
        usernameLabel.setText(user.getUsername());
        nameLabel.setText(user.getName());
        Image image = new Image("file:"+user.getUserImagePath(), true);
        profilePicImageView.setImage(image);
    }
    @FXML
    private void onSaveEditProfileBtnClick(){
        String newNameText = newNameTextField.getText();
        String currPassText = currPasswordField.getText();
        String newPassText = newPasswordField.getText();
        String confirmPassText = confirmPasswordField.getText();

        //change only name
        if (!newNameText.equals("") && currPassText.equals("") && confirmPassText.equals("")){
            user.setName(newNameText);
            try {
                FXRouter.goTo("user-profile", user.getUsername());
                datasource.writeData(userList);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //change password only
        if (newNameText.equals("") && !currPassText.equals("") && !confirmPassText.equals("")){
            if (!currPassText.equals(user.getPassword())) {
                errorLabel.setText("wrong current password");
                currPasswordField.setText("");
            }
            if (newPassText.equals(currPassText)) {
                errorLabel.setText("password cannot be same");
                newPasswordField.setText("");
                confirmPasswordField.setText("");
            }
            if (!confirmPassText.equals(newPassText)) {
                errorLabel.setText("password not match");
                newPasswordField.setText("");
                confirmPasswordField.setText("");
            }
            if (!newPassText.equals(currPassText) && currPassText.equals(user.getPassword()) && confirmPassText.equals(newPassText)){
                user.setPassword(newPassText);
                try {
                    FXRouter.goTo("user-profile", user.getUsername());
                    datasource.writeData(userList);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        // change both
        if (!newNameText.equals("") && !currPassText.equals("") && !newPassText.equals("") && !confirmPassText.equals("")) {
            if (!currPassText.equals(user.getPassword())) {
            errorLabel.setText("wrong current password");
            currPasswordField.setText("");
            }
            if (newPassText.equals(currPassText)) {
                errorLabel.setText("password cannot be same");
                newPasswordField.setText("");
                confirmPasswordField.setText("");
            }
            if (!confirmPassText.equals(newPassText)) {
                errorLabel.setText("password not match");
                newPasswordField.setText("");
                confirmPasswordField.setText("");
            }
            if (!newPassText.equals(currPassText) && currPassText.equals(user.getPassword()) && confirmPassText.equals(newPassText)){
                //write new data to csv
                user.setPassword(newPassText);
                user.setName(newNameText);
                try {
                    datasource.writeData(userList);
                    FXRouter.goTo("user-profile", user.getUsername());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @FXML
    private void onEditPicBtnClick(ActionEvent event) { //TODO : sent new image to profile page
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG", "*.png", "*.jpg", "*.jpeg"));
        Node source = (Node) event.getSource();
        File file = chooser.showOpenDialog(source.getScene().getWindow());
        if (file != null){
            try {
                File destDir = new File("images/user");
                if (!destDir.exists()) destDir.mkdirs();
                String[] fileSplit = file.getName().split("\\.");
                String filename = LocalDate.now() + "_"+System.currentTimeMillis() + "."
                        + fileSplit[fileSplit.length - 1];
                Path target = FileSystems.getDefault().getPath(
                        destDir.getAbsolutePath()+System.getProperty("file.separator")+filename
                );
                Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING );
                profilePicImageView.setImage(new Image(target.toUri().toString()));
                user.setImagePath(destDir + "/" + filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    public void onBackBtnClick(){
        try {
            FXRouter.goTo("user-profile", user.getUsername());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
