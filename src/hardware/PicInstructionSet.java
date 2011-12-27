package hardware;

public class PicInstructionSet extends InstructionSet
{
	protected static int pcOffset = 0x00;
	
	public PicInstructionSet()
	{
		super();
		
		add( PicInstructionSet.instruction_ADDWF );
		add( PicInstructionSet.instruction_ANDWF );
		add( PicInstructionSet.instruction_CLRF );
		add( PicInstructionSet.instruction_CLRW );
		add( PicInstructionSet.instruction_COMF );
		add( PicInstructionSet.instruction_DECF );
		add( PicInstructionSet.instruction_DECFSZ );
		add( PicInstructionSet.instruction_INCF );
		add( PicInstructionSet.instruction_INCFSZ );
		add( PicInstructionSet.instruction_IORWF );
		add( PicInstructionSet.instruction_MOVF );
		add( PicInstructionSet.instruction_MOVWF );
		add( PicInstructionSet.instruction_NOP );
		add( PicInstructionSet.instruction_RLF );
		add( PicInstructionSet.instruction_RRF );
		add( PicInstructionSet.instruction_SUBWF );
		add( PicInstructionSet.instruction_SWAPF );
		add( PicInstructionSet.instruction_XORWF );
		add( PicInstructionSet.instruction_BCF );
		add( PicInstructionSet.instruction_BSF );
		add( PicInstructionSet.instruction_BTFSC );
		add( PicInstructionSet.instruction_BTFSS );
		add( PicInstructionSet.instruction_ANDLW );
		add( PicInstructionSet.instruction_CALL );
		add( PicInstructionSet.instruction_CLRWDT );
		add( PicInstructionSet.instruction_GOTO );
		add( PicInstructionSet.instruction_IORLW );
		add( PicInstructionSet.instruction_MOVLW );
		add( PicInstructionSet.instruction_OPTION );
		add( PicInstructionSet.instruction_RETLW );
		add( PicInstructionSet.instruction_SLEEP );
		add( PicInstructionSet.instruction_TRIS );
		add( PicInstructionSet.instruction_XORLW );
	}
	
	public void setPC( int offset )
	{
		pcOffset = offset;
	}
	
	// RLF (actually a RIGHT shift due to endian-ness)
	public static Instruction instruction_RLF = new Instruction( "RLF", "0011 01** ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			int offset = op & 0x01F;
			int d = ((op & 0x020) == 0x020)?1:0;
			
			int value = m.get( offset ) & 0xFF;
			int oldStatus = (m.get( "STATUS" ) & 0x01) << 7;
			
			// Check for zero-bit
			if( (value & 0x01) == 0x01 )
				m.setBit( "STATUS", 0 );
			else
				m.clearBit( "STATUS", 0 );
			
			// Actually store the new value
			if( d == 0 )
				p.wReg = ((value >> 1) & 0xFF) | oldStatus;
			else
				m.set( offset, ((value >> 1) & 0xFF) | oldStatus );
			
			out.println( "RLF " + Integer.toHexString(offset) + " [" +d+ "]" );
			return 0;
		}
	};
	
	// RRF (actually a LEFT shift due to endian-ness)
	public static Instruction instruction_RRF = new Instruction( "RRF", "0011 00** ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			int offset = op & 0x01F;
			int d = ((op & 0x020) == 0x020)?1:0;
			
			int value = m.get( offset );
			int oldStatus = (m.get( "STATUS" ) & 0x01);
			
			// Check for zero-bit
			if( (value & 0x80) == 0x80 )
				m.setBit( "STATUS", 0 );
			else
				m.clearBit( "STATUS", 0 );
			
			// Actually store the new value
			if( d == 0 )
				p.wReg = ((value << 1) & 0xFF) | oldStatus;
			else
				m.set( offset, ((value << 1) & 0xFF) | oldStatus );
			
			out.println( "RRF " + Integer.toHexString(offset) + " [" +d+ "]" );
			return 0;
		}
	};
	
	// SLEEP
	public static Instruction instruction_SLEEP = new Instruction( "SLEEP", "0000 0000 0011" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			out.println( "SLEEP - Powerdown mode not implemented!" );
			return 0;
		}
	};
	
	// TRIS
	public static Instruction instruction_TRIS = new Instruction( "TRIS", "0000 0000 0***" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			out.println( "TRIS - TRIS registers ignored!" );
			return 0;
		}
	};
	
	// OPTION
	public static Instruction instruction_OPTION = new Instruction( "OPTION", "0000 0000 0010" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			out.println( "OPTION - OPTION registers ignored!" );
			return 0;
		}
	};
	
	// CLRWDT
	public static Instruction instruction_CLRWDT = new Instruction( "CLRWDT", "0000 0000 0100" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			out.println( "CLRWDT - Watchdog timer not implemented on this emulator!" );
			return 0;
		}
	};
	
	// DECF
	public static Instruction instruction_DECF = new Instruction( "DECF", "0000 11** ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			int offset = op & 0x01F;
			int d = ((op & 0x020) == 0x020)?1:0;
			
			int result = m.get( offset ) - 1;
			
			if( d == 0 )
				p.wReg = result;
			else
				m.set( offset, result );
			
			// Set the zero status bit, if zero
			if( result == 0 )
				m.setBit( "STATUS", 2 );
			else
				m.clearBit( "STATUS", 2 );
			
			out.println( "DECF " +Integer.toHexString(offset) + " [" +d+ "]" );
			return 0;
		}
	};
	
	// INCF
	public static Instruction instruction_INCF = new Instruction( "INCF", "0000 10** ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			int offset = op & 0x01F;
			int d = ((op & 0x020) == 0x020)?1:0;
			
			int result = m.get( offset ) + 1 % p.maxValue;
			
			if( d == 0 )
				p.wReg = result;
			else
				m.set( offset, result );
			
			// Set the zero status bit, if zero
			if( result == 0 )
				m.setBit( "STATUS", 2 );
			else
				m.clearBit( "STATUS", 2 );
			
			out.println( "DECF " +Integer.toHexString(offset) + " [" +d+ "]" );
			return 0;
		}
	};
	
	// NOP
	public static Instruction instruction_NOP = new Instruction( "NOP", "0000 0000 0000" )
	{
		public int operator( Processor p, Memory m, int op ) throws Throwable
		{
			out.println( "NOP" );
			return 0;
		}
	};
	
	// GOTO
	public static Instruction instruction_GOTO = new Instruction( "GOTO", "101* **** ****" )
	{
		public int operator( Processor p, Memory m, int op ) throws Throwable
		{
			int offset = op & 0x1FF;
			m.set( pcOffset, offset );
			out.println( "GOTO 0x" + Integer.toHexString(offset) );
			return 0;
		}
	};
	
	
	// ADDWF
	public static Instruction instruction_ADDWF = new Instruction( "ADDWF", "0001 11** ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			int offset = op & 0x01F;
			int d = ((op & 0x020) == 0x020)?1:0;
			
			int result = m.get( offset ) + p.wReg;
			
			if( d == 0 )
				p.wReg = result;
			else
				m.set( offset, result );
				
			// Set the zero status bit, if zero
			if( result == 0 )
				m.setBit( "STATUS", 2 );
			else
				m.clearBit( "STATUS", 2 );
			
			out.println( "ADDWF 0x" +Integer.toHexString(offset)+ ", [" +d+ "] = " + result );
			return 0;
		}
	};
	
	// SUBWF
	public static Instruction instruction_SUBWF = new Instruction( "SUBWF", "0000 10** ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			int offset = op & 0x01F;
			int d = ((op & 0x020) == 0x020)?1:0;
			
			int result = m.get( offset ) - p.wReg;
			
			if( d == 0 )
				p.wReg = result;
			else
				m.set( offset, result );
				
			// Set the zero status bit, if zero
			if( result == 0 )
				m.setBit( "STATUS", 2 );
			else
				m.clearBit( "STATUS", 2 );
			
			out.println( "SUBWF 0x" +Integer.toHexString(offset)+ ", [" +d+ "] = " + result );
			return 0;
		}
	};
	
	// SWAPF
	public static Instruction instruction_SWAPF = new Instruction( "SWAPF", "0011 10** ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			int offset = op & 0x01F;
			int d = ((op & 0x020) == 0x020)?1:0;
			
			int result = ((m.get( offset ) & 0x0F) << 4) | ((m.get( offset ) & 0xF0) >> 4);
			
			if( d == 0 )
				p.wReg = result;
			else
				m.set( offset, result );
				
			// Set the zero status bit, if zero
			if( result == 0 )
				m.setBit( "STATUS", 2 );
			else
				m.clearBit( "STATUS", 2 );
			
			out.println( "SWAPF 0x" +Integer.toHexString(offset)+ ", [" +d+ "] = " + result );
			return 0;
		}
	};
	
	// ANDWF
	public static Instruction instruction_ANDWF = new Instruction( "ANDWF", "0001 01** ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			int offset = op & 0x01F;
			int d = ((op & 0x020) == 0x020)?1:0;
			
			int result = (m.get( offset ) & p.wReg) % p.maxValue;
			
			if( d == 0 )
				p.wReg = result;
			else
				m.set( offset, result );
				
			// Set the zero status bit, if zero
			if( result == 0 )
				m.setBit( "STATUS", 2 );
			else
				m.clearBit( "STATUS", 2 );
			
			out.println( "ANDWF 0x" +Integer.toHexString(offset)+ ", [" +d+ "] = " + result );
			return 0;
		}
	};
	
	// ANDLW
	public static Instruction instruction_ANDLW = new Instruction( "ANDLW", "1110 **** ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			int value = op & 0x0FF;
			
			p.wReg = value & p.wReg;
			
			out.println( "ANDLW 0x" +Integer.toHexString(value) );
			return 0;
		}
	};
	
	// IORLW
	public static Instruction instruction_IORLW = new Instruction( "IORLW", "1101 **** ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			int value = op & 0x0FF;
			
			p.wReg = value | p.wReg;
			
			out.println( "IORLW 0x" +Integer.toHexString(value) );
			return 0;
		}
	};
	
	// IORWF
	public static Instruction instruction_IORWF = new Instruction( "IORWF", "0001 00** ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			int offset = op & 0x01F;
			int d = ((op & 0x020) == 0x020)?1:0;
			
			int result = m.get( offset ) | p.wReg;
			
			if( d == 0 )
				p.wReg = result;
			else
				m.set( offset, result );
				
			// Set the zero status bit, if zero
			if( result == 0 )
				m.setBit( "STATUS", 2 );
			else
				m.clearBit( "STATUS", 2 );
			
			out.println( "IORWF 0x" +Integer.toHexString(offset) );
			return 0;
		}
	};
	
	// XORWF
	public static Instruction instruction_XORWF = new Instruction( "XORWF", "0001 10** ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			int offset = op & 0x01F;
			int d = ((op & 0x020) == 0x020)?1:0;
			
			int result = m.get( offset ) ^ p.wReg;
			
			if( d == 0 )
				p.wReg = result;
			else
				m.set( offset, result );
				
			// Set the zero status bit, if zero
			if( result == 0 )
				m.setBit( "STATUS", 2 );
			else
				m.clearBit( "STATUS", 2 );
			
			out.println( "XORWF 0x" +Integer.toHexString(offset) );
			return 0;
		}
	};
	
	// MOVF
	public static Instruction instruction_MOVF = new Instruction( "MOVF", "0010 00** ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			int offset = op & 0x01F;
			int d = ((op & 0x020) == 0x020)?1:0;
			
			int result = m.get( offset );
			
			if( d == 0 )
				p.wReg = result;
			else
				m.set( offset, result );
				
			// Set the zero status bit, if zero
			if( result == 0 )
				m.setBit( "STATUS", 2 );
			else
				m.clearBit( "STATUS", 2 );
			
			out.println( "MOVF 0x" +Integer.toHexString(offset) );
			return 0;
		}
	};
	
	// CLRF
	public static Instruction instruction_CLRF = new Instruction( "CLRF", "0000 011* ****" )
	{
		public int operator( Processor p, Memory m, int op ) throws Throwable
		{
			int offset = op & 0x01F;
			
			m.set( offset, 0x00 );
			
			out.println( "CLRF 0x" +Integer.toHexString(offset) );
			return 0;
		}
	};
	
	// COMF
	public static Instruction instruction_COMF = new Instruction( "COMF", "0010 01** ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			int offset = op & 0x01F;
			int d = ((op & 0x020) == 0x020)?1:0;
			
			int result = ~m.get( offset );
			
			if( d == 0 )
				p.wReg = result;
			else
				m.set( offset, result );
				
			// Set the zero status bit, if zero
			if( result == 0 )
				m.setBit( "STATUS", 2 );
			else
				m.clearBit( "STATUS", 2 );
			
			out.println( "CLRF 0x" +Integer.toHexString(offset) );
			return 0;
		}
	};
	
	// CLRW
	public static Instruction instruction_CLRW = new Instruction( "CLRW", "0000 0100 0000" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			p.wReg = 0;
			
			out.println( "CLRW" );
			return 0;
		}
	};
	
	// BTFSC
	public static Instruction instruction_BTFSC = new Instruction( "BTFSC", "0110 **** ****" )
	{
		public int operator( Processor p, Memory m, int op ) throws Throwable
		{
			int bit = (op & 0x0E0) >> 5;
			int file = op & 0x01F;
			
			// Bit test 'bit' and skip if zero
			if( (m.get( file ) & (1 << bit)) == 0 )
				m.inc( pcOffset );
			
			out.println( "BTFSC " +bit+ ", " +Integer.toHexString(file) );
			return 0;
		}
	};
	
	// BTFSS
	public static Instruction instruction_BTFSS = new Instruction( "BTFSS", "0111 **** ****" )
	{
		public int operator( Processor p, Memory m, int op ) throws Throwable
		{
			int bit = (op & 0x0E0) >> 5;
			int file = op & 0x01F;
			
			// Bit test 'bit' and skip if zero
			if( (m.get( file ) & (1 << bit)) == (1 << bit) )
				m.inc( pcOffset );
			
			out.println( "BTFSS " +bit+ ", " +Integer.toHexString(file) );
			return 0;
		}
	};
	
	// XORLW
	public static Instruction instruction_XORLW = new Instruction( "XORLW", "1111 **** ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			int literal = op & 0x0FF;
			
			p.wReg = p.wReg ^ literal;
			
			out.println( "XORLW " +literal );
			return 0;
		}
	};
	
	// CALL
	public static Instruction instruction_CALL = new Instruction( "CALL", "1001 **** ****" )
	{
		public int operator( Processor p, Memory m, int op ) throws Throwable
		{
			try
			{
				int lowerAddress = op & 0x0FF;
				int upperAddress = (m.get( "STATUS" ) & 0x30) << 4;
				
				p.stack.push( m.get( pcOffset )+1 );
				m.set( pcOffset, lowerAddress );
				
				out.println( "CALL " +Integer.toBinaryString(m.get( pcOffset )) );
				return 0;
			}
			catch( NoSuchAliasException e )
			{
				out.println( "CALL " +e );
				return -1;
			}
		}
	};
	
	// RETLW
	public static Instruction instruction_RETLW = new Instruction( "RETLW", "1000 **** ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			try
			{
				PicProcessor p = (PicProcessor)chip;
				
				// Set the working register
				p.wReg = op & 0x0FF;
				
				int newPC = p.stack.pop();
				
				out.println( "Stack address was: " +newPC );
				
				m.set( pcOffset, newPC );
				
				out.println( "RETLW " +Integer.toBinaryString(m.get( pcOffset )) );
				return 0;
			}
			catch( NoSuchAliasException e )
			{
				out.println( "RETLW " +e );
				return -1;
			}
		}
	};
	
	
	// DECFSZ 
	public static Instruction instruction_DECFSZ = new Instruction( "DECFSZ", "0010 11** ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			int file = op & 0x01F;
			int d = ((op & 0x020) == 0x020)?1:0;
			int result = 1;
			
			if( d == 1 )
				result = m.dec( file );
			else
				result = p.wReg = m.get( file ) - 1;
			
			if( result == 0 )
				m.inc( pcOffset );
			
			out.println( "DECFSZ " +Integer.toHexString(file) );
			return 0;
		}
	};
	
	// INCFSZ 
	public static Instruction instruction_INCFSZ = new Instruction( "INCFSZ", "0011 11** ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			int file = op & 0x01F;
			int d = ((op & 0x020) == 0x020)?1:0;
			int result = 1;
			
			if( d == 1 )
				result = m.dec( file );
			else
				result = p.wReg = m.get( file ) + 1;
			
			if( result == 0 )
				m.inc( pcOffset );
			
			out.println( "INCFSZ " +Integer.toHexString(file) );
			return 0;
		}
	};
	
	// BCF 
	public static Instruction instruction_BCF = new Instruction( "BCF", "0100 **** ****" )
	{
		public int operator( Processor p, Memory m, int op ) throws Throwable
		{
			int bit = (op & 0x0E0) >> 5;
			int file = op & 0x01F;
			
			m.clearBit( file, bit );
			
			out.println( "BCF " +bit+ ", " +Integer.toHexString(file) );
			return 0;
		}
	};
	
	// BSF 
	public static Instruction instruction_BSF = new Instruction( "BSF", "0101 **** ****" )
	{
		public int operator( Processor p, Memory m, int op ) throws Throwable
		{
			int bit = (op & 0x0E0) >> 5;
			int file = op & 0x01F;
			
			m.setBit( file, bit );
			
			out.println( "BSF " +bit+ ", " +Integer.toHexString(file) );
			return 0;
		}
	};
	
	// MOVLW
	public static Instruction instruction_MOVLW = new Instruction( "MOVLW", "1100 **** ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			int literal = op & 0x0FF;
			
			p.wReg = literal;
			
			out.println( "MOVLW " +Integer.toHexString(literal) );
			return 0;
		}
	};
	
	
	// MOVWF 0000 0010 1110
	public static Instruction instruction_MOVWF = new Instruction( "MOVWF", "0000 001* ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			int file = op & 0x01F;
			
			m.set( file, p.wReg );
			
			out.println( "MOVWF " +Integer.toHexString(file)+ " <- " +p.wReg );
			return 0;
		}
	};
	
}
