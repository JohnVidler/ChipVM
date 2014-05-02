package hardware.pic;

import hardware.Processor;

public abstract class PicProcessor extends Processor
{
	public int wReg = 0;
	protected int maxValue = (int)Math.pow(2, 8);
	
	public PicProcessor( int pins, int[] program )
	{
		super( pins, program );
	}
	
        protected int op_readNextInstruction()
	{
		int op = 0;
		try
		{
			op = progmem.get( registers.get( "PCL" ) );
		}
		catch( Exception err )
		{
			System.err.println( err );
			err.printStackTrace();
		}
		
		try
		{
			registers.inc( "PCL" );
		}
		catch( Throwable t ){ System.err.println( "Could not inc PCL (No PCL?)" ); System.exit(1); }
				
		return op;
	}
}
