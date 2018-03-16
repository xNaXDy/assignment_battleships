import java.util.HashMap;
import java.util.Map;

/*
 * Durham University
 * Student: xtmv28
 * ID: 000686851
 */

public class Board implements BoardInterface
{
	private Map<Placement, ShipInterface> ships;
	
	public Board()
	{
		ships = new HashMap<Placement, ShipInterface>();
	}
	
	@Override
	public void placeShip(ShipInterface ship, Position position, boolean isVertical)
			throws InvalidPositionException, ShipOverlapException
	{
		if(ships.containsKey(position))
		{
			throw new ShipOverlapException("There already exists a ship in that position.");
		}
		
		if(position.getX() + (!isVertical ? ship.getSize() : 0) >= 10 || position.getY() + (isVertical ? ship.getSize() : 0) >= 10)
		{
			throw new InvalidPositionException("Ship boundaries are out of board range.");
		}
		
		int[][] drawArray = new int[10][10];
		
		for(int i = 0; i < drawArray.length; i++)
		{
			for(int j = 0; j < drawArray[i].length; j++)
			{
				drawArray[i][j] = 0;
			}
		}
		
		for (Map.Entry<Placement,ShipInterface> pair : ships.entrySet())
		{
			for(int i = 0; i < pair.getValue().getSize(); i++)
			{
				drawArray[pair.getKey().getPosition().getX() + (!pair.getKey().isVertical() ? i : 0)][pair.getKey().getPosition().getY() + (pair.getKey().isVertical() ? i : 0)] = 1;
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
		
		ships.put(new Placement(position, isVertical), ship);
	}
	
	private ShipInterface getShipAt(Position pos)
	{
		for (Map.Entry<Placement,ShipInterface> pair : ships.entrySet())
		{
			for(int i = 0; i < pair.getValue().getSize(); i++)
			{
				int x = pair.getKey().getPosition().getX() + (!pair.getKey().isVertical() ? i : 0);
				int y = pair.getKey().getPosition().getY() + (pair.getKey().isVertical() ? i : 0);
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
		if(ships.containsValue(ship))
		{
			boolean isVertical = false;
			for (Map.Entry<Placement,ShipInterface> pair : ships.entrySet())
			{
				if(pair.getValue().equals(ship))
				{
					shipPos = pair.getKey().getPosition();
					isVertical = pair.getKey().isVertical();
					break;
				}
			}
			offset = !isVertical ? Math.abs(position.getX() - shipPos.getX()) : Math.abs(position.getY() - shipPos.getY());
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
		for (Map.Entry<Placement,ShipInterface> pair : ships.entrySet())
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
