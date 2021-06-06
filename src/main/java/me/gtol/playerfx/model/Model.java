package me.gtol.playerfx.model;

import java.nio.file.Path;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
	private final ObservableList<Path> recentFiles = FXCollections.observableArrayList();

	public ObservableList<Path> getRecentFiles() {
		return recentFiles;
	}
}
