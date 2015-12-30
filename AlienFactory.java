// factory class to create aliens
package towerDefense;

import java.util.logging.Logger;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class AlienFactory 
{
	// create the type of alien requested
	public int AlienCount = 15;
	
	public void setAlienCount(int aliens){
		this.AlienCount = aliens;
	}
	
	public int getAlienCount(){
		return this.AlienCount;
	}
	public static Alien CreateInstance(AlienType type, ContinuousSpace<Object> space, Grid<Object> grid, int defense, int id)
	{
		Alien newAlien= null;
		
		switch (type)
		{
		case Smart:
			newAlien = new SmartAlien(space, grid, defense, id);
			newAlien.setDefenseSteps(defense);
			break;
			
		case Dumb:
			newAlien = new DumbAlien(space, grid, defense, id);
			newAlien.setDefenseSteps(defense);
			break;
		}
		
		return newAlien;
	}

}
