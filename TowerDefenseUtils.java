// TowerDefenseUtils.java
// couple of utility functions. 

package towerDefense;

import java.awt.Point;

import repast.simphony.space.grid.Grid;

public class TowerDefenseUtils 
{
	public static boolean IsGridCellEmpty(Grid grid, Point point)
	{
		return IsGridCellEmpty(grid, (int)point.getX(), (int)point.getY()); 
	}
	
	public static boolean IsGridCellEmpty(Grid grid, int x, int y)
	{
		int size = 0;
		for(Object obj : grid.getObjectsAt(x, y))
		{
			size++;
		}
		
		return (0 == size? true:false);
	}
}
