package hardware;

public abstract class Processor extends Chip
{
	private Processor self = null;
	
	protected Memory progmem = null;
	protected InstructionSet instructionSet = null;
	protected Memory registers = null;
	protected Stack stack = null;
	
	protected int busWidth = 8;
	
	public int skippedInstructions = 0;
	
	private int op = 0;
	
	public WireListener clkWire = new WireListener()
	{
		public void stateChange( WireState state )
		{
			if( state == WireState.RISING )
			{
				op = op_readNextInstruction();
			}
			else if( state == WireState.HIGH )
			{
				//System.out.print( "0x" +Integer.toHexString(getPC()) + "\t" + Integer.toHexString(op) + "\t" );
				
				try
				{
					int err = instructionSet.doOp( self, registers, op );
				}
				catch( Throwable e )
				{
					skippedInstructions++;
					System.out.println( e.getMessage() );
					//e.printStackTrace();
				}
			}
			else if( state == WireState.LOW )
			{
				// Catch if the processor runs out of PROGMEM
				if( getPC() + 1 > progmem.getSize()-1 )
				{
					System.out.println( "Processor ran off the end of PROGMEM!" );
					System.out.println( "Skipped " +skippedInstructions+ " instructions." );
					System.exit(1);
				}
			}
		}
	};
	
	public Processor( int pins, int program[] )
	{
		super( pins );
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
	public Stack getStack() { return stack; }
	public Memory getROM() { return progmem; }
	public int getPC(){ return 0x00; }
	
	
	protected abstract int op_readNextInstruction();
}
