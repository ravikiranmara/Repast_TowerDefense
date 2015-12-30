// base.java
// the target destination for aliens. located in the center of the board
// counts damage on the aliens
package towerDefense;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class Base 
{
	private final Logger logger=Logger.getLogger(this.getClass().getPackage().getName());

	private Grid<Object> grid; 
	private ContinuousSpace<Object> space;
	
	// number of aliens that have reached base
	int alienCount;
	
	// sum of damage of all aliens
	int cumulativeDamage;

	// sum of all steps taken by aliens
	int cumulativeSteps;

	public Base(ContinuousSpace<Object> space, Grid<Object> grid)
	{
		this.space = space;
		this.grid = grid;
		this.alienCount = 0;
		this.cumulativeDamage = 0;
		this.cumulativeSteps = 0;
		
		return;
	}
	
	@ScheduledMethod(start=1, interval=1, priority=6)
	public void step()
	{
		logger.log(Level.INFO, "Base Step funcion called");
	
		this.CheckForAlienAndReadDamage();
		
		return;
	}
	
	// read damage on aliens
	private void CheckForAlienAndReadDamage()
	{
		AliensReincarnator alienReincarnator = AliensReincarnator.getInstance();
		
		// aliens that are 1 block away, will reach base in next step. This is as
		// good as alien reaching the base. ask reincarnator to deal with them
		GridPoint pt = grid.getLocation(this);
		GridCellNgh<Alien> nghCreator = new GridCellNgh<Alien>(grid, pt,
				Alien.class, 1, 1); 
		List<GridCell<Alien>> gridCells = nghCreator.getNeighborhood(true);

		for (GridCell<Alien> cell : gridCells) 
		{
            Iterator<Alien>itr = cell.items().iterator();
            while(itr.hasNext())
            {
            	Alien alien = itr.next();
                int damage = alien.GetDamage();
                int steps = alien.GetSteps();
                logger.log(Level.INFO, "Alien reached base : Id = {0} Damage {1} Number of steps taken {2}", 
                		new Object[] {alien.GetId(), damage, steps});
                
                // update our counters
                this.cumulativeDamage += damage;
                this.cumulativeSteps += steps;
                this.alienCount++;
                
                // reincarnate. important step
                alienReincarnator.ReIncarnate(alien);
            }
        }
		
		return;
	}
	
	public int getAlienCount(){
		return this.alienCount;
	}
	
	public int getCumulativeDamage(){
		return this.cumulativeDamage;
	
	}
}