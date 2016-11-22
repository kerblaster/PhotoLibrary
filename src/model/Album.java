package model;

import java.util.List;

import interfaces.Alertable;
import javafx.scene.control.Alert.AlertType;

import java.util.ArrayList;

/*
 * PhotoAlbum model
 * @author Renard Tumbokon, Nikhil Menon
 */
public class Album implements Alertable{
	private String name;
	private Photo oldestPhoto;
	private Photo newestPhoto;
	private List<Photo> photos;
	
	/*
	 * Constructor
	 * @param String name
	 */
	public Album(String name){
		this.name = name;
		this.photos = new ArrayList<Photo>();
		refreshInfo();
	}
	
	/*
	 * Constructor
	 * @param String name
	 * @param List<Photo> photos
	 */
	public Album(String name, List<Photo> photos){
		this.name = name;
		this.photos = photos; //for new album from search function
		refreshInfo();
	}
	
	/*
	 * Add photo
	 * @param Photo photo
	 */
	public boolean addPhoto(Photo photo){
		if (photos.add(photo)){
			refreshInfo();
			return true;
		}
		return false;
	}
	
	/*
	 * Delete photo
	 * @param Delete photo
	 */
	public boolean deletePhoto(Photo photo){
		if (photos.remove(photo)){
			refreshInfo();
			return true;
		}
		return false;
	}

	/*
	 * Delete photo
	 * @param Delete photo
	 */
	public boolean deletePhoto(int index){
		if (index >= photos.size()){
			alert(AlertType.ERROR, "Invalid Index", "Cannot delete photo due to invalid index");
			return false;
		}
		photos.remove(index);
		return true;
	}
	
	/*
	 * Set name
	 * @param String name
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/*
	 * Get name
	 * @return String name;
	 */
	public String getName(){
		return name;
	}
	
	/*
	 * Get photo at index
	 */
	public Photo getPhotoAtIndex(int index){
		if (index >= photos.size()){
			alert(AlertType.ERROR, "Invalid Index", "Cannot get index of photo");
			return null;
		}
		return photos.get(index);
	}
	
	/*
	 * Get size of photos list
	 * @return int photos.size()
	 */
	public int getSize(){
		return photos.size();
	}
	
	/*
	 * Get oldest date
	 * @return String 
	 */
	public String getOldestDate(){
		if (oldestPhoto == null){
			return "n/a";
		}
		return oldestPhoto.getDateStr();
	}
	
	/*
	 * Get newest date
	 * @return String 
	 */
	public String getNewestDate(){
		if (newestPhoto == null){
			return "n/a";
		}
		return newestPhoto.getDateStr();
	}
	
	/*
	 * Get list of all photos
	 * @return List<Photo>
	 */
	public List<Photo> getAllPhotos(){
		return photos;
	}
	
	/*
	 * Find photo with given tag
	 * @param String tag
	 * @return List<Photo> 
	 */
	public List<Photo> findPhotos(Tag tag){
		List<Photo> searchList = new ArrayList<Photo>();
		for (Photo photo : photos){
			if (photo.findTag(tag)){
				searchList.add(photo);
			}
		}
		return searchList; 
	}
	
	/*
	 * Find oldest photo and newest photo and update values
	 */
	private void refreshInfo(){
		if (photos.size() == 0){
			return;
		}
		oldestPhoto = photos.get(0);
		newestPhoto = photos.get(0);
		for (Photo photo : photos){ // find oldest
			if (photo.getDate().isAfter(oldestPhoto.getDate())){
				oldestPhoto = photo;
			}
			if (photo.getDate().isBefore(newestPhoto.getDate())){
				newestPhoto = photo;
			}
		}
	}
	
	/*
	 * String output when AlbumListController calls it
	 * @return String
	 */
	public String toString(){
		return "Name: " + getName() + " - " + getSize() + " photos - Oldest Photo: " 
				+ getOldestDate() + " - Range: " + getNewestDate() + " to " + getOldestDate();
	}
}
