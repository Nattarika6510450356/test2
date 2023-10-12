package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class OrganizerController {
    @FXML private ImageView pic1;
    @FXML private ImageView pic2;
    @FXML private ImageView pic3;
    @FXML private ImageView pic4;

    @FXML
    protected void onBackBtnClick() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
