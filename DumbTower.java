// DumbTower.java
// blindly fires at the first found alien that is not in its hitlist
// no communication between towers
package towerDefense;


import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class DumbTower extends Tower 
{
	public DumbTower(ContinuousSpace<Object> space, Grid<Object> grid, int range, int ammo, int id)
	{
		super(space, grid, range, ammo, id);
		logger.log(Level.INFO, "DumbTower Initializer");
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 3)
	public void Step() 
	{
		GridPoint pt = grid.getLocation(this);
		logger.log(Level.INFO, "Tower {0} ({1},{2}) : step function", new Object[]{this.id, pt.getX(), pt.getY()});
		
		// update the defense mode count of aliens 
		this.UpdateHitListPerStep();
		
		// get neighbouring cells
		GridCellNgh<Alien> nghCreator = new GridCellNgh<Alien>(grid, pt,
				Alien.class, range, range);		

		List<GridCell<Alien>> gridCells = nghCreator.getNeighborhood(true);

		// in each cell, check for aliens 
		for (GridCell<Alien> cell : gridCells) 
		{
			Iterator<Alien>itr = cell.items().iterator();
			
			// number of shots per taken per step
			int hitCount = 0;
			while(itr.hasNext()) 
			{ 
				Alien alien = itr.next();
				if(this.ammo==0){
					break;
				}
				// check if alien is in hitlist
				if(true == this.IsAlienInHitList(alien))
					continue;		
				
				// alien is not in hitlist, so we haven't fired at it yet
				logger.log(Level.INFO, "Fire At id={0}", alien.GetId()); 
				boolean damage = alien.FiredAt();
				this.ammo--;
				// add alien to hitlist, even if we didn't cause damage. 
				this.AddAlienToHitList(alien);
			
				// update my reputation
				if(false == damage) 
				{ 
					reputation -= Constants.missPenalty;
					logger.log(Level.INFO, "Fire At id={0} : Negative impact (Repuation : {1})", 
							new Object[]{alien.id, this.reputation});					
				}
				else 
				{
					reputation += Constants.hitReward;
					logger.log(Level.INFO, "Fire At id={0} : its a hit!! (Repuation : {1})",
							new Object[]{alien.id, this.reputation});					
				} 
				
				hitCount++;	
				
				// number of hits per round
				if(hitCount >= Constants.HitsPerRound)
					break;
			}	
			
			// number of hits allowed per round. break two loops
			if(hitCount >= Constants.HitsPerRound)
				break;
		}

		return;
	}
	
}
