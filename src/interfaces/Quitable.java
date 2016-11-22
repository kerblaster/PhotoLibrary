package interfaces;

import java.io.IOException;

import javafx.application.Platform;
import model.Admin;

public interface Quitable {
	default void quit(Admin admin){
		try {
			Admin.write(admin); //serialized save
		} catch (IOException e) {
			System.out.println("Error in serializing admin in Quittable interface");
			e.printStackTrace();
		} 
	    Platform.exit();
	    System.exit(0);		
	}
}
