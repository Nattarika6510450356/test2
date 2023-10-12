package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.models.collection.TeamCommentList;
import cs211.project.models.collection.TimeScheduleList;
import cs211.project.services.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class TeamScheduleController {

    @FXML
    private TableView teamActivityTableView;
    @FXML
    private TableView commentTableView;
    @FXML
    private Label activityNameLabel;
    @FXML
    private TextField yourcommentTextField;
    private TeamCommentList teamCommentList;
    private Datasource<TeamCommentList> teamCommentListDatasource;
    private TimeScheduleList timeScheduleList;
    private EventDatasource<TimeScheduleList> timeScheduleListDatasource;
    private TeamUser current;
    private String teamName;
    private String eventName;
    private String currentUser;
    private String currentActivity;
    private TimeSchedule selectedItem;
    private TeamComment teamComment;

    @FXML
    public void initialize() {
        teamCommentListDatasource = new TeamCommentListFileDatasource("data", "comment-list.csv");
        timeScheduleListDatasource = new TimeScheduleListFileDatasource("data", "schedule-list.csv");
        teamCommentList = teamCommentListDatasource.readData();
        timeScheduleList = timeScheduleListDatasource.readData();
        for (TeamComment comment: teamCommentList.getTeamComments()) {
            System.out.println(comment.getActivityName());
        }
        current = (TeamUser) FXRouter.getData();
        teamName = current.getTeamName();
        eventName = current.getEventName();
        currentUser = current.getUserName();
        activityNameLabel.setText("");
        showTable(timeScheduleList);
        showCommentTable(teamCommentList);
        if (!teamCommentList.isEmpty()){
            teamComment = teamCommentList.getTeamComments().get(0);
        }

        teamActivityTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TimeSchedule>() {
            @Override
            public void changed(ObservableValue observable, TimeSchedule oldValue, TimeSchedule newValue) {
                if (newValue == null){
                    activityNameLabel.setText("");
                    selectedItem = null;
                } else {
                    selectedItem = newValue;
                    activityNameLabel.setText(newValue.getActivityName());
                    currentActivity = newValue.getActivityName();
                    showCommentTable(teamCommentList);
                }
            }
        });
    }

    @FXML
    public void showTable(TimeScheduleList timeScheduleList) {
        TableColumn<TimeSchedule, String> activityNameColumn = new TableColumn<>("Activity Name");
        activityNameColumn.setCellValueFactory(new PropertyValueFactory<>("activityName"));

        TableColumn<TimeSchedule, String> activityDetailColumn = new TableColumn<>("Activity Detail");
        activityDetailColumn.setCellValueFactory(new PropertyValueFactory<>("activityDetail"));

        TableColumn<TimeSchedule, String> activityStatusColumn = new TableColumn<>("Activity Status");
        activityStatusColumn.setCellValueFactory(new PropertyValueFactory<>("activityStatus"));

        teamActivityTableView.getColumns().clear();
        teamActivityTableView.getColumns().add(activityNameColumn);
        teamActivityTableView.getColumns().add(activityDetailColumn);
        teamActivityTableView.getColumns().add(activityStatusColumn);

        teamActivityTableView.getItems().clear();

        for (TimeSchedule timeSchedule : timeScheduleList.getTimeSchedules()) {
            if (timeSchedule.getInTeam().equals(teamName) && timeSchedule.getActivityStatus().equals("active")) {
                teamActivityTableView.getItems().add(timeSchedule);
            }
        }
        for (TimeSchedule timeSchedule : timeScheduleList.getTimeSchedules()) {
            if (timeSchedule.getInTeam().equals(teamName) && timeSchedule.getActivityStatus().equals("finish")) {
                teamActivityTableView.getItems().add(timeSchedule);
            }
        }
    }

    @FXML
    public void showCommentTable(TeamCommentList teamCommentList) {
        TableColumn<TeamComment, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<TeamComment, String> commentColumn = new TableColumn<>("Comment");
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        commentTableView.getColumns().clear();
        commentTableView.getColumns().add(usernameColumn);
        commentTableView.getColumns().add(commentColumn);
        commentTableView.getItems().clear();

        if (selectedItem != null){
            String selectedActivityName = selectedItem.getActivityName();
            for (TeamComment teamComment : teamCommentList.getTeamComments()) {
                if (teamComment.getActivityName().equals(selectedActivityName)) {
                    commentTableView.getItems().add(teamComment);
                }
            }

        }
    }

    @FXML
    private void commentButtonClick(){
        String usernameText = currentUser;
        String eventNameText = eventName;
        String teamNameText = teamName;
        String activityNameText = currentActivity;
        String commentText = yourcommentTextField.getText();
        if (!commentText.equals("")){
            TeamCommentList commentList = new TeamCommentList();
            commentList.addNewComment(usernameText, eventNameText, teamNameText, activityNameText, commentText);
            System.out.println("Current User: " + usernameText);
            System.out.println("Current Event: " + eventNameText);
            System.out.println("Team Name: " + teamNameText);
            System.out.println("Activity Name: " + activityNameText);
            System.out.println("Comment Text: " + commentText);
            System.out.println("-------------------");
            teamCommentListDatasource.writeData(commentList);
            yourcommentTextField.setText("");
            showCommentTable(teamCommentListDatasource.readData());
        }
    }

    @FXML
    private void onBackBtnClick() {
        EventUser eventUser = new EventUser(eventName, currentUser);
        try {
            FXRouter.goTo("event-detail", eventUser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
