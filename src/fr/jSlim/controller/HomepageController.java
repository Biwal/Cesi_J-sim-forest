package fr.jSlim.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import fr.jSlim.models.algorithm.Updater;
import fr.jSlim.models.algorithm.UpdaterFire;
import fr.jSlim.models.algorithm.UpdaterForest;
import fr.jSlim.models.algorithm.UpdaterInsects;
import fr.jSlim.models.cell.Square;
import fr.jSlim.models.cell.SquareImpl;
import fr.jSlim.models.dao.ConfigurationDAO;
import fr.jSlim.models.dao.SquareDAO;
import fr.jSlim.models.enums.State;
import fr.jSlim.models.grid.CSVFile;
import fr.jSlim.models.grid.Configuration;
import fr.jSlim.models.grid.GridImpl;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class HomepageController implements Initializable {

	private State currentState = State.VOID;
	private Configuration configuration;
	private CSVFile csvFile;
	private SquareDAO squareDAO = new SquareDAO();
	private ConfigurationDAO configurationDAO = new ConfigurationDAO();
	private UtilsController utilsController = new UtilsController();
	private int cycleNumberInt = 0;

	@FXML
	private AnchorPane grille;
	private GridImpl grid;
	@FXML
	private Label currentCycle, simFire, simInsect;
	@FXML
	private TextField longueur, largeur, cycleNumber, cycleTime;
	@FXML
	private RadioButton jeunePousse, arbuste, arbre, feu, insectes, vide;
	@FXML Label dVoid, dSprout, dShrub, dTree, dFire, dAsh, dInsects; 
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		newGrid(null);
	}

	public void initDLabels() {
		dVoid.setText("0");
		dSprout.setText("0");
		dShrub.setText("0");
		dTree.setText("0");
		dFire.setText("0");
		dAsh.setText("0");
		dInsects.setText("0");
	}
	
	
	@FXML
	void exportGrid(MouseEvent event) {
		configuration.setTurns(getCycleNumber());
		configurationDAO.persist(this.configuration);
		for (int i = 0; i < grid.getSquareGrid().size(); i++) {
			grid.getSquareGrid().get(i).setConfiguration(configuration);
			squareDAO.persist((SquareImpl) grid.getSquareGrid().get(i));
		}
		utilsController.showAlert("Fini !", "La configuration a été sauvegardée avec succès !");
	}

	@FXML
	void importGrid(MouseEvent event) {
		List<Configuration> allConfig = configurationDAO.list();
		List<Integer> allConfigId = new ArrayList<Integer>();
		for (Configuration config : allConfig) {
			allConfigId.add(config.getId());
		}
		ChoiceDialog<String> dialog = UtilsController.getConfigNumberDialog(allConfigId);
		Optional<String> result = dialog.showAndWait();
		result.ifPresent(s -> { 
			Configuration configuration = configurationDAO.findById(Integer.parseInt(s));
			configuration.toString();
			grille.getChildren().clear();
			GridImpl tmp = new GridImpl(configuration.getRows(), configuration.getColumns(), grille.getPrefWidth(), grille.getPrefHeight(), this);
			grille.getChildren().add(tmp);
			grid = tmp;
			grid.setMaxSize(847.0, 757.0);
			setLargeur(configuration.getRows());
			setLongueur(configuration.getColumns());
			setCycleTime(configuration.getSpeed());
			setCurrentCycle(configuration.getTurns());
			List<Square> nouveaux = squareDAO.findByConfiguration(Integer.parseInt(s));
			for (Square nouveau :nouveaux ) {
				grid.getSquare(nouveau.getRow(), nouveau.getColumn()).setState(nouveau.getState());
			}
			utilsController.displayColorToNode(grid);
			  this.csvFile = new CSVFile(grid);
		        try {
		        	updateDLabel();
		            this.csvFile.initCSV();
		            this.csvFile.addOneRow();
		        } catch (FileNotFoundException e) {
		            e.printStackTrace();
		        }
		});
	}

	@FXML
	void exportCSV(MouseEvent event) {
		Dialog<String> dialog = UtilsController.getPathToCsv();
		Optional<String> result = dialog.showAndWait();
		result.ifPresent(s -> {
			this.csvFile.setUrl(s);
			try {
				this.csvFile.exportCSV();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@FXML
	private void newGrid(MouseEvent event) {
		grille.getChildren().clear();
		setCurrentCycle(0);
		currentState = State.VOID;
		int row = getLongueur();
		int col = getLargeur();
		setCycleTime(getCycleTime());
		configuration = new Configuration(0, row, col);
		GridImpl tmp = new GridImpl(row, col, grille.getPrefWidth(), grille.getPrefHeight(), this);
		grille.getChildren().add(tmp);
        grid = tmp;
        grid.setHgap(0);
        grid.setVgap(0);
        double maxRow = grille.getPrefHeight() / row;
        double maxWidth = grille.getPrefWidth() / col;

        RowConstraints rConstraints = new RowConstraints(maxRow);
        ColumnConstraints cConstraints = new ColumnConstraints(maxWidth);

        for (int i = 0; i < col; i++) {
            tmp.getColumnConstraints().add(cConstraints);
        }
        for (int i = 0; i < row; i++) {
            tmp.getRowConstraints().add(rConstraints);
        }

        this.csvFile = new CSVFile(grid);
        try {
            this.csvFile.initCSV();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        jeunePousse.setSelected(true);
        this.currentState = State.SPROUT;
     initDLabels();
    }

	@FXML
	void nextStep(MouseEvent event) {
		this.propagation();
	
	}

	@FXML
	void simulation() {
		
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		
		cycleNumberInt = getCycleNumber();
		Runnable r = () -> {
			this.propagation();
			
			

			cycleNumberInt--;
			if (cycleNumberInt <= 0) {
				executor.shutdown();
			}
		};
		executor.scheduleWithFixedDelay(r, 0, getCycleTime(), TimeUnit.MILLISECONDS);

	}

	private void propagation() {
		int row = this.configuration.getRows();
		int col = this.configuration.getColumns();

		Updater update = new UpdaterFire(col, row);
		List<Square> nouveaux = update.update(grid.getSquareGrid());
		for (Square nouveau : nouveaux) {
			grid.getSquare(nouveau.getRow(), nouveau.getColumn()).setState(nouveau.getState());
			grid.getSquare(nouveau.getRow(), nouveau.getColumn()).setGrowthShrub(nouveau.isGrowthShrub());
		}

		update = new UpdaterInsects(col, row);
		nouveaux = update.update(grid.getSquareGrid());
		for (Square nouveau : nouveaux) {
			grid.getSquare(nouveau.getRow(), nouveau.getColumn()).setState(nouveau.getState());
		}

		update = new UpdaterForest(col, row);
		nouveaux = update.update(grid.getSquareGrid());
		for (Square nouveau : nouveaux) {
			grid.getSquare(nouveau.getRow(), nouveau.getColumn()).setState(nouveau.getState());
		}

		utilsController.displayColorToNode(grid);

		this.csvFile.addOneRow();
		incrementCycle();
		Platform.runLater(() -> this.updateDLabel());
	}

	private void incrementCycle() {
	
		int cycle = getCurrentCycle();
		setCurrentCycle(cycle + 1);
	}

	

	@FXML
	void quit(MouseEvent event) {
		Platform.exit();
	}

	@FXML
	void videSelect(ActionEvent event) {
		this.currentState = State.VOID;
		
	}

	@FXML
	void tree1Select(ActionEvent event) {
		this.currentState = State.SPROUT;
	}

	@FXML
	void tree2Select(ActionEvent event) {
		this.currentState = State.SHRUB;
	}

	@FXML
	void tree3Select(ActionEvent event) {
		this.currentState = State.TREE;
	}

	@FXML
	void fireSelect(ActionEvent event) {
		this.currentState = State.FIRE;
	}

	@FXML
	void insectSelect(ActionEvent event) {
		this.currentState = State.INSECTS;
	}

	public State getCurrentState() {
		return currentState;
	}

	public int getCurrentCycle() {
		try {
			return Integer.valueOf(currentCycle.getText());
		} catch (NumberFormatException e) {
			return 1;
		}
	}

	public void setCurrentCycle(int currentCycle) {
		Platform.runLater(() -> this.currentCycle.setText(String.valueOf(currentCycle)));
	}

	public int getLongueur() {
		if (utilsController.isAnInteger(longueur.getText()) && !(longueur.getText().isEmpty())) {
			if (Integer.valueOf(longueur.getText()) < 3) {

				utilsController.showAlert("Erreur !", "La longueur doit être supérieur à 2");
			} else {
				return Integer.valueOf(longueur.getText());
			}
		} else if (!(longueur.getText().isEmpty())) {
			utilsController.showAlert("Erreur !", "La longueur doit être un nombre entier");
		}
		longueur.setText("100");
		return 100;
	}

	public void setLongueur(int longueur) {
		this.longueur.setText(String.valueOf(longueur));
	}

	public int getLargeur() {
		if (utilsController.isAnInteger(largeur.getText()) && !(largeur.getText().isEmpty())) {
			if (Integer.valueOf(largeur.getText()) < 3) {

				utilsController.showAlert("Erreur !", "La largeur doit être supérieur à 2");
			} else {
				return Integer.valueOf(largeur.getText());
			}
		} else if (!(largeur.getText().isEmpty())) {
			utilsController.showAlert("Erreur !", "La largeur doit être un nombre entier");
		}
		largeur.setText("100");
		return 100;
	}

	public void setLargeur(int largeur) {
		this.largeur.setText(String.valueOf(largeur));

	}

	public int getCycleNumber() {
		try {
			return Integer.valueOf(cycleNumber.getText());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public void setCycleNumber(int cycleNumber) {
		this.cycleNumber.setText(String.valueOf(cycleNumber));
	}

	// todo: devra être float/double pour gérer les fraction de secondes
	public int getCycleTime() {
		if (utilsController.isAnInteger(cycleTime.getText()) && !(cycleTime.getText().isEmpty())) {
			if (Integer.valueOf(cycleTime.getText()) < 100) {

				utilsController.showAlert("Erreur !", "Le temps par cycle doit être supérieur à 100 ms");
			} else {
				return Integer.valueOf(cycleTime.getText());
			}
		} else if (!(cycleTime.getText().isEmpty())) {
			utilsController.showAlert("Erreur !", "Le temps par cycle doit être un nombre entier");
		}
		cycleTime.setText("150");
		return 150;
	}

	public void setCycleTime(int cycleTime) {
		this.cycleTime.setText(String.valueOf(cycleTime));
	}

	public CSVFile getCsvFile() {
		return csvFile;
	}

	public void setCsvFile(CSVFile csvFile) {
		this.csvFile = csvFile;
	}

	public UtilsController getUtilsController() {
		return utilsController;
	}

	public void setUtilsController(UtilsController utilsController) {
		this.utilsController = utilsController;
	}

	public void setConfiguration(Configuration configuration) {
		setCycleNumber(configuration.getTurns());
		setLargeur(configuration.getColumns());
		setLongueur(configuration.getRows());
		this.configuration = configuration;
	}

	public Configuration getConfiguration() {
		return new Configuration(getCycleNumber(), configuration.getRows(), configuration.getColumns());
	}

	public Label getdVoid() {
		return dVoid;
	}

	public void setdVoid(Label dVoid) {
		this.dVoid = dVoid;
	}

	public Label getdSprout() {
		return dSprout;
	}

	public void setdSprout(Label dSprout) {
		this.dSprout = dSprout;
	}

	public Label getdShrub() {
		return dShrub;
	}

	public void setdShrub(Label dShrub) {
		this.dShrub = dShrub;
	}

	public Label getdTree() {
		return dTree;
	}

	public void setdTree(Label dTree) {
		this.dTree = dTree;
	}

	public Label getdFire() {
		return dFire;
	}

	public void setdFire(Label dFire) {
		this.dFire = dFire;
	}

	public Label getdAsh() {
		return dAsh;
	}

	public void setdAsh(Label dAsh) {
		this.dAsh = dAsh;
	}

	public Label getdInsects() {
		return dInsects;
	}

	public void setdInsects(Label dInsects) {
		this.dInsects = dInsects;
	}
	
	public void updateDLabel() {
	double treeCounter = 0;
	double voidCounter = 0;
	double sproutCounter = 0;
	double shrubCounter = 0;
	double fireCounter = 0;
	double ashCounter = 0;
	double insectsCounter = 0;
	for (int i = 0; i < this.grid.getSquareGrid().size(); i++) {
		Square tmp = this.grid.getSquareGrid().get(i);
		treeCounter = csvFile.incrementCounter(treeCounter,State.TREE,tmp);
		voidCounter = csvFile.incrementCounter(voidCounter,State.VOID,tmp);
		sproutCounter = csvFile.incrementCounter(sproutCounter,State.SPROUT,tmp);
		shrubCounter = csvFile.incrementCounter(shrubCounter,State.SHRUB,tmp);
		fireCounter = csvFile.incrementCounter(fireCounter,State.FIRE,tmp);
		ashCounter = csvFile.incrementCounter(ashCounter,State.ASH,tmp);
		insectsCounter = csvFile.incrementCounter(insectsCounter,State.INSECTS,tmp);
	}
	dVoid.setText(csvFile.convertCounter(voidCounter));
	dSprout.setText(csvFile.convertCounter(sproutCounter));
	dShrub.setText(csvFile.convertCounter(shrubCounter));
	dTree.setText(csvFile.convertCounter(treeCounter));
	dFire.setText(csvFile.convertCounter(fireCounter));
	dAsh.setText(csvFile.convertCounter(ashCounter));
	dInsects.setText(csvFile.convertCounter(insectsCounter));
	}
	
	
}
