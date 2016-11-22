package interfaces;

import javafx.scene.control.Alert;

/*
 * Interface for popup alerts
 * @author Renard Tumbokon, Nikhil Menon
 */
public interface Alertable {
	default void alert(Alert.AlertType alertType, String title, String content){
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(title);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
