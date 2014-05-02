package hardware.avr;

import hardware.Memory;

/**
 *
 * @author John Vidler
 */
public class ATtiny9 extends AVRProcessor
{
    protected Memory sRAM = null;
    
    public ATtiny9( int pins, int program[] )
    {
        super( pins, program );
        
        progmem = new Memory( 1024, 12 );
        registers = new Memory( 16, 8 );
        sRAM = new Memory( 32, 8 );
        
    }
}
