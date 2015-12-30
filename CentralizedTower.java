// Centralized Tower : the tower is dependent on the centralized planner. It acts only if it 
// recieves any command from the planner. 
package towerDefense;

import java.util.logging.Level;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class CentralizedTower extends Tower 
{
	// reference to centralized planner. works in pull model. will check if it
	// has any command from planner
	private CentralizedPlanner centralizedPlanner = null;
	
	// constructor, nothing fancy
	public CentralizedTower(ContinuousSpace<Object> space, Grid<Object> grid, int range, int ammo, int id)
	{
		super(space, grid, range, ammo, id);
		logger.log(Level.INFO, "CentralizedPlanner Initializer");
	}
	

	
	@ScheduledMethod(start=1, interval=1, priority = 3)
	public void Step()
	{
		// get reference to the planner if not already initialized
		// cannot put it in constructor, as it leads to a 
		// chicken and egg problem
		if(null == this.centralizedPlanner)
			this.InitializeCentralizedPlanner();
		
		logger.log(Level.INFO, "CentralizedTower id : {0}", this.id);
		// this is not required. central planner maintains this list
		// lesser overhead for agent
		// this.UpdateHitListPerStep();	 

		if(!hasAmmo()){
			return;
		}
		Alien alien = this.centralizedPlanner.GetAssignment(this.id);
		
		// check if we have an assignment, then we gotta shoot!! 
		if(null != alien)
		{
			boolean damage = alien.FiredAt();
			this.ammo--;
			// need a better equation to manage reputation
			
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
		}
		
		return;
	}
	
	// Get reference to the planner object 
	private void InitializeCentralizedPlanner()
	{
		// we know where the planner is on the grid. get it from there directly
		for(Object obj : grid.getObjectsAt(Constants.CentralPlannerX, Constants.CentralPlannerY))
		{
			if(obj.getClass() == CentralizedPlanner.class)
			{
				logger.log(Level.INFO, "Obtained reference to CentralizedPlanner");
				this.centralizedPlanner = (CentralizedPlanner)obj;
			}
		}
		
		return;
	}
}
