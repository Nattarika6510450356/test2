package cs211.project.controllers;

import cs211.project.models.TimeSchedule;
import cs211.project.models.collection.TimeScheduleList;
import cs211.project.services.Datasource;
import cs211.project.services.EventDatasource;
import cs211.project.services.FXRouter;
import cs211.project.services.TimeScheduleListFileDatasource;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class CreateEventScheduleController {
    @FXML private TextField activityNameTextField;
    @FXML private  TextField activityDetailTextField;
    @FXML private DatePicker activityStart;
    @FXML private DatePicker activityFinish;
    @FXML private Label errorLabel;

    private EventDatasource<TimeScheduleList> datasource;
    private TimeScheduleList timeScheduleList;
    private TimeSchedule timeSchedule;
    private String eventName;

    @FXML
    public void initialize(){
        datasource = new TimeScheduleListFileDatasource("data", "schedule-list.csv");
        timeScheduleList = datasource.readData();
        eventName = (String) FXRouter.getData();
        errorLabel.setText("");
        if (!timeScheduleList.isEmpty()) {
            timeSchedule = timeScheduleList.getTimeSchedules().get(0);
        }
    }


    @FXML
    private void onCreateBtnClick(){
        String activityNameText = activityNameTextField.getText();
        String activityDetailText = activityDetailTextField.getText();
        String activityStartText = String.valueOf(activityStart.getValue());
        String activityFinishText = String.valueOf(activityFinish.getValue());
        String scheduleEventNameText = eventName;
        String activityStatus = "active";

        if (timeSchedule != null){
            if (!activityNameText.equals("") && !activityDetailText.equals("") && !activityStartText.equals("") && !activityFinishText.equals("") && !scheduleEventNameText.equals("")){
                timeScheduleList.addNewTimeSchedule(activityNameText, activityDetailText, activityStartText, activityFinishText, scheduleEventNameText, null, activityStatus);
                try {
                    datasource.writeData(timeScheduleList);
                    FXRouter.goTo("edit-schedule");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                activityNameTextField.setText("");
                activityDetailTextField.setText("");
                errorLabel.setText("Please fill all the field");
            }
        } else {
            System.err.println("Can't create Schedule");
        }

    }

    @FXML
    private void onBackBtnClick(){
        try {
            FXRouter.goTo("edit-schedule");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
