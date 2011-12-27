package hardware;

public class EEPROM
{
	protected int storage[] = null;
	protected int size = 0;
	
	public EEPROM( int size )
	{
		this.size = size;
		storage = new int[size];
		
		for( int i=0; i<size; i++ )
			storage[i] = 0;
	}
	
	public void set( int offset, int value )
	{
		storage[offset] = value;
	}
	
	public int get( int offset )
	{
		return storage[offset];
	}
	
}
