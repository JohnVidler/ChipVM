package hardware;

public class ProgrammableTimer implements WireListener
{
	// 'Physical' memory hardware
	protected Memory hardware = null;
	
	// Register offsets
	protected int timerReg = 0x00;
	protected int optionReg = 0x00;
	//protected int = 0x00;
	//protected int = 0x00;
	//protected int = 0x00;
	
	
	public ProgrammableTimer( Memory memory, int timerReg )
	{
		hardware = memory;
		
	}
	
	
	/**
	 * Actually handle a clock signal from a 'wire' source
	 */
	public void stateChange( WireState state )
	{
		
	}
}
