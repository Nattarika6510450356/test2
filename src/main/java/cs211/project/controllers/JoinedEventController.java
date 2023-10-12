package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.models.collection.EventUserList;
import cs211.project.models.collection.EventsList;
import cs211.project.models.collection.TeamList;
import cs211.project.models.collection.TeamUserList;
import cs211.project.services.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.ArrayList;

public class JoinedEventController {
    @FXML private TableView joinedEventTable;
    @FXML private TableView joinedTeamTable;

    private EventUserList eventUserList;
    private EventsList eventsList;
    private TeamUserList teamUserList;
    private TeamList teamList;
    private Datasource<EventUserList> eventUserListDatasource;
    private Datasource<EventsList> eventsListDatasource;
    private Datasource<TeamUserList> teamUserListDatasource;
    private EventDatasource<TeamList> teamListDatasource;
    private String currentUser;
    @FXML
    public void initialize(){
        currentUser = (String) FXRouter.getData();
        eventUserListDatasource = new EventUserFileDatasource("data", "event-user-list.csv");
        eventUserList = eventUserListDatasource.readData();
        eventsListDatasource = new EventsListFileDatasource("data", "events-list.csv");
        eventsList = eventsListDatasource.readData();
        teamUserListDatasource = new TeamUserListFileDatasource("data", "team-user-list.csv");
        teamUserList = teamUserListDatasource.readData();
        teamListDatasource = new TeamListFileDatasource("data", "team-list.csv");
        teamList = teamListDatasource.readData();
        showEventTable(eventsList);
        showTeamTable(teamList);

        joinedEventTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Events>() {
            @Override
            public void changed(ObservableValue observable, Events oldValue, Events newValue) {
                if (newValue != null) {
                    try {

                        EventUser eventUser = new EventUser(newValue.getEventName(), currentUser);
                        FXRouter.goTo("event-schedule2", eventUser);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        joinedTeamTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TeamUser>() {
            @Override
            public void changed(ObservableValue observable, TeamUser oldValue, TeamUser newValue) {
                if (newValue != null) {
                    try {

                        TeamUser teamUser = new TeamUser(newValue.getEventName(), newValue.getTeamName(), currentUser);
                        FXRouter.goTo("team-table2", teamUser);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
    @FXML
    private void showEventTable(EventsList eventsList) {
        TableColumn<Events, String> eventNameColumn = new TableColumn<>("Event Name");
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));

        TableColumn<Events, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        joinedEventTable.getColumns().clear();
        joinedEventTable.getColumns().add(eventNameColumn);
        joinedEventTable.getColumns().add(statusColumn);
        joinedEventTable.getItems().clear();

        ArrayList<String> userJoinedEvents = getUserJoinedEvents(currentUser);

        for (Events event : eventsList.getEvents()) {
            if (userJoinedEvents.contains(event.getEventName()) && event.getStatus().equals("active")) {
                joinedEventTable.getItems().add(event);
            }
        }
        for (Events event : eventsList.getEvents()) {
            if (userJoinedEvents.contains(event.getEventName()) && event.getStatus().equals("finish")) {
                joinedEventTable.getItems().add(event);
            }
        }
    }

    @FXML
    private void showTeamTable(TeamList teamList) {
        TableColumn<Team, String> teamNameColumn = new TableColumn<>("Team Name");
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));

        TableColumn<Team, String> eventNameColumn = new TableColumn<>("Event Name");
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));

        joinedTeamTable.getColumns().clear();
        joinedTeamTable.getColumns().add(teamNameColumn);
        joinedTeamTable.getColumns().add(eventNameColumn);
        joinedTeamTable.getItems().clear();

        ArrayList<String> userJoinedEvents = getUserJoinedEvents(currentUser);

        for (TeamUser teamUser : teamUserList.getTeamUser()) {
            if (teamUser.getUserName().equals(currentUser)) {
                joinedTeamTable.getItems().add(teamUser);
            }
        }
    }


    private ArrayList<String> getUserJoinedEvents(String currentUser) {
        ArrayList<String> userJoinedEvents = new ArrayList<>();

        for (EventUser eventUser : eventUserList.getEventUser()) {
            if (eventUser.isUser(currentUser)) {
                userJoinedEvents.add(eventUser.getEvent());
            }
        }

        return userJoinedEvents;
    }

    @FXML
    private void onBackBtnClick(){
        try {
            FXRouter.goTo("user-profile", currentUser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
