

public class Ship implements ShipInterface
{
	private int[] hits;
	
	public Ship(int size)
	{
		hits = new int[size];
		for(int i = 0; i < hits.length; i++)
		{
			hits[i] = 0;
		}
	}
	
	@Override
	public int getSize()
	{
		return hits.length;
	}

	@Override
	public boolean isSunk()
	{
		for(int i = 0; i < hits.length; i++)
		{
			if(hits[i] == 0)
			{
				return false;
			}
		}
		
		return true;
	}

	@Override
	public void shoot(int offset) throws InvalidPositionException
	{
		if(offset < 0 || offset >= hits.length)
		{
			throw new InvalidPositionException("Invalid ship length specified: " + offset + " (must be within 0 or " + hits.length + ")");
		}
		
		hits[offset] = 1;
	}

	@Override
	public ShipStatus getStatus(int offset) throws InvalidPositionException
	{
		if(offset < 0 || offset >= hits.length)
		{
			throw new InvalidPositionException("Invalid ship length specified: " + offset + " (must be within 0 or " + hits.length + ")");
		}
		
		for(int i = 0; i < hits.length; i++)
		{
			// if the ship is intact at at least one point, return the ship status for the point specified
			if(hits[i] == 0)
			{
				return hits[offset] == 0 ? ShipStatus.INTACT : ShipStatus.HIT;
			}
		}
		
		// otherwise, tell that it's sunk
		return ShipStatus.SUNK;
	}

}
