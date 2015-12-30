// TowerFactory.java
// factory to create tower based on the tower type we receive

package towerDefense;

import java.util.logging.Logger;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class TowerFactory 
{
	public static Tower CreateInstance(TowerType type, ContinuousSpace<Object> space, Grid<Object> grid, int range, int ammo, int id)
	{
		Tower newTower = null;
		
		switch (type)
		{
		case Smart:
			newTower = new SmartTower(space, grid, range, ammo, id);
			break;
			
		case Dumb:
			newTower = new DumbTower(space, grid, range, ammo, id);
			break;
			
		case Centralized:
			newTower = new CentralizedTower(space, grid, range, ammo, id);
			break;
		}
		
		return newTower;
	}

}
