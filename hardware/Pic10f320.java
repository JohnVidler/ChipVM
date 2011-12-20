package hardware;

public class Pic10f320 extends PicProcessor
{
	
	
	public Pic10f320( int program[] )
	{
		super( program );
		progmem = new Memory( 256, 12 );			// 256 lines of 12-bit opcode instructions
		registers = new Memory( 0x00FF, 8 );		// 0x00FF registers
		stack = new Stack( 8, 9 );					// Two level stack
		
		// Load the program in ROM
		flash( program );
		
		// Configure memory aliases for this chip
		registers.setAlias( 0x00,	"INDF" );
		registers.setAlias( 0x01,	"TMR0" );
		registers.setAlias( 0x02,	"PCL" );
		registers.setAlias( 0x03,	"STATUS" );
		registers.setAlias( 0x04,	"FSR" );
		registers.setAlias( 0x05,	"PORTA" );
		registers.setAlias( 0x06,	"TRISA" );
		registers.setAlias( 0x07,	"LATA" );
		registers.setAlias( 0x08,	"ANSELA" );
		registers.setAlias( 0x09,	"WPUA" );
		registers.setAlias( 0x0a,	"PCLATH" );
		registers.setAlias( 0x0b,	"INTCON" );
		registers.setAlias( 0x0c,	"PIR1" );
		registers.setAlias( 0x0d,	"PIE1" );
		registers.setAlias( 0x0e,	"OPTION_REG" );
		registers.setAlias( 0x0f,	"PCON" );
		registers.setAlias( 0x10,	"OSCCON" );
		registers.setAlias( 0x11,	"TMR2" );
		registers.setAlias( 0x12,	"PR2" );
		registers.setAlias( 0x13,	"T2CON" );
		registers.setAlias( 0x14,	"PWM1DCL" );
		registers.setAlias( 0x15,	"PWN1DC" );
		registers.setAlias( 0x16,	"PWM1CON" );
		registers.setAlias( 0x17,	"PWM2DCL" );
		registers.setAlias( 0x18,	"PWM2DC" );
		registers.setAlias( 0x19,	"PWM2CON" );
		registers.setAlias( 0x1a,	"IOCAP" );
		registers.setAlias( 0x1b,	"IOCAN" );
		registers.setAlias( 0x1c,	"IOCAF" );
		registers.setAlias( 0x1d,	"FVRCON" );
		registers.setAlias( 0x1e,	"ADRES" );
		registers.setAlias( 0x1f,	"ADCON" );
		
		
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

