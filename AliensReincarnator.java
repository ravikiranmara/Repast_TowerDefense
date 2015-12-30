// AlienReincarnator.java
// handles adding new aliens to the grid once the alien reaches the base
// we maintain constants.AlienCount number of aliens on the board each count

package towerDefense;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

// singleton class. 
public class AliensReincarnator 
{
	// singleton class reference method
	private static AliensReincarnator instance = null;
	
	private final Logger logger=Logger.getLogger(this.getClass().getPackage().getName());
	
	private Grid<Object> grid; 
	private ContinuousSpace<Object> space;
	
	// id assigned to the aliens. incremented to alien each time
	private static int index;
	
	protected AliensReincarnator() 
	{
	      // Exists only to defeat instantiation.
	}
	   
	// return singleton instance
	public static AliensReincarnator getInstance() 
	{
		if(instance == null) 
	    {
	       instance = new AliensReincarnator();
	    }
	      
	    return instance;
	}
	
	// pseudo-constructor
	public void InitializeSpaceGrid(ContinuousSpace<Object> space, Grid<Object> grid, int index)
	{
		this.space = space;
		this.grid = grid;
		this.index = index;
		
		return;
	}
	
	// reincarnate alien that has reached the base
	public void ReIncarnate(Alien alien)
	{
		NdPoint spacePt = space.getLocation(alien);
		Context<Object> context = ContextUtils.getContext(alien);
		
		// assign new index
		alien.Reset(index);
		
		// move to new place in grid
		this.Replace(alien);
		
		// increment index only if we are reincarnating alien
		// .i.e. it has reached base
		index++;
		return;
	}
	
	// should change name to relocate
	public void Replace(Alien alien)
	{
		boolean empty = false;
		int x = 0 , y = 0;
		
		// find an empty place in grid
		while(false == empty)
		{
			x = RandomHelper.nextIntFromTo(0, 49);
			y = RandomHelper.nextIntFromTo(0, 49);
			
			empty = TowerDefenseUtils.IsGridCellEmpty(grid, x, y);
		}
		
		logger.log(Level.INFO, "Relocating Alien to ({0},{1})", new Object[] {x, y} );
		
		// move alien to new place
		grid.moveTo(alien, x, y);
		index++;
		
		return;
	}
}