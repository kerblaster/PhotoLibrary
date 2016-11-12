package app;

import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;

public class PhotoAlbum {
	public String name;
	private Date oldestDate;
	private Date newestDate;
	private ArrayList<Photo> photos;
	
	
	public PhotoAlbum(String name){
		this.name = name;
		this.photos = new ArrayList<Photo>();
		refreshInfo();
	}
	public PhotoAlbum(String name, ArrayList<Photo> photos){
		this.name = name;
		this.photos = photos; //for new album from search function
		refreshInfo();
	}
	
	public boolean addPhoto(Photo photo){
		if (photos.add(photo)){
			refreshInfo();
			return true;
		}
		return false;
	}
	
	public boolean deletePhoto(Photo photo){
		if (photos.remove(photo)){
			refreshInfo();
			return true;
		}
		return false;
	}
	
	public int getSize(){
		return photos.size();
	}
	
	public String getOldestDate(){
		return;
	}
	public String getNewestDate(){
		return ;
	}
	
	public ArrayList<Photo> getAllPhotos(){
		return photos;
	}
	
	// Find photos from tag
	public ArrayList<Photo> findPhotos(String tag){
		ArrayList<Photo> searchList = new ArrayList<Photo>();
		return searchList; 
	}
	// Find photos from date range
	public ArrayList<Photo> findPhotos(int dateFrom, int dateTo){
		ArrayList<Photo> searchList = new ArrayList<Photo>();
		return searchList; 		
	}
	
	private void refreshInfo(){
		//go through arraylist and find oldest date, newest date
		//have condition for 0 pictures
	}
}
