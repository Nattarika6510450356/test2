module cs211.project {
    requires javafx.controls;
    requires javafx.fxml;


    opens cs211.project.cs211661project to javafx.fxml;
    exports cs211.project.cs211661project;

    exports cs211.project.controllers;
    opens cs211.project.controllers to javafx.fxml;

    exports cs211.project.models;
    opens cs211.project.models to javafx.base;
    exports cs211.project.models.collection;
    opens cs211.project.models.collection to javafx.base;
}