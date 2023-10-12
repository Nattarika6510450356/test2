package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.models.collection.*;
import cs211.project.services.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class EditTeamController {
    @FXML private ChoiceBox<String> statusChoiceBox;
    @FXML private TableView userTableView;
    @FXML private TableView scheduleTableView;
    @FXML private Label usernameLabel;
    @FXML private Label activityNameLabel;
    @FXML private Label detailLabel;
    @FXML private Label activityLabel;
    @FXML private TableView commentTableView;
    @FXML private TextField yourCommentTextField;
    private Datasource<UserList> userListDatasource;
    private UserList userList;
    private User selectedUser;
    private EventDatasource<TimeScheduleList> timeScheduleListDatasource;
    private TimeScheduleList timeScheduleList;
    private TimeSchedule selectedActivity;
    private Datasource<TeamUserList> teamUserListDatasource;
    private TeamUser currTeam;
    private TeamUserList teamUserList;
    private EventDatasource<TeamList> teamListDatasource;
    private TeamList teamList;
    private Datasource<TeamCommentList> teamCommentListDatasource;
    private TeamCommentList teamCommentList;
    private String teamName;
    private String eventName;
    private String currentUser;
    private String currentActivity;

    @FXML
    public void initialize() {
        usernameLabel.setText("");
        activityNameLabel.setText("");
        activityLabel.setText("");
        detailLabel.setText("");

        currTeam = (TeamUser) FXRouter.getData(); //ชื่ออีเว้นกับทีม
        teamName = currTeam.getTeamName();
        eventName = currTeam.getEventName();
        currentUser = currTeam.getUserName();
        System.out.println(currentUser);

        userListDatasource = new UserListFileDatasource("data", "user-list.csv");
        userList = userListDatasource.readData();
        timeScheduleListDatasource = new TimeScheduleListFileDatasource("data", "schedule-list.csv");
        timeScheduleList = timeScheduleListDatasource.readData();
        teamUserListDatasource = new TeamUserListFileDatasource("data", "team-user-list.csv");
        teamUserList = teamUserListDatasource.readData();
        teamListDatasource = new TeamListFileDatasource("data", "team-list.csv");
        teamList = teamListDatasource.readData();
        teamCommentListDatasource = new TeamCommentListFileDatasource("data", "comment-list.csv");
        teamCommentList = teamCommentListDatasource.readData();


        showUserTable(userList);
        userTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue observableValue, User oldValue, User newValue) {
                selectedUser = newValue;
                if (newValue  != null) {
                    usernameLabel.setText(newValue.getUsername());
                }
            }
        });

        showActivityTable(timeScheduleList);
        scheduleTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TimeSchedule>() {
            @Override
            public void changed(ObservableValue observableValue, TimeSchedule oldValue, TimeSchedule newValue) {
                selectedActivity = newValue;
                if (newValue != null) {
                    currentActivity = newValue.getActivityName();
                    activityNameLabel.setText(newValue.getActivityName());
                    activityLabel.setText(newValue.getActivityName());
                    detailLabel.setText(newValue.getActivityDetail());
                    showCommentTable(teamCommentList);
                }
            }
        });
        statusChoiceBox.setItems(FXCollections.observableArrayList("active", "finish"));
        statusChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldStatus, String newStatus) {
                selectedActivity.setActivityStatus(newStatus);
                timeScheduleListDatasource.writeData(timeScheduleList);
                scheduleTableView.refresh();
            }
        });
    }

    private void showUserTable(UserList userList) {
        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        userTableView.getColumns().clear();
        userTableView.getColumns().add(usernameColumn);
        userTableView.getColumns().add(nameColumn);
        userTableView.getItems().clear();

        for (User user: userList.getUsers()) {
            if (teamUserList.findTeamUser(currTeam.getEventName(), currTeam.getTeamName(), user.getUsername()) != null) {
                userTableView.getItems().add(user);
            }
        }
    }

    private void showActivityTable(TimeScheduleList activityList) {
        TableColumn<TimeSchedule, String> activityNameColumn = new TableColumn<>("Name");
        activityNameColumn.setCellValueFactory(new PropertyValueFactory<>("activityName"));
        TableColumn<TimeSchedule, String> detailColumn = new TableColumn<>("Detail");
        detailColumn.setCellValueFactory(new PropertyValueFactory<>("activityDetail"));
        TableColumn<TimeSchedule, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("activityStatus"));

        scheduleTableView.getColumns().clear();
        scheduleTableView.getColumns().add(activityNameColumn);
        scheduleTableView.getColumns().add(detailColumn);
        scheduleTableView.getColumns().add(statusColumn);
        scheduleTableView.getItems().clear();

        for (TimeSchedule activity: activityList.getTimeSchedules()) {
            if (activity.getInTeam().equals(currTeam.getTeamName())) {
                scheduleTableView.getItems().add(activity);
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

        if (selectedActivity != null){
            String selectedActivityName = selectedActivity.getActivityName();
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
        String commentText = yourCommentTextField.getText();
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
            yourCommentTextField.setText("");
            showCommentTable(teamCommentListDatasource.readData());

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
    private void banBtnClick() {
        teamUserList.getTeamUser().remove(teamUserList.findTeamUser(currTeam.getEventName(), currTeam.getTeamName(), selectedUser.getUsername()));
        teamUserListDatasource.writeData(teamUserList);
        userTableView.getItems().remove(selectedUser);
        Team team = teamList.findTeamByTeamName(currTeam.getTeamName());
        team.userBan();
        teamListDatasource.writeData(teamList);
        selectedUser = null;
        usernameLabel.setText("");
    }
    @FXML
    private void createBtnClick() {
        try {
            FXRouter.goTo("create-team-schedule", currTeam);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void delBtnClick() {
        timeScheduleList.getTimeSchedules().remove(selectedActivity);
        timeScheduleListDatasource.writeData(timeScheduleList);
        scheduleTableView.getItems().remove(selectedActivity);
        selectedActivity = null;
        activityNameLabel.setText("");
        detailLabel.setText("");
        statusChoiceBox.setValue("");
    }
}
