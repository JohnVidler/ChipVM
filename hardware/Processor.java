package hardware;

public class Processor extends Chip
{
	private Processor self = null;
	
	protected int progmem[] = null;
	protected InstructionSet instructionSet = null;
	protected Memory registers = null;
	protected Stack stack = null;
	
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
			
				System.out.print( "0x" +Integer.toHexString(pc) + "\t" + Integer.toBinaryString(op) + "\t" );
				
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
				if( pc + 1 == (int)Math.pow(2, busWidth+1) )
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
		progmem = program;
		instructionSet = new InstructionSet();
		registers = new Memory( 0x1F, 8 );
		stack = new Stack( 2, 9 );
		
		//registers.debug = true;
		//stack.debug = true;
		
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
			public int operator( Processor p, Memory m, int op ) throws Throwable
			{
				System.out.println( "NOP" );
				return 0;
			}
		} );
		
		// GOTO
		instructionSet.add( new Instruction( "GOTO", "101* **** ****" )
		{
			public int operator( Processor p, Memory m, int op ) throws Throwable
			{
				int offset = op & 0x1FF;
				//p.pc = offset-1;
				System.out.println( "GOTO 0x" + Integer.toHexString(offset) );
				return 0;
			}
		} );
		
		
		// ADDWF
		instructionSet.add( new Instruction( "ADDWF", "0001 11** ****" )
		{
			public int operator( Processor p, Memory m, int op ) throws Throwable
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
			public int operator( Processor p, Memory m, int op ) throws Throwable
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
			public int operator( Processor p, Memory m, int op ) throws Throwable
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
			public int operator( Processor p, Memory m, int op ) throws Throwable
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
			public int operator( Processor p, Memory m, int op ) throws Throwable
			{
				wReg = 0;
				
				System.out.println( "CLRW" );
				return 0;
			}
		} );
		
		// BTFSC
		instructionSet.add( new Instruction( "BTFSC", "0110 **** ****" )
		{
			public int operator( Processor p, Memory m, int op ) throws Throwable
			{
				int bit = (op & 0x0E0) >> 5;
				int file = op & 0x01F;
				
				// Bit test 'bit' and skip if zero
				if( (m.get( file ) & (1 << bit)) == 0 )
					p.pc = p.pc + 1;
				
				System.out.println( "BTFSC " +bit+ ", " +Integer.toHexString(file) );
				return 0;
			}
		} );
		
		// BTFSS
		instructionSet.add( new Instruction( "BTFSS", "0111 **** ****" )
		{
			public int operator( Processor p, Memory m, int op ) throws Throwable
			{
				int bit = (op & 0x0E0) >> 5;
				int file = op & 0x01F;
				
				// Bit test 'bit' and skip if zero
				if( (m.get( file ) & (1 << bit)) == (1 << bit) )
					p.pc = p.pc + 1;
				
				System.out.println( "BTFSS " +bit+ ", " +Integer.toHexString(file) );
				return 0;
			}
		} );
		
		// XORLW
		instructionSet.add( new Instruction( "XORLW", "1111 **** ****" )
		{
			public int operator( Processor p, Memory m, int op ) throws Throwable
			{
				int literal = op & 0x0FF;
				
				p.wReg = p.wReg ^ literal;
				
				System.out.println( "XORLW " +literal );
				return 0;
			}
		} );
		
		// CALL
		instructionSet.add( new Instruction( "CALL", "1001 **** ****" )
		{
			public int operator( Processor p, Memory m, int op ) throws Throwable
			{
				try
				{
					int lowerAddress = op & 0x07F;
					int upperAddress = (m.get( "STATUS" ) & 0x30) << 4;
					
					p.stack.push( p.pc + 1 );
					//p.pc = (upperAddress | lowerAddress) - 1;
					
					System.out.println( "CALL " +Integer.toBinaryString(p.pc) );
					return 0;
				}
				catch( NoSuchAliasException e )
				{
					System.out.println( "CALL " +e );
					return -1;
				}
			}
		} );
		
		
		// DECFSZ 
		instructionSet.add( new Instruction( "DECFSZ", "0010 11** ****" )
		{
			public int operator( Processor p, Memory m, int op ) throws Throwable
			{
				int file = op & 0x01F;
				int d = ((op & 0x020) == 0x020)?1:0;
				int result = 1;
				
				if( d == 1 )
					result = m.dec( file );
				else
					result = p.wReg = m.get( file ) - 1;
				
				if( result == 0 )
					p.pc = p.pc + 1;
				
				System.out.println( "DECFSZ " +Integer.toHexString(file) );
				return 0;
			}
		} );
		
		// BCF 
		instructionSet.add( new Instruction( "BCF", "0100 **** ****" )
		{
			public int operator( Processor p, Memory m, int op ) throws Throwable
			{
				int bit = (op & 0x0E0) >> 5;
				int file = op & 0x01F;
				
				m.set( file, m.get( file ) & ~(1 << bit) );
				
				System.out.println( "BCF " +bit+ ", " +Integer.toHexString(file) );
				return 0;
			}
		} );
		
		// MOVLW
		instructionSet.add( new Instruction( "MOVLW", "1100 **** ****" )
		{
			public int operator( Processor p, Memory m, int op ) throws Throwable
			{
				int literal = op & 0x0FF;
				
				p.wReg = literal;
				
				System.out.println( "MOVLW " +Integer.toHexString(literal) );
				return 0;
			}
		} );
		
		
		// MOVWF 0000 0010 1110
		instructionSet.add( new Instruction( "MOVWF", "0000 001* ****" )
		{
			public int operator( Processor p, Memory m, int op ) throws Throwable
			{
				int file = op & 0x01F;
				
				m.set( file, p.wReg );
				
				System.out.println( "MOVWF " +Integer.toHexString(file)+ " <- " +p.wReg );
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
