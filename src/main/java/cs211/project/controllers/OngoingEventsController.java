package cs211.project.controllers;

import cs211.project.models.EventUser;
import cs211.project.models.Events;
import cs211.project.models.collection.EventsList;
import cs211.project.services.Datasource;
import cs211.project.services.EventsListFileDatasource;
import cs211.project.services.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.time.LocalDate;

public class OngoingEventsController {
    @FXML
    private TextField searchEventsTextField;
    @FXML
    private TableView<Events> eventsTableView;
    private ObservableList<Events> eventsObservableList = FXCollections.observableArrayList();
    private Datasource<EventsList> datasource;
    private EventsList eventsList;
    private String currentUser;

    @FXML
    public void initialize() {
        datasource = new EventsListFileDatasource("data", "events-list.csv");
        eventsList = datasource.readData();
        showTable(eventsList);

        currentUser = (String) FXRouter.getData();

        FilteredList<Events> filteredEvents = new FilteredList<>(eventsObservableList, b -> true);
        searchEventsTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredEvents.setPredicate(events -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCase = newValue.toLowerCase();

                if (events.getEventName().toLowerCase().indexOf(lowerCase) != -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Events> sortedEvents = new SortedList<>(filteredEvents);
        sortedEvents.comparatorProperty().bind(eventsTableView.comparatorProperty());
        eventsTableView.setItems(sortedEvents);

        eventsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Events>() {
            @Override
            public void changed(ObservableValue<? extends Events> observable, Events oldValue, Events newValue) {
                if (newValue != null) {
                    try {
                        EventUser eventUser = new EventUser(newValue.getEventName(), currentUser);
                        FXRouter.goTo("event-detail", eventUser);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    @FXML
    public void showTable(EventsList eventsList){
        TableColumn<Events, String> eventImageColumn = new TableColumn<>("Event Image");
        eventImageColumn.setCellValueFactory(new PropertyValueFactory<>("eventImagePath"));
        eventImageColumn.setCellFactory(param -> new ImageTableCell());

        TableColumn<Events, String> eventNameColumn = new TableColumn<>("Event Name");
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));

        TableColumn<Events, String> seatAvailableColumn = new TableColumn<>("Seat Available");
        seatAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeat"));

        TableColumn<Events, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        eventsTableView.getColumns().clear();
        eventsTableView.getColumns().add(eventImageColumn);
        eventsTableView.getColumns().add(eventNameColumn);
        eventsTableView.getColumns().add(seatAvailableColumn);
        eventsTableView.getColumns().add(statusColumn);
        eventsTableView.getItems().clear();

        for (Events events : eventsList.getEvents()) {
            LocalDate finishDate = LocalDate.parse(events.getFinishDate());
            if (finishDate.isBefore(LocalDate.now())) {
                events.setStatus("finish");
            } else {
                events.setStatus("active");
            }
            eventsList.addNewEvent(events.getEventName(), events.getEventDetail(), events.getMaxSeat(), events.getAvailableSeat(), events.getStartDate(), events.getFinishDate(), events.getEventImagePath(), events.getEventCreatorUsername(), events.getStatus());
            datasource.writeData(eventsList);
        }
        for (Events events : eventsList.getEvents()) {
            if (events.getStatus().equals("active")) {
                eventsTableView.getItems().add(events);
                eventsObservableList.add(events);
            }
        }
        for (Events events : eventsList.getEvents()) {
            if (events.getStatus().equals("finish")) {
                eventsTableView.getItems().add(events);
                eventsObservableList.add(events);
            }
        }
    }
    private class ImageTableCell extends TableCell<Events, String> {
        private final ImageView imageView = new ImageView();
        private final int imageWidth = 50;
        private final int imageHeight = 50;

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
    private void onProfileButtonClick() {
        try {
            FXRouter.goTo("user-profile", currentUser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onLogOutBtnClick() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new  RuntimeException(e);
        }
    }
}
