package cs211.project.controllers;

import cs211.project.models.Events;
import cs211.project.models.collection.EventsList;
import cs211.project.models.Team;
import cs211.project.models.collection.TeamList;
import cs211.project.services.*;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class CreateTeamController {
    @FXML
    private TextField teamNameTextField;
    @FXML
    private TextField teamMaxSeatTextField;
    @FXML
    private DatePicker registOpen;
    @FXML
    private DatePicker registClose;
    @FXML
    private Label errorLabel;

    private TeamList teamList;
    private Team team;
    private EventDatasource<TeamList> datasource;
    private Datasource<EventsList> eventsListDatasource;
    private String eventName;
    private Events events;
    private EventsList eventsList;

    @FXML
    public void initialize(){
        errorLabel.setText("");
        datasource = new TeamListFileDatasource("data", "team-list.csv");
        eventsListDatasource = new EventsListFileDatasource("data", "events-list.csv");
        teamList = datasource.readData();
        eventsList = eventsListDatasource.readData();
        eventName = (String) FXRouter.getData();
        events = eventsList.findEventByName(eventName);
    }

    @FXML
    protected void onCreateTeamBtnClick(){
        String teamNameText = teamNameTextField.getText();
        Integer teamMaxSeatText = Integer.valueOf(teamMaxSeatTextField.getText());
        String registOpenText = String.valueOf(registOpen.getValue());
        String registCloseText = String.valueOf(registClose.getValue());
        String teamInEventText = eventName;

        if (teamList.findTeamByTeamName(teamNameText) == null || !teamList.findTeamByTeamName(teamNameText).getTeamInEvent().equals(eventName)) {
            teamList.addNewTeam(teamNameText, teamMaxSeatText, teamMaxSeatText, registOpenText, registCloseText, teamInEventText);
            datasource.writeData(teamList);
            try {
                FXRouter.goTo("edit-event");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            errorLabel.setText("Team name already used");
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
