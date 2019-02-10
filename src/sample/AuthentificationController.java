package sample;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AuthentificationController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField loginField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button signInButton;

    @FXML
    private Button registerButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void signInButtonPushed() {

    }

    public void registerButtonPushed() {
        transitionScene();
    }

    private void transitionScene(){
        FadeTransition transition = new FadeTransition();
        transition.setDuration(Duration.millis(1000));
        transition.setNode(rootPane);
        transition.setFromValue(1);
        transition.setToValue(0);
        transition.setOnFinished(e-> loadNextScene());
        transition.play();
    }

    private void loadNextScene(){
        try {
            Parent secondView;
            secondView = (VBox) FXMLLoader.load(getClass().getResource("/register.fxml"));
            Scene scene2 = new Scene(secondView);
            Stage currentStage = (Stage) rootPane.getScene().getWindow();
            currentStage.setScene(scene2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
