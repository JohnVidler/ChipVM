package util;

import java.io.*;

public class DebugPrintStream extends PrintStream
{
	protected boolean enabled = false;
	
	public DebugPrintStream( OutputStream out )
	{
		super( out );
	}
	
	public void setEnabled( boolean mode ) { enabled = mode; }
	
	public void write( int b )
	{
		if( enabled )
			super.write( b );
	}
}
