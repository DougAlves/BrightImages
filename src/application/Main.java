package application;
	
import org.opencv.core.Core;

import application.controllers.OcrController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {
	
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Ocr Light Bill");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("views/Main.fxml"));
			AnchorPane root = (AnchorPane) loader.load();
			
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
			OcrController controller = new OcrController();
			controller.setMainStage(this);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
