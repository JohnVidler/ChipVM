package hardware;

public abstract class Instruction implements Comparable<Object>
{
	protected String name;
	protected int opcode;
	protected int mask;
	protected int length;
	
	/**
	 * Takes a single argument in the form of 00001111****, where * == ignore
	 */
	public Instruction( String name, String opcode )
	{
		this.name = name;
		opcode = opcode.replace( " ", "" );
		length = opcode.replace("0", "1").replace("*","0").replace("0","").length();
		mask = Integer.parseInt( opcode.replace("0", "1").replace("*","0"), 2 );
		
		String buffer = opcode.replace( "*", "0" );
		this.opcode = Integer.parseInt( buffer, 2 );
	}
	
	public String getName() { return name; }
	public int getOpcode() { return opcode; }
	public int getMask() { return mask; }
	
	public abstract int operator( Processor p, Memory m, int op ) throws Throwable;
	
	public int compareTo( Object o )
	{
		Instruction b = (Instruction)o;
		int delta = length - b.length;
		int opDelta = opcode - b.opcode;
		
		if( delta != 0 )
			return delta;
		
		return opDelta;
		
		// If this < o, return a negative value
		// If this = o, return 0
		// If this > o, return a positive value
	}
}
