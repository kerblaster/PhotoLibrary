package controller;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import model.Admin;
import interfaces.Alertable;
import interfaces.Logoutable;
import interfaces.Quitable;
import model.User;

/*
 * Handles user list from admin privileges
 * @author Renard Tumbokon, Nikhil Menon
 */
public class AdminController implements Alertable, Logoutable, Quitable{
	Stage window;
	Admin admin;
	private ObservableList<String> obsList;
	
	@FXML private ListView<String> userListView;
	@FXML private TextField userField;

	
	/*
	 * Called by LoginController.java
	 * @param Stage stage
	 * @param Admin admin
	 */
	public void setMainStage(Stage stage, Admin admin){
		this.window = stage;
		this.admin = admin;
		
		refreshObsList();
	}
	
	/*
	 * Build obslist again to display user list
	 */
	public void refreshObsList(){
		if (obsList != null && obsList.size() > 0){
			obsList.clear();
		}
		obsList = FXCollections.observableArrayList();
		for (User userToAdd : admin.getUserList()){
			obsList.add(userToAdd.getName());	
		}
		userListView.setItems(obsList);		
	}
	
	/*
	 * When add button is pressed
	 */
	@FXML
	private void handleAddUserButton(){
		String userToAdd = userField.getText(); 
		if (userToAdd.equals("")){ //empty or nonselected listview
			alert(AlertType.ERROR, "Invalid Name", "Enter a valid name");
			return;			
		}
		admin.addUserToList(new User(userToAdd));
		refreshObsList();
	}	
	
	/*
	 * When delete button is pressed
	 */
	@FXML
	private void handleDeleteUserButton(){
		int indexToDelete = userListView.getSelectionModel().getSelectedIndex();
		if (indexToDelete == -1){ //empty or nonselected listview
			alert(AlertType.ERROR, "Invalid User", "No user selected");
			return;			
		}
		if (admin.getUserList().get(indexToDelete).getName().equals("admin")){
			alert(AlertType.ERROR, "Admin Deletion", "Cannot delete the admin user (self)");
			return;
		}
		admin.deleteUserFromList(indexToDelete);
		refreshObsList();
	}
	
	/*
	 * When "Go to Album List"
	 */
	@FXML
	private void handleAdminAlbumListButton(){
		try{
			// load fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/AlbumList.fxml")); //change
			AnchorPane pane = (AnchorPane) loader.load();	
			// load controller
			AlbumListController controller = loader.getController();  //change
			controller.setMainStage(window, admin, admin.self()); //change
			// set scene
			Scene scene = new Scene(pane, 700, 600);
			window.setScene(scene);
			window.show();		
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/*
	 * When logout button is selected
	 */
	@FXML
	private void handleLogoutButton(){
		logout(window, admin);
	}
	
	/*
	 * When quit button is selected
	 */
	@FXML
	private void handleQuitButton(){
		quit(admin);
	}
}
