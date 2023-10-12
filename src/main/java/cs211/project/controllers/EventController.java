package cs211.project.controllers;

import cs211.project.models.EventUser;
import cs211.project.models.collection.EventUserList;
import cs211.project.models.Events;
import cs211.project.models.collection.EventsList;
import cs211.project.services.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import cs211.project.models.Team;
import cs211.project.models.collection.TeamList;

import java.io.IOException;
import java.time.LocalDate;

public class EventController {
    @FXML
    private Label eventNameLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label seatLabel;
    @FXML
    private Label detailLabel;
    @FXML
    private ImageView eventImageView;
    @FXML
    private TableView teamTableView;
    @FXML
    private Button joinIndividualButton;
    @FXML
    private Button scheduleButton;
    @FXML
    private Label seatFullLabel;

    private Datasource<EventsList> datasource;
    private EventsList eventsList;
    private Events events;
    private Datasource<EventUserList> eventUserListDatasource;
    private EventUserList eventUserList;
    private EventUser eventUser;
    private String currentUser;

    private EventDatasource<TeamList> datasource1;
    private TeamList teamList;
    Image image;

    @FXML
    public void initialize() {
        seatFullLabel.setText("");
        scheduleButton.setVisible(false);
        datasource = new EventsListFileDatasource("data", "events-list.csv");
        eventsList = datasource.readData();

        eventUserListDatasource = new EventUserFileDatasource("data", "event-user-list.csv");
        eventUserList = eventUserListDatasource.readData();

        eventUser = (EventUser) FXRouter.getData();
        currentUser = eventUser.getUser();
        events = eventsList.findEventByName(eventUser.getEvent());
        image = new Image("file:" + events.getEventImagePath());
        System.out.println(events.getEventImagePath());
        System.out.println(image.getClass());
        showEvents(events);

        if (events.getAvailableSeat() == 0) {
            joinIndividualButton.setVisible(false);
            seatFullLabel.setText("Seat Full");
        }
        if (eventUserList.findEventUser(eventUser.getEvent(), currentUser) != null) {
            joinIndividualButton.setVisible(false);
            scheduleButton.setVisible(true);
        }
        LocalDate finDate = LocalDate.parse(events.getFinishDate());
        if (finDate.isBefore(LocalDate.now())) {
            joinIndividualButton.setVisible(false);
            scheduleButton.setVisible(false);
            seatFullLabel.setText("Event Finished");
        }

        datasource1 = new TeamListFileDatasource("data", "team-list.csv");
        teamList = datasource1.readData();
        showTable(teamList);
        teamTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Team>() { //ยังกดตารางไปอีกหน้าไม่ได้
            public void changed (ObservableValue observable, Team oldValue, Team newValue){
                if (newValue != null) {
                    try {
                        DataContainer dataContainer = new DataContainer();
                        dataContainer.put("eventName", eventUser.getEvent());
                        dataContainer.put("teamName", newValue.getTeamName());
                        dataContainer.put("username", eventUser.getUser());
                        FXRouter.goTo("team-detail", dataContainer);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void showEvents (Events events){
        eventNameLabel.setText(events.getEventName());
        dateLabel.setText(events.getStartDate());
        seatLabel.setText(events.getMaxSeat() - events.getAvailableSeat() + "/" + events.getMaxSeat());
        detailLabel.setText(events.getEventDetail());
        eventImageView.setImage(image);
    }
    @FXML
    protected void joinBtnClick() {
        if(eventUserList.findEventUser(eventUser.getEvent(), currentUser)==null && !events.getAvailableSeat().equals(0)) {
            eventUserList.addEventUser(eventUser.getEvent(), currentUser);
            eventUserListDatasource.writeData(eventUserList);
            events.userJoinEvent();
            datasource.writeData(eventsList);
            try {
                FXRouter.goTo("event-schedule", eventUser);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @FXML
    protected void scheduleBtnClick() {
        try {
            FXRouter.goTo("event-schedule", eventUser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void showTable(TeamList teamList){
        TableColumn<Team, String> teamNameColumn = new TableColumn<>("Team Name");
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));

        TableColumn<Team, String> teamDateColumn = new TableColumn<>("Register Start");
        teamDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationOpenDate"));

        TableColumn<Team, String> teamEndColumn = new TableColumn<>("Register End");
        teamEndColumn.setCellValueFactory(new PropertyValueFactory<>("registrationCloseDate"));


        TableColumn<Team, String> seatAvailableColumn = new TableColumn<>("Seat Available");
        seatAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeat"));

        teamTableView.getColumns().clear();
        teamTableView.getColumns().add(teamNameColumn);
        teamTableView.getColumns().add(teamDateColumn);
        teamTableView.getColumns().add(teamEndColumn);
        teamTableView.getColumns().add(seatAvailableColumn);

        teamTableView.getItems().clear();
        for (Team team: teamList.getTeams()){
            if (team.getTeamInEvent().equals(events.getEventName())){
                teamTableView.getItems().add(team);
            }
        }
    }
        @FXML
        protected void backBtnClick () {
            try {
                FXRouter.goTo("events");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

