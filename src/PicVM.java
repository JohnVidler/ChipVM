import hardware.pic.Pic10f200;
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
		int[] progmem = null;
		
		try
		{
			progmem = Hex32Parser.toProgmem( new File( "demo.hex" ), 0xFF );
		}
		catch( Throwable t )
		{
			t.printStackTrace();
			System.exit( -1 );
		}
		
                if( new File("eeprom").exists() )
                {
                    PersistentEEPROM eeprom = new PersistentEEPROM( 1024, new File("eeprom") );
                    eeprom.readIn();
                }
		
		
		Processor proc = new Pic10f200( progmem );
		//Processor proc = new Pic10f320( progmem );
		
		mainWin = new AppWindow( proc );
		
		//clk.attach( proc.clkWire );
	}
}
