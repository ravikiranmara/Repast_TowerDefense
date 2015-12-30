package towerDefense;

import java.util.logging.Level;
import java.util.logging.Logger;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.valueLayer.GridValueLayer;

public abstract class Alien 
{
	protected final Logger logger=Logger.getLogger(this.getClass().getPackage().getName());
	
	protected Grid<Object> grid; 
	protected ContinuousSpace<Object> space;
	
	protected int id;
	
	protected boolean defenseMode;
	protected int defenseModeStepCount;
	protected int damage;
	protected int steps;
	protected int defaultDefense;

	public Alien(ContinuousSpace<Object> space, Grid<Object> grid,int defenseSteps, int id)
	{
		this.space = space;
		this.grid = grid;
		this.defaultDefense = defenseSteps;
		this.Reset(id);
	}
	
	public void Reset(int id)
	{
		this.id = id;
		this.defenseMode = false;
		this.defenseModeStepCount = this.defaultDefense;
		this.damage = 0;
		this.steps = 0;
	}
	
	@ScheduledMethod(start=1, interval=1, priority = 4)
	abstract protected void Step();
	
	
	protected void UpdateDefenseMode()
	{
		this.steps++;
		this.defenseModeStepCount--;
		if(this.defenseModeStepCount <= 0)
		{
			logger.log(Level.INFO, "Alien {0} : disable DefenseMode", this.id);
			this.defenseMode = false;
		}
		
		return;
	}
	
	public void setPheromones(GridValueLayer pgrid){
		
	}
	public void setDefenseSteps(int s){
		this.defenseModeStepCount = s;
	}
	
	public int getDefense(){
		return this.defaultDefense;
	}
	public int GetId()
	{
		return id;
	}
	
	public int GetDamage()
	{
		return this.damage;
	}
	
	public int GetSteps()
	{
		return this.steps;
	}
	
	public boolean FiredAt()
	{
		
		if(true == this.defenseMode)
		{
			return false;
		}
		
		this.damage++;
		logger.log(Level.INFO, "Alien {0} : enable DefenseMode", this.id);
		this.defenseMode = true;
		this.defenseModeStepCount = this.defaultDefense;
		return true;
	}

}
