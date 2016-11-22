package interfaces;

import javafx.application.Platform;

public interface Quitable {
	default void quit(){
	    Platform.exit();
	    System.exit(0);		
	}
}
