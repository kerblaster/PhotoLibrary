package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert.AlertType;

import interfaces.Alertable;
import interfaces.Logoutable;
import interfaces.Quitable;

/*
 * Admin model
 * @author: Renard Tumbokon, Nikhil Menon
 */
public class Admin implements Alertable, Logoutable, Quitable, Serializable{

	private static final long serialVersionUID = 7810104385687138317L;
	
	public static final String storeDir = "data";
	public static final String storeFile = "admin.dat";
	
	private List<User> userList;
	private User admin;

	/*
	 *  constructor	
	 */
	public Admin(){
		userList = new ArrayList<User>(); //unique to admin
		admin = new User("admin");
		userList.add(admin);
	}

	/*
	 *  Getter for list of users
	 *  @return List<user>	
	 */
	public List<User> getUserList(){
		return userList;
	}

	/*
	 *  Setter for list of users
	 * @param user
	 * @return boolean isSuccess
	 */
	public boolean addUserToList(User userToAdd){
		for (User user : getUserList()){
			if (user.getName().equals(userToAdd.getName())){
				alert(AlertType.ERROR, "User Already Exists", "Enter a unique username");
				return false;
			}
		}
		userList.add(userToAdd);
		return true;
	}
	
	/*
	 *  Delete user from list of users
	 * @param index
	 * @return boolean isSuccess
	 */
	public boolean deleteUserFromList(int index){
		if (index > userList.size()){
			alert(AlertType.ERROR, "Invalid User Delete", "Select a valid user to delete");
			return false;
		}
		userList.remove(index);
		return true;
	}
	
	/*
	 *  Acts as user
	 *  @return user
	 */
	public User self(){
		return admin;
	}
	
	/* SERIALIZATION WRITE
	 * Write admin model date into admin.dat, overwrite
	 * @param admin
	 * @throws IOException
	 */
	public static void write(Admin admin) throws IOException{ //admin.write(admin);
		ObjectOutputStream oos = new ObjectOutputStream(
			new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(admin);
		oos.close();
	}
	
	/* SERIALIZATION READ
	 * Read admin.dat file, return admin model with data
	 * @return Admin
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Admin read() throws IOException, ClassNotFoundException{
		ObjectInputStream ois = new ObjectInputStream(
			new FileInputStream(storeDir + File.separator + storeFile));
		Admin admin = (Admin)ois.readObject();
		ois.close();
		return admin;
		
	}
	
}
