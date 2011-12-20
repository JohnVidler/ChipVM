package file;

import java.io.*;
import java.util.*;

public abstract class Hex32Parser
{
	public static final boolean MPLAB_KLUDGE = true;
	
	public static int[] toProgmem( File source, int progmemSize )
	{
		ArrayList<Integer> code = new ArrayList<Integer>();
		String lineBuffer = null;
		char buffer[] = null;

		int progmem[] = new int[progmemSize];
		for( int idx=0; idx<progmemSize; idx++ )
			progmem[idx] = 0;	// Normally a 'NOP' instruction
		
		try
		{
			BufferedReader reader = new BufferedReader( new FileReader( source ) );
			while( (lineBuffer = reader.readLine()) != null )
			{
				// Get the input into a sane format...
				buffer = lineBuffer.replace(":","").toCharArray();
				String hex[] = new String[ buffer.length / 2 ];
				for( int i=0; i<buffer.length; i+=2 )
					hex[i/2] = "" + buffer[i] + buffer[i+1];
				
				// Extract data from the line...
				int size = Integer.parseInt( hex[0], 16 )/2;
				int address = Integer.parseInt( hex[1]+hex[2], 16 );
				
				if( MPLAB_KLUDGE )
					address /= 2;
				
				int type = Integer.parseInt( hex[3] );
				
				int baseAddress = 0;
				
				switch( type )
				{
					case 0:
						System.out.println( "Program data..." );
						for( int idx=0; idx<size; idx++ )
						{
							int offset = idx * 2;
							int data = Integer.parseInt( "" + hex[5+offset] + hex[4+offset], 16 );
							progmem[baseAddress+address+idx] = data;
							System.out.println( Integer.toHexString(baseAddress+address+idx) + " -> " + Integer.toHexString(data) );
						}
						break;
					
					case 1:
						System.out.println( "EOF" );
						break;
					
					case 4:
						baseAddress = Integer.parseInt( "" + hex[5] + hex[4] ) << 16;
						System.out.println( "32-bit offset: " + Integer.toHexString( baseAddress ) );
						break;
					
					default:
						System.out.println( "Ignored type " +type );
				}
				
			}
			
			return progmem;
		}
		catch( Throwable t )
		{
			t.printStackTrace();
		}
		
		return null;
	}
	
}
