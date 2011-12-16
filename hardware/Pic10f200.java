package hardware;

public class Pic10f200 extends Processor
{
	public int wReg = 0;
	protected int maxValue = (int)Math.pow(2, 8);
	
	public Pic10f200( int program[] )
	{
		super( program );
		progmem = new Memory( 256, 12 );			// 256 lines of 12-bit opcode instructions
		registers = new Memory( 0x1F, 8 );		// 0x1F registers
		stack = new Stack( 2, 9 );					// Two level stack
		
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
		
		
		// Build a new unstruction set
		instructionSet = new InstructionSet();
		
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
				p.pc = offset-1;
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
				
				int result = (m.get( offset ) + wReg) % maxValue;
				
				if( d == 0 )
					wReg = result;
				else
					m.set( offset, result );
					
				// Set the zero status bit, if zero
				if( result == 0 )
					m.setBit( "STATUS", 2 );
				else
					m.clearBit( "STATUS", 2 );
				
				System.out.println( "ADDWF 0x" +Integer.toHexString(offset)+ ", [" +d+ "] = " + result );
				return 0;
			}
		} );
		
		// ANDWF
		instructionSet.add( new Instruction( "ANDWF", "0001 01** ****" )
		{
			public int operator( Processor p, Memory m, int op ) throws Throwable
			{
				int offset = op & 0x01F;
				int d = ((op & 0x020) == 0x020)?1:0;
				
				int result = (m.get( offset ) & wReg) % maxValue;
				
				if( d == 0 )
					wReg = result;
				else
					m.set( offset, result );
					
				// Set the zero status bit, if zero
				if( result == 0 )
					m.setBit( "STATUS", 2 );
				else
					m.clearBit( "STATUS", 2 );
				
				System.out.println( "ANDWF 0x" +Integer.toHexString(offset)+ ", [" +d+ "] = " + result );
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
		
		// CLRF
		instructionSet.add( new Instruction( "CLRF", "0000 011* ****" )
		{
			public int operator( Processor p, Memory m, int op ) throws Throwable
			{
				int offset = op & 0x01F;
				
				m.set( offset, 0x00 );
				
				System.out.println( "CLRF 0x" +Integer.toHexString(offset) );
				return 0;
			}
		} );
		
		// COMF
		instructionSet.add( new Instruction( "COMF", "0010 01** ****" )
		{
			public int operator( Processor p, Memory m, int op ) throws Throwable
			{
				int offset = op & 0x01F;
				int d = ((op & 0x020) == 0x020)?1:0;
				
				int result = ~m.get( offset );
				
				if( d == 0 )
					wReg = result;
				else
					m.set( offset, result );
					
				// Set the zero status bit, if zero
				if( result == 0 )
					m.setBit( "STATUS", 2 );
				else
					m.clearBit( "STATUS", 2 );
				
				System.out.println( "CLRF 0x" +Integer.toHexString(offset) );
				return 0;
			}
		} );
		
		// CLRW
		instructionSet.add( new Instruction( "CLRW", "0000 0100 0000" )
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
			public int operator( Processor chip, Memory m, int op ) throws Throwable
			{
				Pic10f200 p = (Pic10f200)chip;
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
			public int operator( Processor chip, Memory m, int op ) throws Throwable
			{
				Pic10f200 p = (Pic10f200)chip;
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
			public int operator( Processor chip, Memory m, int op ) throws Throwable
			{
				Pic10f200 p = (Pic10f200)chip;
				int literal = op & 0x0FF;
				
				p.wReg = literal;
				
				System.out.println( "MOVLW " +Integer.toHexString(literal) );
				return 0;
			}
		} );
		
		
		// MOVWF 0000 0010 1110
		instructionSet.add( new Instruction( "MOVWF", "0000 001* ****" )
		{
			public int operator( Processor chip, Memory m, int op ) throws Throwable
			{
				Pic10f200 p = (Pic10f200)chip;
				int file = op & 0x01F;
				
				m.set( file, p.wReg );
				
				System.out.println( "MOVWF " +Integer.toHexString(file)+ " <- " +p.wReg );
				return 0;
			}
		} );
		
		instructionSet.dump();
	}
	
}
