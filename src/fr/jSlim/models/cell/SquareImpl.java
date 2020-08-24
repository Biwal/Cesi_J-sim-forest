package fr.jSlim.models.cell;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import fr.jSlim.models.enums.State;
import fr.jSlim.models.grid.Configuration;
import javafx.scene.shape.Rectangle;

@Entity(name="square")
public class SquareImpl extends Rectangle implements Square, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_square")
	private int id;
	@Column(name="position")
	private int position;
	@Column(name="column_square")
	private int column;
	@Column(name="row_square")
	private int row;
	@Column(name="growth_shrub")
	private boolean growthShrub;
	@Enumerated(EnumType.STRING)
	@Column(name="state")
	private State state;
	@ManyToOne(cascade = CascadeType.PERSIST, fetch=FetchType.LAZY)
	@JoinColumn(name="id_configuration")
	private Configuration configuration; 

	public SquareImpl() {
		this.position = -1 ;
		this.state = State.INVALID;
		this.column = 0;
		this.row = 0;
		this.setGrowthShrub(false);
	}

	public SquareImpl(int position, State state, int column, int row) {
		this.position = position;
		this.state = state;
		this.column = column;
		this.row = row;
		this.setGrowthShrub(false);
	}

	@Override
	public String toString() {
		return Character.toString(state.getSymbol());
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public boolean isGrowthShrub() {
		return growthShrub;
	}

	public void setGrowthShrub(boolean growthShrub) {
		this.growthShrub = growthShrub;
	}
	
	public int getIdSquare() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}


}
