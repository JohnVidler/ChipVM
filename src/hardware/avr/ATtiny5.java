package hardware.avr;

import hardware.Memory;

/**
 *
 * @author John Vidler
 */
public class ATtiny5 extends AVRProcessor
{
    protected Memory sRAM = null;
    
    public ATtiny5( int pins, int program[] )
    {
        super( pins, program );
        
        progmem = new Memory( 512, 12 );
        registers = new Memory( 16, 8 );
        sRAM = new Memory( 32, 8 );
        
    }
}
