package fr.jSlim.models.cell;

import fr.jSlim.models.enums.State;
import fr.jSlim.models.grid.Configuration;

public interface Square extends Cloneable{

	@Override
	public String toString();

	public State getState();

	public void setState(State value);

	public int getPosition();

	public void setPosition(int position);

	public int getColumn();

	public void setColumn(int column);

	public int getRow();

	public void setRow(int row);

	public boolean isGrowthShrub();

	public void setGrowthShrub(boolean b);

	public Configuration getConfiguration() ;

	public void setConfiguration(Configuration configuration) ;
}
