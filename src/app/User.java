package app;

import java.util.ArrayList;

public class User {
	public String username;
	public ArrayList<PhotoAlbum> PhotoAlbum;

	public User(String username){
		this.username = username;
		PhotoAlbum = new ArrayList<PhotoAlbum>();
	}
}
