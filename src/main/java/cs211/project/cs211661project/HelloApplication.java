package cs211.project.cs211661project;

import javafx.application.Application;
import javafx.stage.Stage;
import cs211.project.services.FXRouter;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        configRoute();

        FXRouter.bind(this, stage, "CS211 661 Project");
        FXRouter.goTo("login");

    }

    private static void configRoute() {
        String resourcesPath = "cs211/project/views/";
        FXRouter.when("register", resourcesPath + "registration-page-view.fxml");
        FXRouter.when("organizer", resourcesPath + "organizer-view.fxml");
        FXRouter.when("events", resourcesPath + "ongoing-events-view.fxml");
        FXRouter.when("login", resourcesPath + "login-page-view.fxml");
        FXRouter.when("user-profile", resourcesPath + "userprofile-view.fxml");
        FXRouter.when("edit-profile", resourcesPath + "edit-profile-view.fxml");
        FXRouter.when("event-detail", resourcesPath + "event-view.fxml");
        FXRouter.when("create-event", resourcesPath + "create-event.fxml");
        FXRouter.when("admin-view-user-list", resourcesPath + "admin-view-user-list-view.fxml");
        FXRouter.when("joined-event", resourcesPath + "joined-event-view.fxml");
        FXRouter.when("create-team", resourcesPath + "create-team-view.fxml");
        FXRouter.when("edit-event", resourcesPath + "edit-event-view.fxml");
        FXRouter.when("admin-change-password", resourcesPath + "admin-change-password-view.fxml");
        FXRouter.when("create-schedule", resourcesPath + "create-event-schedule-view.fxml");
        FXRouter.when("event-schedule", resourcesPath + "event-schedule-view.fxml");
        FXRouter.when("event-schedule2", resourcesPath + "event-schedule-2-view.fxml");
        FXRouter.when("edit-schedule", resourcesPath + "edit-event-schedule-view.fxml");
        FXRouter.when("team-detail", resourcesPath + "team-view.fxml");
        FXRouter.when("team-table", resourcesPath + "team-schedule-view.fxml");
        FXRouter.when("team-table2", resourcesPath + "team-schedule-2-view.fxml");
        FXRouter.when("add-team", resourcesPath + "create-team-schedule-view.fxml");
        FXRouter.when("event-manage-user", resourcesPath + "event-manage-user-view.fxml");
        FXRouter.when("edit-team-schedule", resourcesPath + "edit-team-schedule-view.fxml");
        FXRouter.when("edit-team", resourcesPath + "edit-team-view.fxml");
        FXRouter.when("create-team-schedule", resourcesPath + "create-team-schedule-view.fxml");
        FXRouter.when("instruction", resourcesPath + "instruction-view.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}