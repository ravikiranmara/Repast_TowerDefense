package towerDefense;

import java.util.logging.Level;
import java.util.logging.Logger;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.RandomGridAdder;
import repast.simphony.space.grid.StickyBorders;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.valueLayer.GridValueLayer;

public class TowerDefenseContextBuilder implements ContextBuilder<Object> 
{
	private final Logger logger=Logger.getLogger(this.getClass().getPackage().getName());
	
	@Override 
	public Context<Object> build(Context<Object> context)
	{
		logger.log(Level.INFO, "BuildContext - setting Context Id");
		context.setId("TowerDefense");
		
		//Create Parameters
		Parameters p = RunEnvironment.getInstance().getParameters();
		int AlienCount = (Integer)p.getValue("AlienCount");
		String AlienMode = p.getValue("AlienMode").toString();
		String TowerMode = p.getValue("TowerMode").toString();
		int defense = (Integer)p.getValue("DefenseSteps");
		int towerRange = (Integer)p.getValue("TowerRange");
		int towerCommunication = (Integer)p.getValue("CommunicationRange");
		int ammoValue = (Integer)p.getValue("AmmoValue");
		
		// TowerType
		TowerType towerType = null;
		if(TowerMode.equals("Centralized")){
			towerType = TowerType.Centralized;
		} else if(TowerMode.equals("Dumb")){
			towerType = TowerType.Dumb;
		} else if(TowerMode.equals("Smart")){
			towerType = TowerType.Smart;
		}
		
		// create space
		logger.log(Level.INFO, "Creating Space");
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace("space", context, 
				new RandomCartesianAdder<Object>(), new repast.simphony.space.continuous.WrapAroundBorders(), 
				Constants.SpaceBreadth, Constants.SpaceHeight);
		
		// create grid
		logger.log(Level.INFO, "Creating grid");
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context, 
				new GridBuilderParameters<Object>(new StickyBorders(), 
						new SimpleGridAdder<Object>(), false, 
						Constants.GridBreadth, Constants.GridHeight));
		
		// create Pheremone Grid
		logger.log(Level.INFO, "Creating Pheremone grid");
		GridValueLayer pgrid = new GridValueLayer("pgrid", 0, false, Constants.GridBreadth, Constants.GridHeight);

		// add base to context
		for (int i=0; i<Constants.BaseCount; i++)
		{
			Base base = new Base(space, grid);
			context.add(base);
			grid.moveTo(base, (int)Constants.BaseLocation[i].getX(), (int)Constants.BaseLocation[i].getY()); 
		}
		
		// add towers
		for(int i=0; i<Constants.TowerCount; i++)
		{
			Tower tower = TowerFactory.CreateInstance(towerType, space, grid, towerRange, ammoValue, i);
			if(TowerMode.equals("Smart")){
				tower.setComms(towerCommunication);
			}
			context.add(tower);
			grid.moveTo(tower, (int)Constants.TowerLocation[i].getX(), (int)Constants.TowerLocation[i].getY());
		}
				
		// add centralized planner
		CentralizedPlanner centralizedPlanner = new CentralizedPlanner(space, grid, towerType, 30);
		context.add(centralizedPlanner);
		grid.moveTo(centralizedPlanner, (int)Constants.CentralPlannerX, (int)Constants.CentralPlannerY);		
		
		// add Obstacles
		for (int i=0; i<Constants.ObstacleCount; i++)
		{
			Obstacle obstacle = new Obstacle(space, grid);
			context.add(obstacle);
			grid.moveTo(obstacle, (int)Constants.ObstacleLocation[i].getX(), (int)Constants.ObstacleLocation[i].getY());
		}		
		
		// incarnator
		AliensReincarnator aliensReincarnator = AliensReincarnator.getInstance();
		aliensReincarnator.InitializeSpaceGrid(space, grid, AlienCount);
				
		
		// add aliens
		for (int i=0; i<AlienCount; i++)
		{
			Alien alien;
			if(AlienMode.equals("Dumb")){
				alien = AlienFactory.CreateInstance(AlienType.Dumb, space, grid, defense, i);
			} else {
				alien = AlienFactory.CreateInstance(AlienType.Smart, space, grid, defense, i);
				alien.setPheromones(pgrid);
			}
			context.add(alien);
			aliensReincarnator.Replace(alien);	
		}
		
		return context;
	}
}
;