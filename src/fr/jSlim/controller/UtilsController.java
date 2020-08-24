package fr.jSlim.controller;

import java.util.ArrayList;
import java.util.List;

import fr.jSlim.models.cell.CellNode;
import fr.jSlim.models.cell.Square;
import fr.jSlim.models.grid.GridImpl;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class UtilsController {
	
	public static TextInputDialog getPathToCsv() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Choix du chemin");
		dialog.setContentText("Choisissez le chemin où sauvegarder ");
		return dialog;
	}
	 public static ChoiceDialog<String> getConfigNumberDialog(List<Integer> allConfigId) {
	        List<String> choices = new ArrayList<>();
	        for (int configId : allConfigId) {
				choices.add("" + configId);
			}
	        ChoiceDialog<String> dialog = new ChoiceDialog<>("Choisissez la configuration", choices);
	        dialog.setTitle("Choix de la configuration");
	        dialog.setContentText("Choisissez la configuration");

	        return dialog;
	    }
	public boolean isAnInteger(String chaine) {
		try {
			Integer.parseInt(chaine);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public void showAlert(String title, String content) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	public void displayColorToNode(GridImpl grid) {
		for (Square square : grid.getSquareGrid()) {
			CellNode node = getCellNode(square.getColumn(), square.getRow(), grid);
			if (null != node) {
				node.setFill(square.getState().getColor());
			}
		}
	}
	
	public CellNode getCellNode(int col, int row, GridImpl grid) {
		for (Node node : grid.getChildren())
			if (GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) != null
					&& GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col)
				return (CellNode) node;
		return null;
	}
}
