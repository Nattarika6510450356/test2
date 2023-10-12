package cs211.project.controllers;

import cs211.project.models.TimeSchedule;
import cs211.project.models.collection.TimeScheduleList;
import cs211.project.services.Datasource;
import cs211.project.services.EventDatasource;
import cs211.project.services.FXRouter;
import cs211.project.services.TimeScheduleListFileDatasource;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class EditEventScheduleController {
    @FXML
    private TableView timeScheduleTable;
    private TimeScheduleList timeScheduleList;
    private EventDatasource<TimeScheduleList> datasource;
    private String eventName;
    @FXML
    private ChoiceBox<String> activityStatusChoiceBox;//add
    private TimeSchedule selectedTimeSchedule;


    @FXML
    public void initialize(){
        eventName = (String) FXRouter.getData();
        datasource = new TimeScheduleListFileDatasource("data", "schedule-list.csv");
        timeScheduleList = datasource.readData();
        showTable(timeScheduleList);

        timeScheduleTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TimeSchedule>() {
            @Override
            public void changed(ObservableValue observableValue, TimeSchedule oldValue, TimeSchedule newValue) {
                selectedTimeSchedule = newValue;
            }
        });

        activityStatusChoiceBox.setItems(FXCollections.observableArrayList("active", "finish"));
        activityStatusChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldStatus, String newStatus) {
                selectedTimeSchedule.setActivityStatus(newStatus);
                datasource.writeData(timeScheduleList);
                timeScheduleTable.refresh();
            }
        });
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

        timeScheduleTable.getColumns().clear();
        timeScheduleTable.getColumns().add(activityNameColumn);
        timeScheduleTable.getColumns().add(activityDetailColumn);
        timeScheduleTable.getColumns().add(activityStartColumn);
        timeScheduleTable.getColumns().add(activityFinishColumn);
        timeScheduleTable.getColumns().add(activityStatusColumn);

        timeScheduleTable.getItems().clear();

        for(TimeSchedule timeSchedule: timeScheduleList.getTimeSchedules()){
            if (timeSchedule.getScheduleInEvent().equals(eventName) && timeSchedule.getInTeam().equals("null")){
                if (timeSchedule.getScheduleInEvent().equals(eventName)){
                    timeScheduleTable.getItems().add(timeSchedule);
                }
            }
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
    @FXML
    private void onCreateScheduleBtnClick(){
        try {
            FXRouter.goTo("create-schedule", eventName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void delBtnClick() {
        timeScheduleList.getTimeSchedules().remove(selectedTimeSchedule);
        datasource.writeData(timeScheduleList);
        timeScheduleTable.getItems().remove(selectedTimeSchedule);
        selectedTimeSchedule = null;
    }


}
