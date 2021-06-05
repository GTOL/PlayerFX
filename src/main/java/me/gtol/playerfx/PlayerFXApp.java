package me.gtol.playerfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import me.gtol.playerfx.ui.MusicPlayerPane;

public class PlayerFXApp extends Application {
	private Stage primaryStage;
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		FXMLLoader loader = new FXMLLoader(MusicPlayerPane.class.getResource("MusicPlayerPane.fxml"));
		BorderPane root = loader.load();
		
		Scene scene = new Scene(root, 400, 400);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("PlayerFX");
		primaryStage.show();
	}

}
