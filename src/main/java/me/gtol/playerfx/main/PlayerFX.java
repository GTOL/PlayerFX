package me.gtol.playerfx.main;

import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import me.gtol.playerfx.model.Model;
import me.gtol.playerfx.ui.MusicPlayerPane;

public class PlayerFX {
	private Stage	primaryStage;
	private Model	primaryModel;

	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryModel = new Model();
		FXMLLoader loader = new FXMLLoader(MusicPlayerPane.class.getResource("MusicPlayerPane.fxml"));
		BorderPane root = loader.load();
		MusicPlayerPane rootController = loader.getController();
		rootController.setMain(this);

		Scene scene = new Scene(root, 400, 400);

		primaryStage.setScene(scene);
		primaryStage.setTitle("PlayerFX");
		primaryStage.show();
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public Model getPrimaryModel() {
		return primaryModel;
	}

	public void updateRecentFiles(Path recentFile) {
		final Deque<Path> queue = new ArrayDeque<>(20);
		queue.addAll(primaryModel.getRecentFiles());
		queue.remove(recentFile);
		if (queue.size() == 20) {
			queue.pollLast();
			queue.offerFirst(recentFile);
		}
		primaryModel.getRecentFiles().setAll(queue);
	}
}
