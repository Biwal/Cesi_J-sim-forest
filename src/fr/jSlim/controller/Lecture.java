package fr.jSlim.controller;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Lecture extends Application {

	@Override
	public void start(Stage stage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/fr/jSlim/view/Homepage.fxml"));
		Scene scene = new Scene(root);
		String css = Lecture.class.getResource("/fr/jSlim/view/application.css").toExternalForm();
		scene.getStylesheets().add(css);
		stage.setTitle("Projet JAVA - G.Massonnat / J.Biteau");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) throws IOException {
		launch(args);
	}
}
