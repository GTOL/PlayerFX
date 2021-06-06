package me.gtol.playerfx.ui;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;
import me.gtol.playerfx.main.PlayerFX;

public class MusicPlayerPane implements Initializable {
	MediaPlayer player;

	@FXML
	SplitMenuButton load;

	@FXML
	MenuItem clearHistory;

	@FXML
	Button play;

	@FXML
	Slider timeSlider;

	@FXML
	Button mute;

	@FXML
	Slider volumeSlider;

	@FXML
	Label currentTimeLabel;

	@FXML
	Label totalTimeLabel;

	private ListChangeListener<Path> recentFilesListener = change -> {
		updateRecentFiles(change.getList());
	};

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// 当时间轴被拖动后，调整播放时间
		timeSlider.valueChangingProperty().addListener((ob, o, n) -> {
			if (player != null) {
				if (o && !n) {
					player.seek(Duration.millis(timeSlider.getValue()));
					player.getCurrentTime();
				}
			}
		});
		// 当在时间轴上点击时，调整播放时间
		timeSlider.valueProperty().addListener((ob, o, n) -> {
			if (player != null) {
				if (Math.abs(n.doubleValue() - o.doubleValue()) > 500) {
					if (!timeSlider.isValueChanging()) {
						player.seek(Duration.millis(timeSlider.getValue()));
						player.getCurrentTime();
					}
				}
			}
		});
		volumeSlider.valueProperty().addListener((ob, o, n) -> {
			if (player != null && player.isMute())
				player.setMute(false);
		});
	}

	public void setMain(PlayerFX main) {
		main.getPrimaryModel().getRecentFiles().addListener(new WeakListChangeListener<>(recentFilesListener));
		updateRecentFiles(main.getPrimaryModel().getRecentFiles());
	}

	@FXML
	public void loadClicked() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open");
		chooser.getExtensionFilters().addAll(new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.m4a"));
		File file = new File(
				"C:\\Users\\GT\\Music\\iTunes\\iTunes Media\\Music\\Coldplay\\Live 2012\\09 Viva la Vida (Live).m4a");
		Media media = new Media(file.toURI().toString());
		if (player != null) {
			player.dispose();
		}
		player = new MediaPlayer(media);
		player.volumeProperty().bind(volumeSlider.valueProperty());

		player.setOnReady(() -> {
			final Duration duration = player.getMedia().getDuration();
			timeSlider.setMax(duration.toMillis());
			totalTimeLabel.setText(duration2str(duration));
			player.currentTimeProperty().addListener(ob -> {
				if (!timeSlider.isValueChanging()) {
					timeSlider.setValue(player.getCurrentTime().toMillis());
					currentTimeLabel.setText(duration2str(player.getCurrentTime()));
				}
			});

			player.statusProperty().addListener((ob, o, n) -> {
				if (n == Status.PLAYING) {
					play.setText("Pause");
				} else {
					play.setText("Play");
				}
			});

			player.muteProperty().addListener((ob, o, n) -> {
				if (n) {
					mute.setText("Unmute");
				} else {
					mute.setText("Mute");
				}
			});

			player.setOnEndOfMedia(() -> {
				if (player.getCurrentCount() == player.getCycleCount()) {
					player.stop();
					player.seek(Duration.ZERO);
				}
			});

			player.play();
		});
	}

	@FXML
	public void playClicked() {
		if (player != null) {
			if (player.getStatus() == Status.PLAYING) {
				player.pause();
			} else {
				player.play();
			}
		}
	}

	private String duration2str(Duration duration) {
		if (duration.toHours() > 1) {
			int hours = (int) duration.toHours();
			int minutes = (int) (duration.toMinutes() % 60);
			int seconds = (int) (duration.toSeconds() % 60);
			return String.format("%02d:%02d:%02d", hours, minutes, seconds);
		} else {
			int minutes = (int) (duration.toMinutes() % 60);
			int seconds = (int) (duration.toSeconds() % 60);
			return String.format("%02d:%02d", minutes, seconds);
		}
	}

	@FXML
	public void muteClicked() {
		if (player != null) {
			player.setMute(!player.isMute());
		}
	}

	private void updateRecentFiles(List<? extends Path> recentFiles) {
		List<MenuItem> items = recentFiles.stream()
				.map(p -> new MenuItem(p.getFileName().toString()))
				.collect(Collectors.toList());
		load.getItems().setAll(items);
		load.getItems().add(new SeparatorMenuItem());
		load.getItems().add(clearHistory);
		clearHistory.setDisable(items.isEmpty());
	}
}
