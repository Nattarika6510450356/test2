package cs211.project.controllers;

import cs211.project.models.EventUser;
import cs211.project.models.collection.EventUserList;
import cs211.project.models.Events;
import cs211.project.models.collection.EventsList;
import cs211.project.services.Datasource;
import cs211.project.services.EventUserFileDatasource;
import cs211.project.services.EventsListFileDatasource;
import cs211.project.services.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class EventManageUserController {
    @FXML private Label usernameLabel;
    @FXML private TableView userTableview;
    Datasource<EventUserList> datasource;
    EventUserList eventUserList;
    EventUser selectedItem;
    String eventName;
    Datasource<EventsList> eventsListDatasource;
    EventsList eventsList;

    @FXML
    public void initialize(){
        eventName = (String) FXRouter.getData();
        datasource = new EventUserFileDatasource("data", "event-user-list.csv");
        eventUserList = datasource.readData();
        usernameLabel.setText("");
        showTable(eventUserList);

        eventsListDatasource = new EventsListFileDatasource("data", "events-list.csv");
        eventsList = eventsListDatasource.readData();

        userTableview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<EventUser>() {
            @Override
            public void changed(ObservableValue observable, EventUser oldValue, EventUser newValue){
                if (newValue == null){
                    usernameLabel.setText("");
                    selectedItem = null;
                } else {
                    selectedItem = newValue;
                    usernameLabel.setText(newValue.getUser());
                }
            }
        });
    }
    @FXML
    private void showTable(EventUserList eventUserList){
        TableColumn<Events, String> usernameColumn = new TableColumn<>("User name");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        userTableview.getColumns().clear();
        userTableview.getColumns().add(usernameColumn);
        userTableview.getItems().clear();

        for (EventUser eventUser: eventUserList.getEventUser()){
            if (eventUser.getEvent().equals(eventName)){
                userTableview.getItems().add(eventUser);
            }
        }
    }

    @FXML
    private void onBanUserBtnClick() {
        if (selectedItem != null) {
            eventUserList.getEventUser().remove(selectedItem);
            datasource.writeData(eventUserList);

            userTableview.getItems().remove(selectedItem);

            Events currEvent = eventsList.findEventByName(selectedItem.getEvent());
            currEvent.userBan();
            eventsListDatasource.writeData(eventsList);

            selectedItem = null;
            usernameLabel.setText("");
        }
    }

    @FXML
    private void onBackBtnClick(){
        try {
            FXRouter.goTo("edit-event");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
