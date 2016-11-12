package app;

import java.util.ArrayList;

public class Admin{
	public ArrayList<User> userList;
	public ArrayList<PhotoAlbum> photoAlbum;

	public Admin(){
		userList = new ArrayList<User>(); //unique to admin
		photoAlbum = new ArrayList<PhotoAlbum>();
	}
}
