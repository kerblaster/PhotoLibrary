package controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import model.Admin;
import model.Album;
import model.Photo;
import model.Tag;
import interfaces.Logoutable;
import interfaces.Quitable;
import interfaces.Alertable;
import model.User;

/*
 * Handles album list from given user
 * 
 * @author Renard Tumbokon, Nikhil Menon
 */
public class AlbumListController implements Alertable, Logoutable, Quitable{
	Stage window;
	Admin admin; 
	User user;
	private ObservableList<String> obsList;
	
	@FXML private ListView<String> albumListView;
	@FXML private Text headerText; 
	@FXML private TextField addAlbumNameField;
	@FXML private TextField renameAlbumField;
	@FXML private TextField typeValueField;
	@FXML private DatePicker fromDatePicker;
	@FXML private DatePicker toDatePicker;
	
	/*
	 * Called by LoginController.java and AdminController.java
	 * @param Stage stage
	 * @param User user
	 * @param Admin admin
	 */
	public void setMainStage(Stage stage, Admin admin, User user){
		this.window = stage;
		this.admin = admin;
		this.user = user;
		
		headerText.setText(user.getName() + "'s Album List");
		refreshObsList();
	}
	
	/*
	 * Build obslist again to display album list
	 */
	public void refreshObsList(){
		if (obsList != null && obsList.size() > 0){
			obsList.clear();
		}
		obsList = FXCollections.observableArrayList();
		for (Album album : user.getAlbums()){
			obsList.add(album.toString());	
		}
		albumListView.setItems(obsList);		 
	}
	
	/*
	 * Open selected album ERROR
	 */
	@FXML
	private void handleOpenAlbumButton(){
		if (albumListView.getSelectionModel().getSelectedIndex() == -1){ //empty or nonselected listview
			alert(AlertType.ERROR, "Invalid Album", "No album selected");
			return;			
		}
		int albumIndex = albumListView.getSelectionModel().getSelectedIndex();	
		try{
			//load fxml
			FXMLLoader loader = new FXMLLoader();			
			loader.setLocation(getClass().getResource("/view/PhotoList.fxml")); //change
			AnchorPane pane = (AnchorPane) loader.load();	
			// load controller
			PhotoListController controller = loader.getController(); //change
			controller.setMainStage(window, admin, user, albumIndex); //change
			//set scene
			Scene scene = new Scene(pane, 700, 600);
			window.setScene(scene);
			window.show();	
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/*
	 * Rename selected album
	 */
	@FXML
	private void handleRenameAlbumButton(){
		String rename = renameAlbumField.getText();
		if (rename.equals("")){
			alert(AlertType.ERROR, "Invalid Rename", "Cannot rename album to empty string");
			return;			
		}
		if (albumListView.getSelectionModel().getSelectedIndex() == -1){ //empty or nonselected listview
			alert(AlertType.ERROR, "Invalid Album", "No album selected");
			return;			
		}
		int albumIndex = albumListView.getSelectionModel().getSelectedIndex();	
		user.renameAlbumAtIndex(albumIndex, rename);
		refreshObsList();
	}
	
	/*
	 * Add album to list
	 */
	@FXML
	private void handleAddAlbumButton(){
		String albumToAdd = addAlbumNameField.getText();
		if (albumToAdd.equals("")){
			alert(AlertType.ERROR, "Invalid Album Name", "Cannot name album an empty string");
			return;			
		}
		user.addAlbum(new Album(albumToAdd));
		refreshObsList();
	}
	
	/*
	 * Delete album from list
	 */
	@FXML
	private void handleDeleteAlbumButton(){
		if (albumListView.getSelectionModel().getSelectedIndex() == -1){ //empty or nonselected listview
			alert(AlertType.ERROR, "Invalid Album", "No album selected");
			return;			
		}
		int albumIndex = albumListView.getSelectionModel().getSelectedIndex();	
		user.getAlbums().remove(albumIndex);
		refreshObsList();
	}
	
	/*
	 * Search for date range in each album's photos
	 */
	@FXML
	private void handleSearchButton(){
		List<Photo> foundList = new ArrayList<Photo>(); //starts empty, gets added
		String query = ""; //search query
		
		if (user.getAlbums().size() < 1){ //no albums to search into
			alert(AlertType.ERROR, "Invalid Search", "There are no albums to search");
			return;
		}
		
		//tag:value init
		String input = typeValueField.getText();
		if (input.isEmpty() && fromDatePicker.getValue() == null && toDatePicker.getValue() == null) { // date input error check
			alert(AlertType.ERROR, "Invalid Input", "Input data required from either tag:value or date");
			return;
		}
		if (fromDatePicker.getValue() != null && toDatePicker.getValue() != null && fromDatePicker.getValue().isAfter(toDatePicker.getValue())) {
			alert(AlertType.ERROR, "Invalid Dates", "Dates must be in chronological order");
			return;
		}
		if (!input.isEmpty() && !input.contains(":")){
			alert(AlertType.ERROR, "Invalid Input", "Input value must be separated by ':' from input pair");
			return;
		}
		String typeValue[] = input.split(":"); 
		if (!input.isEmpty() && (typeValue[0] == null || typeValue[1] == null)){
			alert(AlertType.ERROR, "Invalid Input", "Tag must be in type:value pair");
			return;
		}
		typeValue[0] = typeValue[0].replaceAll("\\s+", ""); //type
		typeValue[1] = typeValue[1].replaceAll("\\s+", ""); //value

		Tag tagToFind = new Tag(typeValue[0], typeValue[1]);
		
		//date init
		LocalDate from, to;
		String fromStr = ""; 
		String toStr = "";
		
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd"); //format date to str
		if (fromDatePicker.getValue() == null){
			from = LocalDate.MIN;
			fromStr = "past";
		} else{
			from = fromDatePicker.getValue();
			fromStr = from.format(format);
		}
		if (toDatePicker.getValue() == null){
			to = LocalDate.MAX;
			toStr = "present";
		} else{
			to = toDatePicker.getValue();
			toStr = to.format(format);
		}
		for (Album album : user.getAlbums()){
			for (Photo photo : album.getAllPhotos()){
				if (input.isEmpty()){
					if (photo.isBetweenDates(from, to)){ //only search dates
						foundList.add(photo);
					}
				} else{
					if (photo.hasTag(tagToFind) && photo.isBetweenDates(from, to)){ //search tag
						foundList.add(photo);
					}
				}
			}
		}	
		query = "'" + input + "' <" + fromStr + " to " + toStr + ">";
		
		//check if a result exists
		if (foundList.size() < 1){
			alert(AlertType.ERROR, "No Results", "There are 0 search results");
			return;
		}
		
		try{
			// load fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/SearchList.fxml")); //change
			AnchorPane pane = (AnchorPane) loader.load();	
			// load controller
			SearchListController controller = loader.getController(); //change
			controller.setMainStage(window, admin, user, foundList, query); //change
			// set scene
			Scene scene = new Scene(pane, 700, 600);
			window.setScene(scene);
			window.show();		
		} catch (IOException e){
			e.printStackTrace();
		}		
	}
	
	@FXML
	private void handleLogoutButton(){
		logout(window, admin);
	}
	
	@FXML
	private void handleQuitButton(){
		quit();
	}
}
