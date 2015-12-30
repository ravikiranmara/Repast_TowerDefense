// Class is responsible for creating a plan for the agents. The centralized towers 
// will call the centralized planner to check if they need to perform any action

package towerDefense;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class CentralizedPlanner 
{
	protected final Logger logger=Logger.getLogger(this.getClass().getPackage().getName());
	
	protected Grid<Object> grid; 
	protected ContinuousSpace<Object> space;
	
	// list of all towers
	Tower[] towerList;
	
	// list of towers that will recieve command. chosen based on efficiency
	HashMap<Tower, List<Alien>> towerWorkingList;
	
	// list of aliens that have defense mode enabled
	HashMap<Integer, Integer> hitList;
	
	// from the working list, we assigne an alien or each alien to fire at
	HashMap<Integer, Alien> assignment;
	
	// given by the user. how efficient the central tower is. if 100% efficinet
	// then all towers get command 
	private int efficiencyPercentage;
	
	// used as pointer in circular array
	int index;
	
	// tower type, only if centralized, we will do something. 
	TowerType towerType;
	
	
	// constructor
	protected CentralizedPlanner(ContinuousSpace<Object> space, Grid<Object> grid, TowerType towerType, int efficiency)
	{
		this.space = space;
		this.grid = grid;
		this.efficiencyPercentage = efficiency;
		this.index = 0;
		this.towerType = towerType;
		
		this.hitList = new HashMap<Integer, Integer>();
		this.assignment = new HashMap<Integer, Alien>();
		this.towerWorkingList = new HashMap<Tower, List<Alien>> ();
		
		// executed after the towers are initialized and placed on the grid
		this.InitializeTowerList();
	}

	@ScheduledMethod(start=1, interval=1, priority = 4)
	public void Step()
	{
		// if not centralized tower run, then do nothing
		if(TowerType.Centralized != towerType)
			return;
		
		// update the defense mode count on aliens
		this.UpdateHitListPerStep();
		
		// get list of towers that it needs to send command to
		this.GetTowerWorkingList();
		
		// for the towers, assign an alien for it to fire at
		this.MakeAssignment();
	}
	
	// returns the alien location
	// null to indicate that there is no assignment and the tower
	// will do nothing for that step
	public Alien GetAssignment(int towerId)
	{
		Alien alien = null;
		
		if(this.assignment.containsKey(towerId))
		{
			alien = this.assignment.get(towerId);
		}
		
		return alien;
	}
	
	// using the working set, make the assignment of target to towers
	// current policy is random assignment. We can change this to more
	// sophisticated policies. 
	private void MakeAssignment()
	{
		// clear previous assignment
		this.assignment.clear();
		
		// run through the working set list. if there is a alien in its
		// range, check if it is on hitlist assign it to the tower, 
		// add alien to the hitlist progress to next item. 
		for (Map.Entry<Tower, List<Alien>> entry : this.towerWorkingList.entrySet())
		{
			// get the tower and aliens in its range
			Tower tower = entry.getKey();
			List<Alien> alienList = entry.getValue();
			
			// number of shots tower can take per step
			int hitCount = 0;
			
			// check if the list is empty. 
			if(true == alienList.isEmpty())
			{
				// nothing to do. goto next tower
				continue;
			}
			
			// the list is not empty, pick an alien that is not in 
			// hitlist and assign to tower in assignment map
			for (Alien alien : alienList)
			{
				if(false == this.IsAlienInHitList(alien))
				{
					this.assignment.put(tower.GetId(), alien);
					this.AddAlienToHitList(alien);
					hitCount++;
					logger.log(Level.INFO, "Assignment <towerId:{0} alienId:{1}>", new Object[] {tower.GetId(), alien.GetId()});

					// number of aliens assigned == nimber of shots allowed
					if(hitCount >= Constants.HitsPerRound)
					break;
				}
			}
		}
		
		return;
	}
	
	// get all towers in the grid
	private void InitializeTowerList()
	{
		ArrayList<Tower> tempList = new ArrayList<Tower>();
	
		// starting from 0,0 look at the entire grid
		GridPoint pt = new GridPoint(0, 0);
		GridCellNgh<Tower> nghCreator = new GridCellNgh<Tower>(grid, pt,
				Tower.class, Constants.GridBreadth, Constants.GridHeight);		

		List<GridCell<Tower>> gridCells = nghCreator.getNeighborhood(true);

		// iterate through each cell and get towers
		for (GridCell<Tower> cell : gridCells) 
		{
			Iterator<Tower>itr = cell.items().iterator();
			
			while(itr.hasNext()) 
			{ 
				Tower tower = itr.next();
				tempList.add(tower);
			}
		}
		
		// have the tower list in array form
		this.towerList = new Tower[tempList.size()];
		this.towerList = tempList.toArray(this.towerList);
		
		logger.log(Level.INFO, "Count of all Towers in the grid : {0}", this.towerList.length);
		
		return;
	}
	
	private Tower GetNextTowerInList()
	{
		// wrap around. poor man's circular queue
		if(this.towerList.length == index)
		{
			index = 0;
		}
		
		// return the next tower in the array
		return towerList[index++];
	}
	
	// get towers, and aliens in its range. easier to make assignment with such a list
	private void GetTowerWorkingList()
	{
		// get percentage count. number of towers to send message to
		int count = (int)Math.floor((efficiencyPercentage * towerList.length) / 100);
		logger.log(Level.INFO, "Sending command to {0} towers", count);

		// starting with a fresh list
		this.towerWorkingList.clear();
		
		// foreach tower, get alien list and add to working list
		for (int i=0; i<count; i++)
		{
			// get next tower from list			
			Tower tower = this.GetNextTowerInList();
			logger.log(Level.INFO, "Examining Tower id to add to working set: {0}", tower.GetId());
//to prevent infinite loop
			int j=0;
			//skip out of ammo towers
			while(!tower.hasAmmo()){
				j++;
				this.GetNextTowerInList();
				if(j>towerList.length){
					break;
				}
			}
			logger.log(Level.INFO, "working list Tower id : {0}", tower.GetId());
			
			// get list of aliens for the tower
			ArrayList<Alien> tempAliensInTargetRange = tower.GetAliensInTargetRangeList();			
			ArrayList<Alien> aliensInTargetRange = new ArrayList<Alien>();
			
			// filter the target list and remove all aliens in defenseMode 
			for(Alien alien : tempAliensInTargetRange)
			{
				if(false == hitList.containsKey(alien.GetId()))
				{					
					aliensInTargetRange.add(alien);
					logger.log(Level.INFO, "Adding to workingset : <towerId:{0} AlienId:{1}>", new Object[]{ tower.GetId(), alien.GetId()});
				}
			}
			
			// now that we have the filtered list, lets add this to our working list
			logger.log(Level.INFO, "Tower {0} has {1} potential target aliens", new Object [] {tower.GetId(), aliensInTargetRange.size() });
			this.towerWorkingList.put(tower, aliensInTargetRange);			
		}
		
		return;
	}
	
	private void UpdateHitListPerStep()
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
	
}
