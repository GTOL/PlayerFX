module me.gtol.playerfx {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.media;
	requires transitive javafx.graphics;
	
	opens me.gtol.playerfx.ui to javafx.fxml;
	exports me.gtol.playerfx;
}