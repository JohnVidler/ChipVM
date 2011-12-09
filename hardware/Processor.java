package hardware;

public class Processor extends Chip
{
	private Processor self = null;
	
	protected int progmem[] = null;
	protected InstructionSet instructionSet = null;
	protected Memory registers = null;
	
	protected int busWidth = 8;
	
	public int wReg = 0;
	public int pc = 0;
	
	public int skippedInstructions = 0;
	
	public WireListener clkWire = new WireListener()
	{
		public void stateChange( WireState state )
		{
			if( state == WireState.RISING )
			{
				int op = op_readNextInstruction();
				int operand = 0;
				int mode = 0;
			
				System.out.println( "0x" +Integer.toHexString(pc) + "\t" + Integer.toBinaryString(op) );
				
				try{
					int err = instructionSet.doOp( self, registers, op );
				}
				catch( Exception e )
				{
					skippedInstructions++;
					//e.printStackTrace();
				}
				
				// Catch if the processor runs out of PROGMEM
				if( pc + 1 == (int)Math.pow(2, busWidth+1) )
				{
					System.out.println( "Processor ran off the end of PROGMEM!" );
					System.out.println( "Skipped " +skippedInstructions+ " instructions." );
					System.exit(1);
				}

				pc = (pc + 1) % (int)Math.pow(2, busWidth+1);
			}
		}
	};
	
	public Processor( int program[] )
	{
		super( 8 );
		self = this;
		progmem = program;
		instructionSet = new InstructionSet();
		registers = new Memory( 0x1F, 8 );
		
		// Configure memory aliases for this chip
		registers.setAlias( 0x00,	"INDF" );
		registers.setAlias( 0x01,	"TMR0" );
		registers.setAlias( 0x02,	"PCL" );
		registers.setAlias( 0x03,	"STATUS" );
		registers.setAlias( 0x04,	"FSR" );
		registers.setAlias( 0x05,	"OSCCAL" );
		registers.setAlias( 0x06,	"GPIO" );
		registers.setAlias( 0x07,	"CMCON0" );
		
		
		// NOP
		instructionSet.add( new Instruction( "NOP", "0000 0000 0000" )
		{
			public int operator( Processor p, Memory m, int op )
			{
				System.out.println( "NOP" );
				return 0;
			}
		} );
		
		// GOTO
		instructionSet.add( new Instruction( "GOTO", "101* **** ****" )
		{
			public int operator( Processor p, Memory m, int op )
			{
				int offset = op & 0x1FF;
				p.pc = offset-1;
				System.out.println( "GOTO 0x" + Integer.toHexString(offset) );
				return 0;
			}
		} );
		
		
		// ADDWF
		instructionSet.add( new Instruction( "ADDWF", "0001 11** ****" )
		{
			public int operator( Processor p, Memory m, int op )
			{
				int offset = op & 0x01F;
				int d = ((op & 0x020) == 0x020)?1:0;
				
				if( d == 0 )
					wReg = m.get( offset ) + wReg;
				else
					m.set( offset, m.get( offset ) + wReg );
				
				System.out.println( "ADDWF 0x" +Integer.toHexString(offset)+ ", [" +d+ "]" );
				return 0;
			}
		} );
		
		// ANDLW
		instructionSet.add( new Instruction( "ANDLW", "1110 **** ****" )
		{
			public int operator( Processor p, Memory m, int op )
			{
				int value = op & 0x0FF;
				
				wReg = value & wReg;
				
				System.out.println( "ANDLW 0x" +Integer.toHexString(value) );
				return 0;
			}
		} );
		
		// IORLW
		instructionSet.add( new Instruction( "IORLW", "1101 **** ****" )
		{
			public int operator( Processor p, Memory m, int op )
			{
				int value = op & 0x0FF;
				
				wReg = value | wReg;
				
				System.out.println( "IORLW 0x" +Integer.toHexString(value) );
				return 0;
			}
		} );
		
		// CRLF
		instructionSet.add( new Instruction( "CRLF", "0000 011* ****" )
		{
			public int operator( Processor p, Memory m, int op )
			{
				int offset = op & 0x01F;
				
				m.set( offset, 0x00 );
				
				System.out.println( "CLRF 0x" +Integer.toHexString(offset) );
				return 0;
			}
		} );
		
		// CRLW
		instructionSet.add( new Instruction( "CRLW", "0000 0100 0000" )
		{
			public int operator( Processor p, Memory m, int op )
			{
				wReg = 0;
				
				System.out.println( "CLRW" );
				return 0;
			}
		} );
		
		instructionSet.dump();
		
	}
	
	
	protected int op_readNextInstruction()
	{
		int idx = pc * 2;
		
		int opcode = 0;	//NOP
		
		if( pc % 2 == 0 )
			opcode = ((progmem[idx] & 0xFF) << 4) | ((progmem[idx+1] & 0xFF) >> 4);
		else
			opcode = (progmem[idx] & 0xFF) | ((progmem[idx-1] & 0xFF) << 8 );
		
		return opcode & 0xFFF;
	}
	
}
