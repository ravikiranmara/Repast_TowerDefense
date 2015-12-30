// DumbAlien.java
// blindly tries to walk to the base. They have certain orientation
// to go either left or right when they hit an obstacle. This may not
// always lead to shortest path
package towerDefense;

import java.util.logging.Level;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class DumbAlien extends Alien
{
	public DumbAlien(ContinuousSpace<Object> space, Grid<Object> grid, int defense, int id)
	{
		super(space, grid, defense, id);
		logger.log(Level.INFO, "DumbAlien Constructor");
	}
	
	@ScheduledMethod(start=1, interval=1, priority = 5)
	public void Step()
	{
		logger.log(Level.INFO, "DumbAlien {0} Step function", this.id);
		
		// check if your defense mode expired
		this.UpdateDefenseMode();
		
		// move to next cell
		MakeNextMove();
		
		return;
	}
	
	private void MakeNextMove()
	{
		// xy coordinates of your location and base
		int myx, myy, ringx, ringy, basex, basey;
		
		// if no obstacle advance, else sidestep
		int advance, sidestep;
		
		// number of blocks an alien can move in one step
		int step = 1; 
		
		// used to determine which direction alien goes in, if it
		// encounters an obstacle
		// alien with odd id will move in -ve direction,
		sidestep = (this.id%2 == 0)? 1 : -1;		
		
		// get my coordinates
		GridPoint pt = grid.getLocation(this);
		myx = pt.getX();
		myy = pt.getY();
		
		// get coordinates of base
		basex = Constants.BaseLocation[0].x;
		basey = Constants.BaseLocation[0].y;		
		
		// visualizing the tiles as concentric rings around the base 
		// check which ring the agent is in
		ringx = Math.abs(myx - basex);
		ringy = Math.abs(myy - basey);
		
		// the higher coordinate, give the ring the agent is in. 
		// if ringx is farther, then we advance x coordinate towards the base
		if(ringx - ringy > (int)RandomHelper.nextIntFromTo(-3, 3))
		{
			// depending on were we are on the board, either we need to add 1 or 
			// subtract 1 from x coordinate
			advance = ((Math.abs((myx+1) - basex)) < ringx)? 1 : -1;
			
			// advance forward. check if we have hit any obstacle
			if(TowerDefenseUtils.IsGridCellEmpty(grid, myx + step*advance, myy))
			{
				MoveTo(myx + step*advance, myy);
			}
			// obstacle, take a sidestep
			else if(TowerDefenseUtils.IsGridCellEmpty(grid, myx, myy + (step*sidestep)))
			{
				MoveTo(myx, myy + (step*sidestep));
			}
			// obstacle at sidestep as well, then randomly choose to stay still
			// or take on step back
			else
			{
				if((int)RandomHelper.nextIntFromTo(0, 49) % 2 == 0)
				{
					MoveTo(myx - step*advance, myy);
				}
			}
		}
		// else y is farther. we try to advance y coordinate to go closer to base
		else
		{
			// the same logic as above, but applied to y
			advance = ((Math.abs((myy+1) - basey)) < ringy)? 1 : -1;

			// advance forward. check if we have hit any obstacle
			if(TowerDefenseUtils.IsGridCellEmpty(grid, myx, myy + step*advance))
			{
				MoveTo(myx, myy + step*advance);
			}
			// obstacle, take a sidestep
			else if(TowerDefenseUtils.IsGridCellEmpty(grid, myx + (step*sidestep), myy))
			{
				MoveTo(myx + (step*sidestep), myy);
			}
			// obstacle at sidestep as well, then randomly choose to stay still
			// or take on step back
			else
			{
				if((int)RandomHelper.nextIntFromTo(0, 49) % 2 == 0)
				{
					// take a step back
					MoveTo(myx, myy - step*advance);
				}
				// else do nothing
			}
		}
		
		return;
	}
	
	private void MoveTo(int x, int y)
	{
		logger.log(Level.INFO, "Alien [{0}]:MoveTo -> ({1},{2})", new Object[]{this.id, x, y});
		
		grid.moveTo(this, x, y);
		
		return;
	}
}
