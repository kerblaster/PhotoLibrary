package model;

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
public class Admin implements Alertable, Logoutable, Quitable{
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
	
}
