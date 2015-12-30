// SmartTower.java
// towers that can communicate. tower sends and receive info to its neighbour

package towerDefense;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class SmartTower extends Tower 
{
	// list of my neighbours
	ArrayList<Tower> myNeighbours; 
	int commRange;
	
	public SmartTower(ContinuousSpace<Object> space, Grid<Object> grid, int range, int ammo, int id)
	{
		super(space, grid, range, ammo, id);
		logger.log(Level.INFO, "SmartTower Initializer");
		
		// look through my communication range and find all towers
		myNeighbours = new ArrayList<Tower>();
	}
	
	public void setComms(int r){
		this.commRange= r;
	}
	
	// all Towers that are in my twice range. 
	private void GetMyNeighbours()
	{
		GridPoint pt = grid.getLocation(this);
		
		// my neighboring cells
		GridCellNgh<Tower> nghCreator = new GridCellNgh<Tower>(grid, pt,
				Tower.class, commRange, commRange);		
		List<GridCell<Tower>> gridCells = nghCreator.getNeighborhood(true);
		
		// add them to my list
		for (GridCell<Tower> cell : gridCells) 
		{
			Iterator<Tower>itr = cell.items().iterator();
			
			int hitCount = 0;
			while(itr.hasNext()) 
			{ 
				Tower tower = itr.next();
				this.myNeighbours.add(tower);
			}
		}
		
		logger.log(Level.INFO, "Tower {1} can communicate with {0} towers", 
				new Object[] {this.myNeighbours.size(), this.GetId() });
		
		return;
	}
	
	@ScheduledMethod(start=1, interval=1, priority = 3)
	public void Step()
	{
		GridPoint pt = grid.getLocation(this);
		logger.log(Level.INFO, "Tower {0} ({1},{2}) : step function", 
				new Object[]{this.id, pt.getX(), pt.getY()});
		
		// update our list of aliens in defense mode
		this.UpdateHitListPerStep();
		
		// this cannot be done in constructor as the towers are still in creation. 
		// we wait till all towers are initialized and placed
		if(0 == this.myNeighbours.size())
			this.GetMyNeighbours();
		
		// simple approach and no strategy between towers
		// the smart towers just communicate with the other towers. 
		// whenever a tower attacks an alien, it informs its neighbours
		// neighbours will put the alien in its hitlist and wait till the
		// defense mode is down
		
		GridCellNgh<Alien> nghCreator = new GridCellNgh<Alien>(grid, pt,
				Alien.class, range, range);		

		List<GridCell<Alien>> gridCells = nghCreator.getNeighborhood(true);

		for (GridCell<Alien> cell : gridCells) 
		{
			Iterator<Alien>itr = cell.items().iterator();
			
			// number of shots per round
			int hitCount = 0;
			while(itr.hasNext()) 
			{ 
				Alien alien = itr.next();
				if(ammo == 0){
					break;
				}
				// check if alien is in hitlist
				if(true == this.IsAlienInHitList(alien))
					continue;		
				
				// alien is not in hitlist, so we haven't fired at it yet
				logger.log(Level.INFO, "Fire At id={0}", alien.GetId()); 
				boolean damage = alien.FiredAt();
				this.ammo--;
				// add alien to our list so we don't shoot while in defense mode
				this.AddAlienToHitList(alien);

				//Additionally tell all the neighbours that you shot this alien
				for(Tower tower : this.myNeighbours)
				{
					tower.AddAlienToHitList(alien);
				}
			
				// update your reputation
				if(false == damage) 
				{ 
					// we should never be here
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
				
				// overkill. each cell has only one item. This is not required. But just in case
				hitCount++;	
				if(hitCount >= Constants.HitsPerRound)
					break;
			}	
			
			// number of shots allowed per step
			if(hitCount >= Constants.HitsPerRound)
				break;
		}

		return;
	}
}
