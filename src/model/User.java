package model;

import java.util.ArrayList;
import java.util.List;

import interfaces.Alertable;
import javafx.scene.control.Alert.AlertType;

/*
 *  User model
 *  @author Renard Tumbokon, Nikhil Menon
 */
public class User implements Alertable {
	private String username;
	private List<Album> albums;

	/*
	 *  constructor
	 *  @param String username	
	 */
	public User(String username){
		this.username = username;
		albums = new ArrayList<Album>();
	}
	
	/*
	 *  Name getter
	 *  @return String username
	 */
	public String getName(){
		return username;
	}
	
	/*
	 *  Album getter
	 *  @return List<PhotoAlbum> albums
	 */
	public List<Album> getAlbums(){
		return albums;
	}
	
	/*
	 * Album setter 
	 * @param String name
	 */
	public void addAlbum(String name){
		if (albumNameExists(name)){
			alert(AlertType.ERROR, "Album Name Already Exists", "Enter a unique album name");
			return;
		}
		albums.add(new Album(name));
	}
	
	/*
	 * Album setter
	 * @param PhotoAlbum newAlbum
	 */
	public void addAlbum(Album newAlbum){
		if (albumNameExists(newAlbum.getName())){
			alert(AlertType.ERROR, "Album Name Already Exists", "Enter a unique album name");
			return;
		}
		albums.add(newAlbum);
	}
	
	/*
	 * Album renamer
	 */
	public void renameAlbumAtIndex(int index, String newName){
		if (albumNameExists(newName)){
			alert(AlertType.ERROR, "Album Name Already Exists", "Enter a unique album name");
			return;
		}
		albums.get(index).setName(newName);
	}
	
	/*
	 * Album deletor
	 * @param PhotoAlbum album
	 */
	public void deleteAlbum(Album album){
		albums.remove(album);
	}
	
	/*
	 * Add photo to album using index of album list
	 * @param Photo
	 * @param int indexAlbum
	 */
	public void addPhoto(Photo photo, int indexAlbum){
		albums.get(indexAlbum).addPhoto(photo);
	}
	
	/*
	 * Add photo to album using album name
	 * @param Photo photo
	 * @param String nameAlbum
	 */
	public void addPhoto(Photo photo, String nameAlbum){
		Album album = getAlbum(nameAlbum);
		if (album == null){
			return;
		}
		album.addPhoto(photo);
	}
	
	/*
	 * Finds album if it exists
	 * @param String name
	 */
	public boolean albumNameExists(String name){
		for (Album album : albums){
			if (album.getName().toLowerCase().equals(name.toLowerCase())){
				return true;
			}
		}
		return false;
	}	
	
	/**
	 * Get album by name
	 * @param String name
	 * @return Return PhotoAlbum
	 */
	public Album getAlbum(String name) {
		for(Album album : albums)
		{
			if(album.getName().equals(name))
				return album;
		}
		return null;
	}
	
	/*
	 * Get album by index
	 */
	public Album getAlbum(int index){
		if (index > albums.size()){
			alert(AlertType.ERROR, "Invalid index", "Cannot get album with this index");
			return null;
		}
		return albums.get(index);
	}
	
	
}
