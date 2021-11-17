package PresentationLayer;

import java.io.Serializable;

public class Alert implements Serializable {

    public void alert(String title, String header, String content) {
        javafx.scene.control.Alert invalidInput = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        invalidInput.setTitle(title);
        invalidInput.setHeaderText(header);
        invalidInput.setContentText(content);
        invalidInput.showAndWait();
    }

}
