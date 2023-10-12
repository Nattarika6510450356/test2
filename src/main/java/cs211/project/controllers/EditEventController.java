package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.models.collection.EventsList;
import cs211.project.models.collection.TeamList;
import cs211.project.models.collection.TimeScheduleList;
import cs211.project.services.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class EditEventController {
    @FXML
    private Label eventNameLabel;
    @FXML
    private Label eventDetailLabel;
    @FXML
    private Label maxSeatLabel;
    @FXML
    private Label eventStartDateLabel;
    @FXML
    private Label eventFinishDateLabel;
    @FXML
    private TextField editEventNameTextField;
    @FXML
    private TextField editDetailTextField;
    @FXML
    private TextField editMaxSeatTextField;
    @FXML
    private DatePicker editDateStart;
    @FXML
    private DatePicker editDateFinish;
    @FXML
    private TableView teamTableView;
    @FXML
    private ImageView eventImageView;

    private Datasource<EventsList> datasource;
    private EventDatasource<TeamList> teamListDatasource;
    private EventDatasource<TimeScheduleList> timeScheduleListDatasource;
    private EventsList eventsList;
    private TeamList teamList;
    private Events events;
    private String eventName;
    private DataContainer dataContainer;
    private String userName;
    Image image;

    @FXML
    public void initialize(){
        datasource = new EventsListFileDatasource("data", "events-list.csv");
        teamListDatasource = new TeamListFileDatasource("data", "team-list.csv");
        timeScheduleListDatasource = new TimeScheduleListFileDatasource("data", "schedule-list.csv");
        eventsList = datasource.readData();
        teamList = teamListDatasource.readData();
        dataContainer = (DataContainer) FXRouter.getData();
        eventName = (String) dataContainer.get("eventName");
        System.out.println(eventName);
        userName = (String) dataContainer.get("username");
        events = eventsList.findEventByName(eventName);
        image = new Image("file:"+events.getEventImagePath(), true);
        showEvents(events);
        showTable(teamList);

        teamTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Team>() {
            @Override
            public void changed(ObservableValue observable, Team oldValue, Team newValue) {
                if (newValue != null) {
                    try {
                        String teamName = newValue.getTeamName();
                        TeamUser teamUser = new TeamUser(eventName, teamName, userName);
                        FXRouter.goTo("edit-team", teamUser);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void showEvents(Events events){
        eventNameLabel.setText(events.getEventName());
        eventDetailLabel.setText(events.getEventDetail());
        maxSeatLabel.setText(String.valueOf(events.getMaxSeat()));
        eventStartDateLabel.setText(events.getStartDate());
        eventFinishDateLabel.setText(events.getFinishDate());
        eventImageView.setImage(image);
    }

    private void showTable(TeamList teamList){
        TableColumn<Events, String> teamNameColumn = new TableColumn<>("Team Name");
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));

        TableColumn<Events, String> teamMaxSeatColumn = new TableColumn<>("Team Max seat");
        teamMaxSeatColumn.setCellValueFactory(new PropertyValueFactory<>("teamMaxSeat"));

        teamTableView.getColumns().clear();
        teamTableView.getColumns().add(teamNameColumn);
        teamTableView.getColumns().add(teamMaxSeatColumn);

        teamTableView.getItems().clear();

        for (Team team: teamList.getTeams()){
            if (team.getTeamInEvent().equals(eventName)){
                teamTableView.getItems().add(team);
            }
        }
    }


    @FXML
    private void onEditEventPictureBtnClick(ActionEvent event){
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG", "*.png", "*.jpg", "*.jpeg"));
        Node source = (Node) event.getSource();
        File file = chooser.showOpenDialog(source.getScene().getWindow());
        if (file != null){
            try {
                File destDir = new File("images/event");
                if (!destDir.exists()) destDir.mkdirs();
                String[] fileSplit = file.getName().split("\\.");
                String filename = LocalDate.now() + "_"+System.currentTimeMillis() + "."
                        + fileSplit[fileSplit.length - 1];
                Path target = FileSystems.getDefault().getPath(
                        destDir.getAbsolutePath()+System.getProperty("file.separator")+filename
                );
                Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING );
                eventImageView.setImage(new Image(target.toUri().toString()));
                events.setEventImagePath(destDir + "/" + filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onCreateNewTeamBtnClick(){
        try {
            FXRouter.goTo("create-team", eventName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void onSaveBtnClick() {
        String newEventNameText = editEventNameTextField.getText();
        String newEventDetailText = editDetailTextField.getText();
        String newMaxSeatText = editMaxSeatTextField.getText();
        String newstartDateText = String.valueOf(editDateStart.getValue());
        String newFinishDateText = String.valueOf(editDateFinish.getValue());

        // Get the old event name
        String oldEventName = events.getEventName();

        // Update the events object
        events.setEventName(newEventNameText);
        events.setEventDetail(newEventDetailText);
        events.setMaxSeat(Integer.valueOf(newMaxSeatText));
        events.setStartDate(newstartDateText);
        events.setFinishDate(newFinishDateText);

        try {
            // Update the event-list.csv
            FXRouter.goTo("user-profile");
            datasource.writeData(eventsList);

            // Update the team-list.csv with the new event name
            timeScheduleListDatasource.updateEventName(oldEventName, newEventNameText);

            teamListDatasource.updateEventName(oldEventName, newEventNameText);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    private void onViewActivityTableBtnClick(){
        try {
            FXRouter.goTo("edit-schedule", eventName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onManageUserBtnClick(){
        try {
            FXRouter.goTo("event-manage-user", eventName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onBackBtnClick(){
        try {
            FXRouter.goTo("user-profile");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
