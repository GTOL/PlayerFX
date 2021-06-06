package me.gtol.playerfx;

import javafx.application.Application;
import javafx.stage.Stage;
import me.gtol.playerfx.main.PlayerFX;

public class PlayerFXApp extends Application {
	private PlayerFX playerFX;

	@Override
	public void init() throws Exception {
		playerFX = new PlayerFX();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		playerFX.start(primaryStage);
	}
}
