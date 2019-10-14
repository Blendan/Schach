module Schach {
	requires javafx.controls;
	requires  javafx.fxml;

	opens schach to javafx.fxml;
	exports schach;
}