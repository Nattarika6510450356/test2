package cs211.project.controllers;

import cs211.project.models.TeamUser;
import cs211.project.models.TimeSchedule;
import cs211.project.models.collection.TimeScheduleList;
import cs211.project.services.Datasource;
import cs211.project.services.EventDatasource;
import cs211.project.services.FXRouter;
import cs211.project.services.TimeScheduleListFileDatasource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


import java.io.IOException;
public class CreateTeamScheduleController {
    @FXML
    private TextField activityNameTextField;
    @FXML
    private  TextField activityDetailTextField;
    @FXML
    private Label errorLabel;

    private EventDatasource<TimeScheduleList> timeScheduleListDatasource;
    private TimeScheduleList timeScheduleList;
    private TimeSchedule timeSchedule;
    private TeamUser currTeam;

    @FXML
    public void initialize() {
        timeScheduleListDatasource = new TimeScheduleListFileDatasource("data", "schedule-list.csv");
        timeScheduleList = timeScheduleListDatasource.readData();
        currTeam = (TeamUser) FXRouter.getData();
        errorLabel.setText("");
    }

    @FXML
    private void createBtnClick() {
        String activityNameText = activityNameTextField.getText();
        String activityDetailText = activityDetailTextField.getText();
        String activityStatus = "active";

        if (!activityNameText.equals("") && !activityDetailText.equals("")) {
            timeScheduleList.addNewTimeSchedule(activityNameText, activityDetailText,"0001-01-01", "0001-01-01", currTeam.getEventName(), currTeam.getTeamName(),activityStatus);
            try {
                timeScheduleListDatasource.writeData(timeScheduleList);
                FXRouter.goTo("edit-team", currTeam);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            errorLabel.setText("Please fill all the field");
        }
    }
    @FXML
    private void backBtnClick(){
        try {
            FXRouter.goTo("edit-team", currTeam);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
