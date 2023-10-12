package cs211.project.controllers;

import cs211.project.models.Events;
import cs211.project.models.collection.EventsList;
import cs211.project.services.Datasource;
import cs211.project.services.EventsListFileDatasource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import cs211.project.services.FXRouter;
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

public class CreateEventController {
    @FXML
    private TextField eventNameTextField;
    @FXML
    private TextField detailsTextField;
    @FXML
    private TextField maxSeatTextField;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker finishDate;
    @FXML
    private ImageView uploadEventImageView;

    private EventsList eventsList;
    private Events events;
    private Datasource<EventsList> datasource;
    private String currentUser;
    private String eventImagePathText;


    @FXML
    public void initialize(){
        datasource = new EventsListFileDatasource("data", "events-list.csv");
        eventsList = datasource.readData();
        currentUser = (String) FXRouter.getData();

        events = new Events(null,null,0,0,null,null,null,null,null);
        events.setEventImagePath("images/event/samplePic.png");
        events.setStatus("active");
    }



    @FXML
    protected void uploadEventImageBtnClick(ActionEvent event){
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
                uploadEventImageView.setImage(new Image(target.toUri().toString()));
                events.setEventImagePath(destDir + "/" + filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    protected void createButtonClick() {
        String eventNameText = eventNameTextField.getText();
        String eventDetailText = detailsTextField.getText();
        Integer maxSeatTextFieldText = Integer.valueOf(maxSeatTextField.getText());
        String startDateText = String.valueOf(startDate.getValue());
        String finishDateText = String.valueOf(finishDate.getValue());
        String eventCreatorUsernameText = currentUser;
        String eventImagePathText = events.getEventImagePath();
        String statusText = events.getStatus();

        if (events != null) {
            eventsList.addNewEvent(eventNameText, eventDetailText, maxSeatTextFieldText, maxSeatTextFieldText, startDateText, finishDateText, eventImagePathText, eventCreatorUsernameText, statusText);

            try {
                datasource.writeData(eventsList);
                FXRouter.goTo("user-profile", currentUser);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.err.println("events is null, cannot create event.");
        }
    }

    @FXML
    private void onBackBtnClick(){
        try {
            FXRouter.goTo("user-profile",currentUser );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}