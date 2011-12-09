import ui.*;
import hardware.*;

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
		
		int progmem[] = new int[4096];
		
		try
		{
			FileInputStream reader = new FileInputStream( "demo.hex" );
			
			int op = 0;
			int offset = 0;
			while( (op = reader.read()) != -1 )
			{
				progmem[offset++] = op;
				System.out.print( "Flashed " +offset+ "B\r" );
			}
			System.out.println("");
			
			progmem[0] = 0x1C;
			progmem[1] = 0x0E;
			progmem[2] = 0xEF;
			
			reader.close();
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
