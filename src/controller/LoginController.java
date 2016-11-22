package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import interfaces.Alertable;
import model.Admin;
import model.User;

/*
 * Handles login page
 */
public class LoginController implements Alertable{
	Stage window;
	Admin admin;
	
	@FXML private Button loginButton;
	@FXML private TextField userField;
	
	/*
	 * Called by main() when program launches
	 * @param Stage
	 */
	public void setMainStage(Stage stage){
		window = stage; 
		try {
			admin = Admin.read(); // see if can read
		} catch (ClassNotFoundException e) {
			admin = new Admin(); // set new admin for entire program
			//e.printStackTrace();
		} catch (IOException e) {
			admin = new Admin(); // set new admin for entire program
			//e.printStackTrace();
		}
	}
	
	/*
	 * Called by logout button. MUST be called or defaults to new admin
	 * @param Stage
	 * @param admin
	 */
	public void setMainStage(Stage stage, Admin admin){
		window = stage; 
		this.admin = admin; 
	}
	
	/*
	 * When login button is clicked
	 */
	@FXML 
	private void handleLoginButton(){
		String userToFind = userField.getText();
		for (User user : admin.getUserList()){
			if (user.getName().equals(userToFind)){
				try{
					// load fxml
					FXMLLoader loader = new FXMLLoader();			
					if (userToFind.equals("admin")){ //admin
						loader.setLocation(getClass().getResource("/view/Admin.fxml"));
						AnchorPane pane = (AnchorPane) loader.load();	
						// load controller
						AdminController controller = loader.getController();
						controller.setMainStage(window, admin); 
						//set scene
						Scene scene = new Scene(pane, 700, 600);
						window.setScene(scene);
						window.show();		
					} else{ //normal user
						loader.setLocation(getClass().getResource("/view/AlbumList.fxml"));
						AnchorPane pane = (AnchorPane) loader.load();	
						// load controller
						AlbumListController controller = loader.getController();
						controller.setMainStage(window, admin, user); 
						//set scene
						Scene scene = new Scene(pane, 700, 600);
						window.setScene(scene);
						window.show();	
					}	
					return;
				} catch (IOException e){
					e.printStackTrace();
				}
			}
		}
		alert(AlertType.ERROR, "Invalid Username", "Enter a correct username, or 'admin' for admin privilages");
	}
}
