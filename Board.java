import java.util.HashMap;
import java.util.Map;

/*
 * Durham University
 * Student: xtmv28
 * ID: 000686851
 */

public class Board implements BoardInterface
{
	private Map<Position, ShipInterface> horizontalShips;
	private Map<Position, ShipInterface> verticalShips;
	
	public Board()
	{
		horizontalShips = new HashMap<Position, ShipInterface>();
		verticalShips = new HashMap<Position, ShipInterface>();
	}
	
	@Override
	public void placeShip(ShipInterface ship, Position position, boolean isVertical)
			throws InvalidPositionException, ShipOverlapException
	{
		if(horizontalShips.containsKey(position) || verticalShips.containsKey(position))
		{
			throw new ShipOverlapException("There already exists a ship in that position.");
		}
		
		int[][] drawArray = new int[10][10];
		
		for(int i = 0; i < drawArray.length; i++)
		{
			for(int j = 0; j < drawArray[i].length; j++)
			{
				drawArray[i][j] = 0;
			}
		}
		
		for (Map.Entry<Position,ShipInterface> pair : horizontalShips.entrySet())
		{
			for(int i = pair.getKey().getX(); i < pair.getValue().getSize(); i++)
			{
				drawArray[pair.getKey().getX() + i][pair.getKey().getY()] = 1;
			}
        }
		
		for (Map.Entry<Position,ShipInterface> pair : verticalShips.entrySet())
		{
			for(int i = pair.getKey().getX(); i < pair.getValue().getSize(); i++)
			{
				drawArray[pair.getKey().getX()][pair.getKey().getY() + i] = 1;
			}
        }
		
		for(int i = 0; i < ship.getSize(); i++)
		{
			int x = position.getX() + (!isVertical ? i : 0);
			int y = position.getY() + (isVertical ? i : 0);
			if(drawArray[x][y] == 1)
			{
				throw new ShipOverlapException("Ship overlaps in position (" + x + ", " + y + ")");
			}
		}
		
		if(isVertical)
		{
			verticalShips.put(position, ship);
		}
		else
		{
			horizontalShips.put(position, ship);
		}
	}
	
	private ShipInterface getShipAt(Position pos)
	{
		for (Map.Entry<Position,ShipInterface> pair : horizontalShips.entrySet())
		{
			for(int i = pair.getKey().getX(); i < pair.getValue().getSize(); i++)
			{
				int x = pair.getKey().getX() + i;
				int y = pair.getKey().getY();
				if(pos.getX() == x && pos.getY() == y)
				{
					return pair.getValue();
				}
			}
        }
		
		for (Map.Entry<Position,ShipInterface> pair : verticalShips.entrySet())
		{
			for(int i = pair.getKey().getX(); i < pair.getValue().getSize(); i++)
			{
				int x = pair.getKey().getX();
				int y = pair.getKey().getY() + i;
				if(pos.getX() == x && pos.getY() == y)
				{
					return pair.getValue();
				}
			}
        }
		
		return null;
	}

	@Override
	public void shoot(Position position) throws InvalidPositionException
	{
		ShipInterface ship = getShipAt(position);
		if(ship != null)
		{
			int offset = getShipOffset(ship, position);
			
			ship.shoot(offset);
		}
	}
	
	private int getShipOffset(ShipInterface ship, Position position)
	{
		Position shipPos = null;
		int offset = 0;
		if(horizontalShips.containsValue(ship))
		{
			for (Map.Entry<Position,ShipInterface> pair : horizontalShips.entrySet())
			{
				if(pair.getValue().equals(ship))
				{
					shipPos = pair.getKey();
					break;
				}
			}
			offset = Math.abs(position.getX() - shipPos.getX());
		}
		else if(verticalShips.containsValue(ship))
		{
			for (Map.Entry<Position,ShipInterface> pair : verticalShips.entrySet())
			{
				if(pair.getValue().equals(ship))
				{
					shipPos = pair.getKey();
					break;
				}
			}
			offset = Math.abs(position.getX() - shipPos.getY());
		}
		
		return offset;
	}

	@Override
	public ShipStatus getStatus(Position position) throws InvalidPositionException
	{
		ShipInterface ship = getShipAt(position);
		if(ship != null)
		{
			int offset = getShipOffset(ship, position);
			return ship.getStatus(offset);
		}
		
		return ShipStatus.NONE;
	}

	@Override
	public boolean allSunk()
	{
		for (Map.Entry<Position,ShipInterface> pair : horizontalShips.entrySet())
		{
			if(!pair.getValue().isSunk())
			{
				return false;
			}
		}
		
		for (Map.Entry<Position,ShipInterface> pair : verticalShips.entrySet())
		{
			if(!pair.getValue().isSunk())
			{
				return false;
			}
		}
		
		return true;
	}

	@Override
	public BoardInterface clone()
	{
		// TODO Auto-generated method stub
		return this.clone();
	}

}
