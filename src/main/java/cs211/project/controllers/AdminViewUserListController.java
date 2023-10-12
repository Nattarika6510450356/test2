package cs211.project.controllers;

import cs211.project.models.User;
import cs211.project.models.collection.UserList;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.UserListFileDatasource;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import cs211.project.services.AdminViewUserListComparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class AdminViewUserListController {
    @FXML private TableView usersTableView;
    private UserList userList;
    private Datasource<UserList> datasource;
    String admin;

    @FXML
    public void initialize() {
        datasource = new UserListFileDatasource("data", "user-list.csv");
        userList = datasource.readData();
        admin = (String) FXRouter.getData();
        showTable(userList);
    }

    @FXML
    void showTable(UserList userList) { // todo: Must have user profile picture in table
        TableColumn<User, String> userImageColumn = new TableColumn<>("User Image");
        userImageColumn.setCellValueFactory(new PropertyValueFactory<>("userImagePath"));
        userImageColumn.setCellFactory(param -> new  ImageTableCell()); // Use a custom cell factory for images

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<User, String> loginTimeColumn = new TableColumn<>("Latest Use");
        loginTimeColumn.setCellValueFactory(new PropertyValueFactory<>("loginTime"));

        // ใส่ข้อมูลทั้งหมดจาก userList ไปแสดงใน TableView
        for (User user: userList.getUsers()) {
            if (user.getRank().equals("user")) {
                usersTableView.getItems().add(user);
            }
        }
        SortedList<User> sortedUsers = new SortedList<>(FXCollections.observableArrayList(userList.getUsers()));
        sortedUsers.setComparator(new AdminViewUserListComparator());
        ObservableList<User> rankUser = sortedUsers.filtered(user -> user.getRank().equals("user"));

        usersTableView.getColumns().clear();
        usersTableView.getColumns().add(userImageColumn);
        usersTableView.getColumns().add(usernameColumn);
        usersTableView.getColumns().add(nameColumn);
        usersTableView.getColumns().add(loginTimeColumn);
        usersTableView.getItems().clear();
        usersTableView.setItems(rankUser);

    }
    private class ImageTableCell extends TableCell<User, String> {
        private final ImageView imageView = new ImageView();
        private final int imageWidth = 50; // Adjust the image width as needed
        private final int imageHeight = 50; // Adjust the image height as needed

        @Override
        protected void updateItem(String imagePath, boolean empty) {
            super.updateItem(imagePath, empty);
            if (empty || imagePath == null) {
                setGraphic(null);
            } else {
                Image image = new Image("file:" + imagePath, imageWidth, imageHeight, true, true);
                imageView.setImage(image);
                setGraphic(imageView);
            }
        }
    }
    @FXML
    private void onEditPasswordButtonClick(){
        //todo : admin info and change pass
        try {
            FXRouter.goTo("admin-change-password", admin);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onLogOutBtnClick(){
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
