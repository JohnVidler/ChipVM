package hardware.avr;

import hardware.Processor;
import java.util.logging.Level;

/**
 *
 * @author John Vidler
 */
public class AVRProcessor extends Processor
{
    public AVRProcessor( int pins, int program[] )
    {
        super( pins, program );
    }

    @Override
    protected int op_readNextInstruction()
    {
        int op = 0;
        try
        {
            op = progmem.get( registers.get( "PC" ) );
        }
        catch( Exception err )
        {
            chipLog.log( Level.WARNING, "Unable to access the PC register", err );
        }

        try
        {
            registers.inc( "PC" );
        }
        catch( Throwable t )
        {
            chipLog.log( Level.SEVERE, "Could not inc PC (No PC?)", t );
            System.exit(1);
        }

        return op;
    }
    
}
