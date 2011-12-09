package hardware;

public class Stack extends Memory
{
	protected int offset;
	
	public Stack( int size, int width )
	{
		super( size, width );
		offset = 0;
	}
	
	public void push( int data ) throws OutOfMemoryError
	{
		if( offset > sRAM.length )
			throw new OutOfMemoryError( "Stack overflow!" );
		
		sRAM[offset] = data;
		offset++;
	}
	
	public int peek()
	{
		return sRAM[offset];
	}
	
	public int pop() throws IndexOutOfBoundsException
	{
		if( offset < 0 )
			throw new IndexOutOfBoundsException( "POP'd with nothing on the stack!" );
		
		return sRAM[offset--];
	}
	
	
}
