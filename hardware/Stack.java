package hardware;

public class Stack extends Memory
{
	protected int offset;
	
	public Stack( int size, int width )
	{
		super( size, width );
		offset = 0;
	}
	
	public void push( int data ) throws OutOfMemoryError, NoSuchAliasException
	{
		if( offset > sRAM.length )
			throw new OutOfMemoryError( "Stack overflow!" );
		if( offset < 0 )
			throw new OutOfMemoryError( "Stack underflow!" );
		
		
		set( offset, data );
		offset++;
	}
	
	public int peek()
	{
		return sRAM[offset];
	}
	
	public int pop() throws IndexOutOfBoundsException, NoSuchAliasException
	{
		if( offset < 0 )
			throw new OutOfMemoryError( "Stack underflow!" );
		
		offset--;
		return get( offset );
	}
	
	
}
