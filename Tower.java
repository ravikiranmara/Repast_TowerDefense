// Tower.java
// tower base class. contains basic tower data.

package towerDefense;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public abstract class Tower 
{
	
	protected final Logger logger=Logger.getLogger(this.getClass().getPackage().getName());
	
	protected Grid<Object> grid; 
	protected ContinuousSpace<Object> space;
	
	// my grid location
	protected NdPoint myLocation;
	
	// each tower has a unique id
	protected int id;
	
	// reputation of tower based on how many aliens shot, or ammo wasted
	protected int reputation;
	protected int range;
	protected int ammo;
	
	// list of aliens that are in defense mode, so we don't waste 
	// ammo firing at them again
	HashMap<Integer, Integer> hitList;

	protected Tower(ContinuousSpace<Object> space, Grid<Object> grid, int range, int ammo, int id)
	{
		this.space = space;
		this.grid = grid;
		this.id = id;
		this.range = range;
		this.reputation = Constants.TowerInitialReputation;
		this.ammo = ammo;
		
		hitList = new HashMap<Integer, Integer>();
	}
	
	protected void AddAlienToHitList(Alien alien)
	{
		// update the list
		hitList.put(alien.GetId(), alien.getDefense());			
		
	}
	
	protected boolean IsAlienInHitList(Alien alien)
	{
		if(hitList.containsKey(alien.GetId()))
		{
			return true;
		}
		
		return false;
	}
	
	protected ArrayList<Alien> GetAliensInTargetRangeList()
	{
		ArrayList<Alien> alienList = new ArrayList<Alien>();
	
		// get your neighbouring cells
		GridPoint pt = grid.getLocation(this);
		GridCellNgh<Alien> nghCreator = new GridCellNgh<Alien>(grid, pt,
				Alien.class, Constants.TowerFireRange, Constants.TowerFireRange);		

		List<GridCell<Alien>> gridCells = nghCreator.getNeighborhood(true);
		
		// examine each cell, add all the aliens found to the list
		for (GridCell<Alien> cell : gridCells) 
		{
			Iterator<Alien>itr = cell.items().iterator();
			
			while(itr.hasNext()) 
			{ 
				alienList.add(itr.next());
			}
		}
		
		return alienList;
	}
	
	public int GetId()
	{
		return this.id;
	}
	
	public int GetAmmo(){
		return this.ammo;
	}
	
	public boolean hasAmmo(){
		if(ammo==0){
			return false;
		}
		return true;
	}
	
	protected void UpdateHitListPerStep()
	{
		Iterator itr = hitList.keySet().iterator();
		ArrayList<Integer> removeList = new ArrayList<Integer>();
		
		// decrement timer for aliens defense mode 
		while (itr.hasNext())
		{
			Integer key   = (Integer) itr.next();
			hitList.put(key, hitList.get(key)-1);
			
			if(hitList.get(key) < 0)
			{
				removeList.add(key);
			}
		}
		
		// remove list of aliens who have disabled defense mode
		for(Integer key : removeList)
		{
			hitList.remove(key);
		}

		return;
	}
	
	public int getReputation(){
		return this.reputation;
	}
	
	public void setComms(int r){
		
	}
	
	abstract protected void Step();	
}
