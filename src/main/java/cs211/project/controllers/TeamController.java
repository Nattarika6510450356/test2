package cs211.project.controllers;

import cs211.project.models.Team;
import cs211.project.models.collection.TeamList;
import cs211.project.models.TeamUser;
import cs211.project.models.collection.TeamUserList;
import cs211.project.services.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class TeamController {
    @FXML private Label teamNameLabel;
    @FXML private Label teamMaxSeatLabel;
    @FXML private Label availableSeatLabel;
    @FXML private Label teamRegisOpenLabel;
    @FXML private Label teamRegisCloseLabel;
    @FXML private Button joinButton;
    @FXML private Label joinLabel;
    Team team;
    TeamList teamList;
    TeamUser teamUser;
    TeamUserList teamUserList;
    EventDatasource<TeamList> datasource;
    Datasource<TeamUserList> teamUserListDatasource;
    DataContainer dataContainer;
    String eventName;
    String teamName;
    String username;

    @FXML
    public void initialize(){
        joinLabel.setVisible(false);
        datasource = new TeamListFileDatasource("data", "team-list.csv");
        teamUserListDatasource = new TeamUserListFileDatasource("data", "team-user-list.csv");
        teamUserList = teamUserListDatasource.readData();
        dataContainer = (DataContainer) FXRouter.getData();
        eventName = (String) dataContainer.get("eventName");
        teamName = (String) dataContainer.get("teamName");
        username = (String) dataContainer.get("username");
        teamList = datasource.readData();
        team = teamList.findTeamByTeamName(teamName);
        showTeam(team);

        if (teamUserList.findTeamUser(eventName, teamName, username) != null) {
            joinButton.setVisible(false);
            joinLabel.setVisible(true);
        }
    }
    private void showTeam(Team team){
        teamNameLabel.setText(team.getTeamName());
        availableSeatLabel.setText(String.valueOf(team.getAvailableSeat()));
        teamMaxSeatLabel.setText(String.valueOf(team.getTeamMaxSeat()));
        teamRegisOpenLabel.setText(team.getRegistrationOpenDate());
        teamRegisCloseLabel.setText(team.getRegistrationCloseDate());
    }

    @FXML
    private void onBackBtnClick(){
        try {
            FXRouter.goTo("event-detail");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void onJoinTeamBtnClick(){
        if (teamUserList.findTeamUser(eventName, teamName, username) == null && !team.getAvailableSeat().equals(0)){
            teamUserList.addNewTeamUser(eventName, teamName, username);
            teamUserListDatasource.writeData(teamUserList);
            team.userJoin();
            datasource.writeData(teamList);
            TeamUser current = new TeamUser(eventName, teamName, username);
            try {
                FXRouter.goTo("team-table", current);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

