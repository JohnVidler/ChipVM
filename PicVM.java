import ui.*;
import hardware.*;
import file.*;

import java.io.*;

public class PicVM
{
	private AppWindow mainWin = null;
	
	
	// Hardware components
	protected Clock clk = null;
	
	public static void main( String[] args ){ new PicVM( args ); }
	public PicVM( String[] args )
	{
		//mainWin = new AppWindow();
		int[] progmem = null;
		
		try
		{
			progmem = Hex32Parser.toProgmem( new File( "demo.hex" ), 4096 );
		}
		catch( Throwable t )
		{
			t.printStackTrace();
			System.exit( -1 );
		}
		
		
		Processor proc = new Processor( progmem );
		clk = new Clock();
		clk.setFrequency( 200 );
		clk.attach( proc.clkWire );
		
		
	}
}
