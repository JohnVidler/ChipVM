package hardware;

public class Pic10f200 extends PicProcessor
{
	
	
	public Pic10f200( int program[] )
	{
		super( program );
		progmem = new Memory( 256, 12 );			// 256 lines of 12-bit opcode instructions
		registers = new Memory( 0x00FF, 8 );		// 0x00FF registers
		stack = new Stack( 2, 9 );					// Two level stack
		
		Memory offsetMemory = (Memory)new ProxyMemory( registers, 8, 0x10 );
		
		try
		{
			offsetMemory.set( 0, 0x42 );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		// Load the program in ROM
		flash( program );
		
		// Configure memory aliases for this chip
		registers.setAlias( 0x00,	"INDF" );
		registers.setAlias( 0x01,	"TMR0" );
		registers.setAlias( 0x02,	"PCL" );
		registers.setAlias( 0x03,	"STATUS" );
		registers.setAlias( 0x04,	"FSR" );
		registers.setAlias( 0x05,	"OSCCAL" );
		registers.setAlias( 0x06,	"GPIO" );
		registers.setAlias( 0x07,	"CMCON0" );
		
		for( int i=0x08; i<0x0F; i++ )
			registers.setMeta( i, Memory.UNIMPLEMENTED );
		
		
		// Build a new unstruction set
		instructionSet = new PicInstructionSet();
		PicInstructionSet set = (PicInstructionSet)instructionSet;
		set.setPC( 0x02 );
		instructionSet.dump();
	}
	
	public int getPC()
	{
		try
		{
			return registers.get( "PCL" );
		}
		catch( NoSuchAliasException err )
		{
			System.err.println( "Could not find the PCL register!" );
			System.err.println( err.getMessage() );
			return 0;
		}
	}
	
}
