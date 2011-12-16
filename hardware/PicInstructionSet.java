package hardware;

public class PicInstructionSet extends InstructionSet
{
	protected int pcOffset = 0x00;
	
	public PicInstructionSet()
	{
		add( PicInstructionSet.instruction_ADDWF );
		add( instruction_ANDWF );
		add( instruction_CLRF );
		add( instruction_CLRW );
		add( instruction_COMF );
		add( instruction_DECF );
		add( instruction_DECFSZ );
		add( instruction_INCF );
		add( instruction_INCFSZ );
		add( instruction_IORWF );
		add( instruction_MOVF );
		add( instruction_MOVWF );
		add( instruction_NOP );
		//add( instruction_RLF );
		//add( instruction_RRF );
		add( instruction_SUBWF );
		add( instruction_SWAPF );
		add( instruction_XORWF );
		add( instruction_BCF );
		//add( instruction_BSF );
		add( instruction_BTFSC );
		add( instruction_BTFSS );
		add( instruction_ANDLW );
		add( instruction_CALL );
		add( instruction_CLRWDT );
		add( instruction_GOTO );
		add( instruction_IORLW );
		add( instruction_MOVLW );
		//add( instruction_OPTION );
		//add( instruction_RETLW );
		//add( instruction_SLEEP );
		//add( instruction_TRIS );
		add( instruction_XORLW );
	}
	
	public void setPC( int offset )
	{
		pcOffset = offset;
	}
	
	// CLRWDT
	public Instruction instruction_CLRWDT = new Instruction( "CLRWDT", "0000 0000 0100" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			System.out.println( "CLRWDT - Watchdog timer not implemented on this emulator!" );
			return 0;
		}
	};
	
	// DECF
	public Instruction instruction_DECF = new Instruction( "DECF", "0000 11** ****" )
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
			
			System.out.println( "DECF " +Integer.toHexString(offset) + " [" +d+ "]" );
			return 0;
		}
	};
	
	// INCF
	public Instruction instruction_INCF = new Instruction( "INCF", "0000 10** ****" )
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
			
			System.out.println( "DECF " +Integer.toHexString(offset) + " [" +d+ "]" );
			return 0;
		}
	};
	
	// NOP
	public Instruction instruction_NOP = new Instruction( "NOP", "0000 0000 0000" )
	{
		public int operator( Processor p, Memory m, int op ) throws Throwable
		{
			System.out.println( "NOP" );
			return 0;
		}
	};
	
	// GOTO
	public Instruction instruction_GOTO = new Instruction( "GOTO", "101* **** ****" )
	{
		public int operator( Processor p, Memory m, int op ) throws Throwable
		{
			int offset = op & 0x1FF;
			m.set( pcOffset, offset-1 );
			System.out.println( "GOTO 0x" + Integer.toHexString(offset) );
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
			
			System.out.println( "ADDWF 0x" +Integer.toHexString(offset)+ ", [" +d+ "] = " + result );
			return 0;
		}
	};
	
	// SUBWF
	public Instruction instruction_SUBWF = new Instruction( "SUBWF", "0000 10** ****" )
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
			
			System.out.println( "SUBWF 0x" +Integer.toHexString(offset)+ ", [" +d+ "] = " + result );
			return 0;
		}
	};
	
	// SWAPF
	public Instruction instruction_SWAPF = new Instruction( "SWAPF", "0011 10** ****" )
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
			
			System.out.println( "SWAPF 0x" +Integer.toHexString(offset)+ ", [" +d+ "] = " + result );
			return 0;
		}
	};
	
	// ANDWF
	public Instruction instruction_ANDWF = new Instruction( "ANDWF", "0001 01** ****" )
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
			
			System.out.println( "ANDWF 0x" +Integer.toHexString(offset)+ ", [" +d+ "] = " + result );
			return 0;
		}
	};
	
	// ANDLW
	public Instruction instruction_ANDLW = new Instruction( "ANDLW", "1110 **** ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			int value = op & 0x0FF;
			
			p.wReg = value & p.wReg;
			
			System.out.println( "ANDLW 0x" +Integer.toHexString(value) );
			return 0;
		}
	};
	
	// IORLW
	public Instruction instruction_IORLW = new Instruction( "IORLW", "1101 **** ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			int value = op & 0x0FF;
			
			p.wReg = value | p.wReg;
			
			System.out.println( "IORLW 0x" +Integer.toHexString(value) );
			return 0;
		}
	};
	
	// IORWF
	public Instruction instruction_IORWF = new Instruction( "IORWF", "0001 00** ****" )
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
			
			System.out.println( "IORWF 0x" +Integer.toHexString(offset) );
			return 0;
		}
	};
	
	// XORWF
	public Instruction instruction_XORWF = new Instruction( "XORWF", "0001 10** ****" )
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
			
			System.out.println( "XORWF 0x" +Integer.toHexString(offset) );
			return 0;
		}
	};
	
	// MOVF
	public Instruction instruction_MOVF = new Instruction( "MOVF", "0010 00** ****" )
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
			
			System.out.println( "MOVF 0x" +Integer.toHexString(offset) );
			return 0;
		}
	};
	
	// CLRF
	public Instruction instruction_CLRF = new Instruction( "CLRF", "0000 011* ****" )
	{
		public int operator( Processor p, Memory m, int op ) throws Throwable
		{
			int offset = op & 0x01F;
			
			m.set( offset, 0x00 );
			
			System.out.println( "CLRF 0x" +Integer.toHexString(offset) );
			return 0;
		}
	};
	
	// COMF
	public Instruction instruction_COMF = new Instruction( "COMF", "0010 01** ****" )
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
			
			System.out.println( "CLRF 0x" +Integer.toHexString(offset) );
			return 0;
		}
	};
	
	// CLRW
	public Instruction instruction_CLRW = new Instruction( "CLRW", "0000 0100 0000" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			p.wReg = 0;
			
			System.out.println( "CLRW" );
			return 0;
		}
	};
	
	// BTFSC
	public Instruction instruction_BTFSC = new Instruction( "BTFSC", "0110 **** ****" )
	{
		public int operator( Processor p, Memory m, int op ) throws Throwable
		{
			int bit = (op & 0x0E0) >> 5;
			int file = op & 0x01F;
			
			// Bit test 'bit' and skip if zero
			if( (m.get( file ) & (1 << bit)) == 0 )
				m.inc( pcOffset );
			
			System.out.println( "BTFSC " +bit+ ", " +Integer.toHexString(file) );
			return 0;
		}
	};
	
	// BTFSS
	public Instruction instruction_BTFSS = new Instruction( "BTFSS", "0111 **** ****" )
	{
		public int operator( Processor p, Memory m, int op ) throws Throwable
		{
			int bit = (op & 0x0E0) >> 5;
			int file = op & 0x01F;
			
			// Bit test 'bit' and skip if zero
			if( (m.get( file ) & (1 << bit)) == (1 << bit) )
				m.inc( pcOffset );
			
			System.out.println( "BTFSS " +bit+ ", " +Integer.toHexString(file) );
			return 0;
		}
	};
	
	// XORLW
	public Instruction instruction_XORLW = new Instruction( "XORLW", "1111 **** ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			int literal = op & 0x0FF;
			
			p.wReg = p.wReg ^ literal;
			
			System.out.println( "XORLW " +literal );
			return 0;
		}
	};
	
	// CALL
	public Instruction instruction_CALL = new Instruction( "CALL", "1001 **** ****" )
	{
		public int operator( Processor p, Memory m, int op ) throws Throwable
		{
			try
			{
				int lowerAddress = op & 0x07F;
				int upperAddress = (m.get( "STATUS" ) & 0x30) << 4;
				
				p.stack.push( m.get( pcOffset ) + 1 );
				m.set( pcOffset, (upperAddress | lowerAddress) - 1 );
				
				System.out.println( "CALL " +Integer.toBinaryString(m.get( pcOffset )) );
				return 0;
			}
			catch( NoSuchAliasException e )
			{
				System.out.println( "CALL " +e );
				return -1;
			}
		}
	};
	
	// RETLW
	public Instruction instruction_RETLW = new Instruction( "RETLW", "1000 **** ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			try
			{
				PicProcessor p = (PicProcessor)chip;
				
				// Set the working register
				p.wReg = op & 0x0FF;
				
				int newPC = p.stack.pop();
				
				m.set( pcOffset, newPC );
				
				System.out.println( "RETLW " +Integer.toBinaryString(m.get( pcOffset )) );
				return 0;
			}
			catch( NoSuchAliasException e )
			{
				System.out.println( "RETLW " +e );
				return -1;
			}
		}
	};
	
	
	// DECFSZ 
	public Instruction instruction_DECFSZ = new Instruction( "DECFSZ", "0010 11** ****" )
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
			
			System.out.println( "DECFSZ " +Integer.toHexString(file) );
			return 0;
		}
	};
	
	// INCFSZ 
	public Instruction instruction_INCFSZ = new Instruction( "INCFSZ", "0011 11** ****" )
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
			
			System.out.println( "INCFSZ " +Integer.toHexString(file) );
			return 0;
		}
	};
	
	// BCF 
	public Instruction instruction_BCF = new Instruction( "BCF", "0100 **** ****" )
	{
		public int operator( Processor p, Memory m, int op ) throws Throwable
		{
			int bit = (op & 0x0E0) >> 5;
			int file = op & 0x01F;
			
			m.set( file, m.get( file ) & ~(1 << bit) );
			
			System.out.println( "BCF " +bit+ ", " +Integer.toHexString(file) );
			return 0;
		}
	};
	
	// MOVLW
	public Instruction instruction_MOVLW = new Instruction( "MOVLW", "1100 **** ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			int literal = op & 0x0FF;
			
			p.wReg = literal;
			
			System.out.println( "MOVLW " +Integer.toHexString(literal) );
			return 0;
		}
	};
	
	
	// MOVWF 0000 0010 1110
	public Instruction instruction_MOVWF = new Instruction( "MOVWF", "0000 001* ****" )
	{
		public int operator( Processor chip, Memory m, int op ) throws Throwable
		{
			PicProcessor p = (PicProcessor)chip;
			int file = op & 0x01F;
			
			m.set( file, p.wReg );
			
			System.out.println( "MOVWF " +Integer.toHexString(file)+ " <- " +p.wReg );
			return 0;
		}
	};
	
}
