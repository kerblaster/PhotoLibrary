package controller;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import controller.LoginController;

public class Main extends Application {

	Stage window;
	
	/*
	 * Start application
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage){      
		window = primaryStage; 
		window.setTitle("Renard & Nikhil Photo Album");
		
		try{
			// load fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/Login.fxml"));
			AnchorPane pane = (AnchorPane) loader.load();
			
			// load controller
			LoginController controller = loader.getController(); //name of controller class
			controller.setMainStage(window); //main function in controller
			
			//set scene
			Scene scene = new Scene(pane, 700, 600);
			window.setScene(scene);
			window.show();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
