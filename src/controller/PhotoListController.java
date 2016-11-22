package controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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
 * Handles photo list viewer after selecting album
 * @author Renard Tumbokon, Nikhil Menon
 */
public class PhotoListController implements Alertable, Logoutable, Quitable{
	Stage window;
	Admin admin;
	User user;
	Album photoList;
	
	private int index;
	private ObservableList<String> tagObsList;
	
	@FXML private VBox thumbsVBox;
	@FXML private ListView<String> tagsListView;
	@FXML private ImageView bigImageView;
	@FXML private TextField captionTextField;
	@FXML private TextField tagTextField;
	@FXML private Text nameAlbumText;
	@FXML private Text dateAddedText;
	@FXML private ComboBox<String> albumComboBox;
	@FXML private Text counterText;
	
	/*
	 * Called by AlbumListController.java when select album
	 * @param Stage stage
	 * @param Admin admin
	 * @param User user
	 */
	public void setMainStage(Stage stage, Admin admin, User user, int albumIndex){
		this.window = stage;
		this.admin = admin;
		this.user = user;
		photoList = user.getAlbums().get(albumIndex);
		index = 0;
		nameAlbumText.setText(user.getName() + "'s " + photoList.getName()); //update album title
		setAlbumDropdown();
		refreshImageView();
	}
	
	/*
	 * Refresh bigImageView and thumbnail listview
	 */
	private void refreshImageView(){
		if (photoList.getSize() < 1){
			bigImageView.setImage(null);
			return;
		}
		bigImageView.setImage(photoList.getPhotoAtIndex(index).getImage()); //change image
		dateAddedText.setText("Date Created: " + photoList.getPhotoAtIndex(index).getDateStr()); //update header date
		//get new tags from image
		refreshTagView();
		//get caption from image
		captionTextField.setText(photoList.getAllPhotos().get(index).getCaption());
		counterText.setText((index+1) + "/" + photoList.getSize()); //set counter tracker
		//setThumbnailListView(); //set thumbnail list last
	}
	
	/*
	 * Refresh tagListView
	 */
	private void refreshTagView(){
		if (tagObsList != null && tagObsList.size() > 0){ //has stuff
			tagObsList.clear();
		}
		tagObsList = FXCollections.observableArrayList();
		for (Tag tag : photoList.getPhotoAtIndex(index).getTags()){
			tagObsList.add(tag.toString());	//add tags to listView
		}
		tagsListView.setItems(tagObsList);			
	}
	
	/*
	 * Set thumbnail TilePane UDNER COSNTRUCTIOn
	 */
	private void setThumbnailListView(){ 
		if (photoList.getSize() < 1){
			return;
		}				
        ListView<Photo> listView = new ListView<Photo>();
        ObservableList<Photo> items = FXCollections.observableArrayList();
		for (Photo photo : photoList.getAllPhotos()){
			items.add(photo); 
		}
        listView.setItems(items);
        listView.setCellFactory(param -> new ListCell<Photo>() {
        	private ImageView imageView = new ImageView();
            @Override
            public void updateItem(Photo p, boolean empty) {
                super.updateItem(p, empty);
                if (empty || p == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                	  imageView.setImage(p.getImage());
                      setText(p.getCaption());
                      setGraphic(imageView);                   	
                }
            }
        });
        VBox box = new VBox(listView);
        Scene scene = new Scene(box, 200, 200);
        window.setScene(scene);
        window.show();
        	/*
		listView.setMouseTransparent( true ); //make unselectable listview
		listView.setFocusTraversable( false );
		listView.getSelectionModel().select(index);//auto select index
		
		thumbsVBox = new VBox(listView); ; //works?*/
	}
	
	/*
	 * Set drop down of all albums user owns
	 */ 
	private void setAlbumDropdown(){
		if (albumComboBox.getItems() != null && albumComboBox.getItems().size() > 0){
			albumComboBox.getItems().clear();
		}
		for (Album album : user.getAlbums()){
				albumComboBox.getItems().add(album.getName());
		}
	}
	
	/*
	 * Save caption 
	 */
	private void saveCaption(){
		if (photoList.getSize() > 0){
			photoList.getPhotoAtIndex(index).setCaption(captionTextField.getText()); 	
		}
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
		saveCaption();
		index--;
		refreshImageView();
	}
	
	/*
	 * Go right (next) index to display in bigImageView
	 */
	@FXML
	private void handleRightButton(){ 
		if (index >= photoList.getSize()-1){
			return;
		}
		saveCaption();
		index++;
		refreshImageView();
	}	
	
	/*
	 * Make album out of search list
	 */
	@FXML
	private void handleAddPhotoButton(){ 
		//get photo from file
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select Image to Upload");
		fileChooser.setSelectedExtensionFilter(new ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif"));
		File file = fileChooser.showOpenDialog(window); //window?
		if (file.getAbsolutePath() == null){
			return; //no new photo added
		}
		Image newImage = new Image("file:"+file.getAbsolutePath());
		if (newImage.isError()){ //error while loading image
			alert(AlertType.ERROR, "Invalid Image", "Image file cannot be read");
			return;
		}
		Photo newPhoto = new Photo(newImage);
		photoList.addPhoto(newPhoto);
		index = photoList.getSize()-1; //should not be negative bc success add
		saveCaption(); //save caption of photo prev on
		refreshImageView();
	}
	
	/*
	 *  Delete photo from album
	 */
	@FXML
	private void handleDeletePhotoButton(){ 
		photoList.deletePhoto(index); //remove from current album
		if (index != 0){
			index--;
		}
		refreshImageView();		
	}
	
	/*
	 *  Add tag to photo 
	 */
	@FXML
	private void handleAddTagButton(){ 
		String input = tagTextField.getText();
		//tag:value init
		String typeValue[] = input.split(":"); 
		if (typeValue.length < 2){
			alert(AlertType.ERROR, "Invalid Tag", "Requires <Type>: <Value> pair");
			return;			
		}
		typeValue[0] = typeValue[0].replaceAll("\\s+", ""); //type
		typeValue[1] = typeValue[1].replaceAll("\\s+", ""); //value
		if (typeValue[0].equals("") || typeValue[1].equals("")){
			alert(AlertType.ERROR, "Invalid Tag", "Input non-empty valid Type: Value pair");
			return;
		}
		Tag tagToFind = new Tag(typeValue[0], typeValue[1]);
		//get selected photo
		photoList.getPhotoAtIndex(index).addTag(tagToFind);
		refreshTagView();
	}
	
	/*
	 *  Delete selected tag from photo 
	 */
	@FXML
	private void handleDeleteTagButton(){ 
		int tagIndex = tagsListView.getSelectionModel().getSelectedIndex();	
		if (tagIndex == -1){ //empty or nonselected listview
			alert(AlertType.ERROR, "Invalid Delete", "No tag selected");
			return;			
		}
		photoList.getPhotoAtIndex(index).deleteTag(tagIndex);	
		refreshTagView();
	}	
	
	/*
	 *  Copy photo to another album
	 */
	@FXML
	private void handleCopyPhotoButton(){ 
		int albumIndex = albumComboBox.getSelectionModel().getSelectedIndex();
		if (photoList.getSize() < 1){ //empty photolist 
			alert(AlertType.ERROR, "Invalid Image", "No image exists");
			return;			
		}
		if (albumIndex == -1){ //empty or nonselected listview
			alert(AlertType.ERROR, "Invalid Copy", "No album selected");
			return;			
		}
		if (user.getAlbum(albumIndex).getName().equals(photoList.getName())){ //same album error 
			alert(AlertType.ERROR, "Invalid Copy", "Same album selected");
			return;			
		}
		user.getAlbum(albumIndex).addPhoto(photoList.getPhotoAtIndex(index));
		alert(AlertType.INFORMATION, "Copy Success", "The image has been copied successfully");
	}	
	
	/*
	 *  Move photo to another album
	 */
	@FXML
	private void handleMovePhotoButton(){ 
		int albumIndex = albumComboBox.getSelectionModel().getSelectedIndex();
		if (photoList.getSize() < 1){ //empty photolist 
			alert(AlertType.ERROR, "Invalid Image", "No image exists");
			return;			
		}
		if (albumIndex == -1){ //empty or nonselected listview
			alert(AlertType.ERROR, "Invalid Copy", "No album selected");
			return;			
		}
		if (user.getAlbum(albumIndex).getName().equals(photoList.getName())){ //same album error 
			alert(AlertType.ERROR, "Invalid Copy", "Same album selected");
			return;			
		}
		user.getAlbum(albumIndex).addPhoto(photoList.getPhotoAtIndex(index));
		alert(AlertType.INFORMATION, "Copy Success", "The image has been copied successfully");
		handleDeletePhotoButton(); //delete photo
	}
		
	/*
	 * Go back to AlbumList
	 */
	@FXML
	private void handleAlbumListWindowButton(){
		saveCaption();
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
		saveCaption();
		logout(window, admin);
	}
	
	@FXML
	private void handleQuitButton(){
		saveCaption();
		quit();
	}
}