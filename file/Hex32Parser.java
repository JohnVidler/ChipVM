package file;

import java.io.*;
import java.util.*;

public abstract class Hex32Parser
{
	
	public static int[] toProgmem( File source, int progmem )
	{
		ArrayList<Integer> code = new ArrayList<Integer>();
		String lineBuffer = null;
		char buffer[] = null;
		
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
				
				int size = Integer.parseInt( hex[0], 16 );
				int address = Integer.parseInt( hex[1]+hex[2], 16 );
				int type = Integer.parseInt( hex[3] );
				
				
				System.out.println( "# " +Integer.toHexString(type) );
				
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
