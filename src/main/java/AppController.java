import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class AppController {

    @FXML
    public Label label;
    @FXML
    public Button button;

    public void click(ActionEvent actionEvent) {
        System.out.println("Button clicked");
        label.setText("HELLO JAVAFX");
        button.setScaleY(5.0);
        button.setScaleX(5.0);

        button.setText("PRESSED");
    }
}
