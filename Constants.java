// constants. 
// contains layout of grid, and other settings for game

package towerDefense;

import java.awt.Point;

public class Constants 
{
	// grid and space dimensions
	public static int GridBreadth = 50;
	public static int GridHeight = 50;
	public static int SpaceBreadth = 50; 
	public static int SpaceHeight = 50;  
	// agent counts
	public static int TowerCount = 0; 
	public static int ObstacleCount = 0; 
	public static int BaseCount = 0;
	// alien defense mode
	public static int AlienDefenseModeStepCount = 3;
	
	// tower reputation mgmet
	public static int TowerInitialReputation = 10;
	public static int missPenalty = 1;
	public static int hitReward = 2;
	public static int HitsPerRound = 1; // number of aliens tower can shoot per step
	public static int TowerFireRange = 5;	// tower's target range
	public static int TowerCommuncationRange = 10+1;	// can talk to any (smart) tower in this range
	
	// central planner. hack! using one of the obstacle places 
	public static int CentralPlannerX = 5;
	public static int CentralPlannerY = 7;
	
	// higher priority runs first
	public static int BasePriority = 6;
	public static int AlienPriority = 5;
	public static int CentralizedPriority = 4;
	public static int TowerPriority = 3;
	public static int ObstaclePriority = 2;
	
	//placement of agents on the board
	public static Point[] TowerLocation;
	public static Point[] BaseLocation;
	public static Point[] ObstacleLocation;
	
	static 
	{
		// ****** initialize base locations  ******
		BaseLocation = new Point[]
				{
					new Point(25, 25)
				};
		BaseCount = BaseLocation.length;
				
		// ***** initialize the tower locations *****
		// looking at grid as a set of concentric rings centered around base. 
		// each ring can be seen as a level (radius from center)
		TowerLocation = new Point[]
				{
					// level 5, 45 grid location
					new Point(5, 5),	
					new Point(10, 5),
					new Point(15, 5),
					new Point(20, 5), 
					new Point(25, 5),
					new Point(30, 5),
					new Point(35, 5),
					new Point(40, 5),
					new Point(45, 5),
					
					new Point(5, 10),
					new Point(5, 15),
					new Point(5, 20),
					new Point(5, 25),
					new Point(5, 30),
					new Point(5, 35),
					new Point(5, 40),					
					new Point(5, 45),
					
					new Point(10, 45),
					new Point(15, 45),
					new Point(20, 45), 
					new Point(25, 45),
					new Point(30, 45),
					new Point(35, 45),
					new Point(40, 45),
					new Point(45, 45),
					
					new Point(45, 10),
					new Point(45, 15),
					new Point(45, 20),
					new Point(45, 25),
					new Point(45, 30),
					new Point(45, 35),
					new Point(45, 40),
						
					// level 10, 40
					new Point(10, 10),
					new Point(15, 10),
					new Point(20, 10), 
					new Point(25, 10),
					new Point(30, 10),
					new Point(35, 10),
					new Point(40, 10),
					
					new Point(10, 15),
					new Point(10, 20),
					new Point(10, 25),
					new Point(10, 30),
					new Point(10, 35),
					
					new Point(10, 40),					
					new Point(15, 40),
					new Point(20, 40), 
					new Point(25, 40),
					new Point(30, 40),
					new Point(35, 40),
					
					new Point(40, 15),
					new Point(40, 20),
					new Point(40, 25),
					new Point(40, 30),
					new Point(40, 35),
					new Point(40, 40),
					
					// level 15, 35
					new Point(15, 15),
					new Point(20, 15), 
					new Point(25, 15),
					new Point(30, 15),
					new Point(35, 15),
					
					new Point(15, 20),
					new Point(15, 25),
					new Point(15, 30),
					new Point(15, 35),
					
					new Point(20, 35), 
					new Point(25, 35),
					new Point(30, 35),
					
					new Point(35, 20),
					new Point(35, 25),
					new Point(35, 30),
					new Point(35, 35),

					// level 20, 30
					new Point(20, 20), 
					new Point(25, 20),
					new Point(30, 20),
					
					new Point(20, 25),
					
					new Point(20, 30), 
					new Point(25, 30),
					new Point(30, 30),
					
					new Point(30, 25),
				};
		TowerCount = TowerLocation.length;
		
		// ***** initialize obstacle locations *****
		ObstacleLocation = new Point[]
				{
					// level 5, 45
					//new Point(5, 7),	// using this slot for central planner
					new Point(5, 8),
					new Point(5, 9),
					new Point(5, 11),
					new Point(5, 12),
					new Point(5, 13),
					new Point(5, 14),
					
					new Point(5, 21),
					new Point(5, 22),
					new Point(5, 23),
					new Point(5, 24),
					new Point(5, 26),
					new Point(5, 27),
					new Point(5, 28),
					new Point(5, 29),

					new Point(5, 36),
					new Point(5, 37),
					new Point(5, 38),
					new Point(5, 39),
					new Point(5, 41),
					new Point(5, 42),
					new Point(5, 43),
					// new Point(5, 44),

					new Point(45, 7),
					new Point(45, 8),
					new Point(45, 9),
					new Point(45, 11),
					new Point(45, 12),
					new Point(45, 13),
					new Point(45, 14),
					
					new Point(45, 21),
					new Point(45, 22),
					new Point(45, 23),
					new Point(45, 24),
					new Point(45, 26),
					new Point(45, 27),
					new Point(45, 28),
					new Point(45, 29),

					new Point(45, 36),
					new Point(45, 37),
					new Point(45, 38),
					new Point(45, 39),
					new Point(45, 41),
					new Point(45, 42),
					new Point(45, 43),
					// new Point(5, 44),
					
					//new Point(6, 5),
					new Point(7, 5),
					new Point(8, 5),
					new Point(9, 5),
					new Point(11, 5),
					new Point(12, 5),
					new Point(13, 5),
					new Point(14, 5),
					
					new Point(21, 5),
					new Point(22, 5),
					new Point(23, 5),
					new Point(24, 5),
					new Point(26, 5),
					new Point(27, 5),
					new Point(28, 5),
					new Point(29, 5),
					
					new Point(36, 5),
					new Point(37, 5),
					new Point(38, 5),
					new Point(39, 5),
					new Point(41, 5),
					new Point(42, 5),
					new Point(43, 5),
					
					new Point(7, 45),
					new Point(8, 45),
					new Point(9, 45),
					new Point(11, 45),
					new Point(12, 45),
					new Point(13, 45),
					new Point(14, 45),
					
					new Point(21, 45),
					new Point(22, 45),
					new Point(23, 45),
					new Point(24, 45),
					new Point(26, 45),
					new Point(27, 45),
					new Point(28, 45),
					new Point(29, 45),
					
					new Point(36, 45),
					new Point(37, 45),
					new Point(38, 45),
					new Point(39, 45),
					new Point(41, 45),
					new Point(42, 45),
					new Point(43, 45),
					
					// level 10, 40
					new Point(10, 12),
					new Point(10, 13),
					new Point(10, 14),
					new Point(10, 15),
					new Point(10, 16),
					new Point(10, 17),
					new Point(10, 18),
					new Point(10, 19),
					
					new Point(10, 31),
					new Point(10, 32),
					new Point(10, 33),
					new Point(10, 34),
					new Point(10, 35),
					new Point(10, 36),
					new Point(10, 37),
					new Point(10, 38),
					
					new Point(40, 12),
					new Point(40, 13),
					new Point(40, 14),
					new Point(40, 15),
					new Point(40, 16),
					new Point(40, 17),
					new Point(40, 18),
					new Point(40, 19),
					
					new Point(40, 31),
					new Point(40, 32),
					new Point(40, 33),
					new Point(40, 34),
					new Point(40, 35),
					new Point(40, 36),
					new Point(40, 37),
					new Point(40, 38),
					
					new Point(12, 10),
					new Point(13, 10),
					new Point(14, 10),
					new Point(15, 10),
					new Point(16, 10),
					new Point(17, 10),
					new Point(18, 10),
					new Point(19, 10),
					
					new Point(31, 10),
					new Point(32, 10),
					new Point(33, 10),
					new Point(34, 10),
					new Point(35, 10),
					new Point(36, 10),
					new Point(37, 10),
					new Point(38, 10),
					
					new Point(12, 40),
					new Point(13, 40),
					new Point(14, 40),
					new Point(15, 40),
					new Point(16, 40),
					new Point(17, 40),
					new Point(18, 40),
					new Point(19, 40),
					
					new Point(31, 40),
					new Point(32, 40),
					new Point(33, 40),
					new Point(34, 40),
					new Point(35, 40),
					new Point(36, 40),
					new Point(37, 40),
					new Point(38, 40),
					
					// level 15 25
					new Point(15, 21),
					new Point(15, 22),
					new Point(15, 23),
					new Point(15, 24),
					new Point(15, 26),
					new Point(15, 27),
					new Point(15, 28),
					new Point(15, 29),
					
					new Point(21, 15),
					new Point(22, 15),
					new Point(23, 15),
					new Point(24, 15),
					new Point(26, 15),
					new Point(27, 15),
					new Point(28, 15),
					new Point(29, 15),
					
					new Point(35, 21),
					new Point(35, 22),
					new Point(35, 23),
					new Point(35, 24),
					new Point(35, 26),
					new Point(35, 27),
					new Point(35, 28),
					new Point(35, 29),
					
					new Point(21, 35),
					new Point(22, 35),
					new Point(23, 35),
					new Point(24, 35),
					new Point(26, 35),
					new Point(27, 35),
					new Point(28, 35),
					new Point(29, 35),
					
					// level 20 30
					new Point(23, 20),
					new Point(24, 20),
					new Point(26, 20),
					new Point(27, 20),
					
					new Point(23, 30),
					new Point(24, 30),
					new Point(26, 30),
					new Point(27, 30),
					
					new Point(20, 23),
					new Point(20, 24),
					new Point(20, 26),
					new Point(20, 27),
					
					new Point(30, 23),
					new Point(30, 24),
					new Point(30, 26),
					new Point(30, 27),
					
				};
		ObstacleCount = ObstacleLocation.length;
		
		
	}
}
