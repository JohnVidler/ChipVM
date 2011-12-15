package hardware;

public class Processor extends Chip
{
	private Processor self = null;
	
	protected Memory progmem = null;
	protected InstructionSet instructionSet = null;
	protected Memory registers = null;
	protected Stack stack = null;
	public int pc = 0;
	
	protected int busWidth = 8;
	
	public int skippedInstructions = 0;
	
	public WireListener clkWire = new WireListener()
	{
		public void stateChange( WireState state )
		{
			if( state == WireState.RISING )
			{
				int op = op_readNextInstruction();
			
				System.out.print( "0x" +Integer.toHexString(pc) + "\t" + Integer.toHexString(op) + "\t" );
				
				try
				{
					int err = instructionSet.doOp( self, registers, op );
				}
				catch( Throwable e )
				{
					skippedInstructions++;
					System.out.println("");
					//e.printStackTrace();
				}
				
				// Catch if the processor runs out of PROGMEM
				if( pc + 1 > progmem.getSize()-1 )
				{
					System.out.println( "Processor ran off the end of PROGMEM!" );
					System.out.println( "Skipped " +skippedInstructions+ " instructions." );
					System.exit(1);
				}
				
				try { registers.set( "PCL", pc ); }
				catch( Throwable t ){ System.err.println( "Could not set PCL (No PCL?)" ); }
				
				pc = (pc + 1) % (int)Math.pow(2, busWidth+1);
			}
		}
	};
	
	public Processor( int program[] )
	{
		super( 8 );
		self = this;
	}
	
	public void flash( int program[] )
	{
		// Load the program into ROM
		for( int i=0; i<program.length; i++ )
		{
			try
			{
				progmem.set( i, program[i] );
			}
			catch( NoSuchAliasException err )
			{
				System.err.println( err );
				err.printStackTrace();
			}
		}
	}
	
	public Memory getRAM() { return registers; }
	public Memory getStack() { return stack; }
	public Memory getROM() { return progmem; }
	
	
	protected int op_readNextInstruction()
	{
		int op = 0;
		try
		{
			op = progmem.get(pc);
		}
		catch( Exception err )
		{
			System.err.println( err );
			err.printStackTrace();
		}
		return op;
	}
	
}
