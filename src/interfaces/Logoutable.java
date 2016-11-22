package interfaces;

import java.io.IOException;

import controller.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Admin;

public interface Logoutable {
	default void logout(Stage window, Admin admin){
		try{
			// load fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/Login.fxml"));
			AnchorPane pane = (AnchorPane) loader.load();	
			// load controller
			LoginController controller = loader.getController();
			controller.setMainStage(window, admin); 
			// set scene
			Scene scene = new Scene(pane, 700, 600);
			window.setScene(scene);
			window.show();		
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}
