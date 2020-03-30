package dk.fitfit.solitaire;

import dk.fitfit.solitaire.view.FXApplication;
import javafx.application.Application;
import javafx.stage.Stage;

public class LaunchFX extends Application {
    @Override
    public void start(Stage stage) {
        new FXApplication(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
