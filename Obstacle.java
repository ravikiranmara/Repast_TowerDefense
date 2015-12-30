// obstacle.java
// rocks!! does nothing. just sits at one place and blocks
// aliens path
package towerDefense;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Obstacle 
{
	private Grid<Object> grid; 
	private ContinuousSpace<Object> space;

	public Obstacle(ContinuousSpace<Object> space, Grid<Object> grid)
	{
		this.space = space;
		this.grid = grid;
	}
	
	@ScheduledMethod(start=1, interval=1)
	public void step()	
	{
		return;
	}
}
