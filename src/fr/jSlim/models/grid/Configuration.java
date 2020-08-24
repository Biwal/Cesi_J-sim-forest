package fr.jSlim.models.grid;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "configuration")
public class Configuration implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_configuration")
	private int id;
	
	@Column(name = "turns")
	private int turns;
	
	@Column(name = "speed")
	private int speed = 100;
	
	@Column(name = "rows_grid")
	private int rows;
	
	@Column(name = "columns_grid")
	private int columns;
	

	public Configuration() {
		
	}
//	public Configuration( int turns, int speed, int rows, int columns, boolean insectsActivated,
//			boolean fireActivated) {
//		this.turns = turns;
//		this.rows = rows;
//		this.columns = columns;
//
//		//turn :une grille est créé à chaque cycle, une nouvelle conf avec.
//		//2 booleans: les trois algos passeront sur chaque grille,
//		this.speed = speed;
//		this.insectsActivated = insectsActivated;
//		this.fireActivated = fireActivated;
//	}

	public Configuration(int turns, int row, int col) {
		this.turns = turns;
		this.rows = row;
		this.columns = col;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTurns() {
		return turns;
	}

	public void setTurns(int turns) {
		this.turns = turns;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	
	
	

}
