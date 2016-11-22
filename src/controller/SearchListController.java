package controller;

import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Admin;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;
import interfaces.Alertable;
import interfaces.Logoutable;
import interfaces.Quitable;

/*
 * Handles search list from album list search
 * @author Renard Tumbokon, Nikhil Menon
 */
public class SearchListController implements Alertable, Logoutable, Quitable{
	Stage window;
	Admin admin;
	User user;
	
	private int index;
	private List<Photo> searchResults; 
	private ObservableList<String> tagObsList;
	
	@FXML private ListView<Photo> thumbnailListView;
	@FXML private ListView<String> tagsListView;
	@FXML private Text searchText;
	@FXML private ImageView bigImageView;
	@FXML private Text captionText;
	@FXML private TextField albumNameTextField;
	@FXML private Text dateAddedText;
	@FXML private Text counterText;
	@FXML private Button leftButton;
	@FXML private Button rightButton;
	
	/*
	 * Called by AlbumListController.java when searching
	 * @param Stage stage
	 * @param Admin admin
	 * @param User user
	 * @param List foundList
	 */
	public void setMainStage(Stage stage, Admin admin, User user, List<Photo> foundList, String query){
		this.window = stage;
		this.admin = admin;
		this.user = user;
		index = 0;
		searchResults = foundList;
		searchText.setText("QUERY: "+ query); //set main text
		if (searchResults.size() < 1){ //size is empty
			searchText.setText(searchText.getText() + "(empty)");
			return;
		}
		setThumbnailListView();
		refreshImageView();
	}
	
	/*
	 * Refresh bigImageView and thumbnail listview
	 */
	private void refreshImageView(){
		if (searchResults.size() < 1){
			bigImageView.setImage(null);
			return;
		}
		bigImageView.setImage(searchResults.get(index).getImage()); //change image
		dateAddedText.setText("Date Created: " + searchResults.get(index).getDateStr()); //update header date
		//get new tags from image
		refreshTagView();
		//get caption from image
		captionText.setText(searchResults.get(index).getCaption());
		counterText.setText((index+1) + "/" + searchResults.size()); //set counter tracker
		//set grayed out buttons
		if (index >= searchResults.size()-1){
			rightButton.setDisable(true);
		} else{
			rightButton.setDisable(false);
		}
		if (index <= 0){
			leftButton.setDisable(true);
		} else{
			leftButton.setDisable(false);
		}
		setThumbnailListView(); //set thumbnail list last
	}
	
	/*
	 * Refresh tagListView
	 */
	private void refreshTagView(){
		if (tagObsList != null && tagObsList.size() > 0){ //has stuff
			tagObsList.clear();
		}
		tagObsList = FXCollections.observableArrayList();
		for (Tag tag : searchResults.get(index).getTags()){
			tagObsList.add(tag.toString());	//add tags to listView
		}
		tagsListView.setItems(tagObsList);			
	}
	
	/*
	 * Set thumbnail ListView UNDER CONSTRUCTION
	 */
	private void setThumbnailListView(){ 
		if (searchResults.size() < 1){
			return;
		}				
        ObservableList<Photo> items = FXCollections.observableArrayList();
		for (Photo photo : searchResults){
			items.add(photo); 
		}
		thumbnailListView.setItems(items);
		thumbnailListView.setCellFactory(param -> new ListCell<Photo>() {
        	private ImageView imageView = new ImageView();
            @Override
            public void updateItem(Photo p, boolean empty) {
                super.updateItem(p, empty);
                if (empty || p == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                	  imageView.setImage(p.getImage());
                	  imageView.setPreserveRatio(true);
                	  imageView.setFitHeight(100);
                	  imageView.setFitWidth(100);
                      setText(p.getCaption());
                      setGraphic(imageView);                   	
                }
            }
        });
		thumbnailListView.setFocusTraversable(false);
		thumbnailListView.getSelectionModel().select(index);//auto select index
		thumbnailListView.scrollTo(index);
	}
	
	/*
	 * Go left (prev) index to display in bigImageView
	 */
	@FXML
	private void handleLeftButton(){
		if (index <= 0){
			index = 0;
			return;
		}
		index--;
		refreshImageView();
	}
	
	/*
	 * Go right (next) index to display in bigImageView
	 */
	@FXML
	private void handleRightButton(){
		if (index >= searchResults.size()-1){
			return;
		}
		index++;
		refreshImageView();
	}	
	
	/*
	 * Make album out of search list
	 */
	@FXML
	private void handleMakeAlbumButton(){
		String input = albumNameTextField.getText();
		if (input.equals("")){
			alert(AlertType.ERROR, "Invalid Album Name", "Input non-empty album name");
			return;
		}
		for (Album album : user.getAlbums()){
			if (album.getName().equals(input)){
				alert(AlertType.ERROR, "Album Name Already Exists", "Input unique album name");
				return;
			}
		}
		Album newAlbum = new Album(albumNameTextField.getText(), searchResults);
		user.addAlbum(newAlbum);
		alert(AlertType.INFORMATION, "Creation Success", "New album successfully created!");
		try{ // go back to album list
			// load fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/AlbumList.fxml")); //change
			AnchorPane pane = (AnchorPane) loader.load();	
			// load controller
			AlbumListController controller = loader.getController(); //change
			controller.setMainStage(window, admin, user); //change
			// set scene
			Scene scene = new Scene(pane, 700, 600);
			window.setScene(scene);
			window.show();		
		} catch (IOException e){
			e.printStackTrace();
		}	
	}
	
	/*
	 * Go back to AlbumList
	 */
	@FXML
	private void handleAlbumListWindowButton(){
		try{
			// load fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/AlbumList.fxml")); //change
			AnchorPane pane = (AnchorPane) loader.load();	
			// load controller
			AlbumListController controller = loader.getController(); //change
			controller.setMainStage(window, admin, user); //change
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
		quit(admin);
	}
}