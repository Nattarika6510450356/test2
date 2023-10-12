package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.models.collection.TimeScheduleList;
import cs211.project.services.Datasource;
import cs211.project.services.EventDatasource;
import cs211.project.services.FXRouter;
import cs211.project.services.TimeScheduleListFileDatasource;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;

public class EventSchedule2Controller {
    @FXML private TableView scheduleTableView;
    private String eventName;
    private EventUser eventUser;
    private TimeScheduleList timeScheduleList;
    private EventDatasource<TimeScheduleList> datasource;

    @FXML
    public void initialize() {
        eventUser = (EventUser) FXRouter.getData();
        eventName = eventUser.getEvent();

        datasource = new TimeScheduleListFileDatasource("data", "schedule-list.csv");
        timeScheduleList = datasource.readData();
        showTable(timeScheduleList);
    }

    @FXML
    public void showTable(TimeScheduleList timeScheduleList){
        TableColumn<TimeSchedule, String> activityNameColumn = new TableColumn<>("Activity Name");
        activityNameColumn.setCellValueFactory(new PropertyValueFactory<>("activityName"));

        TableColumn<TimeSchedule, String> activityDetailColumn = new TableColumn<>("Activity Detail");
        activityDetailColumn.setCellValueFactory(new PropertyValueFactory<>("activityDetail"));

        TableColumn<TimeSchedule, String> activityStartColumn = new TableColumn<>("Activity Start");
        activityStartColumn.setCellValueFactory(new PropertyValueFactory<>("activityStart"));

        TableColumn<TimeSchedule, String> activityFinishColumn = new TableColumn<>("Activity Finish");
        activityFinishColumn.setCellValueFactory(new PropertyValueFactory<>("activityFinish"));

        TableColumn<TimeSchedule, String> activityStatusColumn = new TableColumn<>("Activity Status");
        activityStatusColumn.setCellValueFactory(new PropertyValueFactory<>("activityStatus"));

        scheduleTableView.getColumns().clear();
        scheduleTableView.getColumns().add(activityNameColumn);
        scheduleTableView.getColumns().add(activityDetailColumn);
        scheduleTableView.getColumns().add(activityStartColumn);
        scheduleTableView.getColumns().add(activityFinishColumn);
        scheduleTableView.getColumns().add(activityStatusColumn);

        scheduleTableView.getItems().clear();

        for(TimeSchedule timeSchedule: timeScheduleList.getTimeSchedules()){
            LocalDate finDate = LocalDate.parse(timeSchedule.getActivityFinish());
            if(finDate.isBefore(LocalDate.now())) {
                timeSchedule.setActivityStatus("finish");
            }
            if (timeSchedule.getInTeam().equals("null") && timeSchedule.getScheduleInEvent().equals(eventName)) {
                scheduleTableView.getItems().add(timeSchedule);
            }
        }
    }

    @FXML
    protected void backBtnClick() {
        try {
            FXRouter.goTo("joined-event", eventUser.getUser());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
