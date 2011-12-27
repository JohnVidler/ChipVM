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
		int offset = 0;
		registers.setAlias( offset++,	"INDF" );
		registers.setAlias( offset++,	"TMR0" );
		registers.setAlias( offset++,	"PCL" );
		registers.setAlias( offset++,	"STATUS" );
		registers.setAlias( offset++,	"FSR" );
		registers.setAlias( offset++,	"PORTA" );
		registers.setAlias( offset++,	"TRISA" );
		registers.setAlias( offset++,	"LATA" );
		registers.setAlias( offset++,	"ANSELA" );
		registers.setAlias( offset++,	"WPUA" );
		registers.setAlias( offset++,	"PCLATH" );
		registers.setAlias( offset++,	"INTCON" );
		registers.setAlias( offset++,	"PIR1" );
		registers.setAlias( offset++,	"PIE1" );
		registers.setAlias( offset++,	"OPTION_REG" );
		registers.setAlias( offset++,	"PCON" );
		registers.setAlias( offset++,	"OSCCON" );
		registers.setAlias( offset++,	"TMR2" );
		registers.setAlias( offset++,	"PR2" );
		registers.setAlias( offset++,	"T2CON" );
		registers.setAlias( offset++,	"PWM1DCL" );
		registers.setAlias( offset++,	"PWN1DC" );
		registers.setAlias( offset++,	"PWM1CON" );
		registers.setAlias( offset++,	"PWM2DCL" );
		registers.setAlias( offset++,	"PWM2DC" );
		registers.setAlias( offset++,	"PWM2CON" );
		registers.setAlias( offset++,	"IOCAP" );
		registers.setAlias( offset++,	"IOCAN" );
		registers.setAlias( offset++,	"IOCAF" );
		registers.setAlias( offset++,	"FVRCON" );
		registers.setAlias( offset++,	"ADRES" );
		registers.setAlias( offset++,	"ADCON" );
		
		registers.setAlias( offset++,	"PMADRL" );
		registers.setAlias( offset++,	"PMADRH" );
		registers.setAlias( offset++,	"PMDATL" );
		registers.setAlias( offset++,	"PMDATH" );
		registers.setAlias( offset++,	"PMCON1" );
		registers.setAlias( offset++,	"PMCON2" );
		registers.setAlias( offset++,	"CLKRCON" );
		registers.setAlias( offset++,	"NCO1ACCL" );
		registers.setAlias( offset++,	"NCO1ACCH" );
		registers.setAlias( offset++,	"NCO1ACCU" );
		registers.setAlias( offset++,	"NCO1INCL" );
		registers.setAlias( offset++,	"NCO1INCH" );
		offset++;										// SKIP due to reserved register...
		registers.setAlias( offset++,	"NCO1CON" );
		registers.setAlias( offset++,	"NCO1CLK" );
		offset++;										// SKIP due to reserved register...
		registers.setAlias( offset++,	"WDTCON" );
		registers.setAlias( offset++,	"CLC1CON" );
		registers.setAlias( offset++,	"CLC1SEL1" );
		registers.setAlias( offset++,	"CLC1SEL2" );
		registers.setAlias( offset++,	"CLC1POL" );
		registers.setAlias( offset++,	"CLC1GATE1" );
		registers.setAlias( offset++,	"CLC1GATE2" );
		registers.setAlias( offset++,	"CLC1GATE3" );
		registers.setAlias( offset++,	"CLC1GATE4" );
		registers.setAlias( offset++,	"CWG1CON0" );
		registers.setAlias( offset++,	"CWG1CON1" );
		registers.setAlias( offset++,	"CWG1ASD" );
		registers.setAlias( offset++,	"CWG1RC" );
		registers.setAlias( offset++,	"CWG1FC" );
		registers.setAlias( offset++,	"VREGCON" );
		registers.setAlias( offset++,	"BORCON" );
		
		
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

